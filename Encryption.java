
import java.io.*;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;

/*******************************************************************
 * Encryption encapsulates the following structures:
 * 1) message -- split up as a list of substrings, and repeatedly 
 *               revised by the user with encryption steps from the
 *               following commands to make the message secure: 
 *       "a <string> <n> -- insert string at position n ",
 *       "d <c>          -- delete all instances of character 'c' ",
 *       "H or h         -- show this help page",
 *       "o <n>          -- obfuscate inserts n random characters at random positions",
 *       "Q or q         -- quit, print the final encrypted message, save message",
 *       "c <j> <k>      -- copy to clipboard cb, the substring from j through k ",
 *       "x <j> <k>      -- cut  to clipboard cb, the substring from j through k ",
 *       "p <cb> <n>     -- paste from clipboard cb to string location n     ",
 *       "R              -- reverse the string",
 *       "r <j> <k>      -- remove the substring in the subrange from j through k ",
 *       "s <j> <k>      -- swap the character at j with the character at k ",
 *       "u              -- undo previous encryption",
 *       "U              -- undo encryption history"
 *       "v              -- undo previous encryption substep",
 * 2) undoCommands       -- a stack of undo command groups such as
 *                               "r 5, r 5, r 5"
 *                          to undo a command like "a ABC 5"
 * 3) clipBoards         -- a list of saved substrings, available for cutting and pasting.
 * 4) decryption         -- reference to a decryption instance with methodology
 *                          to undo the encryption
 * 
 * 
 *******************************************************************/
public class Encryption {

    public String fileName;
    private ListOfStrings message;/** shared by the Decryption instance */

    private List clipBoards;
    private Stack undoCommands;   

    private Decryption decryption;

    private static Scanner keyboard = new Scanner(System.in);
    private static Random random = new Random( );
    public static String[ ] helpPage = { 
            "Commands:",
            "a <string> <n> \t insert string at position n ",
            "d <c>          \t delete all instances of character 'c' ",
            "H or h         \t show this help page",
            "o <n>          \t obfuscate inserts n random characters at random positions",
            "Q or q         \t quit, print the final encrypted message, save message",
            "c <j> <k>      \t copy to clipboard cb, the substring from j through k ",
            "x <j> <k>      \t cut  to clipboard cb, the substring from j through k ",
            "p <cb> <n>     \t paste from clipboard cb to string location n     ",
            "R              \t reverse the string",
            "r <j> <k>      \t remove the substring in the subrange from j through k ",
            "s <j> <k>      \t swap the character at j with the character at k ",
            "u              \t undo previous encryption",
            "U              \t undo encryption history",
            "v              \t undo previous encryption substep"
        };

    /*******************************************************************
     * Constructor initializes fields:
     * 1) to encrypt a message that is split up as a ListOfStrings,
     * 2) to set up a shared reference to this message in the Decryption
     *    class,
     * 3) to create a list for clipboards,
     * 4) to create a stack for undo commands,
     * 5) to choose a file name for the encrypted message.
     *******************************************************************/
    public Encryption(String userMessage ) {
        message = new ListOfStrings( "Strings", userMessage );
        decryption = new Decryption( message );

        clipBoards = new List( " Table: " );

        undoCommands = new Stack( " undo commands " );

        fileName = "EncryptedMessage.txt";

        encrypt( );
    }

    /*******************************************************************
     * The getClipBoard returns the n th clip board in the list.
     *******************************************************************/
    private List getClipBoard( int n )
    {
        if ( n < clipBoards.size( ) )
        {
            return (List) clipBoards.get( n );
        }
        else
        {
            return null;
        }
    }

    /*******************************************************************
     * Add 
     * 1) further encrypts message by inserting a String into the
     *    index place.
     * 2) adds an undo group of commands to the undoCommands for
     *    decryption.
     *******************************************************************/
    private void add(String str, int index) throws Exception
    {
        Stack undoGroup = new Stack("new undo commands ");
        String s;

        if (index > message.size())
        {
            throw new Exception("Add: index out of bounds");
        }

        int length = str.length();
        for (int n = 0; n < length; n++)
        {
            s = str.substring(n, n+1);

            message.add(index + n, s);

            undoGroup.push(new Command("r " + (index + n)));
        }

        undoCommands.push(undoGroup);
    }

    /*******************************************************************
     * Add adds a clipboard String into the index place in the message.
     *******************************************************************/
    private void add( List clipBD, int index ) throws Exception {

        this.add( clipBD.toString( ), index );
    }

    /*******************************************************************
     * Cut invokes copy and remove
     *    1) to copy a sublist to a clipboard, and
     *    2) to remove it from the message.
     *******************************************************************/
    private void cut(int start, int stop) throws Exception {
        copy(start, stop);
        remove(start, stop);
    }

    /*******************************************************************
     * Copy copies a sublist of the message to a clipboard.
     *******************************************************************/
    private void copy(int start, int stop) throws Exception
    {

        if (start < 0 && stop >= message.size())
        {
            throw new Exception("Copy: index out of bounds");
        }

        ListOfStrings clipBD = new ListOfStrings("" + clipBoards.size());
        for (int n = stop; n >= start; n--)
        {

            clipBD.add(message.get(n));

        }

        System.out.println("Clip board number " + clipBD.getTitle() + ": => \'" + clipBD.toString() + "\'");
        clipBoards.append(clipBD);
    }

    /*******************************************************************
     * PrintIndices prints indices up through parameter count.
     *******************************************************************/
    private String indicesToString( int count )
    {
        String str = "  ";
        for (int n = 0; n < count; n++) 
        {
            if (n < 10)
            {
                str += n + "  ";
            }
            else 
            {
                str += n + " ";
            }
        }

        return str;
    }

    /*******************************************************************
     * DisplayMessage writes the message on the console.
     *******************************************************************/
    private String messageToString(  )
    {
        String str = this.indicesToString( message.size() ) + "\n";

        String s = message.toString();
        for (char c : s.toCharArray( )) 
        {
            str += "  " + c ;
        }
        return str;
    }

    /*******************************************************************
     * Delete 
     *  1) deletes all instances of the parameter ch from the message
     *  2) adds an undo group of commands to the undoCommands for
     *     decryption.
     * *****************************************************************/
    private void delete(String ch)
    {
        Stack undoGroup = new Stack("new undo commands ");
        int index;

        while ((index = message.indexOf(ch)) != -1)
        {
            message.remove(index);   // executes the command

            undoGroup.push(new Command("a"+ message.get(index + index)));

        }
        undoCommands.push(undoGroup);
    }

    /**************************************************************************************
     *  insert a randomly chosen character anywhere into the message, n times
     *  create an undo command
     * ************************************************************************************/
    private void obfuscate( int times ) throws Exception
    {
        String ch;
        int index;
        Stack undoGroup = new Stack("new undo commands");

        for (int n = 0; n < times; n++)
        {
            if (n % 2 == 0) {
                ch    = "" + (char)(' ' + (random.nextInt( 30 ) + 1));   // a printable character chosen at random
                index = random.nextInt( message.size( )-1 );
            }
            else {
                ch    = "" + (char)(' ' + (random.nextInt( 61 ) + 33));   // a printable character chosen at random
                index = random.nextInt( message.size( )-1 );
            }

            // 5) one line of code to add the character to the encrypted message
            message.add(index, ch);
            undoGroup.push("r " + index + " " + index);
        }
        undoCommands.push(undoGroup);
    }

    /*******************************************************************
     *  Show help
     * *****************************************************************/
    private void printHelpPage( )
    {
        for (String s: helpPage)
        {
            System.out.println( "\t" + s );
        }
    }

    /*******************************************************************
     * Paste copies the clipboard to the message at the index location
     *******************************************************************/
    private void paste(int clipNum, int index) throws Exception
    {
        // 6A) fix the condition to include all out of range exceptions

        if ( index <= message.size() && clipBoards.size() <= clipNum)
        {
            throw new Exception("Paste: invalid clipboard number");
        }

        // 6B) two lines of code to paste the clipboard to the encrypted message
        add(getClipBoard(clipNum), index);
        undoCommands.push(new Command("r " + (index + " " + getClipBoard(clipNum).size())));
    }

    /********************************************************************
     *  Quit
     * ******************************************************************/
    private void quit()
    {
        System.out.println ( "The encrypted message: " + message );
    }

    /*******************************************************************
     * Remove 
     *  1) removes the substring from the message. 
     *  2) adds an undo group of commands to the undoCommands for
     *     decryption.
     *******************************************************************/
    private void remove(int start, int stop) throws Exception
    {
        Stack undoGroup = new Stack("new undo commands ");
        String str;

        // 7A) fix the condition to include all out of range exceptions

        if (start < 0 && stop >= message.size())
        {
            throw new Exception("Remove: index out of bounds");
        }

        // 7B) Use a for loop 
        //      1) to remove the characters in the
        //         encrypted message in the range from start to stop
        //      2) to create undo commands and push each one on the 
        //         undoGroup

        for(int i = stop; i >= start; i--){
            undoGroup.push(new Command("a " + message.get(i) + " " + i));
            message.remove(i);

        }

        undoCommands.push(undoGroup);
    }

    /*******************************************************************
     * Reverse 
     *   1) reverses the message and create an undo command
     *   2) adds an undo group of commands to the undoCommands for
     *      decryption.
     * *****************************************************************/
    private void reverse()
    {
        Stack undoGroup = new Stack("new undo commands ");

        // 8) two lines of code
        //     1) to reverse the message 
        //     2) create and push an undo command
        //        on the undoGroup stack
        message.reverse();
        undoGroup.push(new Command("R "));
        undoCommands.push(undoGroup);
    }

    /*******************************************************************
     *  Save the file
     * *****************************************************************/
    private void save( ) throws Exception
    {
        FileChooser fileChooser;

        String fileName = "message";

        fileChooser = new FileChooser( );
        fileName    = fileChooser.chooseFile( "save", fileName );  // file to write
        if ( fileName != null && fileName.length() > 0) {
            save( fileName );
        }
    }

    /*******************************************************************
     * Save writes the message and the undo commands to file.
     *******************************************************************/
    public void save(String filename) {

        if ( filename != null && filename.length() > 0) {

            PrintWriter out = null;
            try
            {
                out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            }
            catch (IOException e)
            {
                e.printStackTrace( );
            }

            out.println( message );
            out.println( undoCommands );
            out.close();
        }
    }

    /*******************************************************************
     * Swap 
     *  1) swaps two characters in the message
     *  2) adds an undo group of commands to the undoCommands for
     *     decryption.
     * *****************************************************************/
    private void swap(int j, int k) throws Exception
    {
        Stack undoGroup = new Stack("new undo commands ");

        // 9A) fix the condition to include all out of range exceptions
        if (j > message.size() && k > message.size())
        {
            throw new Exception("Swap: index out of bounds");
        }

        // 9B) two lines of code
        //     1) to swap two characters in the message 
        //     2) create and push an undo command
        //        on the undoGroup stack
        message.swap(j, k);
        undoGroup.push(new Command("s " + j + " " + k));
        undoCommands.push(undoGroup);
    }

    /*******************************************************************
     * UndoPreviousEncryptionSubstep pops the previousCommandGroup
     * from the undoCommands stack.
     * Since the previousCommandGroup is itself a stack of commands,
     * the previousCommandGroup, minus the one command just popped
     * and undone, is pushed back unto undoCommands stack, still to
     * be undone.
     * *****************************************************************/
    private void undoPreviousEncryptionSubstep()
    {
        Stack previousCommandGroup;

        if (!undoCommands.isEmpty())
        {
            previousCommandGroup = (Stack)undoCommands.pop();

            String command = previousCommandGroup.pop().toString();

            undoEncryption(command);

            if (!previousCommandGroup.isEmpty())
            {
                undoCommands.push(previousCommandGroup);
            }
        }
    }

    /*******************************************************************
     * UndoPreviousEncryption undoes all commands in the previous
     * command group
     * *****************************************************************/
    private void undoPreviousEncryption()
    {
        Stack previousCommandGroup;

        if (!undoCommands.isEmpty())
        {
            previousCommandGroup = (Stack)undoCommands.pop();

            while (!previousCommandGroup.isEmpty())
            {
                String command = previousCommandGroup.pop().toString();

                // 10 invoke undoEncryption in one line of code
                undoEncryption(command);
            }
        }
    }

    /*******************************************************************
     * UndoEncryptionHistory repeatedly invokes UndoPreviousEncryption to
     * empty the stack of undoCommands.
     * *****************************************************************/
    private void undoEncryptionHistory()
    {
        while (!undoCommands.isEmpty())
        {
            // 11 invoke undoPreviousEncryption in one line of code
            undoPreviousEncryption();
        }
    }

    /*******************************************************************
     * undoEncryption invokes Decrypt to undo a single Encryption step. 
     * *****************************************************************/
    private void undoEncryption(String commandLine)
    {
        decryption.decrypt(commandLine);
    }

    public String encrypt( String command )
    {

        try
        {
            switch (command) {

                case "a":
                add(keyboard.next(), keyboard.nextInt());
                break;                  

                case "c":
                copy( keyboard.nextInt(), keyboard.nextInt() );
                break;

                case "x":
                cut( keyboard.nextInt(), keyboard.nextInt() );
                break;

                case "d":
                delete(keyboard.next());
                break;

                case "h":
                printHelpPage();
                break;

                case "o":
                this.obfuscate( keyboard.nextInt() );
                break;          

                case "p":
                paste(keyboard.nextInt(), keyboard.nextInt());
                break;

                case "q":  
                case "Q":  
                this.save( );
                this.quit( );
                break;

                case "R":
                reverse( );
                break;                  

                case "r":
                remove( keyboard.nextInt(), keyboard.nextInt() );
                break;                  

                case "s":
                swap( keyboard.nextInt(), keyboard.nextInt() );
                break;       

                case "U":
                undoEncryptionHistory();
                break;

                case "u":
                undoPreviousEncryption();
                break;

                case "v":
                undoPreviousEncryptionSubstep();
                break;
            }
            keyboard.nextLine();   /** flushes the buffer */
        }
        catch (Exception e ) {
            System.out.println ( e.getMessage() );
            System.out.println ("encrypt:  Error on input.");
            keyboard = new Scanner(System.in);  /** completely flushes the buffer  */            
        }        
        return message.toString();
    }

    /*******************************************************************
     * Encrypt invokes encryption steps determined by keyboard commands.
     *******************************************************************/
    private String encrypt( ) {  

        String command = "";
        do {
            System.out.println("Message:");
            System.out.println( messageToString() );  //( message.toString() );
            System.out.print("Command: ");
            try
            {
                command = keyboard.next("[acdhopqQRrSsUuvx]");
                encrypt( command );
            }
            catch (Exception e)
            {
                keyboard = new Scanner(System.in);  /** completely flushes the buffer  */            
            }

            System.out.println("For demostration purposes only: undo commands:\n" + undoCommands);

        } while (!command.equalsIgnoreCase( "Q" ) );
        keyboard.close( );
        return message.toString( );
    }

    /*******************************************************************
     * 
     *******************************************************************/
    public static void main( String[ ] args) {
        String userMessage = "";

        for (int i = 0; i < args.length; i++)
        {
            userMessage += args[i] + " ";
        }

        if (userMessage.length() == 0)
        {
            userMessage = "Testing123";
        }
        Encryption encryption = new Encryption( userMessage );
    }
}
