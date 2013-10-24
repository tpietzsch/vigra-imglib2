package net.imglib2.vigra;
import java.util.Arrays;

import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;

/**
 * An {@link ImgFactory} for VIGRA-backed images.
 * 
 * @author Johannes Schindelin
 */
public class VigraImgFactory2DUnsignedByte extends ImgFactory<UnsignedByteType> {

	@Override
	public Img<UnsignedByteType> create(long[] dimensions, UnsignedByteType type) {
		if (dimensions.length != 2) throw new UnsupportedOperationException("Not 2D: " + Arrays.toString(dimensions));
		return new VigraImg2DUnsignedByte(dimensions[0], dimensions[1]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> ImgFactory<S> imgFactory(S type)
			throws IncompatibleTypeException {
		if (type instanceof UnsignedByteType) return (ImgFactory<S>) this;
		throw new IncompatibleTypeException(type, null);
	}

}
