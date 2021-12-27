package names.model;

public enum HeaderResponseCode {
    /**
     * <code>Success</code>
     * <p>
     * No error condition.
     * <p>
     * value: <code>0</code>
     */
    SUCCESS,
    /**
     * <code>Client Error</code>
     * <p>
     * Format error - The name server was
     * unable to interpret the query.
     * <p>
     * value: <code>1</code>
     */
    CLIENT_ERROR,
    /**
     * <code>Server Error</code>
     * <p>
     * Server failure - The name server was
     * unable to process this query due to a
     * problem with the name server.
     * <p>
     * value: <code>2</code>
     */
    SERVER_ERROR,
    /**
     * <code>Name Error</code>
     * <p>
     * Name Error - Meaningful only for
     * responses from an authoritative name
     * server, this code signifies that the
     * domain name referenced in the query does
     * not exist.
     * <p>
     * value: <code>3</code>
     */
    NAME_ERROR,
    /**
     * <code>Not Implemented</code>
     * <p>
     * Not Implemented - The name server does
     * not support the requested kind of query.
     * <p>
     * value: <code>4</code>
     */
    NOT_IMPLEMENTED,
    /**
     * <code>Refused</code>
     * <p>
     * Refused - The name server refuses to
     * perform the specified operation for
     * policy reasons.  For example, a name
     * server may not wish to provide the
     * information to the particular requester,
     * or a name server may not wish to perform
     * a particular operation (e.g., zone
     * transfer) for particular data.
     * <p>
     * value: <code>5</code>
     */
    REFUSED,
    /**
     * <code>Refused</code>
     * <p>
     * Reserved for future use.
     * <p>
     * value: <code>6-15</code>
     */
    RESERVED,
    ;

}
