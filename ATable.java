//6:52 AM 3/11/2006
//u 6:06 PM 3/17/2006
//u 10:41 AM 3/26/2006
//u 11:02 AM 3/26/2006

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class ATable extends JDialog implements ActionListener
{

   private FileUtilityPlus ref2;
   private Sleepable ref;
   private FileInfoContainer fileInfoContainer;
   private JCheckBox checks[];
   private JTextField texts[];
   private JPanel panels[];
   private JPanel mainPanel;

   private JButton checkAllButton;
   private JButton uncheckAllButton;
   private JButton showAllButton;
   private JTextField searchField;
   private JLabel searchLabel;
   private JButton okButton;
   private JButton helpButton;

   private JPanel upperPanel;
   private JPanel lowerPanel;

   private JScrollPane scroller;
   private Container container;


   public ATable( FileUtilityPlus fup, Sleepable slp, FileInfoContainer fic )
   {

      super( fup, "All File List: [ Total " + fic.allFileInfo.length + " file(s) ]" );

      setBackground( Color.white );

      ref = slp;
      ref2 = fup;
      fileInfoContainer = fic;

      checks = new JCheckBox[ fileInfoContainer.allFileInfo.length ];
      texts = new JTextField[ fileInfoContainer.allFileInfo.length ];
      panels = new JPanel[ fileInfoContainer.allFileInfo.length ];

      mainPanel = new JPanel( new FlowLayout() );

      container = this.getContentPane();
      container.setLayout( new BorderLayout() );

      checkAllButton = new JButton( "Check All" );
      uncheckAllButton = new JButton( "Uncheck All" );
      showAllButton = new JButton( "Show All" );
      searchLabel = new JLabel( "Search" );
      searchField = new JTextField( 10 );
      searchField.setToolTipText( "Type a keyword and press ENTER to search." );
      okButton = new JButton( "OK" );
      helpButton = new JButton( "Help" );

      checkAllButton.addActionListener( this );
      uncheckAllButton.addActionListener( this );
      showAllButton.addActionListener( this );
      searchField.addActionListener( this );
      okButton.addActionListener( this );
      helpButton.addActionListener( this );

      upperPanel = new JPanel( new FlowLayout() );
      upperPanel.add( searchLabel );
      upperPanel.add( searchField );
      upperPanel.add( showAllButton );
      upperPanel.add( checkAllButton );
      upperPanel.add( uncheckAllButton );
      upperPanel. setBackground( Color.white );

      container.add( upperPanel, BorderLayout.NORTH );


      lowerPanel = new JPanel( new FlowLayout() );
      lowerPanel.add( okButton );
      lowerPanel.add( helpButton );
      lowerPanel.setBackground( Color.white);

      container.add( lowerPanel, BorderLayout.SOUTH );

      mainPanel = new JPanel( new GridLayout( fileInfoContainer.allFileInfo.length, 1 ) );
      //mainPanel.setSize( new Dimension( 500, fileInfoContainer.allFileInfo.length  * 5) );
      mainPanel.setBackground( Color.white );

      for( int i =0; i <= ( fileInfoContainer.allFileInfo.length - 1); i++ )
      {

         panels[ i ] = new JPanel( new FlowLayout() );
         panels[ i ].setBackground( Color.white );
         checks[ i ] = new JCheckBox( "Included", true );
         checks[ i ].setBackground( Color.white );
         texts[ i ] = new JTextField( 50 );
         texts[ i ].setText( "Path: " + fileInfoContainer.allFileInfo[ i ].filePath + ", Length: " + fileInfoContainer.allFileInfo[ i ].fileLength + ", Last Modified: " + new Date( fileInfoContainer.allFileInfo[ i ].fileLastModified ).toString() );
         texts[ i ].setEditable( false );
         texts[ i ].setBackground( Color.white );
         panels[ i ].setSize( 600, 15 );
         panels[ i ].add( checks[ i ] );
         panels[ i ].add( texts[ i ] );
         mainPanel.add( panels[ i ] );

      }

      scroller = new JScrollPane( mainPanel );
      scroller.setPreferredSize( new Dimension( 400, 400 ) );
      scroller.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
      scroller.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
      scroller.setBackground( Color.white );
      container.add( scroller, BorderLayout.CENTER );
      container.validate();

      this.setSize( 800, 600 );
      this.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
      ref2.setTitle( "File Utility Plus" );
      this.setVisible( true );

   }

   public void actionPerformed( ActionEvent event )
   {
       if( event.getSource() == searchField )
      {
         String searched = searchField.getText();
         showAll();
         Vector allParsed = parseString( searched.toUpperCase() );

         for( int i = 0; i < allParsed.size(); i++ )
         {
            //d( ( ( String ) allParsed.elementAt( i ) ) );
         }

         for( int i = 0; i < fileInfoContainer.allFileInfo.length; i++ )
         {
            if( !search( fileInfoContainer.allFileInfo[ i ].filePath.toUpperCase() , allParsed ) )
            {
               //d( "Setting invisible to : " + i );
               panels[ i ].setVisible( false );
               checks[ i ].setVisible( false );
               texts[ i ].setVisible( false);
            }
         }
         refreshChecks();
      }

       else if( event.getSource() == checkAllButton )
      {
          for( int i = 0; i < fileInfoContainer.allFileInfo.length; i++ )
         {
            if( panels[ i ].isVisible() )
            {
               checks[ i ].setSelected( true );
            }
         }
      }
       else if( event.getSource() == uncheckAllButton )
      {
          for( int i = 0; i < fileInfoContainer.allFileInfo.length; i++ )
         {
            if( panels[ i ].isVisible() )
            {
               checks[ i ].setSelected( false );
            }
         }
      }
       else if( event.getSource() == showAllButton )
      {
         showAll();
         this.setTitle( "All File List: [ Total " + fileInfoContainer.allFileInfo.length + " file(s) ]" );
      }
       else if( event.getSource() ==  okButton )
      {
         int count = 0;
         for( int i = 0; i < fileInfoContainer.allFileInfo.length; i++ )
         {
            if( checks[ i ].isSelected() )
            {
               count++;
            }
         }
         if( count <= 0 )
         {
            JOptionPane.showMessageDialog( ref2, "You haven't selected any file.", "No file selected", JOptionPane.ERROR_MESSAGE );
            return;
         }
         FileInfoContainer fileIC = new FileInfoContainer();
         fileIC.allFileInfo = new FileInfo[ count ];
         int j = 0;
         for( int i = 0; i < fileInfoContainer.allFileInfo.length; i++ )
         {
            if( checks[ i ].isSelected() )
            {
               fileIC.allFileInfo[ j ] = new FileInfo( fileInfoContainer.allFileInfo[ i ] );
               j++;
            }
         }
         this.setVisible( false );
         ref.setInfoContainer( fileIC );
         ref.awaken();
      }
       else if( event.getSource() == helpButton )
      {
         JTextArea ta = new JTextArea( 15, 60 );
         ta.setEditable( false );
         ta.setText( "A list of files are shown in the main window.\n" +
            "For each file there is a \"Included\" labeled check box.\n" +
            "You can check or uncheck any file(s) as you like.\n" +
            "Only the checked files are included for the current operation.\n" +
            "You can search specific files by typing a file name in the search\n" +
            "box. The searching is not case sensitive; That is lower and upper case\n" +
            "is treated as same. You can use special characters such as \"*\"\n" +
            "and \"?\". \"*\" represents any string of any length including zero\n" +
            "length. \"?\" represents any string of exactly length one. This is somewhat\n" +
            "similar to windows file searching systems. Searching includes only\n" +
            "file names and path and not file length or modification date.\n\n" +
            "Example:\n\n" +
            "search: \"*.exe\" will show all windows executable file such as:\n" +
            "\"\\Notepad.exe\"\n" +
            "\"\\winword.exe\"\n" +
            "\"\\important\\server.exe\"\n" +
            "etc.\n" +
            "search: \"\\path1\\path2\\*.txt\" will show all text files in location( including sub files )\n" +
            "\\path1\\path2.\n\n" +
            "search: \"*\\???.txt\" will show all text files of length 3 such as:\n\n" +
            "\"\\doc.txt\"\n" +
            "\"\\hlp.txt\"\n" +
            "\"\\abc\\def\\ghi.txt\"\n" +
            "etc.\n\n" +
            "and will not show \"\\show.txt\" as it has length 4.\n\n" +
            "After you have searched some files you can click\n" +
            "\"Uncheck All\" button to uncheck all the searched files.\n\n" +
            "\"Check All\" lets you check all the files. You can also check\n" +
            "or uncheck a file by clicking on the tick mark( check box).\n\n" +
            "\"Check All\" and \"Uncheck All\" works for the currently shown\n" +
            "files and not for all the files.\n\n" +
            "To be sure which files are checked and which are not you can\n" +
            "chick \"Show All\" button to view all files.\n\n" +
            "A sample example:\n\n" +
            "Suppose that you have 10 files in your list. Clicking \"Uncheck All\"\n" +
            "will uncheck all the 10 files. Now suppose that you have searched\n" +
            "some specific files using the search field and it shows only 3 of the 10 files.\n" +
            "Then you clicked \"Check All\" button to check the 3 searched files. After this clicking\n" +
            "on \"Show All\" button will show all the 10 files but you will see that only 3\n" +
            "of them are checked. The files which are checked are only used for the\n" +
            "current operation.\n" );

         ta.select( 0, 0 );

         JOptionPane.showMessageDialog( ref2, new JScrollPane( ta ), "Help", JOptionPane.INFORMATION_MESSAGE );

      }
   }
   public void showAll()
   {
       for( int i = 0; i < fileInfoContainer.allFileInfo.length; i++ )
      {
         panels[ i ].setVisible( true );
         checks[ i ].setVisible( true );
         texts[ i ].setVisible( true );
      }
      refreshChecks();
   }
   public void refreshChecks()
   {

      mainPanel.removeAll();

      int count = 0;
      for( int i =0; i <= ( fileInfoContainer.allFileInfo.length - 1); i++ )
      {
         if( panels[ i ].isVisible() )
         {
            mainPanel.add( panels[ i ] );
            count++;
         }
      }

      this.setTitle( "Searched File List: [ Total " + count + " file(s), search text: '" + searchField.getText() + "' ]" );

      mainPanel.setVisible( false );
      mainPanel.setVisible( true );

   }

   public Vector parseString( String searched )
   {
      Vector allParsed = new Vector();
      StringTokenizer tokens1 = new StringTokenizer( searched, "*" );
      String currentToken;
      int start = 0;
      int end = 0;
      while( tokens1.hasMoreTokens() )
      {
         currentToken = tokens1.nextToken();
         end = ( searched.indexOf( currentToken, start ) );
         for( int i = start; i < end; i++ )
         {
            allParsed.addElement( "" + searched.charAt( i ) );
         }
         allParsed.addElement( currentToken );
         start = end + currentToken.length();
      }

      return allParsed;

   }

   public boolean search( String texts, Vector tokens )
   //returns true if the search criteria matches
   {
      int start = 0;
      boolean flag = true, flag2 = true;
      String currentText;

      //d( "Searched string: " + texts );

      boolean searchFlag = false;  //searching does not assume an inherent star(*) sign
      //in the beginning of the search string

      for( int i = 0; i < tokens.size(); i++ )
      {


         currentText = ( ( String ) tokens.elementAt( i ) );

         if( currentText.equals( "*" ) )
         {
            searchFlag = true;
            continue;
         }


         if( searchFlag )
         {
            flag2 = false;
            for( int j = start; j <= ( texts.length() - currentText.length() ); j++ )
            {
               if( check( texts.substring( j, ( j + currentText.length() ) ), currentText ) )
               {
                  flag2 = true;
                  start = j + currentText.length();
                  break;
               }
            }

            if( flag2 == false )
            {
               flag = false;
               break;
            }
         }
         else  //!searchFlag
         {
            if( ( texts.length() < currentText.length() ) || !( check( texts.substring( 0, currentText.length() ), currentText ) ) )
            {
               flag = false;
               start = currentText.length();
               break;
            }
            else
            {
               start = currentText.length();
               //auto -> continue;
            }
         }

         //d( "start: " + start );
         //d( "Left: " + texts.substring( start ) );

      }

      if( flag == true )
      {
         //d( "SEARCH PASSED" );
      }

      return flag;
   }

   private boolean check( String texts, String searched )
   //Assume that all strings are in uppercase
   {

      //d( "checking : " + texts + " with " + searched );

      if( texts.length() != searched.length() )
      {
         return false;
      }
      boolean flag = true;
      for( int i = 0; i < searched.length(); i++ )
      {
         if( !( ( "" + searched.charAt( i ) ).equals( "?" ) ) )
         {
            if( !( ( "" + searched.charAt( i ) ).equals( ( "" + texts.charAt( i ) ) ) ) )
            {
               flag = false;
               break;
            }
         }
      }

      if( flag == true )
      {
         //d( "checking returning true." );
      }

      return flag;
   }

   public void d( String msg )
   {
      JOptionPane.showMessageDialog( ref2, msg );
   }

   public void finalize()
   {

      ref2 = null;
      ref = null;
      fileInfoContainer = null; 
      checks = null;
      texts = null;
      panels = null;
      mainPanel = null;
      checkAllButton = null;
      uncheckAllButton = null;
      showAllButton = null;
      searchField = null;
      searchLabel = null;
      okButton = null;
      helpButton = null;
      upperPanel = null;
      lowerPanel = null;
      scroller = null;
      container = null;

      System.gc();

   }

}