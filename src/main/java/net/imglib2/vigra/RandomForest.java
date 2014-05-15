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

	public RandomForest( final T type ) throws Exception
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

	public < F extends NativeType< F > & RealType< F >, FA extends UnsafeDataAccess< FA >, TA extends UnsafeDataAccess< TA > >
	void learn(
			final ArrayImg< F, FA > features,
			final ArrayImg< T, TA > response ) throws Exception
	{
		learn( new MultiArrayInfo( features ), new MultiArrayInfo( response ) );
	}

	public < F extends NativeType< F > & RealType< F >, FA extends UnsafeDataAccess< FA >, TA extends UnsafeDataAccess< TA > >
	void predictLabels(
			final ArrayImg< F, FA > features,
			final ArrayImg< T, TA > predicted ) throws Exception
	{
		predictLabels( new MultiArrayInfo( features ), new MultiArrayInfo( predicted ) );
	}

	@Override
	protected void finalize() throws Throwable
	{
		// TODO Auto-generated method stub
		super.finalize();
	}

	private static native long constructor( int typeId ) throws Exception;

	private native void learn( MultiArrayInfo features, MultiArrayInfo response ) throws Exception;

	private native void predictLabels( MultiArrayInfo features, MultiArrayInfo predicted ) throws Exception;
}
