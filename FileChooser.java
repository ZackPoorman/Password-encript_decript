 


import java.io.*;
import java.util.*;
import javax.swing.*;

public class FileChooser {
    
    private boolean append;
    
    public FileChooser() {
    }

    /**
     * Allows the user to select a text file from a file chooser.
     * 
     * @return the absolute file name.
     */
    public String chooseFile( String openOrSave, String fileName )
    {
//         // Sets the current directory to be the default.
//         JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir")));

        // Sets the default to be the parent of the current directory.
        File f = new File(System.getProperty( "user.dir" ) );
        JFileChooser chooser = new JFileChooser( f.getParent( ) );

        chooser.setFileFilter( new Filter( ) );
        chooser.setDialogTitle( "Select file to " + openOrSave );

        chooser.setSelectedFile( new File( fileName ) );

        int status = 0;
        if (openOrSave.toLowerCase().equals("open"))
        {
            //             File file = new File( fileName );
            //             if (file.exists())
            //             {
            //                 chooser.setSelectedFile( file );
            //             }

            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            status = chooser.showOpenDialog( null );
        }
        else if (openOrSave.toLowerCase().equals("save"))
        {
            chooser.setDialogType( JFileChooser.SAVE_DIALOG );
            status = chooser.showSaveDialog(null);
        }

        if (status == JFileChooser.CANCEL_OPTION)
        {
            fileName = "";
        }
        else if (status == JFileChooser.APPROVE_OPTION)
        {
            fileName = chooser.getSelectedFile( ).getAbsolutePath( );

            /**
             * A new file is named to include a file type extension,
             * whereas the name of an existing file already includes
             * a file type extension.
             */
            File file = new File(fileName);
            if (!file.exists( ))
            {
                fileName += ".txt";
            }
        }
        return fileName;
    }

    /**
     * Filter, as used in a file chooser, narrows the selection to *.txt files.
     */
    class Filter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File f)
        {
            return f.isDirectory( ) || f.getAbsolutePath( ).endsWith( ".txt" );
        }

        @Override
        public String getDescription()
        {
            return "Text document ( *.txt )";
        }
    }

    public static void main( )
    {
        String inputFileName;
        String outputFileName;

        FileChooser textFile = new FileChooser( );

        inputFileName   = textFile.chooseFile( "open", "file to read" );
        outputFileName  = textFile.chooseFile( "save", "file to write" );

        System.out.println( inputFileName );
        System.out.println( outputFileName );
    }        

}