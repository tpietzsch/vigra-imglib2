package net.imglib2.vigra;

import java.nio.Buffer;

import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.buffer.BufferDataAccess;
import net.imglib2.type.NativeType;
import net.imglib2.type.NativeTypeId;
import net.imglib2.type.numeric.integer.UnsignedByteType;

public class VigraWrapper
{
	static
	{
		NarHelper.loadLibrary( VigraWrapper.class, "net.imglib2.vigra", "vigra-imglib2" );
	}

	public static < T extends NativeType< T >, A extends BufferDataAccess< A > > void invertImage( final ArrayImg< T, A > img )
	{
		final T type = img.firstElement();
		if ( UnsignedByteType.class.isInstance( type ) )
		{
			final A a = img.update( null );
			final java.nio.Buffer buffer = ( Buffer ) a.getCurrentStorageArray();

			final long[] shape = new long[ img.numDimensions() ];
			img.dimensions( shape );

			invertNDim( shape, NativeTypeId.forType( type ).getIntegerId(), buffer );
//			invertNDimObject( new VigraImgInfo( shape, type.getNativeTypeId(), buffer ) );
		}
		else
			throw new UnsupportedOperationException( "not implemented for data type " + type.getClass() );
	}

	public static < T extends NativeType< T >, A extends BufferDataAccess< A > > void gaussianSmoothMultiArray(
			final ArrayImg< T, A > source,
			final ArrayImg< T, A > dest,
			final double sigma )
	{
		final NativeTypeId typeId =  NativeTypeId.forType( dest.firstElement() );
		switch( typeId )
		{
			case UnsignedByte:
			case Int:
			case Float:
				final long[] shape = new long[ dest.numDimensions() ];
				dest.dimensions( shape );
				final java.nio.Buffer bSource = ( Buffer ) source.update( null ).getCurrentStorageArray();
				final java.nio.Buffer bDest = ( Buffer ) dest.update( null ).getCurrentStorageArray();
				gaussianSmoothMultiArray( shape, typeId.getIntegerId(), bSource, bDest, sigma );
				break;
			default:
				throw new UnsupportedOperationException( "not implemented for data type " + dest.firstElement().getClass() );
		}
	}

	private static native void invert( int w, int h, Buffer data );

	private static native void invertNDim( long[] shape, int typeId, Buffer data );

	private static native void gaussianSmoothMultiArray( long[] shape, int typeId, Buffer source, Buffer dest, double sigma );

//	private static native void invertNDimObject( VigraImgInfo img );
}
