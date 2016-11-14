import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Parser for the command line arguments and input file.
 * <p>
 * Provides higher level interface from which we can easily work with the complicated underlying
 * data structures.
 */
class Client {

    /**
     * The recordArray to handles to city coordinates.
     */
    private RecordArray cityCoordinateTable;

    /**
     * memory manager to handle with the memory pool and freeblock list.
     */
    private MemManager manager;


    /**
     * Create a new Client object.
     *
     * @param manager         the memory manager that handles memory pool and freeblock list
     * @param numberOfRecords the initial recordHandles size
     * @param inputFileName   the command file that needs to read
     */
    protected Client( MemManager manager, int numberOfRecords, String inputFileName )
            throws FileNotFoundException {

        cityCoordinateTable = new RecordArray( numberOfRecords );
        this.manager = manager;

        Scanner scanner = new Scanner( new File( inputFileName ) );
        String command;
        while ( scanner.hasNext() ) {

            // remove leading and trailing whitespace from command input
            command = scanner.nextLine().trim();

            // Split up the arguments into an array of strings using whitespace as the delimiter
            String[] commandLineArguments = command.split( "\\s+" );

            if ( commandLineArguments[ 0 ].equals( "insert" ) ) {
                try {
                    insertRecord( stringToInt( commandLineArguments[ 1 ] ),
                            new CoordInfo(
                                    stringToInt( commandLineArguments[ 2 ] ),
                                    stringToInt( commandLineArguments[ 3 ] ),
                                    commandLineArguments[ 4 ]
                            )
                    );

                } catch ( IllegalArgumentException illegalArgumentException ) {

                    illegalArgumentException.printStackTrace();
                }

            } else if ( commandLineArguments[ 0 ].equals( "remove" ) ) {

                try {

                    removeRecord( stringToInt( commandLineArguments[ 1 ] ) );

                } catch ( IllegalArgumentException illegalArgumentException ) {

                    illegalArgumentException.printStackTrace();
                }


            } else if ( command.startsWith( "print" ) ) {

                // if there is another part of this command (e.g. print 1)
                if ( commandLineArguments.length > 1 ) {
                    try {
                        int recordNumber = stringToInt( commandLineArguments[ 1 ] );
                        cityCoordinateTable.printRecord( recordNumber, manager );

                    } catch ( IllegalArgumentException illegalArgumentException ) {

                        illegalArgumentException.printStackTrace();
                    }

                } else {

                    cityCoordinateTable.printContentsOfRecordArray( manager );
                }
            }
        }
    }


    /**
     * put new CoordinateInfo into coordinateTable and memory pool.
     *
     * @param cityCoordInfo that needs to be inserted
     */
    private void insertRecord( int recordNumber, CoordInfo cityCoordInfo ) {

        if ( recordNumber > cityCoordinateTable.getRecordTableSize() - 1 ) {
            throw new IllegalArgumentException( "Invalid record number!" );
        }

        Handle coordInfoHandle = cityCoordinateTable.getHandle( recordNumber );

        if ( coordInfoHandle != null ) {
            cityCoordinateTable.remove( recordNumber );
        }

        byte[] cityInfoByteArray = Serializer.coordInfoToByte( cityCoordInfo );

        Handle newRecordHandle = manager.insert( cityInfoByteArray, cityInfoByteArray.length );
        cityCoordinateTable.put( recordNumber, newRecordHandle );
        System.out.println( cityCoordInfo + " has been added to the memory pool." );
    }


    /**
     * Remove record from cityCoordinateTable.
     *
     * @param recordNumber number of record stored in cityCoordinateTable
     */
    private void removeRecord( int recordNumber ) throws IllegalArgumentException{

        if ( recordNumber > cityCoordinateTable.getRecordTableSize() - 1 ) {
            throw new IllegalArgumentException( "Invalid record number!" );
        }

        Handle coordInfoHandle = cityCoordinateTable.getHandle( recordNumber );

        if ( coordInfoHandle == null ) {
            System.out.println(
                    String.format( "ERROR: Record Number %d does not exist", recordNumber )
            );

        } else {

            cityCoordinateTable.remove( recordNumber );
            manager.remove( coordInfoHandle );
            System.out.println(
                    String.format( "Record %d has been removed from memory", recordNumber )
            );
        }
    }

    private static int stringToInt( String cmndArgument ) {

        int coordinate;

        try {
            coordinate = Integer.valueOf( cmndArgument );

        } catch ( NumberFormatException numberFormatException ) {
            String errorMsg = String.format( "ERROR: Argument %s is not a number", cmndArgument );
            throw new IllegalArgumentException( errorMsg );
        }

        return coordinate;
    }

    public static Client init( MemManager mem, int poolSize, String inputFileName )
            throws FileNotFoundException {

        return new Client( mem, poolSize, inputFileName );
    }

}
