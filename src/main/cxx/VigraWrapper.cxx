#include <iostream>
#include <vigra/multi_array.hxx>

#include "net_imglib2_vigra_VigraWrapper.h"
#include "net_imglib2_vigra_VigraTensorWrapper.h"
#include "VigraWrapper.hxx"

#include <vigra/multi_convolution.hxx>
#include <vigra/tv_filter.hxx>
#include <vigra/boundarytensor.hxx>

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

		// the javaShape is irrelevant to any c++ classes / functions so we ignore it

		this->typeId = env->GetIntField( multiArrayInfo, typeId_field );
		this->dataPtr = reinterpret_cast< void* >( env->GetLongField( multiArrayInfo, dataPtr_field ) );
	}

	template< unsigned int N, class T > MultiArrayView< N, T > getMultiArray();
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


/**********************
 * Gaussian smoothing *
 **********************/
template< class T, unsigned int N >
void gaussianSmoothMultiArray( MultiArrayInfo source, MultiArrayInfo dest, jdouble sigma )
{
	gaussianSmoothMultiArray( source.getMultiArray< N, T >(), dest.getMultiArray< N, T >(), sigma );
}

template< class T >
void gaussianSmoothMultiArray( MultiArrayInfo sourceInfo, MultiArrayInfo destInfo, jdouble sigma )
{
#define F(N) gaussianSmoothMultiArray<T,N>(sourceInfo, destInfo, sigma)
	ALLOW_DIMENSIONS( sourceInfo.shape.size(), 1, 2, 3 )
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
	ALLOW_TYPES( sourceInfo.typeId, UInt8, Int32, float, double )
#undef F
}


/*******************
 * TOTAL VARIATION *
 ********************/
template< class T, unsigned int N >
void totalVariation2d( MultiArrayInfo sourceInfo, MultiArrayInfo destInfo, jdouble alpha, jint steps, jdouble eps)
{
	totalVariationFilter( sourceInfo.getMultiArray< N, T >(), destInfo.getMultiArray< N, T >(), alpha, steps, eps );
}

template< class T >
void totalVariation2d( MultiArrayInfo sourceInfo, MultiArrayInfo destInfo, jdouble alpha, jint steps, jdouble eps )
{
#define F(N) totalVariation2d<T,N>(sourceInfo, destInfo, alpha, steps, eps)
	ALLOW_DIMENSIONS( sourceInfo.shape.size(), 2 )
#undef F
}

/*
 * This is the actual JNI call.
 * It uses the ALLOW_TYPES macro to dispatch to totalVariation2d<T>(...) instantiation for the specified types.
 * These instantiations will in turn dispatch to totalVariation2d<T,N>(...) instantiations for the dimensionalities specified there.
 */
JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraWrapper_totalVariation2d
  (JNIEnv *env, jclass, jobject source, jobject dest, jdouble alpha, jint steps, jdouble eps)
{
	MultiArrayInfo sourceInfo( env, source );
	MultiArrayInfo destInfo( env, dest );

	using namespace vigra;
	// to get UInt8 and Int32
#define F(T) totalVariation2d<T>(sourceInfo, destInfo, alpha, steps, eps)
	ALLOW_TYPES( sourceInfo.typeId, double )
#undef F
}

/********************
 * BOUNDARY TENSOR  *
 ********************/
template< class T, unsigned int N >
void boundaryTensor2d( MultiArrayInfo sourceInfo, MultiArrayInfo destInfo, jdouble sigma )
{
	boundaryTensor( sourceInfo.getMultiArray< N, T >(), destInfo.getMultiArray< N, TinyVector<T,3> >(), sigma );
}

template< class T >
void boundaryTensor2d( MultiArrayInfo sourceInfo, MultiArrayInfo destInfo, jdouble sigma )
{
#define F(N) boundaryTensor2d<T,N>(sourceInfo, destInfo, sigma)
	ALLOW_DIMENSIONS( sourceInfo.shape.size(), 2 )
#undef F
}

JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraTensorWrapper_boundaryTensor2d
  (JNIEnv *env, jclass, jobject source, jobject dest, jdouble sigma)
{
	MultiArrayInfo sourceInfo( env, source );
	MultiArrayInfo destInfo( env, dest );

	using namespace vigra;
	// to get UInt8 and Int32
#define F(T) boundaryTensor2d<T>(sourceInfo, destInfo, sigma)
	ALLOW_TYPES( sourceInfo.typeId, float )
#undef F
}
