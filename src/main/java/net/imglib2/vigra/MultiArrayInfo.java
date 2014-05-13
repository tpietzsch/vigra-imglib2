package net.imglib2.vigra;

import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.unsafe.UnsafeDataAccess;
import net.imglib2.type.NativeType;
import net.imglib2.type.NativeTypeId;

public class MultiArrayInfo
{
	protected final long[] shape;

	protected final int typeId;

	protected final long dataPtr;

	MultiArrayInfo( final long[] shape, final NativeTypeId typeId, final long dataPtr )
	{
		this.shape = shape;
		this.typeId = typeId.getIntegerId();
		this.dataPtr = dataPtr;
	}

	public < T extends NativeType< T >, A extends UnsafeDataAccess< A > > MultiArrayInfo( final ArrayImg< T, A > source )
	{
		this.shape = new long[ source.numDimensions() ];
		source.dimensions( this.shape );
		this.typeId = NativeTypeId.forType( source.firstElement() ).getIntegerId();
		this.dataPtr = ( Long ) source.update( null ).getCurrentStorageArray();
	}
}
