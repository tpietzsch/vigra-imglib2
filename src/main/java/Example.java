/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

import io.scif.SCIFIO;
import io.scif.img.ImgOpener;
import net.imglib2.img.ImgFactory;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.vigra.VigraImg3DUnsignedByte;
import net.imglib2.vigra.VigraImgFactory3DUnsignedByte;

/** Loads and displays a dataset using the ImageJ API. */
public class Example {

	public static void main(final String... args) {
		try {
			main();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void main() throws Exception {
		final SCIFIO scifio = new SCIFIO();
		final ImgOpener opener = new ImgOpener(scifio.getContext());
		final ImgFactory<UnsignedByteType> imgFactory = new VigraImgFactory3DUnsignedByte();
		final String source = "/tmp/img.pgm";
		@SuppressWarnings("unchecked")
		ImgPlus<UnsignedByteType> img = opener.openImg(source, imgFactory);
		VigraImg3DUnsignedByte vigraImg = (VigraImg3DUnsignedByte)img.getImg();
		vigraImg.exportImage("/tmp/img.tiff");
		scifio.getContext().dispose();
	}
}
