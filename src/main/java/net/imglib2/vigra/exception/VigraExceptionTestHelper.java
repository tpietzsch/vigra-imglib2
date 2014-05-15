package net.imglib2.vigra.exception;

import net.imglib2.vigra.NarHelper;
import net.imglib2.vigra.VigraWrapper;

public class VigraExceptionTestHelper {
	static
	{
		NarHelper.loadLibrary( VigraWrapper.class, "net.imglib2.vigra", "vigra-imglib2" );
	}

	public static native void throwPrecondition() throws Exception;
	public static native void throwPostcondition() throws Exception;
	public static native void throwInvariant() throws Exception;

}
