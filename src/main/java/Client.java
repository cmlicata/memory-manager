import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

/**
 * Parser for the command line arguments and input file.
 * <p>
 * Provides higher level interface from which we can easily work with the complicated underlying
 * data structures.
 */
public class Client {

    /**
     * the hash table to store artist name.
     */
    private Hashtable cityCoordinateTable;

    /**
     * memory manager to handle with the memory pool and freeblock list.
     */
    private MemManager manager;


    /**
     * Create a new Client object.
     *
     * @param manager   the memory manager that handles memory pool and freeblock list
     * @param tableSize the initial table size
     * @param fileName  the command file that needs to read
     */
    public Client( MemManager manager, int tableSize, String fileName )
            throws FileNotFoundException {

        cityCoordinateTable = new Hashtable( tableSize );
        this.manager = manager;

        Scanner scanner = new Scanner( new File( fileName ) );
        String command;
        while ( scanner.hasNext() ) {
            command = scanner.nextLine().trim();

            // Split up the arguments into an array of strings using whitespace as the delimiter
            String[] commandLineArguments = command.split( "\\s+" );

            if ( commandLineArguments[ 0 ].equals( "put" ) ) {
                insertRecord(
                        new CoordInfo(
                                stringToInt( commandLineArguments[ 1 ] ),
                                stringToInt( commandLineArguments[ 2 ] ),
                                commandLineArguments[ 3 ]
                        )
                );

            } else if ( commandLineArguments[ 0 ].equals( "remove" ) ) {
                removeRecord( stringToInt( commandLineArguments[ 1 ] ) );

            } else if ( command.startsWith( "print" ) ) {

                // if there is another part of this command (e.g. print 1)
                if ( commandLineArguments.length > 1 ) {
                    cityCoordinateTable.getHandle( stringToInt( commandLineArguments[ 1 ] ) );

                } else {
                    // print entire list
                }
            }
        }
    }


    /**
     * put new CoordinateInfo into coordinateTable and memory pool.
     *
     * @param cityCoordInfo that needs to be inserted
     */
    public void insertRecord( CoordInfo cityCoordInfo ) {
        // put into memory pool and get the handle
        Handle songHandle = cityCoordinateTable.getHandle( cityCoordInfo );
        // put into hash table
        if ( songHandle != null ) {
            System.out.println( "|" + cityCoordInfo
                                + "| duplicates a record already in the song database." );
        } else {
            byte[] songByte = stringToByte( cityCoordInfo );
            if ( songTable.rehashNeed( cityCoordInfo ) ) {
                System.out.println( "Song hash table size doubled." );
            }
            Handle newHandle = manager.insert( songByte, songByte.length );
            songTable.insert( cityCoordInfo, newHandle );
            System.out.println( "|" + cityCoordInfo + "| is added to the song database." );
        }
    }


    /**
     * remove song name from hash table.
     *
     * @param song name
     */
    public void removeRecord( int recordNumber ) {

        Handle songHandle = songTable.getHandle( song );
        if ( songHandle == null ) {
            System.out.println( "|" + song
                                + "| does not exist in the song database." );
        } else {
            songTable.remove( song );
            manager.remove( songHandle );
            System.out.println( "|" + song
                                + "| is removed from the song database." );
        }
    }

    public static int stringToInt( String cmndArgument ) {

        int coordinate;

        try {
            coordinate = Integer.valueOf( cmndArgument );

        } catch ( NumberFormatException numberFormatException ) {
            String errorMsg = String.format( "ERROR: Argument %s is not a number", cmndArgument );
            throw new IllegalArgumentException( errorMsg );
        }

        return coordinate;
    }


    /**
     * Serializer class that provides helper methods to convert different data types to byte arrays
     * and vice versa.
     */
    public static class Serializer {


        public static byte[] coordInfoToByte( CoordInfo info ) throws IllegalArgumentException {

            byte[] x = intToByte( info.getX() );
            byte[] y = intToByte( info.getY() );
            byte[] cityName = stringToByte( info.getName() );

            // Length of record to store in memPool (NOTE: length of record stored in first byte)
            int totalSizeOfRecord = x.length + y.length + cityName.length + 1;

            if ( totalSizeOfRecord > 256 ) {
                throw new IllegalArgumentException( "Total length of a record may not be more than "
                                                    + "256 bytes." );
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byteArrayOutputStream.write( totalSizeOfRecord );
                byteArrayOutputStream.write( x );
                byteArrayOutputStream.write( y );
                byteArrayOutputStream.write( cityName );

            } catch ( IOException ioException ) {
                System.out.println( "ERROR: Failed converting Coordinate info to byte array." );
            }

            return byteArrayOutputStream.toByteArray();
        }

        /**
         * String to byte method.
         *
         * @param cityName needs to convert to String
         *
         * @return byte array containing data
         */
        public static byte[] stringToByte( String cityName ) {

            byte[] data = new byte[ cityName.length() ];

            try {
                ByteBuffer.wrap( data ).put( cityName.getBytes( "US-ASCII" ) );
            } catch ( UnsupportedEncodingException e ) {
                e.printStackTrace();
            }

            return data;
        }

        /**
         * Convert byte array to String.
         *
         * @param b byte array containing city name data
         *
         * @return String of city name
         */
        public static String byteToString( byte[] b ) {

            return new String( b );
        }


        /**
         * Integer to byte method.
         *
         * @param coordinate that needs to converted to byte array
         *
         * @return byte array containing coordinate data
         */
        public static byte[] intToByte( int coordinate ) {

            return ByteBuffer.allocate( 4 )
                    .order( ByteOrder.LITTLE_ENDIAN )
                    .putInt( coordinate )
                    .array();
        }

        /**
         * Convert byte array to int.
         *
         * @param b byte array containing city coordinate data
         *
         * @return int form of data stored in b
         */
        public static int byteArrayToLittleEndianInt( byte[] b ) {

            return ByteBuffer.wrap( b )
                    .order( ByteOrder.LITTLE_ENDIAN )
                    .getInt();
        }

    }
}
