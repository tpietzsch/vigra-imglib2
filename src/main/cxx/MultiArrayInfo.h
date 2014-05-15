#ifndef MULTI_ARRAY_INFO_H
#define MULTI_ARRAY_INFO_H

#include <jni.h>
#include <vigra/multi_array.hxx>

class MultiArrayInfo
{
private:
	JNIEnv *env;
	jobject multiArrayInfo;

	static bool isInitialized;
	static jclass MultiArrayInfo_class;
	static jfieldID typeId_field;
	static jfieldID shape_field;
	static jfieldID dataPtr_field;

public:
	MultiArrayInfo( JNIEnv *env, jobject multiArrayInfo );

	jsize dim();

	jint typeId();

	template< unsigned int N, class T > vigra::MultiArrayView< N, T > getMultiArray();
};

template< unsigned int N, class T > vigra::MultiArrayView< N, T > MultiArrayInfo::getMultiArray()
{
	jlongArray shape_arr = reinterpret_cast< jlongArray >( env->GetObjectField( multiArrayInfo, shape_field ) );
	vigra::TinyVector< jlong, N > shape( vigra::SkipInitialization );
	env->GetLongArrayRegion( shape_arr, 0, N, shape.begin() );
	T* data = reinterpret_cast< T* >( env->GetLongField( multiArrayInfo, dataPtr_field ) );
	return vigra::MultiArrayView< N, T >( shape, data );
}


#endif // MULTI_ARRAY_INFO_H
