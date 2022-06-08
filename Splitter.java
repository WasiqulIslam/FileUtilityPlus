//7:39 PM 7/27/2005
//u 7:21 PM 7/28/2005
//u 6:37 AM 3/11/2006

import java.io.*;
import javax.swing.*;
import java.util.*;

public class Splitter extends Thread
{

   private FileUtilityPlus ref;
   public final static int BYTE_SIZE = 1000000;
   long totalLength, currentLength, remainingLength;

   public Splitter( FileUtilityPlus r )
   {
      ref = r;
   }

   public void run()
   {
      try
      {
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
         int result;
         while( true )
         {
            result = fileChooser.showDialog( ref, "Open" );
            if( result != JFileChooser.APPROVE_OPTION )
            {
               result = JOptionPane.showConfirmDialog( ref, "Really want to cancel?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
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

         File inputFileName = fileChooser.getSelectedFile();

         totalLength = inputFileName.length();
         JOptionPane.showMessageDialog( ref, "File length: " + totalLength + " bytes" );
         fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
         remainingLength = totalLength;
         DataInputStream inputFile = new DataInputStream( new FileInputStream( inputFileName ) );
         byte bytes[] = new byte[ BYTE_SIZE ];
         while( true )
         {
            if( remainingLength <= 0 )
               break;
            while( true )
            {
               try
               {
                  currentLength = Long.parseLong( JOptionPane.showInputDialog( "Enter next file length." + "\n( remaining: " + remainingLength + " )" ) );
                  break;
               }
               catch( NumberFormatException nfe )
               {
                  result = JOptionPane.showConfirmDialog( ref, "Invalid Number Entered.\nTry again?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
                  if( result != JOptionPane.YES_OPTION )
                  {
                     return;
                  }                  
               }
            }
            if( currentLength > remainingLength )
               currentLength = remainingLength;
            remainingLength -= currentLength;

            while( true )
            {
               result = fileChooser.showDialog( ref, "Save" );
               if( result != JFileChooser.APPROVE_OPTION )
               {
                  result = JOptionPane.showConfirmDialog( ref, "Really want to cancel?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
                  if( result == JOptionPane.YES_OPTION )
                  {
                     return;
                  }
               }
               else if( fileChooser.getSelectedFile().exists() )
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

            File outputFileName = fileChooser.getSelectedFile();
            DataOutputStream outputFile = new DataOutputStream( new FileOutputStream( outputFileName ) );
            long remainingLength2 = currentLength;
            long currentLength2 = currentLength;
            int dataRead;
            while( true )
            {
               if( remainingLength2 <= 0  )
                  break;
               if( remainingLength2 < BYTE_SIZE )
                  currentLength2 = remainingLength2;
               else
                  currentLength2 = BYTE_SIZE;
               dataRead = inputFile.read( bytes, 0, ( int ) currentLength2 );
               outputFile.write( bytes, 0, dataRead );
               remainingLength2 -= dataRead;
               ref.setTitle( "" +( int ) ( ( ( float )( currentLength - remainingLength2 ) / currentLength ) * 100 ) + "% complete"  );
            }
            outputFile.close();
         }
         inputFile.close();
         JOptionPane.showMessageDialog( ref, "File Successfully Splitted", "Success", JOptionPane.INFORMATION_MESSAGE );
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
}
