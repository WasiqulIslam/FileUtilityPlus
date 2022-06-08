//8:26 PM 7/27/2005
//getAnorherCopy() adding 10:18 AM 3/26/2006 completed 10:26 AM 3/26/2006
//u 9:01 PM 4/23/2006

import javax.swing.*;

public class FileInfoContainer implements java.io.Serializable
{
   public FileInfo allFileInfo[];
   public void showList()
   {
      StringBuffer sb = new StringBuffer();
      for( int i = 0; i < allFileInfo.length; i++ )
      {
         sb.append( "Path: " + allFileInfo[ i ].filePath + "\t" );
         sb.append( "Modified: " + allFileInfo[ i ].fileLastModified + "\t" );
         sb.append( "Length: " + allFileInfo[ i ].fileLength + "\n" );
      }
      JTextArea ta = new JTextArea();
      ta.setText( sb.toString() );
      JOptionPane.showMessageDialog( null, new JScrollPane( ta ) );
   }

   public FileInfoContainer getAnotherCopy()
   {
      FileInfoContainer nfic = new FileInfoContainer();
      nfic.allFileInfo = new FileInfo[ this.allFileInfo.length ];
      for( int i = 0; i < this.allFileInfo.length; i++ )
      {

         nfic.allFileInfo[ i ] = new FileInfo( this.allFileInfo[ i ] );

      }

      return nfic;

   }
}