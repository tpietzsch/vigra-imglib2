#include <vigra/multi_array.hxx>

#include "net_imglib2_vigra_VigraImg2DUnsignedByte.h"

typedef vigra::MultiArray<2, vigra::UInt8> TYPE;

JNIEXPORT jlong JNICALL Java_net_imglib2_vigra_VigraImg2DUnsignedByte_init
  (JNIEnv *env, jclass clazz, jlong width, jlong height)
{
	TYPE *array = new TYPE(vigra::Shape2(width, height));
	return reinterpret_cast<jlong>(array);
}

JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraImg2DUnsignedByte_gradient
  (JNIEnv *env, jclass clazz, jlong pointer)
{
	TYPE *array = reinterpret_cast<TYPE *>(pointer);
	long width = array->shape(0);
	long height = array->shape(1);
	for (int i = 0; i < width; i++)
		for (int j = 0; j < height; j++)
			(*array)[vigra::Shape2(i, j)] = (unsigned char)((i + j) & 0xff);
}

JNIEXPORT jlong JNICALL Java_net_imglib2_vigra_VigraImg2DUnsignedByte_shape
  (JNIEnv *env, jclass clazz, jlong pointer, jlong d)
{
	TYPE *array = reinterpret_cast<TYPE *>(pointer);
	return array->shape(d);
}

JNIEXPORT jbyte JNICALL Java_net_imglib2_vigra_VigraImg2DUnsignedByte_getPixel
  (JNIEnv *env, jclass clazz, jlong pointer, jlong x, jlong y)
{
	TYPE *array = reinterpret_cast<TYPE *>(pointer);
	return (*array)[vigra::Shape2(x, y)];
}

JNIEXPORT void JNICALL Java_net_imglib2_vigra_VigraImg2DUnsignedByte_setPixel
  (JNIEnv *env, jclass clazz, jlong pointer, jlong x, jlong y, jbyte value)
{
	TYPE *array = reinterpret_cast<TYPE *>(pointer);
	(*array)[vigra::Shape2(x, y)] = (unsigned char)value;
}
