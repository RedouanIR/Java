import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
 
/**
 * A utility that downloads a file from a URL.
 * @author www.codejava.net
 *
 */
public class HttpDownloadUtility {
   private static final int BUFFER_SIZE = 4096;
   
   /*
      Downloads a file from URL
      @param fileUrL HTTp url of the file to be downloaded
      @param saveDir path of the diectory save the file
      @throws IORException      
   */
   public static void downloadFile(String fileURL, String saveDir) throws IOException{
      URL url = new URL(fileURL);
      HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
      int responseCode = httpConn.getResponseCode();
      
      // always check HTTP response code first
      if (responseCode == HttpURLConnection.HTTP_OK) {
         String fileName = "";
         String disposition = httpConn.getHeaderField("Content-Disposition");
         String contentType = httpConn.getContentType();
         int contentLength = httpConn.getContentLength();
         
         if (disposition != null) {
            // extracts file name from header field
            int index = disposition.indexOf("filename=");
            if (index > 0) {
               fileName = disposition.substring(index + 10, disposition.length() - 1);
            }
         } else {
            // extracts filename from URL
            fileName = fileURL.substring(fileURL.lastIndexOf("?") + 1, fileURL.length());
         }
         
         System.out.println("Content-Type = " + contentType);
         System.out.println("Content-Disposition = " + disposition);
         System.out.println("Content-Length = " + contentLength);
         System.out.println("fileName = " + fileName);
         
         // opens inputstream from http connection
         InputStream is = httpConn.getInputStream();
         String saveFilePath = saveDir + File.separator + fileName;
         
         // opens an output stream to save into file
         FileOutputStream os = new FileOutputStream(saveFilePath);
         
         int bytesRead = -1;
         byte[] buffer = new byte[BUFFER_SIZE];
         while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);         
         }
         
         os.close();
         is.close();
         System.out.println("File downloaded");
      } else {
         System.out.println("No file to downlaod. Server replied HTTP code : " + responseCode);
      }
      httpConn.disconnect();
   }
}
class HttpDownloader {
   public static void main(String [] args) {
      String fileURL = "https://m.facebook.com/a/bz?m_sess=&fb_dtsg=AQF4vVo03Q16%3AAQFbbWOBogH4&jazoest=21906&__dyn=1KQEGiFo525Ujwh8-t28sBBgS5UqxKcwRwAxu3-UcodUbE6u7Hwlofocohx6485-0SUhxm3O0AE8o11E52q3q5U2nweS787S78fEeE7ifw5lxyeKdwGwFU6i0JE52229wcq0C9E8EjwbO7E10o2vwAw&__csr=&__req=6&__ajax__=AYkDIJsHj8y8QuMln0jgl4jyoJ5T2bkG86Q4z5aobLHaJJ6ClxrGZN9VhsKuaXmJhQpANPLGOE242ytyN-8_nVpZBoU-wUMs54o31fV3sj2RNA&__a=AYkDIJsHj8y8QuMln0jgl4jyoJ5T2bkG86Q4z5aobLHaJJ6ClxrGZN9VhsKuaXmJhQpANPLGOE242ytyN-8_nVpZBoU-wUMs54o31fV3sj2RNA&__user=100013277417876";
      String saveDir = "E:/Download";
      try{
         HttpDownloadUtility.downloadFile(fileURL, saveDir);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }
}