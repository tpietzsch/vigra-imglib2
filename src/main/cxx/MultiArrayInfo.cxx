#include "MultiArrayInfo.h"

bool MultiArrayInfo::isInitialized = false;
jclass MultiArrayInfo::MultiArrayInfo_class;
jfieldID MultiArrayInfo::typeId_field;
jfieldID MultiArrayInfo::shape_field;
jfieldID MultiArrayInfo::dataPtr_field;

using namespace vigra;

MultiArrayInfo::MultiArrayInfo( JNIEnv *env, jobject multiArrayInfo )
	: env( env ),
	  multiArrayInfo ( multiArrayInfo )
{
	if ( ! isInitialized )
	{
		// get jfieldIDs
		MultiArrayInfo_class = env->FindClass( "net/imglib2/vigra/MultiArrayInfo" );
		typeId_field = env->GetFieldID( MultiArrayInfo_class, "typeId", "I" );
		shape_field = env->GetFieldID( MultiArrayInfo_class, "shape", "[J" );
		dataPtr_field = env->GetFieldID( MultiArrayInfo_class, "dataPtr", "J" );
		isInitialized = true;
	}

	this->env = env;
	this->multiArrayInfo = multiArrayInfo;
}

jsize MultiArrayInfo::dim()
{
	jlongArray shape_arr = reinterpret_cast< jlongArray >( env->GetObjectField( multiArrayInfo, shape_field ) );
	return env->GetArrayLength( shape_arr );
}

jint MultiArrayInfo::typeId()
{
	return env->GetIntField( multiArrayInfo, typeId_field );
}
