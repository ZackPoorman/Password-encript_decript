
import java.io.Serializable;

public class Node implements Serializable
{
    static final long serialVersionUID = 0;

    public Node prev;     // a reference to the previous node in the chain
    public Object data;   // the data stored in this node 
    public Node next;     // a reference to the next node in the chain

    
    /***************************************************************
     * This constructor could be used for doubly-linked structures,
     * e.g., doubly-linked lists.
     * 
     * This node constructor defers execution to the parameter match construcotr
     ***************************************************************/
    public Node( )
    {
        this( null, null, null );
    }

    /***************************************************************
     * This constructor could be used for doubly-linked structures,
     * e.g., doubly-linked lists.
     ***************************************************************/
    public Node( Object data, Node prev, Node next )
    {
        this.data = data; 
        this.prev = prev;
        this.next = next; 
    }

    /***************************************************************
     * This constructor could be used for singly-linked structures,
     * e.g., a stack structure.
     ***************************************************************/
    public Node( Object data, Node next )
    {
        this.data = data; 
        this.next = next; 

        this.prev = null;
    }

    /***************************************************************
     * This equals method compares the data referenced in this Node
     * with the Object parameter for equality.
     ***************************************************************/
    public boolean equals( Object obj )
    {
        return this.data.equals( obj );
    }
    
    public String toString( )
    {
        return this.data.toString( );
    }

    public static void main( String[ ] args )
    {
        Node node = new Node( "String type data", null, null );
        System.out.println( node.toString( ) );

        Node node2 = new Node( new Integer( 17 ), null, null );
        System.out.println( node2.toString( ) );
    }
}