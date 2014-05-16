import ij.ImageJ;
import io.scif.SCIFIO;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgUnsafeFactory;
import net.imglib2.img.basictypeaccess.unsafe.FloatUnsafeAccess;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;
import net.imglib2.vigra.VigraTensorWrapper;

public class Example3
{
	
	public static void main( final String... args ) throws Exception
	{
		boundary2dWithVigra( new FloatType() );
		
		System.out.println("finished");
		
	}

	public static <T extends NativeType<T> & RealType< T > > void boundary2dWithVigra( final T type ) throws ImgIOException
	{
		final String fn = "ghouse.png";
		final SCIFIO scifio = new SCIFIO();
		final ImgOpener opener = new ImgOpener( scifio.getContext() );
		final ArrayImgUnsafeFactory< T > imgFactory = new ArrayImgUnsafeFactory< T >();
		final Img< T > source = opener.openImg( fn, imgFactory, type ).getImg();
		

		long[] destdims = new long[3];
		destdims[0] = 3;
		destdims[1] = source.dimension(0);
		destdims[2] = source.dimension(1);
		
		System.out.println(" destdims [0]: " + destdims[0]);
		System.out.println(" destdims [1]: " + destdims[1]);
		System.out.println(" destdims [2]: " + destdims[2]);
		
		final ArrayImg< T, FloatUnsafeAccess > dest = (ArrayImg<T, FloatUnsafeAccess>) imgFactory.create( destdims, type );
		scifio.getContext().dispose();

		try
		{
			VigraTensorWrapper.boundaryTensor2d( ( ArrayImg ) source, ( ArrayImg ) dest, 3.0 );
			
			IntervalView<T> dp = Views.permute( Views.permute(dest, 2, 0), 0, 1 );
			new ImageJ();
			ImageJFunctions.show( source );
			ImageJFunctions.show( dp );
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

}
