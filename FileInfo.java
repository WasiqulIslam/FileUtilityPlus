//8:23 PM 7/27/2005
//u 5:55 AM 3/12/2006

public class FileInfo implements java.io.Serializable
{

   public String filePath;
   public long fileLastModified;
   public long fileLength;

   public FileInfo( FileInfo newOne )
   {
      filePath = newOne.filePath;
      fileLastModified = newOne.fileLastModified;
      fileLength = newOne.fileLength;
   }
   public FileInfo()
   {
      filePath = "";
      fileLastModified = 0;
      fileLength = 0;
   }
   public boolean equals( FileInfo newOne )
   {
      if( ( filePath == newOne.filePath ) && ( fileLastModified == newOne.fileLastModified ) && ( fileLength == newOne.fileLength ) )
      {
         return true;
      }
      else
      {
         return false;
      }
   }
}