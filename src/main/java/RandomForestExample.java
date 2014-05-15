import java.util.Random;

import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgUnsafeFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.vigra.RandomForest;
import net.imglib2.vigra.VigraWrapper;

public class RandomForestExample
{
	public static < L extends RealType< L > > void checkPrediction(
			final RandomAccessibleInterval< L > groundtruth,
			final RandomAccessibleInterval< L > predicted )
	{
		assert groundtruth.numDimensions() == 2;
		assert predicted.numDimensions() == 2;
		assert groundtruth.dimension( 1 ) == predicted.dimension( 1 );

		final Random random = new Random();
		final int nSamples = ( int ) groundtruth.dimension( 0 );

		final RandomAccess< L > g = groundtruth.randomAccess();
		final RandomAccess< L > p = predicted.randomAccess();
		g.setPosition( 0, 1 );
		p.setPosition( 0, 1 );
		int correct = 0;
		for ( int i = 0; i < nSamples; ++i )
		{
			g.setPosition( i, 0 );
			final int gv = ( int ) g.get().getRealDouble();

			p.setPosition( i, 0 );
			p.get().getRealDouble();
			final int pv = ( int ) p.get().getRealDouble();

			if ( gv == pv )
				++correct;
		}

		System.out.println( ( 100 * ( double ) correct / nSamples ) + "% correct" );
	}

	private static Random random = new Random();

	public static < F extends RealType< F >, L extends RealType< L > > void createData(
			final RandomAccessibleInterval< F > features,
			final RandomAccessibleInterval< L > response )
	{
		assert features.numDimensions() == 2;
		assert response.numDimensions() == 2;
		assert features.dimension( 1 ) == response.dimension( 1 );
		assert features.dimension( 0 ) == 3;

		final int nSamples = ( int ) features.dimension( 0 );

		final RandomAccess< F > f = features.randomAccess();
		final RandomAccess< L > r = response.randomAccess();
		r.setPosition( 0, 1 );
		for ( int i = 0; i < nSamples; ++i )
		{
			f.setPosition( i, 0 );
			r.setPosition( i, 0 );

			final double x1 = random.nextDouble();
			final double x2 = random.nextDouble();
			final double x3 = random.nextDouble();
			f.setPosition( 0, 1 );
			f.get().setReal( x1 );
			f.setPosition( 1, 1 );
			f.get().setReal( x2 );
			f.setPosition( 2, 1 );
			f.get().setReal( x3 );

			int klass = ( x1 > 0.5  && x2 < 0.5 ) ? 1 : 0;
			if ( random.nextDouble() < 0.01 )
				klass = 1 - klass;
			r.get().setReal( klass );
		}
	}

	public static < F extends NativeType< F > & RealType< F >, L extends NativeType< L > & RealType< L > >
	void main( final F featureType, final L labelType, final int nSamples, final int nTestSamples ) throws Exception
	{
		final RandomForest< L > rf = new RandomForest< L >( labelType );
		final ArrayImgUnsafeFactory< F > featureImgFactory = new ArrayImgUnsafeFactory< F >();
		final ArrayImgUnsafeFactory< L > labelImgFactory = new ArrayImgUnsafeFactory< L >();

		// create training data
		final Img< F > learnFeatures = featureImgFactory.create( new long[] { nSamples, 3 }, featureType );
		final Img< L > learnLabels = labelImgFactory.create( new long[] { nSamples, 1 }, labelType );
		createData( learnFeatures, learnLabels );

		// learn
		long t0 = System.currentTimeMillis();
		rf.learn( ( ArrayImg ) learnFeatures, ( ArrayImg ) learnLabels );
		long t = System.currentTimeMillis() - t0;
		System.out.println("learned from " + nSamples + " samples in " + t + " ms" );

		// create test data
		final Img< F > testFeatures = featureImgFactory.create( new long[] { nTestSamples, 3 }, featureType );
		final Img< L > groundTruth = labelImgFactory.create( new long[] { nTestSamples, 1 }, labelType );
		final Img< L > predictedLabels = labelImgFactory.create( new long[] { nTestSamples, 1 }, labelType );
		createData( testFeatures, groundTruth );

		// predict
		t0 = System.currentTimeMillis();
		rf.predictLabels( ( ArrayImg ) testFeatures, ( ArrayImg ) predictedLabels );
		t = System.currentTimeMillis() - t0;
		System.out.println("predicted " + nTestSamples + " samples in " + t + " ms" );

		// compare prediction to ground truth
		checkPrediction( groundTruth, predictedLabels );

		rf.destroy();
	}

	public static void main( final String[] args ) throws Exception
	{
		VigraWrapper.init();
		main( new FloatType(), new UnsignedByteType(), 100, 100000 );
	}
}
