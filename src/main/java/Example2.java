import ij.ImageJ;
import io.scif.SCIFIO;
import io.scif.img.ImgOpener;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgUnsafeFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.vigra.VigraWrapper;

public class Example2
{
	public static <T extends NativeType<T> & RealType< T > > void smoothWithVigra( final T type ) throws Exception
	{
		final String fn = "ghouse.png";
		final SCIFIO scifio = new SCIFIO();
		final ImgOpener opener = new ImgOpener( scifio.getContext() );
		final ArrayImgUnsafeFactory< T > imgFactory = new ArrayImgUnsafeFactory< T >();
		final Img< T > source = opener.openImg( fn, imgFactory, type ).getImg();
		final Img< T > dest = imgFactory.create( source, type );
		scifio.getContext().dispose();

		VigraWrapper.gaussianSmoothMultiArray( ( ArrayImg ) source, ( ArrayImg ) dest, 3.0 );
		new ImageJ();
		ImageJFunctions.show( source );
		ImageJFunctions.show( dest );
	}

	public static void main( final String... args ) throws Exception
	{
//		smoothWithVigra( new UnsignedByteType() ); // doesn't work currently due to a scifio limitation
//		smoothWithVigra( new IntType() );
		smoothWithVigra( new FloatType() );
	}
}
