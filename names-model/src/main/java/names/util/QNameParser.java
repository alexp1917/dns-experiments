package names.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


public class QNameParser {
    // this whole method -- probably should be a more elegant approach
    public List<String> parse(String qName) {
        List<String> result = new ArrayList<>();
        byte[] bytes = qName.getBytes(StandardCharsets.US_ASCII);

        int numLabels = countLabels(bytes);
        Label[] labels = new Label[numLabels];
        int labelsPointer = 0;
        int thisLabelStart = 0;
        int thisLabelEnd = 0;

        for (int i = 0; i < bytes.length; i++) {
            byte nextChar = bytes[i];
            /*
                This is actually a pretty big problem because
                https://datatracker.ietf.org/doc/html/rfc1035#section-4.1.4
                says:

                This allows a pointer to be distinguished
                from a label, since the label must begin with two zero bits b/c
                labels are restricted to 63 octets or less.

                so, actually - the test is not 'Character.isDigit' but rather:
                is the remaining 6 bits an ascii 'isDigit'?
             */
            if (firstTwoBitsAre1(nextChar)) {
                byte offsetValue = readOffsetValue(nextChar);
            } else if (Character.isDigit(nextChar)) {
                if (labelsPointer > 0) {
                    labels[labelsPointer - 1]
                            .setCharSequence(slice(bytes,
                                    thisLabelStart,
                                    thisLabelEnd));
                }
                thisLabelStart = i;
                thisLabelEnd = thisLabelStart;
                labels[labelsPointer++] = new Label();

                if (nextChar == '0') {
                    StringJoiner stringJoiner = new StringJoiner(".");
                    for (Label label : labels)
                        stringJoiner.add(label.getCharSequence());
                    result.add(stringJoiner.toString());
                }
            } else {
                thisLabelEnd++;
                // buffer[bufferPointer++] = nextChar;
            }
        }

        return result;
    }

    public int countLabels(byte[] bytes) {
        int numLabels = 0;
        for (byte b : bytes) {
            if (Character.isDigit(b) &&
                    Character.getNumericValue(b) > 0) {
                numLabels++;
            }
        }
        return numLabels;
    }

    private ByteArraySlice slice(byte[] bytes, int offset, int end) {
        return new ByteArraySlice(bytes, end, offset);
    }

    public boolean firstTwoBitsAre1(byte value) {
        return (0b11 & value) == 0b11;
    }

    public byte readOffsetValue(byte value) {
        return (byte) (Byte.toUnsignedInt(value) >> 2);
    }

    @Accessors(chain = true)
    @Data
    public static class Label {
        private int offset;
        private String value;
        private CharSequence charSequence;
    }
}
