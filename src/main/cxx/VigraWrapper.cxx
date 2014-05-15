#include <iostream>
#include <vigra/multi_array.hxx>

#include "VigraWrapper.hxx"

#include "net_imglib2_vigra_VigraWrapper.h"
#include "MultiArrayInfo.h"

#include <vigra/multi_convolution.hxx>

using namespace vigra;

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
	try
	{
		MultiArrayInfo sourceInfo( env, source );
		MultiArrayInfo destInfo( env, dest );

		using namespace vigra;
		// to get UInt8 and Int32
#define F(T) gaussianSmoothMultiArray<T>(sourceInfo, destInfo, sigma)
		ALLOW_TYPES( sourceInfo.typeId(), UInt8, Int32, float )
#undef F
	}
	CATCH_CPP_EXCEPTION_THROW_JAVA_EXCEPTION
}
