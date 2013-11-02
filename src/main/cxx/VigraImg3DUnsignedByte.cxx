#include <vigra/impex.hxx>
#include <vigra/multi_array.hxx>

#include "net_imglib2_vigra_VigraImg3DUnsignedByte.h"

typedef vigra::MultiArray<3, vigra::UInt8> TYPE;

JNIEXPORT jlong JNICALL Java_net_imglib2_vigra_VigraImg3DUnsignedByte_init
  (JNIEnv *env, jclass clazz, jlong width, jlong height, jlong depth)
{
	TYPE *array = new TYPE(vigra::Shape3(width, height, depth));
	return reinterpret_cast<jlong>(array);
}

JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraImg3DUnsignedByte_gradient
  (JNIEnv *env, jclass clazz, jlong pointer)
{
	TYPE *array = reinterpret_cast<TYPE *>(pointer);
	long width = array->shape(0);
	long height = array->shape(1);
	long depth = array->shape(2);
	for (int i = 0; i < width; i++)
		for (int j = 0; j < height; j++)
			for (int k = 0; k < depth; k++)
				(*array)[vigra::Shape3(i, j, k)] = (unsigned char)((i + j + k) & 0xff);
}

JNIEXPORT jlong JNICALL Java_net_imglib2_vigra_VigraImg3DUnsignedByte_shape
  (JNIEnv *env, jclass clazz, jlong pointer, jlong d)
{
	TYPE *array = reinterpret_cast<TYPE *>(pointer);
	return array->shape(d);
}

JNIEXPORT jbyte JNICALL Java_net_imglib2_vigra_VigraImg3DUnsignedByte_getPixel
  (JNIEnv *env, jclass clazz, jlong pointer, jlong x, jlong y, jlong z)
{
	TYPE *array = reinterpret_cast<TYPE *>(pointer);
	return (*array)[vigra::Shape3(x, y, z)];
}

JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraImg3DUnsignedByte_setPixel
  (JNIEnv *env, jclass clazz, jlong pointer, jlong x, jlong y, jlong z, jbyte value)
{
	TYPE *array = reinterpret_cast<TYPE *>(pointer);
	(*array)[vigra::Shape3(x, y, z)] = (unsigned char)value;
}

JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraImg3DUnsignedByte_exportImage
  (JNIEnv *env, jclass clazz, jlong pointer, jstring path)
{
	TYPE *array = reinterpret_cast<TYPE *>(pointer);
	const char *chars = env->GetStringUTFChars(path, NULL);
	vigra::exportImage(array->bind<2>(0), chars);
	env->ReleaseStringUTFChars(path, chars);
}
