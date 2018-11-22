import java.io.Serializable;
import java.util.NoSuchElementException;

/******************************************************************************
 * A List encapsulates a list of Objects, such as String, etc.
 * 
 * A List is created to have both a top dummy Node and a bottom dummy Node.
 * Cursor is initialized to match the top field. 
 * 
 * The end field is always a reference to the bottom dummy Node.
 * 
 * The convention that is implemented by this List structure goes as follows. 
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

public class List implements Serializable
{
    private static final long serialVersionUID = 1L;

    protected Node top;     // A top dummy Node to begin the list
    protected Node end;     // An end dummy Node to end the list

    protected Node cursor;  // The current Node in the list. 
 
    /*****************************************************************
     * Invokes the other constructor.
     *****************************************************************/
    public List( )
    {
        this( "" );
    }

    /*****************************************************************
     * Constructs the list with two dummy Nodes, 
     * one Node to always lead the list, and a
     * second Node to always trail the list.
     * 
     * The lead node contains the title;
     *****************************************************************/
    public List( String title )
    {
        this.top = new Node( title, null, null );    // dummy top Node
        this.end = new Node( null, this.top, null ); // dummy bottom Node
        this.top.next = this.end;

        this.cursor = this.top;

        this.setTitle( title );
    }

    public void setTitle( String title )
    {
        this.top.data = title;
    }

    public String getTitle(  )
    {
        return this.top.data.toString( );
    }

    /** ---------- */
    /** empty list */
    /** ---------- */
    
    /*****************************************************************
     * Returns whether the list is empty.
     *****************************************************************/
    public boolean isEmpty( ) 
    {
        return this.end.prev == this.top;  //this.top.next.next == null;
    }

    /** ------------------------- */
    /** cursor at top of the list */
    /** ------------------------- */
    
    /*****************************************************************
     * Returns whether the cursor points to
     * the 0th location in the list
     *****************************************************************/
    public boolean cursorIsFirst( )
    {
        return this.cursor == this.top;
    }

    /*****************************************************************
     * Moves the cursor to the top, so that the first item in the
     * list is dereferenced as cursor.next.data
     *****************************************************************/
    public void cursorToFirst( )
    {
        this.cursor = this.top;
    }

    /*****************************************************************
     * Returns the first element in the list, i.e. this.get( 0 )
     *****************************************************************/
    public Object getFirst( )
    {
        if (size() == 0)
        {
            return null;
        }
        return this.top.next.data; 
    }

    /** ------------------------- */
    /** cursor at end of the list */
    /** ------------------------- */
    
    /*****************************************************************
     * Returns whether the cursor points to
     * the end of the list, i.e. the location
     * for appending an item to the list.
     *****************************************************************/
    protected boolean cursorIsAtTheEnd( )
    {
        return  this.cursor == this.end.prev; //this.cursor.next == this.end;  //this.cursor.next.next == null; 
    }

    /*****************************************************************
     * Moves the cursor to the end of the list, which is at 
     * 
     *                    this.end.prev.
     *                    
     * No element is ever at the end of the list. Whenever an element
     * is added to the end of the list, it becomes the last element.
     *****************************************************************/
    protected void cursorToEnd( )
    {
        this.cursor = this.end.prev;
    }

    /** -------------------------------- */
    /** cursor at last place in the list */
    /** -------------------------------- */
    
    /*****************************************************************
     * Returns whether the cursor points to
     * the last occupied location in the list
     *****************************************************************/
    protected boolean cursorIsLast( )
    {
        return this.isEmpty( ) || this.cursor.next.next.next == null;
    }

    /*****************************************************************
     * Moves the cursor to the Node immediately preceding the
     * trailing Node of the list.
     * 
     * No element is ever at the end of the list. Whenever an element
     * is added to the end of the list, it becomes the last element.
     *****************************************************************/
    protected void cursorToLast( ) 
    {
        this.cursorToEnd( );
        if (! this.cursorIsFirst( ))  //cursor.hasPrevious( )
        {
            this.cursorToPrevious( );
        }
    }

    /*****************************************************************
     * Returns the last element in the list.
     *****************************************************************/
    public Object getLast( )
    {
        Node anchor = this.cursor;      // saves cursor

        if (this.isEmpty( ))
        {
            return null;
        }

        this.cursorToLast( );
        Object obj = this.get( );

        this.cursor = anchor;           // restores cursor
        return obj;
    }

    /** -------------------------- */
    /** cursor forward in the list */
    /** -------------------------- */
    
     
    /*****************************************************************
     * Returns whether the list stores a next item.
     *****************************************************************/
    public boolean cursorHasNext( ) 
    {
        return this.cursor.next.next != null;
    }

    /*****************************************************************
     * Advances the cursor to the next place in the list.
     *****************************************************************/
    protected void cursorToNext( )
    {
        //        if (! this.cursorIsAtTheEnd( ))      //this.cursor.next.next != null) 
        if (cursorHasNext( ))
        {       
            this.cursor = this.cursor.next;
        }
    }

    /*****************************************************************
     * Returns the current object, and advances the cursor.
     *****************************************************************/
    public Object getNext( ) 
    {
        Object item = null;
        try
        {
            if (!cursorHasNext())
            {
                throw new Exception( "No element at the end of the list." );
            }
            cursor = cursor.next;
            item = cursor.data;
        }
        catch (Exception e )
        {

        }
        return item;
    }

    /** -------------------------- */
    /** cursor go back in the list */
    /** -------------------------- */
    
    protected boolean cursorHasPrevious()
    {
        return this.cursor.prev != null;
    }

    /*****************************************************************
     * Backs up the cursor to the previous place in the list.
     *****************************************************************/
    protected void cursorToPrevious( )
    {
        if (cursorHasPrevious( ))  //! this.cursorIsFirst( )) or this.cursor.prev != null) 
        {          
            this.cursor = this.cursor.prev;
        }
    }

    /** ------------------------- */
    /** add an object to the list */
    /** ------------------------- */
    
    /*****************************************************************
     * Inserts a Node for the element in the specified index place.
     *****************************************************************/
    public void add( int index, Object obj )
    {     
        if (0 <= index && index <= this.size( ))
        {        
            this.cursorToIndex( index );
            this.add( obj );
        }
    }

    /*****************************************************************
     * inserts a Node for the element immediately after the Node
     * referenced by cursor.  The value of cursor does not change!
     *****************************************************************/
    public void add( Object obj ) 
    { 
        this.cursor.next = new Node( obj, this.cursor, this.cursor.next );
        this.cursor.next.next.prev = this.cursor.next;
    }

    /******************************************************************
     * 
     ******************************************************************/
    public void addAll(int index, List c)
    {
        if (0 <= index && index <= this.size( ))
        {
            c.reverse( );

            c.cursorToFirst( );
            while (c.cursorHasNext( ) )
            {
                this.add( index, c.getNext( ) );
            }

            c.reverse( );
        }
    }

    /*****************************************************************
     * Inserts a Node for the element immediately after the top
     * dummy node to top the list.
     *****************************************************************/
    public void prefix( Object obj )
    { 
        this.top.next = new Node( obj, this.top, this.top.next );
        this.top.next.next.prev = this.top.next;
    }

    /*****************************************************************
     * No element is ever at the end of the list. Whenever an element
     * is added to the end of the list, it becomes the last element.
     *
     *
     * This append method inserts a Node for the element at the end of
     * the list, which, physically, will immediately come before the
     * end (dummy) node.
     *****************************************************************/
    public void append( Object obj )
    {       
        Node anchor = this.cursor;      // saves cursor

        this.cursorToEnd( );
        this.add( obj );

        this.cursor = anchor;           // restores cursor
    }

    /** ----------------------------- */
    /** replace an object in the list */
    /** ----------------------------- */
    
    
    /*****************************************************************
     * This set method replaces the object, obj, to the current.next
     * node data field.
     *****************************************************************/
    public void set( Object obj )
    {
        if (! this.cursorIsAtTheEnd())  // or cursor.hasNext( )
        {
            this.cursor.next.data = obj;
        }
    }

    /*******************************************************************
     * If index is a legitimate list position, this set method 
     * moves the cursor to the specified index position, before
     * invoking the set Object method to complete the object
     * replacement.
     *****************************************************************/
    public void set( int index, Object obj )
    {
        Node anchor = this.cursor;      // saves cursor

        if (0 <= index && index <= this.size( ))
        {
            this.cursorToIndex( index );     
            this.set( obj );
        }

        this.cursor = anchor;           // restores cursor
    }

    /** --------------- */
    /** search the list */
    /** --------------- */

    
    /*****************************************************************
     * Returns the index of temp in the list for a first occurrence;
     * or -1 if temp is not contained in the list.
     * 
     *****************************************************************/
    public int indexOf( Object obj )
    {
        Node anchor = this.cursor;      // saves the cursor

        this.cursorToFirst( );
        int index = 0;
        while (! this.cursorIsAtTheEnd( ))
        {
            if (this.cursor.next.data.equals( obj ))
            {
                this.cursor = anchor;   // restores the cursor
                return index;
            }
            this.cursorToNext( );
            index++;
        }

        this.cursor = anchor;           // restores the cursor
        return -1;
    }

    /*****************************************************************
     * Returns the index of obj for its last occurrence in the list ;
     * or -1 if obj is not contained in the list.
     * 
     * contains ?
     *****************************************************************/
    public int lastIndexOf(Object obj)
    {
        Node anchor = this.cursor;           // saves the cursor

        int index = this.size()-1;

        this.cursorToLast( );
        while (! this.cursorIsFirst() )
        {
            if (this.cursor.next.data.equals( obj ))
            {
                this.cursor = anchor;   // restores the cursor
                return index;
            }

            this.cursorToPrevious( );
            index++;
        }

        this.cursor = anchor;           // restores the cursor
        return -1;
    }

    /*****************************************************************
     * The get method returns the item, cursor.next.getData()
     *****************************************************************/
    public Object get( ) 
    {
        Object obj = null;
        if (!this.isEmpty( ) && !this.cursorIsAtTheEnd( ))
        {
            obj = this.cursor.next.data;
        }
        else
        {
            System.out.println( "Exception: no data to get at the end of the list." );
        }
        return obj;
    }

    /*****************************************************************
     * get moves the cursor to the specified index position, 
     * and then invokes get() to return the item.
     *****************************************************************/
    public Object get( int index )
    {
        Node anchor = this.cursor;      // saves cursor

        this.cursorToIndex( index );  
        Object obj = this.get( );

        this.cursor = anchor;           // restores cursor
        return obj;
    }

    /*****************************************************************
     * Returns the index for the cursor position in the list.
     *****************************************************************/
    public int getIndex( )
    {
        Node anchor = this.cursor;      // saves the cursor

        int index = 0;
        this.cursor = this.top;

        while (this.cursor != anchor)
        {
            this.cursorToNext( );
            index++;
        }

        this.cursor = anchor;           // restores the cursor
        return index;
    }

    /*****************************************************************
     * Changes the cursor to reference the Node specified by the index parameter.
     *****************************************************************/
    protected void cursorToIndex( int index )
    {
        if (0 <= index && index <= this.size( ))
        {
            this.cursorToFirst( );
            while ( index > 0 && !this.cursorIsAtTheEnd( ))
            {
                this.cursorToNext( );
                index--;
            }
        }
    }

    /*******************************************************************
     * Returns a sub-list of elements from start to stop in this list.
     * Elements in the returned sub-list are shared by this list.
     ******************************************************************/
    public List subList(int start, int stop)
    {
        List newList = new List( "sublist" );

        if (start < 0 || start > stop || stop > this.size( ))
        {
            throw new IllegalArgumentException( );
        }   

        this.cursorToIndex( start );

        while (! this.cursorIsAtTheEnd() && start <= stop)
        {
            newList.append( this.get( ) );

            this.cursorToNext();
            start++;
        }

        return newList;
    }

    
    /** --------------- */
    /** modify the list */
    /** --------------- */


    /*****************************************************************
     * Swaps two elements in the list, the element at index j
     * with the element at index k.
     *****************************************************************/
    public void swap( int j, int k )
    {
        Node anchor = this.cursor;      // saves the cursor

        Object temp = this.get( j );    
        this.set( j, this.get( k ) );
        this.set( k, temp );

        this.cursor = anchor;           // restores the cursor
    }

    /*****************************************************************
     * Reverses the order of the list.
     *****************************************************************/
    public void reverse( )
    {
        Node anchor = this.cursor;      // saves the cursor

        this.cursorToIndex( 1 );
        while ( ! this.cursorIsAtTheEnd( ) )
        {
            try
            {
                this.prefix( this.remove( ) );
            }
            catch (Exception e)
            {
            }
        }

        this.cursor = anchor;           // restores the cursor
    }

    /** ------------------------------ */
    /** remove an object from the list */
    /** ------------------------------ */    
    
    /*****************************************************************
     * Returns the value, curror.next.data, and re-links the list to exclude
     * the node that contained the data.  The value of cursor does not change!
     * 
     * @return
     * @throws Exception
     *****************************************************************/
    public Object remove( )
    {
        Object obj = null;

        if (cursorHasNext())
        {
            obj = this.cursor.next.data;
            
            this.cursor.next      = this.cursor.next.next;
            this.cursor.next.prev = this.cursor;
        }

        return obj;
    }

    /*****************************************************************
     * First moves the cursor to the index specified location in the list,
     * and then calls this.remove to remove and return the value.
     * @return
     * @throws Exception
     *****************************************************************/
    public Object remove( int index )
    {
        Node anchor = this.cursor;     // saves the cursor

        Object obj = "";
        try
        {
            this.cursorToIndex( index );
            obj = this.remove( );
        }
        catch (Exception e)
        {
            System.out.println( "Exception: attempt to remove an item beyond on the end of the list" );
        }

        this.cursor = anchor;           // restores the cursor
        return obj;
    }

    /*****************************************************************
     * Empties the list.
     *****************************************************************/
    public void clearAll( )
    {
        this.cursorToFirst( );

        try
        {
            while (! this.isEmpty( ))
            {
                this.remove( );
            }
        }
        catch (Exception e)
        {
            System.out.println( "Exception. Attempt to remove from an empty list." );
        }
    }

    /** --------------------- */
    /** iterate over the list */
    /** --------------------- */

    
    
    /*****************************************************************
     * Returns a count of how many elements are contained within the list.
     *****************************************************************/
    public int size( )
    {
        Node anchor = this.cursor;     // saves the cursor

        int count = 0;
        for (this.cursorToFirst( ); ! this.cursorIsAtTheEnd( ); this.cursorToNext() )
        {
            count++;
        }

        this.cursor = anchor;           // restores the cursor
        return count;
    }

    /*****************************************************************
     * Returns a string for all of the elements in the list from 
     * top to bottom.
     *****************************************************************/
    public String toString( )
    {
        Node anchor = this.cursor;     // saves the cursor

        String returnString = ""; // this.top.data.toString( );
        for (this.cursorToFirst( ); ! this.cursorIsAtTheEnd(); this.cursorToNext( )) 
        {
            returnString += this.get( ).toString( );
        }

        this.cursor = anchor;           // restores the cursor
        return returnString;
    }

    /*****************************************************************
     * Returns a string for the elements in the list 
     * indexed between start and stop, inclusively.
     *****************************************************************/
    public String display( int start, int stop )
    {
        Node anchor = this.cursor;     // saves the cursor  

        String returnString;

        if (start < 0 || start > stop || stop > this.size( ))
        {
            throw new IllegalArgumentException( );
        }   

        this.cursorToIndex( start );
        returnString = "";
        while (! this.cursorIsAtTheEnd() && start <= stop)
        {
            returnString += this.get( );

            this.cursorToNext();
            start++;
        }

        this.cursor = anchor;           // restores the cursor
        return returnString;
    }

    /*****************************************************************
     *****************************************************************/
    //     public void print( )
    //     {
    //         Node anchor = this.cursor;     // saves the cursor
    // 
    //         for ( int n = 0; n < this.size(); n++ )
    //         {
    //             System.out.print( " " + n );
    //         }
    //         System.out.println( );
    // 
    //         String str;
    //         for ( int n = 0; n < this.size(); n++ )
    //         {
    //             str = this.get(n).toString( );
    //             if ( n < 10)
    //             {
    //                 System.out.print( " " + this.get(n).toString( ) );
    //             }
    //             else
    //             {
    //                 System.out.print( "  " + this.get(n).toString( ) );
    //             }
    //         }
    //         System.out.println( );
    // 
    //         this.cursor = anchor;           // restores the cursor
    //     }

    /*****************************************************************
     * 
     * 
     *****************************************************************/
    public static void main( String[ ] args )
    {
        List list = new List( "List:  " );

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
    }
}