package names.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;


class QNameParserTest {
    QNameParser qNameParser = new QNameParser();

    // @Test
    void test_single() {
        List<String> parse = qNameParser.parse("3www8mydomain3com0");
        System.out.println(parse);

        assert parse.size() == 1;
        assert parse.get(0).equals("www.mydomain.com");
    }

    @Test
    void test_firstAndSecondBits() {
        /*
            0b0000_0000 = 0
            0b0000_0001 = 1
            0b0000_0011 = 3
            0b0000_0100 = 4
            0b0000_0110 = 6
         */
        assert !evaluateFirstTwoBitsMethod((byte) 0);
        assert !evaluateFirstTwoBitsMethod((byte) 1);
        assert evaluateFirstTwoBitsMethod((byte) 3);
        assert !evaluateFirstTwoBitsMethod((byte) 4);
        assert !evaluateFirstTwoBitsMethod((byte) 6);
    }

    private boolean evaluateFirstTwoBitsMethod(byte i) {
        return qNameParser.firstTwoBitsAre1(i);
    }

    @Test
    void test_getRestOfValue() {
        /*
            0b0000_00_11 = 0
            0b0000_01_11 = 1
            0b0001_11_11 = 7
            0b1001_11_11 = 39
            0b1111_11_11 = 63
         */
        assert 0 == evaluateReadOffsetValue((byte) 0b0000_00_11);
        assert 1 == evaluateReadOffsetValue((byte) 0b0000_01_11);
        assert 7 == evaluateReadOffsetValue((byte) 0b0001_11_11);
        assert 39 == evaluateReadOffsetValue((byte) 0b1001_11_11);
        assert 63 == evaluateReadOffsetValue((byte) 0b1111_11_11);
    }

    private byte evaluateReadOffsetValue(byte input) {
        return qNameParser.readOffsetValue(input);
    }

    @Test
    void test_countLabels() {
        int i = qNameParser.countLabels("3www8mydomain3com0".getBytes(StandardCharsets.US_ASCII));
        assert i == 3;
    }

    @Test
    void scratchWork() {
        scratch();
    }

    private void scratch() {

    }

    @Test
    void test_generateQName() {
        byte[] qName_mydomain = generateQName("www.mydomain.com");
        byte[] qName_example = generateQName("www.example.com");

        // test first label start
        assert isStartOfLabel(qName_mydomain[0]);
        byte firstLabelLength = readLabelOffsetOrLength(qName_mydomain[0]);
        assert firstLabelLength == 3;

        assert isStartOfLabel(qName_mydomain[4]);
        byte secondLabelLength = readLabelOffsetOrLength(qName_mydomain[4]);
        assert secondLabelLength == 8;

        String firstQNameBits = new String(qName_mydomain, 1, firstLabelLength);
        assert firstQNameBits.equals("www");

        String secondQNameBits = new String(qName_mydomain, 1 + firstLabelLength + 1, secondLabelLength);
        assert secondQNameBits.equals("mydomain");

        assert isStartOfLabel(qName_mydomain[13]);
        byte thirdLabelLength = readLabelOffsetOrLength(qName_mydomain[13]);
        assert thirdLabelLength == 3;

        String thirdQNameBits = new String(qName_mydomain,
                1 + firstLabelLength + 1
                        + secondLabelLength + 1
                , thirdLabelLength);
        assert thirdQNameBits.equals("com");

        System.out.println();
    }

    private byte[] generateQName(String friendly) {
        byte[] bytes = friendly.getBytes(StandardCharsets.US_ASCII);

        byte[] result = new byte[friendly.length() + 2];
        int resultPointer = 0;
        result[resultPointer++] = 0;
        int lastSegmentStart = 0;

        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            if (b == '.') {
                byte lastLength = result[lastSegmentStart];
                result[lastSegmentStart] = desiredByte(false, lastLength);
                result[resultPointer++] = 0;
                lastSegmentStart = i + 1;
            } else {
                result[resultPointer++] = b;
                result[lastSegmentStart]++;
            }
        }

        byte lastLength = result[lastSegmentStart];
        result[lastSegmentStart] = desiredByte(false, lastLength);
        //noinspection UnusedAssignment
        result[resultPointer++] = 0;

        return result;
    }

    @Test
    void test_desiredByte() {
        assert desiredByte(false, 0) == 0;
        assert desiredByte(true, 0) == 3;
        assert desiredByte(false, 1) == 4;
        assert desiredByte(true, 1) == 7;
        assert Byte.toUnsignedInt(desiredByte(true, 63)) == 255;
    }

    boolean isStartOfLabel(byte maybeStart) {
        return (maybeStart & 0b11) == 0b00;
    }

    byte readLabelOffsetOrLength(byte label) {
        byte b = (byte) (label >> 2);
        System.out.println();
        return b;
    }

    byte desiredByte(boolean isPointer, int offsetOrLength) {
        if (offsetOrLength < 0 | offsetOrLength > 63)
            throw new IllegalArgumentException();

        byte value = 0;
        value = (byte) (value | (isPointer ? 0b11 : 0));

        value = (byte) (value | ((offsetOrLength) << 2));
        return value;
    }
}