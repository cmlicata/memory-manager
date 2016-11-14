public class RecordArray {

    private Entry[] recordHandles;

    private int recordTableSize;

    private int numberOfRecordsStored;


    public RecordArray( int numberOfRecords ) {

        recordHandles = new Entry[ numberOfRecords ];
        recordTableSize = numberOfRecords;
    }

    public Handle getHandle( int key ) {

        int hash = ( key % recordHandles.length );

        while ( recordHandles[ hash ] != null && recordHandles[ hash ].getKey() != key ) {
            hash = ( hash + 1 ) % recordHandles.length;
        }

        return recordHandles[ hash ] == null ? null : recordHandles[ hash ].getValue();
    }

    /**
     * Add a new record to the RecordArray
     *
     * @param recordNumber to the Handle stored in the RecordArray
     * @param dataHandle   at which the record will be inserted in the MemoryPool
     */
    public void put( int recordNumber, Handle dataHandle ) {

        int hash = ( recordNumber % recordHandles.length );

        while ( recordHandles[ hash ] != null && recordHandles[ hash ].getKey() != recordNumber ) {
            hash = ( hash + 1 ) % recordHandles.length;
        }
        recordHandles[ hash ] = new Entry( recordNumber, dataHandle );
        numberOfRecordsStored++;
    }


    /**
     * Remove a particular record from the RecordArray
     *
     * @param key to the Handle stored in the RecordArray
     *
     * @return Handle that was removed from the RecordArray.
     */
    public Handle remove( int key ) {

        int hash = ( key % recordHandles.length );

        while ( recordHandles[ hash ] != null && recordHandles[ hash ].getKey() != key ) {
            hash = ( hash + 1 ) % recordHandles.length;
        }

        if ( recordHandles[ hash ] == null ) {
            return null;
        } else {
            Handle handle = recordHandles[ hash ].getValue();
            recordHandles[ hash ] = null;
            numberOfRecordsStored--;
            return handle;
        }
    }

    /**
     * Check if there is room for any more records within the RecordArray.
     */
    public boolean isOutOfSpace() {

        return ( numberOfRecordsStored + 1 ) == recordHandles.length;
    }

    /**
     * print out the hash recordHandles content.
     *
     * @param mem the memory manager to
     *
     * @return str the content of the hash recordHandles;
     */
    public void printContentsOfRecordArray( MemManager mem ) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "[ " );

        if ( numberOfRecordsStored == 0 ) {
            stringBuilder.append( "No records stored in record array" );
        } else {
            //  print in order of record number
            for ( int i = 0; i < recordTableSize; i++ ) {
                if ( recordHandles[ i ] != null ) {
                    stringBuilder.append(
                            // print value of recnum, position handle, and record
                            String.format( "Record # %d :: Position # %d :: %s",
                                    recordHandles[ i ].getKey(),
                                    recordHandles[ i ].getValue().getPosition(),
                                    mem.get( recordHandles[ i ].getValue() )
                            )
                    );

                }
            }
            stringBuilder.append( " ]" );
        }

        System.out.println( stringBuilder.toString() );
    }


    /**
     * Prints the record (coordinates and name) that whose handle is stored in position recnum of
     * the record array.  If there is no record an error message will be printed to stdout.  If
     * the recordNumber is outside of the range 0 to num-recs - 1 then an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param recordNumber the position of the handle to the record stored in the memory pool.
     * @param mem          the memory manager
     */
    public void printRecord( int recordNumber, MemManager mem ) throws IllegalArgumentException {

        if ( recordNumber > recordTableSize - 1 ) {

            throw new IllegalArgumentException( "Invalid record number!" );

        } else if ( getHandle( recordNumber ) == null ) {

            System.out.println( "There is no record associated with that record number." );

        } else {

            System.out.println( mem.get( recordHandles[ recordNumber ].getValue() ).toString() );
        }
    }

    public int getRecordTableSize() {

        return recordTableSize;
    }

    /**
     * Inner class to store two data: key and value for hash recordHandles.
     */
    private class Entry {

        private int key;

        private Handle value;

        private boolean tomb;


        /**
         * create a object with key and value.
         *
         * @param key   of the entry
         * @param value of the value
         */
        protected Entry( int key, Handle value ) {

            this.key = key;
            this.value = value;
            tomb = false;
        }


        /**
         * create a object with tomb.
         *
         * @param tomb of tombstone
         */
        public Entry( boolean tomb ) {

            this.tomb = tomb;
        }


        /**
         * getHandle key of the entry.
         *
         * @return key to getHandle
         */
        public int getKey() {

            return key;
        }


        /**
         * Get value of entry.
         *
         * @return value the handle
         */
        public Handle getValue() {

            return value;
        }


        /**
         * Get the boolean tomb.
         */
        public boolean getTomb() {

            return tomb;
        }

    }
}
