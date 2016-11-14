import java.util.StringJoiner;

public class FreeBlock {

    /**
     * Head node
     */
    private Node head;

    /**
     * Tail node
     */
    private Node tail;

    /**
     * Create a new FreeBlock object and initialize. put the node with
     * position = 0, free block size = poolSize.
     *
     * @param blockSize the initial blocksize of the mempool
     */
    public FreeBlock( int blockSize ) {

        head = new Node( blockSize, 0 );
        tail = null;

        head.next( tail );
    }


    /**
     * Use worst fit algorithm to find where to put the record.
     *
     * @param recordSize the size of the record
     *
     * @return worst fit node
     */
    public Node worstFit( int recordSize ) {

        // Worst node is the one with the most free space which will always be used to service a
        // request first.
        return !isEmpty() && recordSize > head.getBlockSize() ?  null : head;

    }

    /**
     * Find the position of record.
     *
     * @return position of record
     */
    public int findPosition( int recordSize ) {

        Node worst = worstFit( recordSize );
        int position;
        if ( worst == null ) {
            position = -1;
        } else {
            position = worst.getPosition();
        }
        return position;
    }

    /**
     * Append the node to the list.
     *
     * @param newNode that needs to be appended to the freeblock list.
     */
    public void append( Node newNode ) {

        if ( tail != null ) {
            tail.next( newNode );
            newNode.prev( tail );
            tail = newNode;

        } else {

            tail = newNode;
        }
    }

    public boolean isEmpty() {

        return head == null;
    }


    /**
     * Remove the node from list.
     *
     * @param remove the node that need to be removed
     */
    public void remove( Node remove ) {

        // If attempting to remove head or tail of the list
        if ( remove == head ) {
            if ( head != null ) {
                if ( head.next() == null ) {
                    head = null;
                } else {
                    head = head.next();
                }
            }
        } else if ( remove == tail ) {
            if ( tail != null ) {
                if ( tail.prev() != null && tail.prev() != head ) {
                    tail = tail.prev();
                } else {
                    tail = null;
                }
            }
        }

        // Otherwise remove the node normally
        remove.prev().next( remove.next() );
        remove.next().prev( remove.prev() );
    }


    /**
     * When a worst fit block is found, this block need to change the position
     * and size info. If worst is not found, return position = -1
     *
     * @param recordSize the size of the block
     *
     * @return position of the best fit block
     */
    public int updateList( int recordSize ) {

        Node worst = head;
        int position = -1;

        // If the attempt to insert the record fail due to insufficient memory issues return -1.
        if ( worst == null ) {
           return  position;
        }

        if ( recordSize == worst.getBlockSize() ) {
            position = worst.getPosition();
            remove( worst );

        } else
        // If not all the space of the worst block is needed, then the remaining space will make
        // up a new free block and be returned to the free list.
        if ( worst.getBlockSize() > recordSize ) {

            int remainingSpace = worst.getBlockSize() - recordSize;
            int newPosition = worst.getPosition() + worst.getBlockSize();

            Node remainingSpaceNode = new Node( remainingSpace, newPosition );

            insert( remainingSpaceNode );

        }

        return position;
    }


    /**
     * Put a free block, which has position and size reference.
     *
     * @param newNode to be inserted
     */

    public void insert( Node newNode ) {

        Node temp = head;

        if ( head == null ) {
            head = newNode;
            head.next( tail );
            return;
        } else if ( tail == null ) {
            if ( newNode.getBlockSize() > head.getBlockSize() ) {
                tail = head;
                head = newNode;
                head.next( tail );
                tail.prev( head );
            } else {
                tail = newNode;
            }

            head.next( tail );
            tail.prev( head );
        }

        if ( tail.getBlockSize() > newNode.getBlockSize() ) {
            append( newNode );
            return;
        }

        while ( temp.next() != null ) {

            if ( newNode.getBlockSize() >= temp.getBlockSize() ) {
                break;
            }
            temp = temp.next();
        }
        // If the block sizes are the same the nodes should be order by position in memoryPool ASC.
        while ( temp.next() != null ) {
            if ( temp.next().getBlockSize() == newNode.getBlockSize() ) {
                if ( newNode.getPosition() < temp.getPosition() ) {
                    temp = temp.prev();
                    break;
                }
                temp = temp.next();
            } else {
                break; //edge case of temp position less than newnode position so newnode has to
                // be inserted at the end
            }
        }

        // insert new node after temp runner node in list
        newNode.next( temp.next() );
        temp.next( newNode );
        newNode.prev( temp );

        // Call merge method for new node to see if this node should be merged with any others in
        // the list.
        merge( newNode );

    }


    /**
     * Merge all the nodes that are left and right neighbors of the current node's position in the
     * memory pool.
     *
     * @param freeNode node that has just been inserted into the freeblock list.
     */
    public void merge( Node freeNode ) {

        Node runner = head;
        boolean foundLeftNeighbor = false;
        boolean foundRightNeighbor = false;


        // For each free node in the freeblock list
        while ( runner.next() != null ) {

            // if the free node is not equal to the target for merge operation
            if ( runner != freeNode ) {

                // if the freeNode's position is to the right of target node and they are "touching"
                if ( isRightNeighbor( runner, freeNode ) ) {

                    foundRightNeighbor = true;

                    // merge the freeblocks of the nodes and remove both of them and reinsert.
                    Node result = merge( runner, freeNode );
                    insert( result );
                }

                // if the freeNode's position is to the right of target node and they are "touching"
                if ( isLeftNeighbor( runner, freeNode ) ) {

                    foundLeftNeighbor = true;

                    // merge the freeblocks of the nodes and remove both of them and reinsert.
                    Node result = merge( runner, freeNode );
                    insert( result );
                }

                if ( foundLeftNeighbor && foundRightNeighbor ) {
                    break;
                }

            }

        }

        System.out.println( "Freeblock List Nodes has been merged!" );

    }

    /**
     * Merge the block sizes of two Nodes whose freeblocks of memory are adjacent in the memory pool
     *
     * @param freeNode one of the nodes we are merging.
     * @param target   one of the nodes we are merging.
     *
     * @return resulting node after the merge.
     */
    private Node merge( Node freeNode, Node target ) {

        // Merge the blocks of the two nodes.
        int newBlockSize = freeNode.getBlockSize() + target.getBlockSize();

        Node result = new Node();
        result.setBlockSize( newBlockSize );

        // remove the right-most node, of the two, from the list once we are done merging it.
        if ( target.getPosition() < freeNode.getPosition() ) {
            result.setPosition( target.getPosition() );

        } else {
            result.setPosition( freeNode.getPosition() );

        }

        remove( target );
        remove( freeNode );

        return result;
    }

    /**
     * Check if node is the target's right neighbor.
     *
     * @param runner the node that we are analyzing it's proximity to the target node
     * @param target the node that we are trying to merge with other nodes.
     *
     * @return if the runner is the target's right neighbor.
     */
    public boolean isRightNeighbor( Node runner, Node target ) {

        return ( runner.getPosition() + runner.getBlockSize() ) == target.getPosition();
    }

    /**
     * Check if node is the target's left neighbor.
     *
     * @param runner the node that we are analyzing it's proximity to the target node
     * @param target the node that we are trying to merge with other nodes.
     *
     * @return if the runner is the target's left neighbor.
     */
    public boolean isLeftNeighbor( Node runner, Node target ) {

        return ( runner.getPosition() - runner.getBlockSize() ) == target.getPosition();
    }

    /**
     * Print out the list information.
     * <p>
     * e.g. "[ (position: 4 :: blockSize: 100) --> (position: 200 :: blockSize: 10) ]"
     *
     * @return freeBlock list as formatted string.
     */
    public  String toString() {

        StringJoiner list = new StringJoiner( " --> ", "[ ", " ]" );

        Node temp = head;

        if ( !isEmpty() && tail != null ) {

            if ( temp.next().equals( tail ) ) {

                list.add( head.toString() ).add( tail.toString() );

            } else {

                while ( temp.next() != null ) {
                    list.add( temp.toString() );
                    temp = temp.next();
                }
            }
        }
        return list.toString();
    }

}
