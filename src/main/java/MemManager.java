import javax.xml.bind.SchemaOutputResolver;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * The Memory Manager that deals with the byte array that needs to be inserted into memory memPool.
 *
 * Functions Needed:
 *
 * TODO: put recordNum x-coord y-coord cityName
 * TODO: remove recordNum from MemoryPool and update the freeblock freeList.
 * TODO: print recordNum to stdout
 * TODO: print (all records from the freeList)
 */
public class MemManager {

    /**
     * Memory memPool array
     */
    private MemoryPool memPool;

    /**
     * The freeblock freeList to track free blocks of space in the memory memPool.
     */
    private FreeBlock freeList;

    /**
     * The number of blocks in the memPool.
     */
    private int blockSize;

    /**
     * The number of allocations
     */
    private int totalAllocations = 0;


    /**
     * Create a new MemManager object. initialize the memPool array and freeblock
     * freeList.
     *
     * @param blockSize the size of the block
     */
    public MemManager( int blockSize ) {
        memPool = new MemoryPool( blockSize );
        freeList = new FreeBlock( blockSize );

        this.blockSize = blockSize;
    }


    /**
     * Insert a record and return its position handle.
     *
     * @param space contains the record to be inserted
     * @param size  of the record
     *
     * @return handle position handle
     */
    public Handle insert( byte[] space, int size ) {
        int position = freeList.findPosition( size + 2 );

        if ( position != -1 ) {
            memPool.store( space, position );
            freeList.updateList( size + 2 );

            return new Handle( position );
        } else {
            System.out.println("ERROR: mempool at capacity, cannot insert!");

            return null;
        }
    }


    /**
     * Free a block at the position specified by theHandle. Merge adjacent free
     * blocks.
     *
     * @param handle with position of the record that need to be removed
     */
    public void remove( Handle handle ) {
        int position = handle.getPosition();

        // get byte size from memPool array and convert to int
        int recordSize = memPool.read( position ) & 0xFFFF;

        // after remove record, the free block needs to be put back to freeList
        Node freeBlock = new Node( recordSize + 2, position );

        freeList.insert( freeBlock );
    }


    /**
     * Return the record with handle posHandle, up to size bytes, by copying it
     * into space. Return the number of bytes actually copied into space.
     *
     * @param space  that the record needs to be copied into.
     * @param handle with the position of the record
     * @param size   the given size of the record
     *
     * @return copySize the actual size of the record that copies into the space
     */
    public int get( byte[] space, Handle handle, int size ) {
        int copySize;
        int memSize = memPool.read( handle.getPosition() ) & 0xFFFF;

        if ( memSize > size ) {
            copySize = size;
        } else {
            copySize = memSize;
        }

        memPool.read( space, handle.getPosition() + 2, size );

        return copySize;
    }


    /**
     * Get the String with handle.
     *
     * @param handle to get the string
     *
     * @return string returned by handle
     */
    public String get( Handle handle ) {
        int size = memPool.read( handle.getPosition() );
        byte[] space = new byte[ size ];

        memPool.read( space, handle.getPosition() + 2, size );

        return byteToString( space, size );
    }


    /*
     * TODO: rename this function
     */
    /**
     * print out the entire freeblock freeList.
     */
    public void dump() {
        System.out.println( freeList.toString() );
    }


    /*
     * TODO: move this to the serializer class
     */
    /**
     * Convert byte array to string.
     *
     * @param data to convert to string
     * @param size the actual size that used
     *
     * @return str converted from data
     */
    public String byteToString( byte[] data, int size ) {

        String str = null;

        // load data to buffer
        ByteBuffer buffer = ByteBuffer.wrap( data );

        byte[] buf = new byte[ size ];
        buffer.get( buf );
        try {
            str = new String( buf, "US-ASCII" );
        } catch ( UnsupportedEncodingException e ) {
            e.printStackTrace();
        }

        return str;
    }

}
