//7:39 PM 7/27/2005
//u 6:37 AM 3/11/2006


import java.io.*;
import javax.swing.*;
import java.util.*;

public class Combiner extends Thread
{

   private FileUtilityPlus ref;
   public final static int BYTE_SIZE = 1000000;
   long totalLength, currentLength, remainingLength;

   public Combiner( FileUtilityPlus r )
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
            result = fileChooser.showDialog( ref, "Save" );
            if( result != JFileChooser.APPROVE_OPTION )
            {
               result = JOptionPane.showConfirmDialog( ref, "Do you want to cancel?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE );
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
         byte bytes[] = new byte[ BYTE_SIZE ];
         boolean firstTime = true;
         while( true )
         {
            if( firstTime )
            {
               result = JOptionPane.YES_OPTION;
               firstTime = false;
            }
            else
            {
               result = JOptionPane.showConfirmDialog( ref, "Do you want to add another file?", "Add Confirmation", JOptionPane.YES_NO_OPTION );
            }

            if( result != JOptionPane.YES_OPTION )
               break;

         while( true )
         {

            result = fileChooser.showDialog( ref, "Add" );
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
               break;
            }
         }

            File inputFileName = fileChooser.getSelectedFile();
            long inputFileLength = inputFileName.length(), currentLength;
            DataInputStream inputFile = new DataInputStream( new FileInputStream( inputFileName ) );
            int dataRead;
            currentLength = 0;
            while( true )
            {
               dataRead = inputFile.read( bytes, 0, BYTE_SIZE );
               if( dataRead == -1 )
                  break;
               outputFile.write( bytes, 0, dataRead );
               currentLength += dataRead;
               ref.setTitle( "" + ( int ) ( ( ( ( float )currentLength ) / inputFileLength ) * 100 ) + "% complete"  );
            }
            inputFile.close();
         }
         outputFile.close();
         JOptionPane.showMessageDialog( ref, "File Successfully Combined", "Success", JOptionPane.INFORMATION_MESSAGE );
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
