import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;

/**
 * Parser for the command line arguments and input file.
 *
 * Provides higher level interface from which we can easily work with the complicated underlying
 * data structures.
 */
public class Client
{

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
     * @param manager the memory manager that handles memory pool and freeblock list
     * @param tableSize the initial table size
     * @param fileName the command file that needs to read
     * @throws FileNotFoundException
     */
    public Client(MemManager manager, int tableSize, String fileName)
        throws FileNotFoundException
    {
        cityCoordinateTable = new Hashtable(tableSize);
        this.manager = manager;

        Scanner scanner = new Scanner(new File(fileName));
        String command;
        while (scanner.hasNext())
        {
            command = scanner.nextLine().trim();

            // Split up the arguments into an array of strings using whitespace as the delimiter
            String[] commandLineArguments = command.split("\\s+");

            if (commandLineArguments[0].equals("insert") )
            {
                insertRecord(
                        new CoordinateInfo(
                        stringToInt( commandLineArguments[1] ),
                        stringToInt( commandLineArguments[2] ),
                        commandLineArguments[3]
                        )
                );
            }
            else if (commandLineArguments[0].equals("remove"))
            {
                removeRecord( stringToInt( commandLineArguments[1] ) );
            }
            else if (command.startsWith("print"))
            {
                // if there is another part of this command (e.g. print 1)
                if ( commandLineArguments.length > 1){
                   cityCoordinateTable.getHandle( stringToInt( commandLineArguments[1] ) );
                }

                System.out.println( cityCoordinateTable.print(manager) + "artists: "
                                    + cityCoordinateTable.usedSize());
            }
            else if (command.startsWith("print song"))
            {
                System.out.println(songTable.print(manager) + "songs: "
                    + songTable.usedSize());
            }
            else if (command.startsWith("print blocks"))
            {

                manager.dump();
            }
        }
    }


    /**
     * insert new CoordinateInfo into coordinateTable and memory pool.
     *
     * @param cityCoordinateInfo that needs to be inserted
     */
    public void insertRecord(CoordinateInfo cityCoordinateInfo)
    {
        // insert into memory pool and get the handle
        Handle songHandle = cityCoordinateTable.getHandle(cityCoordinateInfo);
        // insert into hash table
        if (songHandle != null)
        {
            System.out.println("|" + cityCoordinateInfo
                + "| duplicates a record already in the song database.");
        }
        else
        {
            byte[] songByte = stringToByte(cityCoordinateInfo);
            if (songTable.rehashNeed(cityCoordinateInfo))
            {
                System.out.println("Song hash table size doubled.");
            }
            Handle newHandle = manager.insert(songByte, songByte.length);
            songTable.insert(cityCoordinateInfo, newHandle);
            System.out.println("|" + cityCoordinateInfo + "| is added to the song database.");
        }
    }


    /**
     * remove song name from hash table.
     *
     * @param song
     *            name
     */
    public void removeRecord( int recordNumber )
    {
        Handle songHandle = songTable.getHandle(song);
        if (songHandle == null)
        {
            System.out.println("|" + song
                + "| does not exist in the song database.");
        }
        else
        {
            songTable.remove(song);
            manager.remove(songHandle);
            System.out.println("|" + song
                + "| is removed from the song database.");
        }
    }


    public static Integer stringToInt(String commandArgument){
        int coordinate = -1;
        try {
            coordinate = Integer.valueOf( commandArgument );
        } catch ( NumberFormatException numberFormatException ) {
            System.out.println( String.format( "ERROR: Argument %s is not a number", commandArgument ) );
        }
        return coordinate;
    }

    /**
     * string to byte method.
     *
     * @param name
     *            needs to convert to String
     * @return data arry
     */
    public static byte[] objectToByte(CoordinateInfo)
    {
        byte[] data = new byte[name.length()];

        try
        {
            ByteBuffer.wrap(data).put(name.getBytes("US-ASCII"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return data;
    }

}
