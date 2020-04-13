import java.util.*;
import java.io.*;
import java.net.*;
import java.text.*;

public class http1 {
   final int BUFFER_SIZE = 8 * 1024;

   public http1() {
        
   }
   
   public void download (String page, String filename) {
      try{
         URL u = new URL(page);
         String str = downloadPage(u);
         saveBinaryFile(filename, str);
      } catch(MalformedURLException e) {
         e.printStackTrace();
      } catch(IOException e) {
         e.printStackTrace();
      }
      
   }
   
   public String downloadPage(URL url) throws IOException {
      StringBuilder result = new StringBuilder();
      InputStream is = url.openStream();
         
      byte buffer[] = new byte[BUFFER_SIZE];
      int size = 0;
         
      while ((size = is.read(buffer)) != -1) {
         result.append(new String(buffer,0,size));
      }     
      
      return result.toString();
   }
   
   public void go() {
      try {
         URL u = new URL("https://www.google.nl");
         String str = downloadPage(u); 
         System.out.println(extract(str,"<b>","</b>", 1));        
      }
   
      catch(MalformedURLException e) {
         System.out.println("Invalid URL ");
      }
   
      catch(IOException e) {
         System.out.println("URL not found");
      }
   }
   
   public String extract (String str, String token1, String token2, int count) {
      int location1, location2;
      location1 = location2 = 0; 
      
      while (count > 0) {
         location1 = str.indexOf(token1, location1);
         if (location1 == -1)
            return null;
         count--;
      }
      
      location2 = str.indexOf(token2, location1 + 1);
      if (location2 == -1)
         return null;
            
      return str.substring(location1 + token1.length(), location2);
   }
   
   public Date getCityTime(int city) throws IOException, ParseException {
      URL u = new URL("http://www.httprecipes.com/1/3/city.php?city=" + city);
      String str = downloadPage(u);
      
      SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyy hh:mm:ss aa");
      Date date = sdf.parse(extract(str,"<b>","</b>",1));
      return date;
   }
   
   public void saveBinaryFile(String filename, String page)throws IOException {
      OutputStream os = new FileOutputStream(filename);
      os.write(page.getBytes());
      os.close();
   }
   
   private void downloadText (InputStream is, OutputStream os) throws IOException {
      byte lineSep [] = System.getPropert("line.separator").getBytes();
      int ch = 0;
      boolean inLineBreak = false;
      boolean hadLF = false;
      boolean hadCR = false;
      
      do {
         ch = is.read();
         if (ch != -1) {
            if ((ch == '\r') || (ch == '\n')) {
            inLineBreak = true;
            if (ch == '\r') {
               (hadCR)
                  os.write(lineSep);              
            } else {
               hadCR = true;
              } 
         } else {
            if (hadLF) {
               os.write(lineSep);
            } else {
               hadLF = true;
            }
         } else {
            if (inlineBreak) {
               os.write(lineSep);
               hadCR = hadLF = inLineBreak = false;
            }
            os.write(ch);
         }

         
         }
         
      } while (ch != -1);
   }
   
   public static void main (String [] args) {      
      try{
         if(args.length != 2) {
            http1 program = new http1();
            program.download("http://www.httprecipes.com/1/3/sea.jpg","./sea2.jpg");
         } else {
            http1 program = new http1();
            program.download(args[0], args[1]);
         }
            
         }catch (Exception e) {
            e.printStackTrace();
         }
     }
      
      
      //program.go();
   
}