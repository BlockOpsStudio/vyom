package studio.blockops.vyom.core.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class UnsafeByteArrayOutputStream extends ByteArrayOutputStream {
	
	public UnsafeByteArrayOutputStream() {
        super(32);
    }

    public UnsafeByteArrayOutputStream(int size) {
        super(size);
    }
	
	public void write(int b) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            buf = Utils.copyOf(buf, Math.max(buf.length << 1, newcount));
        }
        buf[count] = (byte) b;
        count = newcount;
    }

    public void write(byte b[], int off, int len) {
        if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        int newcount = count + len;
        if (newcount > buf.length) {
            buf = Utils.copyOf(buf, Math.max(buf.length << 1, newcount));
        }
        System.arraycopy(b, off, buf, count, len);
        count = newcount;
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(buf, 0, count);
    }

    public void reset() {
        count = 0;
    }

    public byte toByteArray()[] {
        return count == buf.length ? buf : Utils.copyOf(buf, count);
    }

    public int size() {
        return count;
    }
}