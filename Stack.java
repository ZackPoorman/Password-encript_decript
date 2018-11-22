
/** -----------------------------------------------------------------------
 * 
 * Description:  An implementation of a stack as a linked structure.
 * 
 * A stack is a structure with three methods:
 * 
 *                           push, pop, and peek
 *                           
 * As a linked implementation,
 * 
 *                             this.top.data 
 *                             
 * denotes the top item on the stack.
 * 
 * <<< Examples >>>
 * 
 * --------------
 * An empty stack
 * -------------- 
 * this.title == title
 *                                             Stack s = new Stack( );
 * this.top == null                 
 *               
 *                 
 * ----------------------------
 * A stack that contains 1 item
 * ----------------------------
 * this.title == title
 *                                                  s.push( true );
 * this.top.data                        true
 * this.top.next                        null
 *             
 *                 
 * -----------------------------
 * A stack that contains 5 items 
 * ----------------------------- 
 * this.title == title
 *                                                  s.push( true );
 *                                                  s.push( 1 );
 *                                                  s.push( 3.14 );
 *                                                  s.push( "one" );
 *                                                  s.push( "two" );
 * this.top.data                        "two"
 * this.top.next                        link forward
 *                 
 * this.top.next.data                   "one"
 * this.top.next.next                   null
 * 
 * this.top.next.next.data              3.14
 * this.top.next.next.next              link forward
 *                 
 * this.top.next.next.next.data         1
 * this.top.next.next.next.next         link forward
 *                 
 * this.top.next.next.next.next.data    true
 * this.top.next.next.next.next.next    null
 * 
 * ----------------------------------------------   ----------------------------
 */

public class Stack
{
    protected Object title;
    protected Node top;

    protected int count;

    public Stack( )
    {
        this( "" ); 
    }

    /*****************************************************************
     * Constructs the stack;
     *****************************************************************/
    public Stack( String title )
    {
        this.setTitle( title );

        this.top = null;        
        this.count = 0;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    /** ---------------------------------------------------------------------
     * The push method creates a new node, the reference to which is 
     * assigned to this.top.
     * ----------------------------------------------------------------------
     * 
     * @param obj
     */
    public void push( Object obj )
    {
        this.top = new Node( obj, this.top );
        
        this.count++;
    }

    /** ---------------------------------------------------------------------
     * Given that this.top is not null, the "pop" method returns the 
     *
     *                            this.top.data
     *                            
     *  value. Pop 
     *  1) first saves the value to return in a local variable,
     *  2) changes the this.top reference to link around the no longer
     *     top node,
     *  3) returns the value
     *  --------------------------------------------------------------------
     * 
     * @return
     */
    public Object pop( )
    {
        Object item = null;

        try
        {
            if ( this.top == null)
            {
                throw new Exception( );
            }

            item = this.top.data;
            this.top = this.top.next;
            
            this.count--;
        }
        catch (Exception e)
        {
            System.out.println( "     Exception: attempt to pop an empty stack" );
        }

        return item;
    }

    /** ---------------------------------------------------------------------
     * 
     * @return
     */
    public Object peek( )
    {
        if (this.top == null)
        {
            return null;
        }
        
        return this.top.data;
    }

    /** ---------------------------------------------------------------------
     * 
     * @return
     */
    public int size( )
    {
        // return this.count;
        
        int count = 0;

        Node link = this.top;
        while (link != null)
        {
            link = link.next;
            count++;
        }

        return count;
    }

    /** ---------------------------------------------------------------------
     * 
     * @return
     */
    public boolean isEmpty( )
    {
//        return this.count == 0; 
       
        return this.size() == 0;
    }

    /** ---------------------------------------------------------------------
     * 
     * @return
     */
    public String toString( )
    {
        String str = " + \n";   //this.title.toString( );

        Node link = this.top;
        while (link != null)
        {
            str += link.toString( );
            link = link.next;
        }

        return str;
    }

    /** ---------------------------------------------------------------------
     * 
     * @param args
     */
    public static void main( String[ ] args )
    {
        Stack s = new Stack( "Title" );

        s.push( true );
        s.push( 1 );
        s.push( 3.14 );
        s.push( "one" );
        s.push( "two" );

        System.out.println( "Stack: \n" + s.toString( ) );

        System.out.println( "     size: " + s.size( ) );
        System.out.println( "     top:  " + s.peek( ) );

        for (int n = 0; n < 6; n++)
        {
            System.out.println( "s.pop(): " + s.pop( ) );
        }
    }
}