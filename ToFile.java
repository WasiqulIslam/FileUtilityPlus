//7:39 PM 7/27/2005
//u 10:54 PM 7/27/2005
//u 8:25 PM 3/10/2006
//u 6:36 AM 3/11/2006
//u 5:59 PM 3/17/2006
//bug solved 9:30 PM 4/23/2006
//u 9:33 PM 4/23/2006

import java.io.*;
import javax.swing.*;
import java.util.*;

public class ToFile extends Thread implements Sleepable
{

   private FileUtilityPlus ref;
   private Vector allFiles;
   private boolean sleeping;
   private String folderParentPath;
   public FileInfoContainer infoContainer;


   public final static int BYTE_SIZE = 1000000;


   public ToFile( FileUtilityPlus r )
   {
      allFiles = new Vector();
      ref = r;
   }

   public void run()
   {
      try
      {
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
         int result;
         while( true )
         {
            result = fileChooser.showDialog( ref, "Select" );
            if( result != JFileChooser.APPROVE_OPTION )
            {
               result = JOptionPane.showConfirmDialog( ref, "Do you want to cancel?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
               if( result == JOptionPane.YES_OPTION )
               {
                  return;
               }
            }
            else
            {
               break;
            }
         }
         File folderName = fileChooser.getSelectedFile();
         ref.setTitle( "Please wait..." );
         addAllFile( folderName );


         infoContainer = new FileInfoContainer();
         infoContainer.allFileInfo = new FileInfo[ allFiles.size() ];
         String folderPath = folderName.getAbsolutePath();
         folderParentPath = folderName.getParentFile().getAbsolutePath();
         //d( "Folder Parent Path: " + folderParentPath );
         for( int i = 0; i < infoContainer.allFileInfo.length; i++ )
         {
            File tmpFile = ( File ) allFiles.elementAt( i );
            infoContainer.allFileInfo[ i ] = new FileInfo();
            infoContainer.allFileInfo[ i ].filePath = "" + File.separator + folderName.getName() + setPathFromTwoStrings( folderPath, tmpFile.getAbsolutePath() );
            infoContainer.allFileInfo[ i ].fileLastModified = tmpFile.lastModified();
            infoContainer.allFileInfo[ i ].fileLength = tmpFile.length();
         }

         if( infoContainer.allFileInfo.length <= 0 )
         {
            JOptionPane.showMessageDialog( ref, "No file found in the selected folder.", "Empty!", JOptionPane.ERROR_MESSAGE );
            return;
         }

         ref.setTitle( "File Utility Plus" );
         result = JOptionPane.showConfirmDialog( ref, "Do you want to view and edit all file list?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
         ref.setTitle( "Please wait..." );
         if( result == JOptionPane.YES_OPTION )
         {

            sleeping = true;
            ATable aTable = new ATable( ref, this, infoContainer );
            while( true )  //Let this thread sleep until aTable does his work, aTable will awake it
            {
               this.sleep( 1000 );
               if( sleeping == false )
               {
                  break;
               }
            }

            aTable = null;
            System.gc();

         }



         ref.setTitle( "File Utility Plus" );

         fileChooser = null;
         fileChooser = new JFileChooser();
         fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
         fileChooser.setFileFilter( new ArchieveFileFilter() );

         File outputFileName;
         String pathWithExtension;

         while( true )
         {

            result = fileChooser.showDialog( ref, "Save" );

            outputFileName = fileChooser.getSelectedFile();
            //JOptionPane.showMessageDialog( ref, outputFileName.getAbsolutePath() );

            if( outputFileName != null )
            {

               pathWithExtension = outputFileName.getAbsolutePath();

               if ( ( pathWithExtension.length() - pathWithExtension.toUpperCase().lastIndexOf( ".DATA" ) ) != 5 )
               {
                  pathWithExtension = outputFileName.getAbsolutePath() + ".data";
               }
               outputFileName = new File( pathWithExtension );

            }

            if( result != JFileChooser.APPROVE_OPTION )
            {
               result = JOptionPane.showConfirmDialog( ref, "Do you want to cancel?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
               if( result == JOptionPane.YES_OPTION )
               {
                  return;
               }
            }
            else if( outputFileName.exists() )
            {
               result = JOptionPane.showConfirmDialog( ref, "File already exists.\nOverwrite?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE );
               if( result == JOptionPane.YES_OPTION )
               {
                  break;
               }
            }
            else
            {
               break;
            }
         }

         ref.setTitle( "Please wait..." );


         DataOutputStream outputFile = new DataOutputStream( new BufferedOutputStream( new FileOutputStream( outputFileName ) ) );
         long tmpLong = 500000001;   //Format information
         outputFile.writeLong( tmpLong );
         outputFile.writeInt( infoContainer.allFileInfo.length );

         for( int i = 0; i < infoContainer.allFileInfo.length; i++ )
         {
            outputFile.writeUTF( infoContainer.allFileInfo[ i ].filePath );
            //d( "Writing String: " + infoContainer.allFileInfo[ i ].filePath );
            outputFile.writeLong( infoContainer.allFileInfo[ i ].fileLastModified );
            //d( "Writing Long(LModi): " + infoContainer.allFileInfo[ i ].fileLastModified );
            outputFile.writeLong( infoContainer.allFileInfo[ i ].fileLength );
            //d( "Writing Long(length): " + infoContainer.allFileInfo[ i ].fileLength );
         }


         byte bytes[] = new byte[ BYTE_SIZE ];
         File currentFile;
         DataInputStream inputFile;
         int readBytes;
         for( int i = 0; i < infoContainer.allFileInfo.length; i++ )
         {
            currentFile = new File( folderParentPath + infoContainer.allFileInfo[ i ].filePath );  //tag
            ref.setTitle(  "( " + ( i+1 ) + " / " + infoContainer.allFileInfo.length + " )[ " + currentFile.getName() + " at " + currentFile.getParentFile() + " ]" );
            inputFile = new DataInputStream( new BufferedInputStream( new FileInputStream( currentFile ) ) );
            while( true )
            {
               readBytes = inputFile.read( bytes );
               if( readBytes == -1 )
                  break;
               outputFile.write( bytes, 0, readBytes );
            }
            inputFile.close();
            inputFile = null;
         }
         outputFile.close();
          JOptionPane.showMessageDialog( ref, "File Successfully Created", "Success", JOptionPane.INFORMATION_MESSAGE );
         finalize();
      }
      catch( Throwable event )
      {
         event.printStackTrace();
         JOptionPane.showMessageDialog( ref, "Failed to complete the operation\n" + event.toString() , "ERROR", JOptionPane.ERROR_MESSAGE );
      }
      finally
      {
         ref.enableButtons();
      }
   }
   private String setPathFromTwoStrings( String a, String b )
   {
      return b.substring( a.length() );
   }
   private void addAllFile( File f )
   {
      File files[] = f.listFiles();
      for( int i = 0; i < files.length; i++ )
      {
         if( files[ i ].isDirectory() )
         {
            addAllFile( files[ i ] );
         }
         else if( files[ i ].isFile() )
         {
            allFiles.addElement( ( Object ) ( files[ i ] ) );
         }
      }
   }
   public void awaken()
   {
      sleeping = false;
   }
   public void setInfoContainer( FileInfoContainer fic )
   {
      infoContainer = fic;
      //infoContainer.showList();
   }
   public void d( String msg )
   {
      JOptionPane.showMessageDialog( ref, msg );
   }
}
