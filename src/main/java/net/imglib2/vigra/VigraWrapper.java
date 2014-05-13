package net.imglib2.vigra;

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
//		final NativeTypeId typeId =  NativeTypeId.forType( dest.firstElement() );
//		switch( typeId )
//		{
//			case UnsignedByte:
//			case Int:
//			case Float:
//				final long[] shape = new long[ dest.numDimensions() ];
//				dest.dimensions( shape );
//				final long bSource = ( Long ) source.update( null ).getCurrentStorageArray();
//				final long bDest = ( Long ) dest.update( null ).getCurrentStorageArray();
//				gaussianSmoothMultiArray( shape, typeId.getIntegerId(), bSource, bDest, sigma );
//				break;
//			default:
//				throw new UnsupportedOperationException( "not implemented for data type " + dest.firstElement().getClass() );
//		}
		gaussianSmoothMultiArrayI( new MultiArrayInfo( source ), new MultiArrayInfo( dest ), sigma );
	}

	private static native void gaussianSmoothMultiArray( long[] shape, int typeId, long source, long dest, double sigma );

	private static native void gaussianSmoothMultiArrayI( MultiArrayInfo source, MultiArrayInfo dest, double sigma );

	public static < T extends NativeType< T >, A extends UnsafeDataAccess< A > > void doMultiArrayInfo( final ArrayImg< T, A > source )
	{
		doMultiArrayInfo( new MultiArrayInfo( source ) );
	}

	private static native void doMultiArrayInfo( MultiArrayInfo img );
}
