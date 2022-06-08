//3:43 PM 7/20/2005
//u 8:59 AM 7/31/2005
//u 6:37 AM 3/11/2006
//u 1:30 AM 3/16/2006
//v 1.0 2:08 AM 3/16/2006
//u 6:07 PM 3/17/2006
//u 6:46 PM 3/17/2006
//V 1.1( SOME IMPROVEMENTS) 7:40 AM 3/18/2006
//file list view confirmation added and some bug solved 11:12 AM 3/26/2006
//u 11:13 AM 3/26/2006
//u 7:48 PM 4/24/2006


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FileUtilityPlus extends JFrame implements ActionListener, WindowListener
{

   private boolean busy;
   private JButton toFileButton, toFolderButton, splitButton, combineButton, helpButton;

   private ToFile toFile;
   private ToFolder toFolder;
   private Combiner combiner;
   private Splitter splitter;

   public FileUtilityPlus()
   {

      super( "File Utility Plus" );

      busy = false;

      Container container = getContentPane();
      container.setLayout( new FlowLayout() );

      toFileButton = new JButton( "Convert to File" );
      toFileButton.addActionListener( this );
      toFileButton.setPreferredSize( new Dimension( 150, 27 ) );
      toFileButton.setToolTipText( "Convert some file(s) in a folder into a single file" );
      toFileButton.setForeground( Color.blue );
      toFileButton.setBackground( Color.black );
      container.add( toFileButton );

      toFolderButton = new JButton( "Convert to Folder" );
      toFolderButton.addActionListener( this );
      toFolderButton.setPreferredSize( new Dimension( 150, 27 ) );
      toFolderButton.setToolTipText( "Convert a previously created container file into several file(s)" );
      toFolderButton.setForeground( Color.blue );
      toFolderButton.setBackground( Color.black );
      container.add( toFolderButton );

      splitButton = new JButton( "Split" );
      splitButton.addActionListener( this );
      splitButton.setPreferredSize( new Dimension( 150, 27 ) );
      splitButton.setToolTipText( "Split a file into some small files for portability" );
      splitButton.setForeground( Color.orange );
      splitButton.setBackground( Color.black );
      container.add( splitButton );

      combineButton = new JButton( "Combine" );
      combineButton.addActionListener( this );
      combineButton.setPreferredSize( new Dimension( 150, 27 ) );
      combineButton.setToolTipText( "Combine some previously splitted file to make a single file" );
      combineButton.setForeground( Color.orange );
      combineButton.setBackground( Color.black );
      container.add( combineButton );

      helpButton = new JButton( "Help/About" );
      helpButton.addActionListener( this );
      helpButton.setPreferredSize( new Dimension( 150, 27 ) );
      helpButton.setForeground( Color.magenta );
      helpButton.setBackground( Color.black );
      container.add( helpButton );

      //container.setBackground( new Color( 158, 172, 177 ) );
      container.setBackground( new Color( 131, 201, 126 ) );
      setSize( 550, 110 );
      setLocation( 100, 100 );
      setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
      addWindowListener( this );
      setResizable( false );
      setVisible( true );

   }

   public void actionPerformed( ActionEvent event )
   {

      if( event.getSource() == helpButton )
      {
         showHelp();
         return;
      }

      if( busy )
         return;

      disableButtons();

      if( event.getSource() == toFileButton )
      {
         doOperationOne();
      }
      else if( event.getSource() == toFolderButton )
      {
         doOperationTwo();
      }
      else if( event.getSource() == splitButton )
      {
         doOperationThree();
      }
      else if( event.getSource() == combineButton )
      {
         doOperationFour();
      }

   }

   public void showHelp()
   {
      String helpText = "File Utility Plus v1.1( 7:48 PM 4/24/2006 MON)\n"
         + "Programmed by Wasiqul Islam( e_mail: wasiqul_islam@yahoo.com )\n"
         + "This program has four options:\n\n1) CONVERT TO FILE: You can convert a folder and all of its contents into a single file.First select the folder; then a list will show you all the file names of that folder;click OK and then select the output file.\n[ Note: The folder size should not be more than 3.9 GB( Giga Byte ) long in a FAT or FAT32 file system. If it is more than that you have to split the folder contents into two or more folders. This is because some file system does not support creating a large file ( e.g. 5GB etc.). You can use NTFS to create a large file greater than 4GB. ]"
         + "\n\n2) CONVERT TO FOLDER: This is reverse operation of \'Convert to file\'. Just select the source file first and then select a destination folder."
         + "\n\n3) SPLIT: You can split a file into two or more files. This is useful when you consider saving file in a backup device which can contain data less than the size of that file. To do this, first select the source file; then type the size of a destination file and then its name; repeat the operation until all source file data is written. When splitting always remember the splitting sequence."
         + "\n\n4) COMBINE: This is reverse operation of \'Split\'. To combine some files, first select the destination file and then repeatedly add some files to the destination file; a confirmation will let you stop your operation.  When combining always remember the previous splitting sequence.\n\n\n"
         + "An example:\n\n"
         + "Suppose that you have a folder named \"Data\" and its size is about 2 GB long. You want to CD-write the files but a CD can contain about 650 MB data.\n\n"
         + "In this case you have to convert the folder and all of its contents into a single file using \"To File\" button of this software. We assume that You made a file named \"All.data\" which will be the same size of the Data folder( 2GB ).\n\n"
         + "Now Split the file \"All.data\" into \"Part1.data\", \"Part2.data\", \"Part3.data\", \"Part4.data\" files.\n"
         + "To create these four files use the length ( 649 X 1024 X 1024 ) bytes  for all the four files.\n\n"
         + "Now You can write these four files into 4 CDs.\n\n"
         + "You can retrieve all the data from the 4 CDs in 2 ways.\n\n"
         + "(1)\n\n   (A)By combining the four files into a single file named \"All.data\"( using \"Combine\" button ). In this case you have to combine the files sequentially. That is \"Part1.data\", then \"Part2.data\" and so on. \n"
         + "\n   (B)Then you can use \"To Folder\" button of this software to retrieve all data from the file \"All.data\".\n\n"
         + "\n(2)The second way has only one step. Use the \"To Folder\" button and select the file \"Part1.data\" as source. After a while a \"Select Next File\" prompt will allow you to select the second file ( \"Part2.data\" ). This way after you have selected all the four data files sequentially the operation completes. This is a shortcut way which will be very useful to you.\n\n\n"
         + "By the way, you can exclude any computer virus files from the archive files when it is archiving( only if you know the file name; e.g. \"Desktop.ini\", \"WinZipTmp.exe\", \"Folder.htt\", \"Temp.htt\" etc. ). To search any file named \"Desktop.ini\" you have to type \"*\\desktop.ini\" in the search keyword. You can exclude any viruses by searching.";
      JTextArea ta = new JTextArea( 15, 50 );
      ta.setText( helpText );
      ta.setSelectionStart( 0 );
      ta.setSelectionEnd( 0 );
      ta.setEditable( false );
      ta.setLineWrap( true );
      ta.setWrapStyleWord( true );
      JScrollPane sp = new JScrollPane( ta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
      JOptionPane.showMessageDialog( this, sp, "Help/About", JOptionPane.INFORMATION_MESSAGE );      
   }

   public void doOperationOne()   //tag
   {

   
      if( toFile != null )
      {
         toFile.stop();
         toFile = null;
      }

      if( toFolder != null )
      {
         toFolder.stop();
         toFolder = null;
      }

      if( splitter != null )
      {
         splitter.stop();
         splitter = null;
      }
      if( combiner != null )
      {
         combiner.stop();
         combiner = null;
      }
      System.gc();


      toFile = new ToFile( this );
      toFile.start();
   }

   public void doOperationTwo()   //tag
   {


      if( toFile != null )
      {
         toFile.stop();
         toFile = null;
      }

      if( toFolder != null )
      {
         toFolder.stop();
         toFolder = null;
      }

      if( splitter != null )
      {
         splitter.stop();
         splitter = null;
      }
      if( combiner != null )
      {
         combiner.stop();
         combiner = null;
      }
      System.gc();
   
      toFolder = new ToFolder( this );
      toFolder.start();
   }

   public void doOperationThree()   //tag
   {


      if( toFile != null )
      {
         toFile.stop();
         toFile = null;
      }

      if( toFolder != null )
      {
         toFolder.stop();
         toFolder = null;
      }

      if( splitter != null )
      {
         splitter.stop();
         splitter = null;
      }
      if( combiner != null )
      {
         combiner.stop();
         combiner = null;
      }
      System.gc();
   
      splitter = new Splitter( this );
      splitter.start();
   }

   public void doOperationFour()   //tag
   {


      if( toFile != null )
      {
         toFile.stop();
         toFile = null;
      }

      if( toFolder != null )
      {
         toFolder.stop();
         toFolder = null;
      }

      if( splitter != null )
      {
         splitter.stop();
         splitter = null;
      }
      if( combiner != null )
      {
         combiner.stop();
         combiner = null;
      }
      System.gc();
   
      combiner = new Combiner( this );
      combiner.start();
   }

    public void windowOpened(WindowEvent e){}
    public void windowClosed(WindowEvent e){}
    public void windowIconified(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
    public void windowActivated(WindowEvent e){}
    public void windowDeactivated(WindowEvent e){}

    public void windowClosing(WindowEvent e)
   {
      int result = JOptionPane.showConfirmDialog( this, "Really Want to Exit?", "Confirmation", JOptionPane.YES_NO_OPTION );
      if( result == JOptionPane.YES_OPTION )
      {
         System.exit( 0 );
      }
   }

   public void enableButtons()
   {
      toFileButton.setEnabled( true );
      toFolderButton.setEnabled( true );
      splitButton.setEnabled( true );
      combineButton.setEnabled( true );
      busy = false;
      setTitle( "File Utility Plus" );
   }

   private void disableButtons()
   {
      toFileButton.setEnabled( false );
      toFolderButton.setEnabled( false);
      splitButton.setEnabled( false );
      combineButton.setEnabled( false );
      busy = true;
   }

   public static void main( String args[] )
   {
      new FileUtilityPlus();
   }

}