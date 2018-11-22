
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*******************************************************************
 * Decryption encapsulates a scrambled message as a list of
 * substrings. Decryption methods manipulate the list structure
 * to decrypt the message, i.e. restore it to the original
 * string.
 *******************************************************************/
public class Decryption
{

    private ListOfStrings scrambledMessage;

    /*******************************************************************
     * Constructor to decrypt a message that is split up as a
     * ListOfStrings.
     *******************************************************************/
    public Decryption( ListOfStrings message) 
    {
        scrambledMessage = message;
    }

    /*******************************************************************
     * Constructor to decrypt a message stored by a file along with
     * a list of decryption commands.
     *******************************************************************/
    public Decryption(String fileName ) 
    {
        FileChooser fileChooser;
        fileChooser = new FileChooser( );
        fileName = fileChooser.chooseFile( "open", fileName );  // the file to read

        if ( fileName != null && fileName.length() > 0) {

            Scanner reader = this.openInput( fileName );

            scrambledMessage = new ListOfStrings( "Strings", reader.nextLine( ) );
            System.out.println(scrambledMessage );

            String message = "";
            String input;
            while (reader.hasNext( ))
            {
                input = reader.nextLine( );
                if ( input.length() > 0)
                {
                    message = this.decrypt( input );
                    System.out.println( message );
                }
            } 

            reader.close( );
        }
    }

    /*******************************************************************
     * 
     * 
     * @param filename
     * @return
     * 
     *******************************************************************/
    private Scanner openInput(String filename)
    {
        Scanner scanner = null;
        try
        {
            scanner = new Scanner( new File( filename ) );
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace( );
        }
        return scanner;
    }

    /*******************************************************************
     * Remark. A commandLine is a command to undo!
     *******************************************************************/
    public String decrypt( String commandLine )
    {
        Scanner text = new Scanner( commandLine );

        try {
            switch ( text.next( ) )          // selects the command
            {
                case "a":
                this.add( text.next( ).substring( 0, 1 ), text.nextInt( ) );
                break;

                case "r":
                this.remove( text.nextInt( ) );
                break;

                case "R":
                this.reverse( );
                break;

                case "s":
                this.swap( text.nextInt( ), text.nextInt( ) );
                break;

                default:
                System.out.println( "Not a command!" );
            }
        }
        catch (Exception e)
        {
            System.out.println( text.next( ).charAt( 0 ) );
            System.out.println( "Error, invalid command." );
        }

        text.close( );

        return scrambledMessage.toString( );
    }

    /*******************************************************************
     *  The add method restores the scrambled message to the previous
     *  state by adding the "character" ch into the scrambled message 
     *  at the index position.     
     * ******************************************************************/
    private void add( String ch, int index )
    {
        scrambledMessage.add(index, ch);
    }

    /**********************************************************************
     *  The remove method restores the scrambled message to the previous
     *  state by removing the entry from the scrambled message at the
     *  index position.     
     * ********************************************************************/
    private void remove( int index )
    {
        scrambledMessage.remove(index);
    }

    /*******************************************************************
     *  The reverse method restores the scrambled message to the previous
     *  state by reversing the list.     
     * *******************************************************************/
    private void reverse( )
    {
        scrambledMessage.reverse();
    }

    /*******************************************************************
     *  The swap method restores the scrambled message to the previous
     *  state by swapping the item at j with the item at k in the list.     
     * *******************************************************************/
    private void swap( int j, int k )
    {
        scrambledMessage.swap(j, k);
    }

    /*******************************************************************
     * 
     *******************************************************************/
    public static void main(String[] args) {
        // Takes input from the command line.
        //         String userMessage = "";
        // 
        //         for (int i = 0; i < args.length; i++)
        //         {
        //             userMessage += args[i] + " ";
        //         }
        // 
        //         if (userMessage.length() == 0)
        //         {
        //             userMessage = "Testing123";
        //         }
        
        /** takes input from a file */
        Decryption decryption = new Decryption( "message.txt");
    }
}
