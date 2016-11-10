import static java.util.Arrays.copyOf;


/**
 * The MemoryPool stores the record as bytes to memory pool.
 */
public class MemoryPool {

    /**
     * Our pool of memory from which we must allocate space
     */
    private byte[] pool;

    /**
     * The numberOfRecords is used to reallocate.
     */
    private int numberOfRecords;


    /**
     * Create a new MemoryPool object and initial with numberOfRecords.
     *
     * @param numberOfRecords of the pool array
     */
    public MemoryPool( int numberOfRecords ) {

        this.numberOfRecords = numberOfRecords;
        pool = new byte[ numberOfRecords ];
    }


    /**
     * Stores data within the "memory pool" array at index "position".
     *
     * @param data     to be stored into pool array.
     * @param position in which the data should be inserted.
     */
    public void store( byte[] data, int position ) {

        // store the length of record in first two bytes
        pool[ position ] = (byte) ( data.length >> 8 );
        pool[ position + 1 ] = (byte) ( data.length );

        // copy the data into memory pool
        for ( byte index : data ) {
            pool[ position + 2 + index ] = data[ index ];
        }
    }


    /**
     * Get data in pool array with index "position".
     *
     * @param position where the data is
     *
     * @return data in pool array
     */
    public byte read( int position ) {
        // read the size of record from first two bytes
        byte size = (byte) ( pool[ position ] << 8 );
        size = (byte) ( size + pool[ position + 1 ] );

        return size;
    }


    /**
     * Read from pool array to space array.
     *
     * @param space    to which the record will be copied
     * @param position of the record
     * @param size     of the record
     */
    public void read( byte[] space, int position, int size ) {
        System.arraycopy( pool, position, space, 0, size );
    }
}
