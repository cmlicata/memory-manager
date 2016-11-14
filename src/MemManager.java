/**
 * The Memory Manager that deals with the byte array that needs to be inserted into memory memPool.
 * <p>
 * Functions Needed:
 * <p>
 * TODO: put recordNum x-coord y-coord cityName
 * TODO: remove recordNum from MemoryPool and update the freeblock freeList.
 * TODO: print recordNum to stdout
 * TODO: print (all records from the freeList)
 */
public class MemManager {

    public static final int X_COORD_START_OFFSET = 1;

    public static final int Y_COORD_START_OFFSET = 5;

    public static final int CITY_START_OFFSET = 9;

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
     * @param data contains the record to be inserted
     * @param size of the record
     *
     * @return handle position handle
     */
    public Handle insert( byte[] data, int size ) {

        int position = freeList.findPosition( size );

        if ( position != -1 ) {
            memPool.store( data, position );
            freeList.updateList( size );

            return new Handle( position );
        } else {
            System.out.println( "ERROR: Mempool is at capacity, cannot insert!" );

            return null;
        }
    }


    /**
     * Free a block at the position specified by the Handle. Merge adjacent free
     * blocks.
     *
     * @param handle with position of the record that need to be removed
     */
    public void remove( Handle handle ) {

        int position = handle.getPosition();

        // getHandle byte size from memPool array and convert to int
        int recordSize = memPool.read( position ) & 0xFFFF;

        // after remove record, the free block needs to be put back to freeList
        Node freeBlock = new Node( recordSize , position );

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

        memPool.getDataFromMemoryPool( space, handle.getPosition(), size );

        return copySize;
    }


    /**
     * Get the String with handle.
     *
     * @param handle to getHandle the string
     *
     * @return string returned by handle
     */
    public CoordInfo get( Handle handle ) {

        int size = memPool.read( handle.getPosition() );
        int dataStart = handle.getPosition();

        byte[] xCoordinate = new byte[ 4 ];
        memPool.getDataFromMemoryPool(
                xCoordinate,
                dataStart + X_COORD_START_OFFSET,
                xCoordinate.length
        );

        byte[] yCoordinate = new byte[ 4 ];
        memPool.getDataFromMemoryPool(
                yCoordinate,
                dataStart + Y_COORD_START_OFFSET,
                yCoordinate.length
        );

        byte[] cityName = new byte[ size - CITY_START_OFFSET ];
        memPool.getDataFromMemoryPool(
                cityName,
                dataStart + CITY_START_OFFSET,
                cityName.length
        );


        return new CoordInfo( Serializer.byteArrayToLittleEndianInt( xCoordinate ),
                Serializer.byteArrayToLittleEndianInt( yCoordinate ),
                Serializer.byteToString( cityName ) );
    }



    /**
     * print out the entire freeblock freeList.
     */
    public void printFreeBlockList() {

        System.out.println( freeList.toString() );
    }

}
