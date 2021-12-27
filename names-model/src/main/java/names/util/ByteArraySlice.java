package names.util;

public class ByteArraySlice implements CharSequence {
    private final byte[] delegate;
    private final int offset;
    private final int end;

    public ByteArraySlice(byte[] delegate, int end, int offset) {
        this.delegate = delegate;
        this.end = end;
        this.offset = offset;
    }

    @Override
    public int length() {
        return end - offset;
    }

    @Override
    public char charAt(int index) {
        return (char) Byte.toUnsignedInt(delegate[offset + index]);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new ByteArraySlice(delegate,
                offset + end,
                offset + start);
    }
}
