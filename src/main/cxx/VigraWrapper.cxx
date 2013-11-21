#include <iostream>
#include <vigra/multi_array.hxx>

#include "net_imglib2_vigra_VigraWrapper.h"
#include "VigraWrapper.hxx"

#include <vigra/multi_convolution.hxx>

using namespace vigra;

template <class T, unsigned int N>
void gaussianSmoothMultiArray(JNIEnv *env, jlongArray shape, jint typeId, jobject sourceData, jobject destData, jdouble sigma )
{
	TinyVector<jlong,N> v;
	env->GetLongArrayRegion( shape, 0, N, &v[0] );
	void* sourcePtr = env->GetDirectBufferAddress(sourceData);
	MultiArrayView<N,T> source( v, reinterpret_cast<T*>(sourcePtr));
	void* destPtr = env->GetDirectBufferAddress(destData);
	MultiArrayView<N,T> dest( v, reinterpret_cast<T*>(destPtr));
	gaussianSmoothMultiArray(source, dest, sigma);
}

template <class T>
void gaussianSmoothMultiArray(JNIEnv *env, jlongArray shape, jint typeId, jobject sourceData, jobject destData, jdouble sigma )
{
	#define F(N) gaussianSmoothMultiArray<T,N>(env, shape, typeId, sourceData, destData, sigma)
	ALLOW_DIMENSIONS(env->GetArrayLength(shape), 1, 2, 3)
}

/*
 * This is the actual JNI call.
 * It uses the ALLOW_TYPES macro to dispatch to gaussianSmoothMultiArray<T>(...) instantiation for the specified types.
 * These instantiations will in turn dispatch to gaussianSmoothMultiArray<T,N>(...) instantiations for the dimensionalities specified there.
 */
JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraWrapper_gaussianSmoothMultiArray
  (JNIEnv *env, jclass, jlongArray shape, jint typeId, jobject sourceData, jobject destData, jdouble sigma)
{
	#define F(T) gaussianSmoothMultiArray<T>(env, shape, typeId, sourceData, destData, sigma)
	ALLOW_TYPES(typeId, UInt8, Int32, float)
}
