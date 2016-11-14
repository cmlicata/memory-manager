import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/*
 * I pledge that this assignment has been completed in compliance with the Graduate Honor Code and
 * that I have neither given nor received any unauthorized aid on this work. Further, I did not
 * use any source codes from any other unauthorized sources, either modified or unmodified. The
 * submitted programming assignment is solely done by me and is my original work.
 **/

/**
 * This is the main class.
 */

public class Memman {

    /**
     * the initial recordHandles size for hash recordHandles.
     */
    public static int numberOfRecords;

    /**
     * the max number of records that will have to be stored in the memory pool.
     */
    public static int poolSize;

    /**
     * the name of the file which contains the commands we need to to parser.
     */
    public static String commandInputFile;


    /**
     * Main method.
     *
     * @param args input arguments
     */
    public static void main( String[] args )
            throws IOException,
                   IllegalArgumentException,
                   IllegalAccessException,
                   InvocationTargetException {

        if ( args.length != 3 ) {
            System.out.println( "invalid command, please follow: "
                                + "java Memman{initial-hash-size}" );
        }

        // getHandle the arguments from the command line
        poolSize = Integer.parseInt( args[ 0 ] );
        numberOfRecords = Integer.parseInt( args[ 1 ] );
        commandInputFile = args[ 2 ];

        // create memory manager object and initialize it with poolSize
        MemManager manager = new MemManager( poolSize );

        Client.init( manager, numberOfRecords, commandInputFile );

    }

}
