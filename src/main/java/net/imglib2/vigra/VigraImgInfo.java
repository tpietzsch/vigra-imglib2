package net.imglib2.vigra;

import java.nio.Buffer;

import net.imglib2.type.NativeTypeId;

public class VigraImgInfo
{
	public final long[] shape;

	public final int typeId;

	public final Buffer data;

	VigraImgInfo( final long[] shape, final NativeTypeId typeId, final Buffer data )
	{
		this.shape = shape;
		this.typeId = typeId.getIntegerId();
		this.data = data;
	}
}