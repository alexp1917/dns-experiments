package names.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class QNameParser {
    private final byte[] buffer = new byte[10_000];
    private int bufferPointer = 0;
    private int lastLabelStartPointer = 0;
    private final List<Label> labels = new ArrayList<>();
    private int lastLabelAdded = 0;

    public QNameParser clearLabels() {
        labels.clear();
        return this;
    }

    public List<String> parse(String qName) {
        List<String> result = new ArrayList<>();
        byte[] bytes = qName.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < bytes.length; i++) {
            byte nextChar = bytes[i];
            if (firstTwoBitsAre1(nextChar)) {
                byte offsetValue = readOffsetValue(nextChar);
            } else if (Character.isDigit(nextChar)) {
                if (bufferPointer != 0) {
                    labels.add(new Label()
                            .setOffset(lastLabelStartPointer)
                            .setValue(new String(buffer, 0, bufferPointer, StandardCharsets.US_ASCII)));
                }

                lastLabelStartPointer = i;
                bufferPointer = 0;

                if (nextChar == '0') {
                    result.add(labels.stream().map(Label::getValue).collect(Collectors.joining(".")));
                }
            } else {
                buffer[bufferPointer++] = nextChar;
            }
        }

        return result;
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
    }
}
