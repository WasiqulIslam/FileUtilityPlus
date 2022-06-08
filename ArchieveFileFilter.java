//7:51 PM 8/17/2005
//u 10:33 PM 8/17/2005
//buf fixed 8:40 AM 8/18/2005
//copied from WZIPFileFilter 12:23 AM 3/14/2006

import java.io.*;

public class ArchieveFileFilter extends javax.swing.filechooser.FileFilter
{
   private String description = "Wasiq's Archieve";
   public boolean accept( File file )
   {
      try
      {
         if( !file.exists() )
         {
            return false;
         }
         if( file.isDirectory() )
         {
            return true;
         }
         if( !file.isFile() )
         {
            return false;
         }
         StringBuffer fileName = new StringBuffer( file.getName() );
         int length = fileName.length();
         if( length <= 5 )
            return false;
         length = length - 5;
         fileName = new StringBuffer( fileName.substring( length ) );
         String tmpString = ( ( fileName.toString() ).toUpperCase() );
         if( tmpString.equals( ".DATA" ) )
         {
            return true;
         }
         else
         {
            return false;
         }
      }
      catch( Exception exception )
      {
         return false;
      }
   }
   public String getDescription()
   {
      return new String( description );
   }
}