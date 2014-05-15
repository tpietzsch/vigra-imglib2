#include <iostream>
#include <vigra/error.hxx>

#include "net_imglib2_vigra_exception_VigraExceptionTestHelper.h"
#include "VigraWrapper.hxx"

using namespace vigra;

JNIEXPORT void JNICALL Java_net_imglib2_vigra_exception_VigraExceptionTestHelper_throwPrecondition( JNIEnv *env, jclass clazz )
{
   try
   {
      throw PreconditionViolation("Test throw of VigraPreconditionViolation");
   }
   CATCH_CPP_EXCEPTION_THROW_JAVA_EXCEPTION
}

void Java_net_imglib2_vigra_exception_VigraExceptionTestHelper_throwPostcondition( JNIEnv *env, jclass clazz )
{
   try
   {
      throw PostconditionViolation("Test throw of VigraPostconditionViolation");
   }
   CATCH_CPP_EXCEPTION_THROW_JAVA_EXCEPTION
}

void Java_net_imglib2_vigra_exception_VigraExceptionTestHelper_throwInvariant( JNIEnv *env, jclass clazz )
{
   try
   {
      throw InvariantViolation("Test throw of VigraInvariantViolation");
   }
   CATCH_CPP_EXCEPTION_THROW_JAVA_EXCEPTION
}

