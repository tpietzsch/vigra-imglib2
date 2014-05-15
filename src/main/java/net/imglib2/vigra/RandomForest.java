package net.imglib2.vigra;

import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.unsafe.UnsafeDataAccess;
import net.imglib2.type.NativeType;
import net.imglib2.type.NativeTypeId;
import net.imglib2.type.numeric.RealType;

public final class RandomForest< T extends NativeType< T > & RealType< T > >
{
	private final long ptr;

	private final int typeId;

	public RandomForest( final T type )
	{
		typeId = NativeTypeId.forType( type ).getIntegerId();
		ptr = constructor( typeId );
		System.out.println( "ptr = " + ptr );
	}

	public void destroy()
	{
		System.out.println( "destroy: TODO." );
		// TODO
	}

	public < A extends UnsafeDataAccess< A > > void learn(
			final ArrayImg< T, A > features,
			final ArrayImg< T, A > response )
	{
		learn( new MultiArrayInfo( features ), new MultiArrayInfo( response ) );
	}

	@Override
	protected void finalize() throws Throwable
	{
		// TODO Auto-generated method stub
		super.finalize();
	}

	private static native long constructor( int typeId );

	private native void learn( MultiArrayInfo features, MultiArrayInfo response );
}
