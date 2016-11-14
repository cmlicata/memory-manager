import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

// -------------------------------------------------------------------------

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
     * main method.
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
