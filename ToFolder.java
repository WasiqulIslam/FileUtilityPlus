//7:39 PM 7/27/2005
//buf fixed 12:19 PM 7/29/2005
//u 6:37 AM 3/11/2006
//u 6:06 PM 3/17/2006
//bug solved 6:46 PM 3/17/2006
//u 7:35 AM 3/18/2006
//u 9:33 PM 4/23/2006


import java.io.*;
import javax.swing.*;
import java.util.*;

public class ToFolder extends Thread implements Sleepable
{

   private FileUtilityPlus ref;
   public final static int BYTE_SIZE = 1000000;
   public boolean sleeping;
   public FileInfoContainer infoContainer, infoContainer2;
   private int writeLimit;
   public boolean checked[];

   public ToFolder( FileUtilityPlus r )
   {
      ref = r;
   }

   public void makeDirectoryTree( File f, boolean b ) throws Exception   //recursive operation
   {
      //System.out.println( "Inside makeDirectoryTree()" );
      //System.out.println( f.getAbsolutePath() );
      if( !f.getParentFile().exists() )
         makeDirectoryTree( f.getParentFile(), false );
      if( b == false )
         f.mkdir();
   }

   public void run()
   {
      try
      {
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setFileFilter( new ArchieveFileFilter() );
         fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );

         int result;

         while( true )
         {
            result = fileChooser.showDialog( ref, "Open" );
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

         ref.setTitle( "Please wait..." );

         File inputFileName = fileChooser.getSelectedFile();

         RandomAccessFile inputFile = new RandomAccessFile( inputFileName , "r" );
         long tmpLong = inputFile.readLong();


         if( tmpLong != 500000001 )
         {
            JOptionPane.showMessageDialog( ref, "Not a valid file", "Invalid", JOptionPane.INFORMATION_MESSAGE );
            return;
         }


         int infoContinerLength = inputFile.readInt();
         infoContainer = new FileInfoContainer();
         infoContainer.allFileInfo = new FileInfo[ infoContinerLength ];
         for( int icCount = 0; icCount < infoContinerLength; icCount++ )
         {
            infoContainer.allFileInfo[ icCount ] = new FileInfo();
            infoContainer.allFileInfo[ icCount ].filePath = inputFile.readUTF();
            //d( "Read File Path: " + infoContainer.allFileInfo[ icCount ].filePath );
            infoContainer.allFileInfo[ icCount ].fileLastModified = inputFile.readLong();
            //d( "Read File Last Modified: " + infoContainer.allFileInfo[ icCount ].fileLastModified );
            infoContainer.allFileInfo[ icCount ].fileLength = inputFile.readLong();
            //d( "Read File Length: " + infoContainer.allFileInfo[ icCount ].fileLength );
         }

          if( infoContainer.allFileInfo.length <= 0 )
         //This condition may never be true as empty archieve creation is not allowed
         {
            JOptionPane.showMessageDialog( ref, "No file found in the archieve.", "Empty!", JOptionPane.ERROR_MESSAGE );
            return;
         }

         ref.setTitle( "File Utility Plus" );
         result = JOptionPane.showConfirmDialog( ref, "Do you want to view and edit all file list?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
         ref.setTitle( "Please Wait..." );
         if( result == JOptionPane.YES_OPTION )
         {

            sleeping = true;
            ATable aTable = new ATable( ref, this, infoContainer );
            while( true )  //Let this thread sleep until aTable does its work, aTable will awake it
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
         else
         {
            infoContainer2 = infoContainer.getAnotherCopy();
         }

         updateChecks();

         //infoContainer2.showList();

/*
         for( int i = 0; i < checked.length; i++ )
         {
            //d( "checked[ " + i + "]: " + checked[ i ] );
         }
*/

         ref.setTitle( "File Utility Plus" );

         fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

         while( true )
         {
            result = fileChooser.showDialog( ref, "Save In(Folder)" );
            if( result != JFileChooser.APPROVE_OPTION )
            {
               result = JOptionPane.showConfirmDialog( ref, "Do you want to cancel?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
               if( result == JOptionPane.YES_OPTION )
               {
                  return;
               }
            }
            else if( ( fileChooser.getSelectedFile().listFiles().length ) > 0 )
            {
               result = JOptionPane.showConfirmDialog( ref, "This folder contains data.\nSaving here may cause your data to be lost.\nAre you sure to proceed?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE );
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

         File outputFolderName = fileChooser.getSelectedFile();

         ref.setTitle( "Please wait..." );


         int bytesRead;
         byte bytes[] = new byte[ BYTE_SIZE ];
         int currentLength = 0;
         long lengthOfFirstFile = inputFileName.length();

  
         //d( "Length: " + lengthOfFirstFile );


         String sourceFolderName = outputFolderName.getAbsolutePath();
         String currentFileName;
         DataOutputStream currentFile;
         File tmpFile;
         long currentPosition = 0;
         long remainingLength = 0;
         for( int i = 0; i < infoContainer.allFileInfo.length; i++ )
         {

            if( i > writeLimit )
            {
               break;
            }

            //d( "Current File: " + infoContainer.allFileInfo[ i ].filePath  );

            if( checked[ i ] == false )  //If not checked this file is omitted in writing(saving)
            {

               remainingLength = infoContainer.allFileInfo[ i ].fileLength;
               currentPosition = inputFile.getFilePointer();

               //d( "currentPosition: " + currentPosition );
               //d( "remainingLength: " + remainingLength );
               //d( "inputFileName.length(): " + inputFileName.length() );

               if( ( currentPosition + remainingLength ) > inputFileName.length() )
               //if current output file length is greater then bytes remaining in the current input file
               {

                  remainingLength -= inputFileName.length() - inputFile.getFilePointer();

                  while( true )
                  {
                     currentPosition = inputFile.getFilePointer();
                     fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
                     result = fileChooser.showDialog( ref, "Select next file" );
                     if( result != JFileChooser.APPROVE_OPTION )
                     {
                        result = JOptionPane.showConfirmDialog( ref, "Do you want to cancel?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
                        if( result == JOptionPane.YES_OPTION )
                        {
                           return;
                        }
                     }
                     else if(  !( fileChooser.getSelectedFile().exists() ) )
                     {
                         result = JOptionPane.showConfirmDialog( ref, "Invalid file selected.\nTry again?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
                        if( result != JOptionPane.YES_OPTION )
                        {
                           return;
                        }
                     }
                     else
                     {

                        inputFile.close();
                        inputFile = null;
                        inputFileName = fileChooser.getSelectedFile();
                        inputFile = new RandomAccessFile( inputFileName, "r" );
                        inputFile.seek( 0 );
                        break;

                     }

                  }

                  if( inputFileName.length() >= remainingLength )
                  {
                     currentPosition = inputFile.getFilePointer();
                     //d( "remainingLength: " + remainingLength );
                     //d( " currentPosition : " +  currentPosition  );
                     //d( "going to location(1): " + ( currentPosition + remainingLength ) );
                     inputFile.seek( currentPosition + remainingLength );
                     continue;
                  }
                  else
                  {
                     remainingLength -= inputFileName.length();
                     continue;
                  }

               }
               else  // !( ( currentPosition + remainingLength  ) >= inputFileName.length() )
               {
                  currentPosition = inputFile.getFilePointer();
                  //d( "going to location(2): " + ( currentPosition + remainingLength ) );
                  inputFile.seek( currentPosition + remainingLength );
               }
               continue;
            }  //End of: if( checked[ i ] == false )

            currentFileName = new String( sourceFolderName );
            //JOptionPane.showMessageDialog( ref, "" + infoContainer.allFileInfo[ i ].filePath );
            currentFileName += infoContainer.allFileInfo[ i ].filePath;
            ref.setTitle(  "( " + ( i+1 ) + " / " + infoContainer.allFileInfo.length + " )[ " + new File( currentFileName ).getName() + " at " + new File( currentFileName ).getParentFile() + " ]" );
            //JOptionPane.showMessageDialog( ref, currentFileName );
            tmpFile = new File( currentFileName );
            makeDirectoryTree( tmpFile, true );
            //d( "cflength: " + currentFileName );
            currentFile = new DataOutputStream( new FileOutputStream( currentFileName ) );
            remainingLength = infoContainer.allFileInfo[ i ].fileLength;
            //d( "remainingLength: " + remainingLength );
            while( true )
            {
               if( remainingLength <= 0 )
                  break;
               if( remainingLength < BYTE_SIZE )
               {
                  currentLength = ( int ) remainingLength;
                  //d( "currentRead: " + currentLength );
               }
               else
               {
                  currentLength = BYTE_SIZE;
                  //d( "currentRead: " + currentLength );
               }

               //d( "inputFile.getfilePointer() + currentLength: " + ( inputFile.getFilePointer() + currentLength ) );
               //d( "inputFileName.length(): " + inputFileName.length() );

               if( ( inputFile.getFilePointer() + currentLength ) > inputFileName.length() )
               {
                  //d( "Greater: " );
                  //d( "inputFileName.length(): " + inputFileName.length() );
                  //d( "inputFile.getFilePointer(): " + inputFile.getFilePointer() ); 
                  currentLength = ( int ) ( inputFileName.length() - inputFile.getFilePointer() );
                  //d( "currentLength: " + currentLength );
               }

/*
               if( currentLength <= 0 )
               {
                  //d( "currentLength: " + currentLength );
                  //d( "remainingLength: " + remainingLength );
               }
*/

               if( currentLength <= 0 && remainingLength != 0 )  
               /*If there exists more data in FileInfo than the selected input file then take another file as input*/
               {
                  fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
                  while( true )
                  {
                     result = fileChooser.showDialog( ref, "Select next file" );
                     if( result != JFileChooser.APPROVE_OPTION )
                     {
                        result = JOptionPane.showConfirmDialog( ref, "Do you want to cancel?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
                        if( result == JOptionPane.YES_OPTION )
                        {
                           return;
                        }
                     }
                     else if(  !( fileChooser.getSelectedFile().exists() ) )
                     {
                         result = JOptionPane.showConfirmDialog( ref, "Invalid file selected.\nTry again?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
                        if( result != JOptionPane.YES_OPTION )
                        {
                           return;
                        }
                     }
                     else
                     {
                        inputFile.close();
                        inputFile = null;
                        inputFileName = fileChooser.getSelectedFile();
                        inputFile = new RandomAccessFile( inputFileName, "r" );
                        inputFile.seek( 0 );
                        break;
                     }
                  }
               }

               //d( "currentLength: " + currentLength );
               bytesRead = inputFile.read( bytes, 0, currentLength );

               remainingLength -= bytesRead;
               //d( "" + remainingLength );
               //d( "" + currentLength );
               //d( "" + bytesRead );
               if( bytesRead > 0 )
               {
                  currentFile.write( bytes, 0, bytesRead );
               }
            }
            currentFile.close();
            ref.setTitle(  "( " + ( i+1 ) + " / " + infoContainer.allFileInfo.length + " )[ " + new File( currentFileName ).getName() + " at " + new File( currentFileName ).getParentFile() + " ]" );
            tmpFile.setLastModified( infoContainer.allFileInfo[ i ].fileLastModified );
         }
         inputFile.close();
         JOptionPane.showMessageDialog( ref, "Operation Completed Successfully", "Job Done", JOptionPane.INFORMATION_MESSAGE );
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
   public void awaken()
   {
      sleeping = false;
   }
   public void setInfoContainer( FileInfoContainer fic )
   {
      infoContainer2 = fic;
   }
   public void updateChecks()
   {

      //d( "infoContainer2.allFileInfo.length: " + infoContainer2.allFileInfo.length );

      checked = new boolean[ infoContainer.allFileInfo.length ];
      for( int i = 0; i < infoContainer.allFileInfo.length; i++ )
      {
         checked[ i ] = false;
      }
      int j = 0;
      for( int i = 0; i< infoContainer.allFileInfo.length; i++ )
      {

         //d( "i: " + i + " j: " + j );

         if( j  >= infoContainer2.allFileInfo.length )
         {
            writeLimit = i;
            break;
         }
         if( infoContainer.allFileInfo[ i ].equals( infoContainer2.allFileInfo[j ] ) )
         {
            checked[ i ] = true;
            writeLimit = i;
            j++;
         }
      }
   }
   public void d( String msg )
   {
      JOptionPane.showMessageDialog( ref, msg );
   }
}

                                                                                                           