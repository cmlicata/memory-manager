/**
 * The MemoryPool stores the record as bytes to memory memPool.
 */
public class MemoryPool {

    /**
     * Our memPool of memory from which we must allocate space
     */
    private byte[] memPool;

    /**
     * Create a new MemoryPool object and initial with blocksize.
     *
     * @param blocksize of the memPool array
     */
    public MemoryPool( int blocksize ) {

        memPool = new byte[ blocksize ];
    }



    /**
     * Stores data within the "memory memPool" array at index "position".
     *
     * @param data                   to be stored into memPool array.
     * @param startingInsertPosition in which the data should be inserted.
     */
    public void store( byte[] data, int startingInsertPosition ) {

        // copy the data into memory memPool starting with the size
        System.arraycopy( data, 0, memPool, startingInsertPosition, data.length );
    }


    /**
     * Get data in memPool array with index "position".
     *
     * @param position where the data is
     *
     * @return data in memPool array
     */
    public int read( int position ) {

        // read the size of record from first byte
        int size = memPool[ position ];

        return size;
    }


    /**
     * Read from memPool array to space array.
     *
     * @param space    to which the record will be copied
     * @param position of the record
     * @param size     of the record
     */
    public void getDataFromMemoryPool( byte[] space, int position, int size ) {

        System.arraycopy( memPool, position, space, 0, size );
    }
}
