#include <iostream>
#include <vigra/multi_array.hxx>

#include "net_imglib2_vigra_VigraWrapper.h"
#include "net_imglib2_vigra_RandomForest.h"
#include "VigraWrapper.hxx"

#include <vigra/multi_convolution.hxx>
#include <vigra/random_forest.hxx>

using namespace vigra;



struct MultiArrayInfo
{
	std::vector< jlong > shape;

	jint typeId;

	void* dataPtr;

	MultiArrayInfo( std::vector< jlong >& shape, jint typeId, void* dataPtr )
			: shape( shape.begin(), shape.end() ), typeId( typeId ), dataPtr( dataPtr )
	{}

	MultiArrayInfo( JNIEnv *env, jobject multiArrayInfo )
	{
		// get jfieldIDs (later this should be done only once)
		jclass MultiArrayInfo_class = env->GetObjectClass( multiArrayInfo );
		jfieldID typeId_field = env->GetFieldID( MultiArrayInfo_class, "typeId", "I" );
		jfieldID shape_field = env->GetFieldID( MultiArrayInfo_class, "shape", "[J" );
		jfieldID dataPtr_field = env->GetFieldID( MultiArrayInfo_class, "dataPtr", "J" );

		// get the fields
		jlongArray shape_arr = reinterpret_cast< jlongArray >( env->GetObjectField( multiArrayInfo, shape_field ) );
		jsize shape_length = env->GetArrayLength( shape_arr );
		jlong shape_v[ shape_length ];
		env->GetLongArrayRegion( shape_arr, 0, shape_length, shape_v );
		this->shape = std::vector< jlong >( shape_v, shape_v + shape_length );
		this->typeId = env->GetIntField( multiArrayInfo, typeId_field );
		this->dataPtr = reinterpret_cast< void* >( env->GetLongField( multiArrayInfo, dataPtr_field ) );
	}

	template< unsigned int N, class T > MultiArrayView< N, T > getMultiArray();

	unsigned int dim()
	{
		return shape.size();
	}
};

template< unsigned int N, class T > MultiArrayView< N, T > MultiArrayInfo::getMultiArray()
{
	TinyVectorView< jlong, N > v( &shape[ 0 ] );
	return MultiArrayView< N, T >( v, reinterpret_cast< T* >( dataPtr ) );
}

void printMultiArrayInfo( const MultiArrayInfo& info )
{
	std::cout << "typeId = " << info.typeId << std::endl;

	std::cout << "shape = ";
	for ( int d = 0; d < info.shape.size(); ++d )
		std::cout << info.shape[ d ] << (
				d < info.shape.size() - 1 ? ", " : "\n" );

	std::cout << "dataPtr = " << reinterpret_cast< long >( info.dataPtr ) << std::endl;
}

struct  RandomForestInfo
{
	jint typeId;

	void* ptr;

	RandomForestInfo( JNIEnv *env, jobject rfObj )
	{
		// get jfieldIDs (later this should be done only once)
		jclass RandomForest_class = env->GetObjectClass( rfObj );
		jfieldID typeId_field = env->GetFieldID( RandomForest_class, "typeId", "I" );
		jfieldID ptr_field = env->GetFieldID( RandomForest_class, "ptr", "J" );

		// get the fields
		this->typeId = env->GetIntField( rfObj, typeId_field );
		this->ptr = reinterpret_cast< void* >( env->GetLongField( rfObj, ptr_field ) );
	}

	template< class T > RandomForest< T >& get();
};

template< class T > RandomForest< T >& RandomForestInfo::get()
{
	return *(reinterpret_cast< RandomForest< T >* >( ptr ));
}

void Java_net_imglib2_vigra_VigraWrapper_catchVigraViolationExample( JNIEnv *env, jclass clazz )
{
   try
   {
//      throw PreconditionViolation("Let's get ready to rumble");
      throw PostconditionViolation("My feet are so swollen right now");
//      throw InvariantViolation("Put your back into it");
   }
   CATCH_CPP_EXCEPTION_THROW_JAVA_EXCEPTION
}





JNIEXPORT jlong JNICALL Java_net_imglib2_vigra_RandomForest_constructor
  (JNIEnv *env, jclass, jint typeId )
{
#define F(T) return reinterpret_cast< jlong >( new RandomForest< T >() );
	ALLOW_TYPES( typeId, double, float )
#undef F
}


JNIEXPORT void JNICALL Java_net_imglib2_vigra_RandomForest_learn
  (JNIEnv *env, jobject obj, jobject features, jobject responses)
{
	RandomForestInfo randomForestInfo( env, obj );
	MultiArrayInfo featuresInfo( env, features );
	MultiArrayInfo responsesInfo( env, responses );

	randomForestInfo.get< float >().learn(
			featuresInfo.getMultiArray< 2, float >(),
			responsesInfo.getMultiArray< 2, float >() );
}



template< class T >
void gaussianSmoothMultiArray( MultiArrayInfo source, MultiArrayInfo dest, jdouble sigma )
{
#define F(N) gaussianSmoothMultiArray( source.getMultiArray< N, T >(), dest.getMultiArray< N, T >(), sigma )
	ALLOW_DIMENSIONS( source.dim(), 1, 2, 3 )
#undef F
}

/*
 * This is the actual JNI call.
 * It uses the ALLOW_TYPES macro to dispatch to gaussianSmoothMultiArray<T>(...) instantiation for the specified types.
 * These instantiations will in turn dispatch to gaussianSmoothMultiArray<T,N>(...) instantiations for the dimensionalities specified there.
 */
JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraWrapper_gaussianSmoothMultiArray
  (JNIEnv *env, jclass, jobject source, jobject dest, jdouble sigma)
{
	MultiArrayInfo sourceInfo( env, source );
	MultiArrayInfo destInfo( env, dest );

	using namespace vigra;
	// to get UInt8 and Int32
#define F(T) gaussianSmoothMultiArray<T>(sourceInfo, destInfo, sigma)
	ALLOW_TYPES( sourceInfo.typeId, UInt8, Int32, float )
#undef F
}
