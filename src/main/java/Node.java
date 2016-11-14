/**
 * Node Object which is our elemental data type for our doubly linked list
 */
public class Node {

    // The number of bytes that this record takes up
    private int blockSize = -1;

    // Position within the Memory Pool occupied by this Node
    private int position = -1;

    private Node next = null;

    private Node prev = null;

    public Node() {

    }

    public Node( int blockSize, int position ) {

        this.blockSize = blockSize;
        this.position = position;
    }

    public int getBlockSize() {

        return blockSize;
    }


    public void setBlockSize( int blockSize ) {

        this.blockSize = blockSize;
    }


    public int getPosition() {

        return position;
    }


    public void setPosition( int position ) {

        this.position = position;
    }


    public Node next() {

        return next;
    }


    public void next( Node next ) {

        this.next = next;
    }


    public Node prev() {

        return prev;
    }


    public void prev( Node prev ) {

        this.prev = prev;
    }


    public String toString() {

        return "(position: " + position + " :: blockSize: " + blockSize + ")";
    }

}
