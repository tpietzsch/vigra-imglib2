package net.imglib2.vigra;

import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.unsafe.UnsafeDataAccess;
import net.imglib2.type.NativeType;

public class VigraTensorWrapper 
{
	static
	{
		NarHelper.loadLibrary( VigraWrapper.class, "net.imglib2.vigra", "vigra-imglib2" );
	}

	public static < T extends NativeType< T >, A extends UnsafeDataAccess< A > > void boundaryTensor2d(
			final ArrayImg< T, A > source,
			final ArrayImg< T, A > dest,
			final double sigma ) throws Exception
	{
		int ndims = source.numDimensions();
		
		// reshape the MultiArrayInfo so that vigra sees a X x Y image
		long[] shape = new long[ ndims];
		for(int d=0; d<ndims; d++){
			shape[ d ] = source.dimension(d);
		}
		
		boundaryTensor2d( new MultiArrayInfo( source ), new MultiArrayInfo( dest, shape ), sigma );
	}
	
	public static native void boundaryTensor2d( MultiArrayInfo source, MultiArrayInfo dest, double sigma ) throws Exception;
}
