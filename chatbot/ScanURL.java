import java.io.IOException;
import java.net.*;

public class ScanURL {
   /*
      Scan the URL and dispplay headers
      @params u the url scan
      @throws IOEXception Error scanning URL
   */
   public void scan(String u) throws IOException {
      URL url = new URL(u);
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      int count = 0;
      String key, value;
      
      do {
         key = http.getHeaderFieldKey(count);
         value = http.getHeaderField(count);
         count++;
         if (value != null) {
            if (key == null) {
               System.out.println(value);
            } else {
               System.out.println(key + ": " + value);
            }
         }
      } while (value != null);
   }
   
   public static void main(String [] args) {
      try {
         if (args.length != 1) {
            System.out.println("Usage: \njava ScanURL [URL to Scan]");
         } else {
            ScanURL d = new ScanURL();
            d.scan(args[0]);
         }
      } catch (Exception e) {
         e.printStackTrace();
      } 
   }
}