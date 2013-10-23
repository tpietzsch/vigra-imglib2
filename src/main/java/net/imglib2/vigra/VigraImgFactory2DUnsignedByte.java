package net.imglib2.vigra;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;

/**
 * An {@link ImgFactory} for VIGRA-backed images.
 * 
 * @author Johannes Schindelin
 */
public class VigraImgFactory2DUnsignedByte {

	public Img<UnsignedByteType> createImg(long width, long height) {
		return new VigraImg2DUnsignedByte(width, height);
	}

}
