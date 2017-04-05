

import com.sun.xml.internal.ws.util.StringUtils;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PipedInputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 * A class to represent a single configuration in the Nurikabe puzzle.
 *
 * @author Sean Strout @ RITCS
 * @author Matthew Richmond
 */
public class NurikabeConfig implements Configuration {
    private int rows;
    private int cols;
    private int seas;
    private char[][] board;


    /**
     * Construct the initial configuration from an input file whose contents
     * are, for example:<br>
     * <tt><br>
     * 3 3          # rows columns<br>
     * 1 . #        # row 1, .=empty, 1-9=numbered island, #=island, &#64;=sea<br>
     * &#64; . 3    # row 2<br>
     * 1 . .        # row 3<br>
     * </tt><br>
     * @param filename the name of the file to read from
     * @throws FileNotFoundException if the file is not found
     */
    public NurikabeConfig(String filename) throws FileNotFoundException {
        try (Scanner in = new Scanner(new File(filename)))
        {
        }
    }

    /**
     * The copy constructor takes a config, other, and makes a full "deep" copy
     * of its instance data.
     *
     * @param other the config to copy
     */

    protected NurikabeConfig(NurikabeConfig other)
    {

    }

    @Override
    public Collection<Configuration> getSuccessors()
    {

    }

    @Override
    public boolean isValid()
    {

    }

    private int countcharDFS(char sym, String ref)
    {

    }
    private Map<int[], Character> countcharhelper(Map visited,String ref,char sym)
    {

    }

    @Override
    public boolean isGoal()
    {

    }

    /**
     * Returns a string representation of the puzzle, e.g.: <br>
     * <tt><br>
     * 1 . #<br>
     * &#64; . 3<br>
     * 1 . .<br>
     * </tt><br>
     */
    @Override
    public String toString()
    {

    }

    public int getRows(){return rows;}
    public int getCols(){return cols;}
    public int getSeas(){return seas;}
    public char[][] getBoard(){return board;}
}
