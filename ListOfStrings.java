 
import java.io.Serializable;

/******************************************************************************
 * A ListOfStrings encapsulates a list of Strings. It could be generalized for Objects.
 * 
 * A ListOfStrings is created to have both a top dummy Node and a bottom dummy Node.
 * Cursor is initialized to match the top field. 
 * 
 * The end field is always a reference to the bottom dummy Node.
 * 
 * The convention that is implemented by this ListOfStrings structure goes as follows. 
 * For each value of the cursor field, 
 * 
 *                               cursor
 * 
 * the data are stored in the next Node,
 * 
 *                            cursor.next.data
 * 
 * -----------------------------------------
 *     An empty list
 * Below, cursor = top, i.e. the cursor index is 0
 * 
 * top.prev (cursor.next)   null
 * top.data (cursor.next)   dummy
 * top.next (cursor.next)   link forward
 *                 
 * end.prev                 link backward
 * end.data                 dummy
 * end.next                 null
 * 
 * 
 * 
 * -----------------------------------------
 *     A list of 1 element
 * Below, cursor = top, i.e. the cursor index is 0
 * 
 * top.prev (cursor.next)   null
 * top.data (cursor.next)   dummy
 * top.next (cursor.next)   link forward
 *                 
 * cursor.next.prev         links backward
 * cursor.next.data         "one"
 * cursor.next.next         link forward
 *                 
 * end.prev                 link backward
 * end.data                 dummy
 * end.next                 null
 * 
 * -------------------------------------------
 *     A list of 2 elements
 * Below, cursor = top.next, i.e. the cursor index is 1
 * 
 * top.prev                 null
 * top.data                 dummy
 * top.next                 link forward
 *                 
 * cursor.prev              link backward
 * cursor.data              "one"
 * cursor.next              link forward
 *                 
 * cursor.next.prev         link backward
 * cursor.next.data         "two"
 * cursor.next.next         link forward
 *                 
 * end.prev                 link backward
 * end.data                 dummy
 * end.next                 null
 * 
 * -------------------------------------------
 * 
 * For the initial cursor value, an item will be entered in the
 * 0th position in the list. Unless the cursor is changed, a 
 * subsequent item will be entered still in the 0th position, 
 * where each existing item in the list will be demoted by one place.
 * 
 **********************************************************************/

public class ListOfStrings extends List //implements Serializable
{

    /*****************************************************************
     * Invokes the other constructor.
     *****************************************************************/
    public ListOfStrings( )
    {
        super("" );
    }

    /*****************************************************************
     * Defers to the List constructor.
     *****************************************************************/
    public ListOfStrings( String title )
    {
        super ( title );
    }

    /*****************************************************************
     * Defers to the List constructor, and populates the list with 
     * substrings of length 1.
     *****************************************************************/
    public ListOfStrings( String title, String source )
    {
        super ( title );
        for (int n = 0; n < source.length(); n++ )
        {
            this.append( source.substring(n,n+1) );
        }       
    }

    /*****************************************************************
     * The get method returns the item, cursor.next.getData()
     *****************************************************************/
    public String get( ) 
    {
        Object obj = super.get();
        if (obj instanceof String)
        {
            return (String)obj;
        }
        else
        {
          return "";   // null
        }

    }

    /*****************************************************************
     * get moves the cursor to the specified index position, 
     * and then invokes get() to return the item.
     *****************************************************************/
    public String get( int index )
    {
        Object obj = super.get( index );
        if (obj instanceof String)
        {
            return (String)obj;
        }
        else
        {
          return "";   // null
        }

    }

    /*****************************************************************
     * Returns the value, curror.next.data, and re-links the list to exclude
     * the node that contained the data.  The value of cursor does not change!
     * 
     * @return
     * @throws Exception
     *****************************************************************/
    public String remove( ) //throws Exception
    {
        Object obj = super.remove( );

        if (obj instanceof String)
        {
            return (String)obj;
        }

        return ""; // or null;
    }

    /*****************************************************************
     * First moves the cursor to the index specified location in the list,
     * and then calls this.remove to remove and return the value.
     * 
     * @return
     * @throws Exception
     *****************************************************************/
    public String remove( int index )
    {
        Object obj = super.remove( index );

        if (obj instanceof String)
        {
            return (String)obj;
        }

        return ""; // or null;
    }



    /*****************************************************************
     * 
     * 
     *****************************************************************/
    public static void main( String[ ] args )
    {
        ListOfStrings list = new ListOfStrings( "ListOfStrings: " );

        list.add( "one" );
        list.add( "two" );
        list.add( "three" );

        System.out.println( list.toString( ) );

        list.reverse( );
        list.setTitle( "List reversed: " );
        System.out.println( list.toString( ) );

        list.reverse( );
        list.setTitle( "List reversed twice: " );
        System.out.println( list.toString( ) );
        
        System.out.println( list.indexOf( "one" ) );
        
//        list.get( 1 );
        System.out.println( list.get( 1 ).toString( ) );

    }
}