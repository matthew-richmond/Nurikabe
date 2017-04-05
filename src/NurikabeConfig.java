

import com.sun.xml.internal.ws.util.StringUtils;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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
            String[] rowcol = in.nextLine().trim().split(" ");
            this.seas = 0;
            this.rows = Integer.parseInt(rowcol[0]);
            this.cols = Integer.parseInt(rowcol[1]);
            this.board = new char[this.rows][this.cols];
            while(!in.nextLine().equals(""))
            {
                for (int row = 0; row < this.rows; row++)
                {
                    char[] line = in.nextLine().trim().replaceAll(" ","").toCharArray();
                    this.board[row] = line;
                }
            }
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
        this.seas = other.seas;
        this.rows = other.rows;
        this.cols = other.cols;
        this.board = other.board;
    }

    @Override
    public Collection<Configuration> getSuccessors()
    {
        List<Configuration> succ= new LinkedList<>();
        NurikabeConfig suc1 = new NurikabeConfig(this);
        NurikabeConfig suc2 = new NurikabeConfig(this);
        for(int row = 0; row<this.rows; row++)
        {
            for(int col = 0; col<this.cols; col++)
            {
                if(this.board[row][col] == '.')
                {
                    suc1.board[row][col] = '@';
                    suc1.seas += 1;
                    suc2.board[row][col] = '#';
                    succ.add(suc1);
                    succ.add(suc2);
                    return succ;
                }
            }
        }

        return new LinkedList<>();
    }

    @Override
    public boolean isValid()
    {
        boolean complete = true;
        for(int row = 0; row<this.rows; row++) {
            for (int col = 0; col < this.cols; col++)
            {
                char view = this.board[row][col];
                if(view == '.')
                {
                    complete = false;
                }
                else if(Character.isDigit(view))
                {
                    int[] ref = {row,col};
                    this.countchar('#', ref);
                }

            }
        }
        return true;
    }

    private int countchar(char sym, int[] ref)
    {
        return 0;
    }

    @Override
    public boolean isGoal() {
        if(this.isValid())
        {
            for(int row = 0; row<this.rows;row++)
            {
                char[] line = new char[this.cols];
                for(char x: line)
                {
                    if(x == '.')
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
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
        String ret = "";
        for (int row = 0; row<this.rows; row++)
        {
            char[] line = this.board[row];
            for(char x: line)
            {
                ret += x + " ";
            }
            ret = ret.trim();
            ret += "\n";
        }
        return ret.trim();
    }
}
