public class RecordArray {

    private final static int TABLE_SIZE = 128;

   Entry[] table;

    public RecordArray() {

        table = new Entry[ TABLE_SIZE ];
        for ( int i = 0; i < TABLE_SIZE; i++ ) {
            table[ i ] = null;
        }
    }

    public Handle get( int key ) {

        int hash = ( key % TABLE_SIZE );
        while ( table[ hash ] != null && table[ hash ].getKey() != key ) {
            hash = ( hash + 1 ) % TABLE_SIZE;
        }
        if ( table[ hash ] == null ) {
            return null;
        } else {
            return table[ hash ].getValue();
        }
    }

    public void put( int key, int value ) {

        int hash = ( key % TABLE_SIZE );
        while ( table[ hash ] != null && table[ hash ].getKey() != key ) {
            hash = ( hash + 1 ) % TABLE_SIZE;
        }

        table[ hash ] = new Entry( key, new Handle( value ) );
    }

    public Handle remove( int key ) {

        Handle ret = null;
        int hash = ( key % TABLE_SIZE );

        while ( table[ hash ] != null && table[ hash ].getKey() != key ) {
            hash = ( hash + 1 ) % TABLE_SIZE;
        }

        if ( table[ hash ] == null ) {
            return null;
        } else {
            ret = table [ hash ].getValue();
            table [ hash ] = null;
            return ret;
        }
    }

    /**
     * inner class Entry store two data: key and value for hash table.
     */
    private class Entry {

        private long key;

        private Handle value;

        private boolean tomb;


        /**
         * create a object with key and value.
         *
         * @param key   of the entry
         * @param value of the value
         */
        public Entry( long key, Handle value ) {

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
         * get key of the entry.
         *
         * @return key to get
         */
        public long getKey() {

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
