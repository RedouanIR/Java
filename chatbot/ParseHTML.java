import java.io.*;
import java.util.*;

public class ParseHTML {
   private static Map<String, Character> charMap;
   
   private PeekableInputStream source;
   
   private HTMLTag tag = new HTMLTag();
   
   private String lockedEndTag;
   
   public ParseHTML(InputStream is ) {
   
   }

}