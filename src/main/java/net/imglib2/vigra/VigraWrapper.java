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

	public static < T extends NativeType< T >, A extends UnsafeDataAccess< A > > void totalVariation2d(
			final ArrayImg< T, A > source,
			final ArrayImg< T, A > dest,
			final double alpha,
			final int    steps,
			final double eps)
	{
		totalVariation2d( new MultiArrayInfo( source ), new MultiArrayInfo( dest ), alpha, steps, eps );
	}
	
	public static < T extends NativeType< T >, A extends UnsafeDataAccess< A > > void totalVariation2d(
			final ArrayImg< T, A > source,
			final ArrayImg< T, A > dest,
			final double alpha,
			final int    steps)
	{
		totalVariation2d( source, dest, alpha, steps, 0);
	}
	
	private static native void gaussianSmoothMultiArray( MultiArrayInfo source, MultiArrayInfo dest, double sigma );
	private static native void totalVariation2d( MultiArrayInfo source, MultiArrayInfo dest, double alpha, int steps, double eps);

	public static native void catchVigraViolationExample() throws Exception, InvocationTargetException;
}
