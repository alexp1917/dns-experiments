package names.util;

import org.junit.jupiter.api.Test;

import java.util.List;


class QNameParserTest {
    QNameParser qNameParser = new QNameParser();

    @Test
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
}