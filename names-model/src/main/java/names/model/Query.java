package names.model;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Query Object
 *
 * <p>
 * 4.1. Format
 * <p>
 * All communications inside the domain protocol are carried in a single
 * format called a message.  The top level format of message is divided
 * into 5 sections (some of which are empty in certain cases) shown below:
 * <p>
 *
 * <pre>
 *     +---------------------+
 *     |        Header       |
 *     +---------------------+
 *     |       Question      | the question for the name server
 *     +---------------------+
 *     |        Answer       | RRs answering the question
 *     +---------------------+
 *     |      Authority      | RRs pointing toward an authority
 *     +---------------------+
 *     |      Additional     | RRs holding additional information
 *     +---------------------+
 * </pre>
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc1035#section-4">RFC1035</a>
 */
@Accessors(chain = true)
@Data
public class Query {
    private Header header;
    /**
     * the question for the name server
     */
    private Question question;
    /**
     * RRs answering the question
     */
    private Answer answer;
    /**
     * RRs pointing toward an authority
     */
    private Authority authority;
    /**
     * RRs holding additional information
     */
    private Additional additional;
}
