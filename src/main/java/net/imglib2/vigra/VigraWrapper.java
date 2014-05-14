package net.imglib2.vigra;

import java.lang.reflect.InvocationTargetException;

import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.unsafe.UnsafeDataAccess;
import net.imglib2.type.NativeType;

public class VigraWrapper
{
	static
	{
		NarHelper.loadLibrary( VigraWrapper.class, "net.imglib2.vigra", "vigra-imglib2" );
	}

	public static < T extends NativeType< T >, A extends UnsafeDataAccess< A > > void gaussianSmoothMultiArray(
			final ArrayImg< T, A > source,
			final ArrayImg< T, A > dest,
			final double sigma )
	{
		gaussianSmoothMultiArray( new MultiArrayInfo( source ), new MultiArrayInfo( dest ), sigma );
	}

	private static native void gaussianSmoothMultiArray( MultiArrayInfo source, MultiArrayInfo dest, double sigma );

	public static native void catchVigraViolationExample() throws Exception, InvocationTargetException;
}
