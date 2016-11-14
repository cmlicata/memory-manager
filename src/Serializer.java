import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Serializer class that provides helper methods to convert different data types to byte arrays
 * and vice versa.
 */
public class Serializer {

    /**
     * Converts CoordInfo object to byte array for storage in the memory pool.
     *
     * @param info one CoordInfo record
     *
     * @return byteArray of info record
     */
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

        String cityName = "";
        try {
            cityName = new String( b, "US-ASCII" );

        } catch ( UnsupportedEncodingException e ) {
            System.out.println( "ERROR: Encoding of String data must be US-ASCII!" );
        }

        return cityName;
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

