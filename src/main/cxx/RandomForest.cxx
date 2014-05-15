#include <iostream>

#include "VigraWrapper.hxx"
#include "MultiArrayInfo.h"

#include "net_imglib2_vigra_RandomForest.h"

#include <vigra/multi_array.hxx>
#include <vigra/random_forest.hxx>

using namespace vigra;


struct RandomForestInfo
{
private:
	jint tId;

	void* ptr;

public:
	RandomForestInfo( JNIEnv *env, jobject rfObj )
	{
		// get jfieldIDs (later this should be done only once)
		jclass RandomForest_class = env->GetObjectClass( rfObj );
		jfieldID typeId_field = env->GetFieldID( RandomForest_class, "typeId", "I" );
		jfieldID ptr_field = env->GetFieldID( RandomForest_class, "ptr", "J" );

		// get the fields
		this->tId = env->GetIntField( rfObj, typeId_field );
		this->ptr = reinterpret_cast< void* >( env->GetLongField( rfObj, ptr_field ) );
	}

	jint typeId();

	template< class T > RandomForest< T >& get();
};

jint RandomForestInfo::typeId()
{
	return tId;
}

template< class T > RandomForest< T >& RandomForestInfo::get()
{
	return *(reinterpret_cast< RandomForest< T >* >( ptr ));
}


#define LABEL_TYPES vigra::Int32, vigra::UInt8, float
#define FEATURE_TYPES float, double



JNIEXPORT jlong JNICALL Java_net_imglib2_vigra_RandomForest_constructor
  (JNIEnv *env, jclass, jint typeId )
{
	try
	{
#define F(LabelType) return reinterpret_cast< jlong >( new RandomForest< LabelType >() )
		ALLOW_TYPES( typeId, LABEL_TYPES )
#undef F
	}
	CATCH_CPP_EXCEPTION_THROW_JAVA_EXCEPTION
}



template< class LabelType >
void predictLabels( RandomForestInfo rf, MultiArrayInfo features, MultiArrayInfo predicted )
{
#define F(FeatureType) \
		MultiArrayView< 2, LabelType > pr = predicted.getMultiArray< 2, LabelType >(); \
		rf.get< LabelType >().predictLabels( features.getMultiArray< 2, FeatureType >(), pr )
	ALLOW_TYPES( features.typeId(), FEATURE_TYPES )
#undef F
}

JNIEXPORT void JNICALL Java_net_imglib2_vigra_RandomForest_predictLabels
  (JNIEnv *env, jobject obj, jobject features, jobject predicted)
{
	try
	{
		RandomForestInfo randomForestInfo( env, obj );
		MultiArrayInfo featuresInfo( env, features );
		MultiArrayInfo predictedInfo( env, predicted );

#define F(LabelType) predictLabels< LabelType >(randomForestInfo, featuresInfo, predictedInfo)
		ALLOW_TYPES( randomForestInfo.typeId(), LABEL_TYPES )
#undef F
	}
	CATCH_CPP_EXCEPTION_THROW_JAVA_EXCEPTION
}



template< class LabelType >
void learn( RandomForestInfo rf, MultiArrayInfo features, MultiArrayInfo responses )
{
#define F(FeatureType) \
		rf.get< LabelType >().learn( features.getMultiArray< 2, FeatureType >(), responses.getMultiArray< 2, LabelType >() )
	ALLOW_TYPES( features.typeId(), FEATURE_TYPES )
#undef F
}

JNIEXPORT void JNICALL Java_net_imglib2_vigra_RandomForest_learn
  (JNIEnv *env, jobject obj, jobject features, jobject responses)
{
	try
	{
		RandomForestInfo randomForestInfo( env, obj );
		MultiArrayInfo featuresInfo( env, features );
		MultiArrayInfo responsesInfo( env, responses );

#define F(LabelType) learn< LabelType >(randomForestInfo, featuresInfo, responsesInfo)
		ALLOW_TYPES( randomForestInfo.typeId(), LABEL_TYPES )
#undef F
	}
	CATCH_CPP_EXCEPTION_THROW_JAVA_EXCEPTION
}
