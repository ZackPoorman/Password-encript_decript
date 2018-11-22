import javax.swing.JOptionPane;

/*******************************************************************
 * 
 *******************************************************************/
public class MainDriver {

    public static void main( String[ ] args )
    {
        String prompt = "Enter E to Encrypt, D to Decrypt, or Q to Quit.";

        String[ ] suggestion = {"Encrypt", "Decrypt", "Quit" };
        int num = 0;

        String str = JOptionPane.showInputDialog( prompt, suggestion[ num ] );
        if ( str == null) {

            System.exit( 1 );
        }
        str = str.toUpperCase( );

        num = (num + 1) % 3;

        while (str.charAt( 0 ) != 'Q')
        {
            if (str.toUpperCase( ).charAt( 0 ) == 'E')
            {
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
            else
            {
                Decryption decryption = new Decryption( "message.txt" );
            }

            str = JOptionPane.showInputDialog( prompt, suggestion[ num ] );
            if ( str == null) {

                System.exit( 1 );
            }
            str = str.toUpperCase( );
            num = (num + 1) % 3;
        }

        System.out.println( "\n <<< End of Execution >>>" );
    }
}