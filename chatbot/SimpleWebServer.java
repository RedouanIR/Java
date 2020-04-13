import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleWebServer
{
   /*
   The serversocket
   */
   private ServerSocket serverSocket = null;
   String httproot = null;
   
   public SimpleWebServer(int port, String httproot) throws IOException {
      serverSocket = new ServerSocket(port);
      this.httproot = httproot;
   }
   
   public void run() {
      for(;;)
      {
         try {
            System.out.println("up and running ");
            Socket clientSocket = serverSocket.accept();
            handleClientSesssion(clientSocket);
         }
         catch(IOException e) {
            e.printStackTrace();
         }
      }//end for
   }//end run
   
   public void handleClientSesssion(Socket socket) throws IOException {
      //read from socket in lines
      InputStream is = socket.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(is);
      BufferedReader in = new BufferedReader(inputStreamReader);
      
      //wrire to sockets in lines
      OutputStream os = socket.getOutputStream();
      PrintStream out = new PrintStream(os);
      
      //read in the first line
      System.out.println("New Request");
      String first = in.readLine();
      System.out.println(first);
      
      //read in headers and postdata
      String line;

      do {
            line = in.readLine();
            if(line != null) {
                System.out.println(line);
            }
         } while (line != null && line.trim().length() > 0);
         
       //write response
       StringTokenizer tok = new StringTokenizer(first);
       String verb = (String) tok.nextElement();
       String path = (String) tok.nextElement();
       
       if (verb.equalsIgnoreCase("GET")) {
         sendFile(os, path);
       } else {
         error(os, 500, "Unspported command");
       }
        
      //close everything up
      in.close();
      out.close();
      socket.close();
   }
   
   private String getContent (String path) {
      path = path.toLowerCase();
      if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
         return "image/jpeg";
      } else if (path.endsWith(".gif")) {
         return "image/gif";
      } else if (path.endsWith(".png")) {
         return "image/png";
      } else {
         return "text/html";
       }
   }
   
   private void sendFile (OutputStream out, String path) throws IOException{
      StringTokenizer tok = new StringTokenizer(path, "/", true);
      System.out.println(path);
      String physicalPath = addSlash(httproot);
      
      while (tok.hasMoreElements()) {
         String e = (String) tok.nextElement();
         if (!e.trim().equalsIgnoreCase(File.separator)) {
            if (e.equals("..") || e.equals(".")) {
               error(out, 500, "Invalid request");
               return;
            }
            physicalPath += e;
         } else {
            physicalPath = addSlash(physicalPath);
      }
      
      //no file specified
      if (physicalPath.endsWith(File.separator)) {
         physicalPath = physicalPath + "index.html";
      }
      
      File file = new File(physicalPath);
      if(file.exists()) {
         FileInputStream fis = new FileInputStream(file);
         byte buffer[] = new byte[(int) file.length()];
         fis.read(buffer);
         fis.close();
         this.transmit(out, 200, "OK", buffer, getContent(physicalPath));
      } else {
         this.error(out, 404, "File not found");
      }      
    }
  }
   
   private void transmit(OutputStream out, int code, String message, byte body[], String content) throws IOException {
      StringBuilder headers = new StringBuilder();
      headers.append("HTTP/1.1 ");
      headers.append(code);
      headers.append(' ');
      headers.append(message);
      headers.append("\n");
      headers.append("Content-Length: " + body.length + "\n");
      headers.append("Server: Heaton Research Example Server\n");
      headers.append("Connection: close\n");
      headers.append("Content-Type: " + content + "\n");
      headers.append("\n");
      out.write(headers.toString().getBytes());
      out.write(body);
   }
   
    private void error(OutputStream out, int code, String message) throws IOException{
      StringBuilder body = new StringBuilder();
      body.append("<html><head><title>");
      body.append(code + ":" + message);
      body.append(
      "</title></head><body><p>An error occurred.</p><h1>");
      body.append(code);
      body.append("</h1><p>");
      body.append(message);
      body.append("</p></body></html>");
      transmit(out, code, message,body.toString().getBytes(), "text/html");

    }
   
   //cinstrust a correct path for the httproot
   private String addSlash(String path) {
      path = path.trim();
      if (path.endsWith(""+File.separatorChar)) {
         return path;
      } else {
         return path + File.separatorChar;
      }
   }
   
   public static void main(String [] args) {
      try {
         if (args.length < 2) {
            System.out.println("Usage: \njava simpleWebserve [port]");
         }//end if
         else {
            int port;
            //String httproot;
            try {
               port = Integer.parseInt(args[0]);
               //httproot = args[1].toString();
               SimpleWebServer server = new SimpleWebServer(port,args[1]);
               server.run();
            }
            catch(NumberFormatException e) {
               System.out.println("Invalid portnumber ");
            }            
         }
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }//end main
   
}//end webserevr