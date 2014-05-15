import java.util.Random;

import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgUnsafeFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import net.imglib2.vigra.RandomForest;
import net.imglib2.vigra.VigraWrapper;

public class RandomForestExample
{
	public static < F extends RealType< F >, L extends RealType< L > > void createData( final RandomAccessibleInterval< F > features, final RandomAccessibleInterval< L > response )
	{
		assert features.numDimensions() == 2;
		assert response.numDimensions() == 2;
		assert features.dimension( 0 ) == response.dimension( 0 );
		assert features.dimension( 1 ) == 3;

		final Random random = new Random();
		final int nSamples = ( int ) features.dimension( 0 );

		final RandomAccess< F > f = features.randomAccess();
		final RandomAccess< L > r = response.randomAccess();
		r.setPosition( 0, 0 );
		for ( int i = 0; i < nSamples; ++i )
		{
			f.setPosition( i, 1 );
			r.setPosition( i, 1 );

			final double x1 = random.nextDouble();
			final double x2 = random.nextDouble();
			final double x3 = random.nextDouble();
			f.setPosition( 0, 0 );
			f.get().setReal( x1 );
			f.setPosition( 1, 0 );
			f.get().setReal( x2 );
			f.setPosition( 2, 0 );
			f.get().setReal( x3 );

			final int klass = ( x1 > 0.5  && x2 < 0.5 ) ? 1 : 0;
			r.get().setReal( klass );
		}
	}

	public static void main( final String[] args )
	{
		VigraWrapper.init();

		// create training data
		final FloatType type = new FloatType();
		final int nSamples = 1000;
		final ArrayImgUnsafeFactory< FloatType > imgFactory = new ArrayImgUnsafeFactory< FloatType >();
		final Img< FloatType > features = imgFactory.create( new long[] { nSamples, 3 }, type );
		final Img< FloatType > response = imgFactory.create( new long[] { nSamples, 1 }, type );
		createData( Views.permute( features, 0, 1 ), Views.permute( response, 0, 1 )  );

		final RandomForest< FloatType > rf = new RandomForest< FloatType >( type );
		rf.learn( ( ArrayImg ) features, ( ArrayImg ) response );
		rf.destroy();
	}
}
