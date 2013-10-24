/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.vigra.VigraImgFactory2DUnsignedByte;

/** Loads and displays a dataset using the ImageJ API. */
public class Example {

	public static void main(final String... args) throws Exception {
		Img<UnsignedByteType> img = new VigraImgFactory2DUnsignedByte().create(new long[] { 256, 256 }, new UnsignedByteType());
		ImageJFunctions.show(img);
	}

}
