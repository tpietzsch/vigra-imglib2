package net.imglib2.type;

import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;


public enum NativeTypeId
{
	Other           ( "Other", -1 ),

	Byte            ( "Byte",         0 ),
	UnsignedByte    ( "UnsignedByte", 1 ),
	Int             ( "Int",          2 ),
	UnsignedInt     ( "UnsignedInt",  3 ),
	Float           ( "Float",        4 ),
	Double          ( "Double",       5 );

	private final String name;

	private final int intId;

	public String getName()
	{
		return name;
	}

	public int getIntegerId()
	{
		return intId;
	}

	private NativeTypeId( final String name, final int intId )
	{
		this.name = name;
		this.intId = intId;
	}

	static public < T extends NativeType< T > > NativeTypeId forType( final T type )
	{
		// return type.getNativeTypeId();
		if ( ByteType.class.isInstance( type ) )
			return Byte;
		else if ( UnsignedByteType.class.isInstance( type ) )
			return UnsignedByte;
		else if ( IntType.class.isInstance( type ) )
			return Int;
		else if ( UnsignedIntType.class.isInstance( type ) )
			return UnsignedInt;
		else if ( FloatType.class.isInstance( type ) )
			return Float;
		else if ( DoubleType.class.isInstance( type ) )
			return Double;
		return Other;
	}
}
