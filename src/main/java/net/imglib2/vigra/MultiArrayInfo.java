package net.imglib2.vigra;

import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.unsafe.UnsafeDataAccess;
import net.imglib2.type.NativeType;
import net.imglib2.type.NativeTypeId;

public class MultiArrayInfo
{
	
	protected final long[] shape;		// the shape of the data as observed by vigra
	
	protected final long[] javaShape;	// the shape of the data as observed by imglib2
										//   this difference is important when, for example
										//   vigra interprets an array as size [X,Y] where
										//   each pixel represents an N-vector, 
										//   but imglib2 interprets the array as an [N,X,Y] interval
	
	protected final int typeId;

	protected final long dataPtr;

	MultiArrayInfo( final long[] shape, final NativeTypeId typeId, final long dataPtr )
	{
		this.shape = shape;
		this.javaShape = shape;
		this.typeId = typeId.getIntegerId();
		this.dataPtr = dataPtr;
	}

	MultiArrayInfo( final long[] shape, final long[] javaShape, final NativeTypeId typeId, final long dataPtr )
	{
		this.shape = shape;
		this.javaShape = javaShape;
		this.typeId = typeId.getIntegerId();
		this.dataPtr = dataPtr;
	}
	
	public < T extends NativeType< T >, A extends UnsafeDataAccess< A > > MultiArrayInfo( final ArrayImg< T, A > source )
	{
		this.shape = new long[ source.numDimensions() ];
		source.dimensions( this.shape );
		this.javaShape = new long[ source.numDimensions() ];
		source.dimensions(this.javaShape);
		this.typeId = NativeTypeId.forType( source.firstElement() ).getIntegerId();
		this.dataPtr = ( Long ) source.update( null ).getCurrentStorageArray();
	}
	
	/**
	 * This constructor allows java and c++ to interpret the UnsafeDataAccess shape differently
	 * @param source
	 * @param shape the shape as seen by c++ / Vigra
	 */
	public < T extends NativeType< T >, A extends UnsafeDataAccess< A > > MultiArrayInfo( final ArrayImg< T, A > source, final long[] shape )
	{
		this.shape = shape;
		this.javaShape = new long[ source.numDimensions() ];
		source.dimensions( this.javaShape );
		this.typeId = NativeTypeId.forType( source.firstElement() ).getIntegerId();
		this.dataPtr = ( Long ) source.update( null ).getCurrentStorageArray();
	}
}
