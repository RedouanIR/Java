import java.io.*;
import java.net.*;
import java.util.*;

public class ScanSites {
   public static int BUFFER_SIZE = 8192;
   
   public String downloadPage (URL url, int timeout) throws IOException {
      StringBuilder result = new StringBuilder();
      byte buffer[] = new byte[BUFFER_SIZE];
      
      URLConnection http = url.openConnection();
      http.setConnectTimeout(100);
      InputStream is = http.getInputStream();
      int size = 0;
      
      size = is.read(buffer);
      while (size != -1) {
         result.append(new String(buffer, 0, size));
         size = is.read(buffer);
      }
      
      return result.toString();       
   }
   
   public String extractNoCase(String str, String token1, String token2, int count) {
      int location1, location2;
      
      String searchStr = str.toLowerCase();
      token1 = token1.toLowerCase();
      token2 = token2.toLowerCase();
      
      location1 = location2 = 0;
      do {
         location1 = searchStr.indexOf(token1, location1 + 1);
         if (location1 == -1) {
            return null;
         }
         count--;
      } while (count > 0);
      
      location2 = searchStr.indexOf(token2, location1 + 1);
      if (location2 == -1) {
         return null;
      }
      
      return searchStr.substring(location1 + token1.length(), location2);   
   }
   
   private String scanIP(String ip) {
      try {
         System.out.println("Scanning: " + ip);
         String page = downloadPage(new URL("http://" + ip), 1000);
         String title = extractNoCase(page, "<title>", "</title>", 0);
         
         if (title == null) {
            title = "[Holland]";            
         }
         return title;
      } catch (IOException e) {
         return null;
      }
   }
   
   public void scan(String ip) {
      if (!ip.endsWith(".")) {
         ip += ".";
      }
      
      List<String> list = new ArrayList<String>();
      
      for (int i = 1; i < 255; i++) {
         String address = ip + i;
         String title = scanIP(address);
         if (title != null) {
            list.add(address + ":" + title);
         }
      }
      
      System.out.println();
      System.out.println("Sites found");
      if (list.size() > 0) {
         for (String site : list) {
            System.out.println(site);
         }
      } else {
         System.out.println("No Sites found");
      }
   }
   
   public static void main(String [] args) {
      try {
      
         if (args.length != 1) {
            System.out.println("Usage: ScanSites [IP prefix, i.e. 192.168.1]");
         } else {
            ScanSites d = new ScanSites();
            d.scan(args[0]);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}