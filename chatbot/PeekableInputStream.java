import java.io.*;

public class PeekableInputStream extends InputStream {
   private InputStream stream;
   private byte peekBytes[];
   private int peekLength;
   
   public PeekableInputStream(InputStream is) {
      this.stream = is;
      this.peekBytes = new byte[10];
      this.peekLength = 0;
   }
   
   public int peek() throws IOException {
      return peek(0);
   }
   
   private int peek(int depth) throws IOException {
      if (this.peekBytes.length <= depth) {
         byte temp[] = new byte[depth + 10];
         for (int i = 0; i < this.peekBytes.length; i++) {
            temp[i] = this.peekBytes[i];
         }
         this.peekBytes = temp;
      }
      
      if (depth >= this.peekLength) {
         int offset = this.peekLength;
         int length = (depth - this.peekLength) + 1;
         int lengthRead = this.stream.read(this.peekBytes, offset, length);
         
         if (lengthRead == -1) {
            return -1;
         }
         this.peekLength = depth + 1;         
      }
      return this.peekBytes[depth];
   }
   
   public int read () throws IOException {
      if (this.peekLength == 0) {
         return this.stream.read();
      }
      
      int result = this.peekBytes[0];
      this.peekLength--;
      for (int i = 0; i < this.peekLength; i++) {
         this.peekBytes[i] = this.peekBytes[i + 1];
      }
      return result;
   }
}