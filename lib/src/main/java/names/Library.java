package names;

import org.xbill.DNS.Name;
import org.xbill.DNS.TextParseException;


public class Library {
    public boolean someLibraryMethod() {
        return true;
    }

    public static void main(String[] args) {
        try {
            Name localhost = Name.fromString("localhost");
            System.out.println(localhost);
        } catch (TextParseException e) {
            e.printStackTrace();
        }
    }
}
