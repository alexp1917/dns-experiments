package names.model;

import lombok.Data;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@Data
public class Header {
    /**
     * ID
     * <p>
     * A 16 bit identifier assigned by the program that
     * generates any kind of query.  This identifier is copied
     * the corresponding reply and can be used by the requester
     * to match up replies to outstanding queries.
     */
    private ByteArray id;
    private SecondRow row;

    /**
     * <code>QDCOUNT</code>
     * <p>
     * an unsigned 16-bit integer specifying the number of
     * entries in the question section.
     */
    private short questions;

    /**
     * <code>ANCOUNT</code>
     * <p>
     * an unsigned 16-bit integer specifying the number of
     * resource records in the answer section.
     */
    private short answers;

    /**
     * <code>NSCOUNT</code>
     * <p>
     * an unsigned 16-bit integer specifying the number of name
     * server resource records in the authority records section.
     */
    private short authorityRecords;

    /**
     * <code>ARCOUNT</code>
     * <p>
     * an unsigned 16-bit integer specifying the number of
     * resource records in the additional records section.
     */
    private short additionalRecords;

    @Accessors(chain = true)
    @Data
    public static class SecondRow {
        /**
         * <code>QR</code>
         * <p>
         * <p>
         * A one bit field that specifies whether this message is a
         * query (0), or a response (1).
         */
        private boolean response;
        /**
         * <code>OPCODE</code>
         * <p>
         * A four bit field that specifies kind of query in this
         * message.  This value is set by the originator of a query
         * and copied into the response.
         * <p>
         * see {@link HeaderQueryKind} for kinds of values. defaults to
         * {@link HeaderQueryKind#STANDARD}.
         */
        private HeaderQueryKind kind = HeaderQueryKind.STANDARD;

        /**
         * <code>AA</code>
         * <p>
         * Authoritative Answer - this bit is valid in responses,
         * and specifies that the responding name server is an
         * authority for the domain name in question section.
         * <p>
         * Note that the contents of the answer section may have
         * multiple owner names because of aliases.  The AA bit
         * corresponds to the name which matches the query name, or
         * the first owner name in the answer section.
         */
        private boolean authoritative;

        /**
         * <code>TR</code>
         * <p>
         * TrunCation - specifies that this message was truncated
         * due to length greater than that permitted on the
         * transmission channel.
         */
        private boolean truncated;

        /**
         * <code>RD</code>
         * <p>
         * Recursion Desired - this bit may be set in a query and
         * is copied into the response.  If RD is set, it directs
         * the name server to pursue the query recursively.
         * Recursive query support is optional.
         */
        private boolean recursionDesired;

        /**
         * <code>RA</code>
         * <p>
         * Recursion Available - this be is set or cleared in a
         * response, and denotes whether recursive query support is
         * available in the name server.
         */
        private boolean recursionAvailable;

        /**
         * <code>Z</code>
         * <p>
         * Reserved for future use.  Must be zero in all queries
         * and responses.
         */
        private ByteArray z = new ByteArray(new byte[]{0, 0, 0, 0});

        /**
         * <code>RCODE</code>
         * <p>
         * Response code - this 4 bit field is set as part of
         * responses. see {@link HeaderResponseCode} for value interpretations
         */
        private HeaderResponseCode responseCode;
    }
}
