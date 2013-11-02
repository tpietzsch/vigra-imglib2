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
public class VigraRandomAccess3DUnsignedByteType implements
		RandomAccess<UnsignedByteType> {
	private VigraImg3DUnsignedByte img;
	private long x, y, z;

	public VigraRandomAccess3DUnsignedByteType(
			VigraImg3DUnsignedByte img) {
		this.img = img;
	}

	@Override
	public void localize(int[] position) {
		position[0] = (int)x;
		position[1] = (int)y;
		position[2] = (int)z;
	}

	@Override
	public void localize(long[] position) {
		position[0] = x;
		position[1] = y;
		position[2] = z;
	}

	@Override
	public int getIntPosition(int d) {
		return d == 0 ? (int)x : d == 1 ? (int)y : (int)z;
	}

	@Override
	public long getLongPosition(int d) {
		return d == 0 ? x : d == 1 ? y : z;
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
		return 3;
	}

	@Override
	public void fwd(int d) {
		if (++x >= img.max(0)) {
			x = 0;
			if (++y >= img.max(1)) {
				y = 0;
				++z;
			}
		}
	}

	@Override
	public void bck(int d) {
		if (--x < 0) {
			x = img.max(0);
			if (--y < 0) {
				y = img.max(1);
				--z;
			}
		}
	}

	@Override
	public void move(int distance, int d) {
		if (d == 0) x += distance;
		else if (d == 1) y += distance;
		else z += distance;
	}

	@Override
	public void move(long distance, int d) {
		if (d == 0) x += distance;
		else if (d == 1) y += distance;
		else z += distance;
	}

	@Override
	public void move(Localizable localizable) {
		x += localizable.getLongPosition(0);
		y += localizable.getLongPosition(1);
		z += localizable.getLongPosition(2);
	}

	@Override
	public void move(int[] distance) {
		x += distance[0];
		y += distance[1];
		z += distance[2];
	}

	@Override
	public void move(long[] distance) {
		x += distance[0];
		y += distance[1];
		z += distance[2];
	}

	@Override
	public void setPosition(Localizable localizable) {
		x = localizable.getLongPosition(0);
		y = localizable.getLongPosition(1);
		z = localizable.getLongPosition(2);
	}

	@Override
	public void setPosition(int[] position) {
		x = position[0];
		y = position[1];
		z = position[2];
	}

	@Override
	public void setPosition(long[] position) {
		x = position[0];
		y = position[1];
		z = position[2];
	}

	@Override
	public void setPosition(int position, int d) {
		if (d == 0) x = position;
		else if (d == 1) y = position;
		else z = position;
	}

	@Override
	public void setPosition(long position, int d) {
		if (d == 0) x = position;
		else if (d == 1) y = position;
		else z = position;
	}

	@Override
	public UnsignedByteType get() {
		return new UnsignedByteType(new ByteAccess() {

			@Override
			public byte getValue(int index) {
				return img.getPixel(x, y, z);
			}

			@Override
			public void setValue(int index, byte value) {
				img.setPixel(x, y, z, value);
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
