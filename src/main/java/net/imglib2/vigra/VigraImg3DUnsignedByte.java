package net.imglib2.vigra;
import java.util.Iterator;

import net.imglib2.Cursor;
import net.imglib2.FlatIterationOrder;
import net.imglib2.Interval;
import net.imglib2.IterableRealInterval;
import net.imglib2.Positionable;
import net.imglib2.RandomAccess;
import net.imglib2.RealPositionable;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;

/**
 * A VIGRA-backed, two-dimensional image of unsigned byte type.
 * 
 * @author Johannes Schindelin
 */
public class VigraImg3DUnsignedByte implements Img<UnsignedByteType> {

	static {
		NarHelper.loadLibrary(VigraImg3DUnsignedByte.class, "net.imglib2.vigra", "vigra-imglib2");
	}

	private final long pointer;

	public VigraImg3DUnsignedByte(long width, long height, long depth) {
		pointer = init(width, height, depth);
		gradient(pointer);
	}

	private static native long init(long width, long height, long depth);
	private static native void gradient(long pointer);
	private static native long shape(long pointer, long d);
	private static native byte getPixel(long pointer, long x, long y, long z);
	private static native void setPixel(long pointer, long x, long y, long z, byte value);
	private static native void exportImage(long pointer, String path);

	@Override
	public RandomAccess<UnsignedByteType> randomAccess() {
		return new VigraRandomAccess3DUnsignedByteType(this);
	}

	@Override
	public RandomAccess<UnsignedByteType> randomAccess(Interval interval) {
		if (interval.min(0) == min(0) && interval.min(1) == min(1) && interval.min(2) == min(2) && interval.max(0) == max(0) && interval.max(1) == max(1) && interval.max(2) == max(2)) {
			return randomAccess();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public int numDimensions() {
		return 3;
	}

	@Override
	public long min(int d) {
		return 0;
	}

	@Override
	public void min(long[] min) {
		min[0] = min[1] = 0;
	}

	@Override
	public void min(Positionable min) {
		min.setPosition(new long[] { 0, 0, 0 });
	}

	@Override
	public long max(int d) {
		return shape(pointer, d) - 1;
	}

	@Override
	public void max(long[] max) {
		for (int i = 0; i < 3; ++i) {
			max[i] = max(i);
		}
	}

	@Override
	public void max(Positionable max) {
		for (int i = 0; i < 3; ++i) {
			max.setPosition(max(i), i);
		}
	}

	@Override
	public double realMin(int d) {
		return 0;
	}

	@Override
	public void realMin(double[] min) {
		min[0] = min[1] = min[2] = 0;
	}

	@Override
	public void realMin(RealPositionable min) {
		min.setPosition(new double[] { 0, 0, 0 });
	}

	@Override
	public double realMax(int d) {
		return max(d);
	}

	@Override
	public void realMax(double[] max) {
		for (int i = 0; i < 3; ++i) {
			max[i] = realMax(i);
		}
	}

	@Override
	public void realMax(RealPositionable max) {
		for (int i = 0; i < i; ++i) {
			max.setPosition(realMax(i), i);
		}
	}

	@Override
	public void dimensions(long[] dimensions) {
		max(dimensions);
	}

	@Override
	public long dimension(int d) {
		return shape(pointer, d);
	}

	@Override
	public Cursor<UnsignedByteType> cursor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Cursor<UnsignedByteType> localizingCursor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long size() {
		return dimension(0) * dimension(1) * dimension(2);
	}

	@Override
	public UnsignedByteType firstElement() {
		return randomAccess().get();
	}

	@Override
	public FlatIterationOrder iterationOrder() {
		return new FlatIterationOrder( this );
	}

	@Override
	@Deprecated
	public boolean equalIterationOrder(IterableRealInterval<?> f) {
		return iterationOrder().equals( f.iterationOrder() );
	}

	@Override
	public Iterator<UnsignedByteType> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ImgFactory<UnsignedByteType> factory() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Img<UnsignedByteType> copy() {
		throw new UnsupportedOperationException();
	}

	public byte getPixel(long x, long y, long z) {
		return getPixel(pointer, x, y, z);
	}

	public void setPixel(long x, long y, long z, byte value) {
		setPixel(pointer, x, y, z, value);
	}

	public void exportImage(String path) {
		exportImage(pointer, path);
	}
}
