import ij.ImageJ;
import io.scif.SCIFIO;
import io.scif.img.ImgOpener;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgBufferFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.vigra.VigraWrapper;

public class Example2
{
	public static void main( final String... args ) throws Exception
	{
		final String fn = "/Users/pietzsch/workspace/data/DrosophilaWing.tif";
		final int sourceNumDimensions = 2;

		final SCIFIO scifio = new SCIFIO();
		final ImgOpener opener = new ImgOpener( scifio.getContext() );
		final ImgFactory< FloatType > imgFactory = new ArrayImgFactory< FloatType >();
		final ImgPlus< FloatType > scifioimg = opener.openImg( fn, imgFactory, new FloatType() );

		// scifio thinks the image is 5D. Therefore: copy to a 2D ArrayImg.
		final long[] dims = new long[ sourceNumDimensions ];
		for ( int d = 0; d < sourceNumDimensions; ++d )
			dims[ d ] = scifioimg.dimension( d );
		final ArrayImgBufferFactory< FloatType > bufferImgFactory = new ArrayImgBufferFactory< FloatType >();
		final Img< FloatType > source = bufferImgFactory.create( dims, new FloatType() );
		final Cursor< FloatType > cin = scifioimg.cursor();
		final Cursor< FloatType > cout = source.cursor();
		while ( cout.hasNext() )
			cout.next().set( cin.next() );

		final Img< FloatType > dest = bufferImgFactory.create( dims, new FloatType() );

//		VigraWrapper.invertImage( ( ArrayImg ) source );
		VigraWrapper.gaussianSmoothMultiArray( ( ArrayImg ) source, ( ArrayImg ) dest, 3.0 );

		new ImageJ();
		ImageJFunctions.show( source );
		ImageJFunctions.show( dest );
		scifio.getContext().dispose();
	}
}
