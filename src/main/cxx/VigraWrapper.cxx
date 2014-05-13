#include <iostream>
#include <vigra/multi_array.hxx>

#include "net_imglib2_vigra_VigraWrapper.h"
#include "VigraWrapper.hxx"

#include <vigra/multi_convolution.hxx>

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
};







template< class T, unsigned int N >
void gaussianSmoothMultiArray( JNIEnv *env, jlongArray shape, jint typeId,
		jlong sourceData, jlong destData, jdouble sigma )
{
	TinyVector< jlong, N > v;
	env->GetLongArrayRegion( shape, 0, N, &v[ 0 ] );
	MultiArrayView< N, T > source( v, reinterpret_cast< T* >( sourceData ) );
	MultiArrayView< N, T > dest( v, reinterpret_cast< T* >( destData ) );
	gaussianSmoothMultiArray( source, dest, sigma );
}

template< class T >
void gaussianSmoothMultiArray( JNIEnv *env, jlongArray shape, jint typeId,
		jlong sourceData, jlong destData, jdouble sigma )
{
#define F(N) gaussianSmoothMultiArray<T,N>(env, shape, typeId, sourceData, destData, sigma)
	ALLOW_DIMENSIONS( env->GetArrayLength( shape ), 1, 2, 3 )
#undef F
}

/*
 * This is the actual JNI call.
 * It uses the ALLOW_TYPES macro to dispatch to gaussianSmoothMultiArray<T>(...) instantiation for the specified types.
 * These instantiations will in turn dispatch to gaussianSmoothMultiArray<T,N>(...) instantiations for the dimensionalities specified there.
 */
JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraWrapper_gaussianSmoothMultiArray(
		JNIEnv *env, jclass, jlongArray shape, jint typeId, jlong sourceData,
		jlong destData, jdouble sigma )
{
	using namespace vigra;
	// to get UInt8 and Int32
#define F(T) gaussianSmoothMultiArray<T>(env, shape, typeId, sourceData, destData, sigma)
	ALLOW_TYPES( typeId, UInt8, Int32, float )
#undef F
}




template< class T, unsigned int N >
void gaussianSmoothMultiArray( MultiArrayInfo sourceInfo, MultiArrayInfo destInfo, jdouble sigma )
{
	TinyVectorView< jlong, N > v( &sourceInfo.shape[0] );
	MultiArrayView< N, T > source( v, reinterpret_cast< T* >( sourceInfo.dataPtr ) );
	MultiArrayView< N, T > dest( v, reinterpret_cast< T* >( destInfo.dataPtr ) );
	gaussianSmoothMultiArray( source, dest, sigma );
}

template< class T >
void gaussianSmoothMultiArray( MultiArrayInfo sourceInfo, MultiArrayInfo destInfo, jdouble sigma )
{
#define F(N) gaussianSmoothMultiArray<T,N>(sourceInfo, destInfo, sigma)
	ALLOW_DIMENSIONS( sourceInfo.shape.size(), 1, 2, 3 )
#undef F
}

/*
 * Class:     net_imglib2_vigra_VigraWrapper
 * Method:    gaussianSmoothMultiArrayI
 * Signature: (Lnet/imglib2/vigra/MultiArrayInfo;Lnet/imglib2/vigra/MultiArrayInfo;D)V
 */
JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraWrapper_gaussianSmoothMultiArrayI
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






MultiArrayInfo getMultiArrayInfo( JNIEnv *env, jobject multiArrayInfo )
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
	std::vector< jlong > shape( shape_v, shape_v + shape_length );
	jint typeId = env->GetIntField( multiArrayInfo, typeId_field );
	void* dataPtr = reinterpret_cast< void* >( env->GetLongField( multiArrayInfo, dataPtr_field ) );

	return MultiArrayInfo( shape, typeId, dataPtr );
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

/*
 * Class:     net_imglib2_vigra_VigraWrapper
 * Method:    doMultiArrayInfo
 * Signature: (Lnet/imglib2/vigra/MultiArrayInfo;)V
 */
JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraWrapper_doMultiArrayInfo(
		JNIEnv *env, jclass, jobject multiArrayInfo )
{
	printMultiArrayInfo( MultiArrayInfo( env, multiArrayInfo ) );
}

//JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraWrapper_arrayMetadata
//  (JNIEnv *env, jclass, jobject sourceData)
//{
//    jclass clazz = env->GetObjectClass(sourceData);
//    jmethodID mid = env->GetMethodID(clazz, "getClass", "()Ljava/lang/Class;");
//    if(mid == 0)
//    {
//        std::cerr << "Method not found\n";
//        return;
//    }
//    jobject arrayClass = env->CallObjectMethod(sourceData, mid);
//    jclass clazz2 = env->GetObjectClass(arrayClass);
//    jmethodID mid2 = env->GetMethodID(clazz2, "toString", "()Ljava/lang/String;");
//    if(mid2 == 0)
//    {
//        std::cerr << "Method 2 not found\n";
//        return;
//    }
//
//    jstring className = (jstring)env->CallObjectMethod(arrayClass, mid2);
//    jboolean isCopy = false;
//    std::cerr << env->GetStringUTFChars(className, &isCopy) << "\n#############################\n\n";
//}
