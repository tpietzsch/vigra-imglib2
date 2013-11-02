package net.imglib2.vigra;
import net.imglib2.Localizable;
import net.imglib2.RandomAccess;
import net.imglib2.Sampler;
import net.imglib2.img.basictypeaccess.ByteAccess;
import net.imglib2.type.numeric.integer.UnsignedByteType;

/**
 * A {@link RandomAccess} of a VIGRA-backed, two-dimensional image of unsigned byte type.
 * 
 * @author Johannes Schindelin
 */
public class VigraRandomAccess2DUnsignedByteType implements
		RandomAccess<UnsignedByteType> {
	private VigraImg2DUnsignedByte img;
	private long x, y;

	public VigraRandomAccess2DUnsignedByteType(
			VigraImg2DUnsignedByte img) {
		this.img = img;
	}

	@Override
	public void localize(int[] position) {
		position[0] = (int)x;
		position[1] = (int)y;
	}

	@Override
	public void localize(long[] position) {
		position[0] = x;
		position[1] = y;
	}

	@Override
	public int getIntPosition(int d) {
		return d == 0 ? (int)x : (int)y;
	}

	@Override
	public long getLongPosition(int d) {
		return d == 0 ? x : y;
	}

	@Override
	public void localize(float[] position) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void localize(double[] position) {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getFloatPosition(int d) {
		return getLongPosition(d);
	}

	@Override
	public double getDoublePosition(int d) {
		return getLongPosition(d);
	}

	@Override
	public int numDimensions() {
		return 2;
	}

	@Override
	public void fwd(int d) {
		if (++x >= img.max(0)) {
			x = 0;
			++y;
		}
	}

	@Override
	public void bck(int d) {
		if (--x < 0) {
			x = img.max(0);
			--y;
		}
	}

	@Override
	public void move(int distance, int d) {
		if (d == 0) x += distance;
		else y += distance;
	}

	@Override
	public void move(long distance, int d) {
		if (d == 0) x += distance;
		else y += distance;
	}

	@Override
	public void move(Localizable localizable) {
		x += localizable.getLongPosition(0);
		y += localizable.getLongPosition(1);
	}

	@Override
	public void move(int[] distance) {
		x += distance[0];
		y += distance[1];
	}

	@Override
	public void move(long[] distance) {
		x += distance[0];
		y += distance[1];
	}

	@Override
	public void setPosition(Localizable localizable) {
		x = localizable.getLongPosition(0);
		y = localizable.getLongPosition(1);
	}

	@Override
	public void setPosition(int[] position) {
		x = position[0];
		y = position[1];
	}

	@Override
	public void setPosition(long[] position) {
		x = position[0];
		y = position[1];
	}

	@Override
	public void setPosition(int position, int d) {
		if (d == 0) x = position;
		else y = position;
	}

	@Override
	public void setPosition(long position, int d) {
		if (d == 0) x = position;
		else y = position;
	}

	@Override
	public UnsignedByteType get() {
		return new UnsignedByteType(new ByteAccess() {

			@Override
			public byte getValue(int index) {
				return img.getPixel(x, y);
			}

			@Override
			public void setValue(int index, byte value) {
				img.setPixel(x, y, value);
			}

		});
	}

	@Override
	public Sampler<UnsignedByteType> copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RandomAccess<UnsignedByteType> copyRandomAccess() {
		// TODO Auto-generated method stub
		return null;
	}

}
