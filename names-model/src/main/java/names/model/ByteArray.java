package names.model;

import lombok.Data;


@Data
public class ByteArray {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private final byte[] bytes;

    public ByteArray(byte[] bytes) {
        this.bytes = bytes;
    }

    public String toHex() {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
