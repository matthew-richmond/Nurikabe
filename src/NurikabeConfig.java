

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
            String[] rowcol = in.nextLine().trim().split(" ");
            this.seas = 0;
            this.rows = Integer.parseInt(rowcol[0]);
            this.cols = Integer.parseInt(rowcol[1]);
            this.board = new char[this.rows][this.cols];
            for (int row = 0; row < this.rows; row++)
            {
                char[] line = in.nextLine().trim().replaceAll(" ","").toCharArray();
                this.board[row] = line;
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
        this.seas = other.getSeas();
        this.rows = other.getRows();
        this.cols = other.getCols();
        this.board = new char[this.rows][this.cols];
        for(int row = 0; row<this.rows; row++)
        {
            for(int col = 0; col<this.cols; col ++)
            {
                this.board[row][col] = other.board[row][col];
            }
        }
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
                    succ.add(suc1);
                    suc2.board[row][col] = '#';
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
        int empties = 0;
        for(int r = 0; r<this.rows; r ++)
        {
            for(int c = 0; c<this.cols; c++)
            {
                if(this.board[r][c] == '.')
                {
                    complete = false;
                }
            }
        }
        for(int row = 0; row<this.rows; row++) {
            for (int col = 0; col < this.cols; col++)
            {
                char view = this.board[row][col];
                if(view == '.')
                {
                    empties++;
                }
                else if(Character.isDigit(view))
                {
                    String ref = row + " " +col;
                    int num_island = Character.getNumericValue(view);
                    if(complete)
                    {
                        if (!(this.countcharDFS('#', ref) == num_island)) {
                            return false;
                        }
                    }
                    else
                        return(this.countcharDFS('#',ref) <= num_island);
                }
                else if(view == '@')
                {

                }

            }

        }
        if(complete)
        {
            String ref =  "";
            for(int row = 0; row<this.rows; row++)
            {
                for (int col = 0; col < this.cols; col++)
                {
                    if(this.board[row][col] == '@')
                    {
                        ref = row + " " + col;
                        if(row+1 < this.rows)
                        {
                            if(col+1 < this.cols)
                            {
                                if(this.board[row+1][col+1] == this.board[row][col])
                                {
                                    if(this.board[row+1][col] == this.board[row][col])
                                    {
                                        if(this.board[row][col+1] == this.board[row][col])
                                        {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }


                    }
                }
            }
            return (countcharDFS('@',ref) == this.seas);

        }
        return true;
    }

    private int countcharDFS(char sym, String ref)
    {
        Map<String,Character> map = new HashMap<>();
        return countcharhelper(map,ref,sym).size();
    }
    private Map<int[], Character> countcharhelper(Map visited,String ref,char sym)
    {
        String[] refs = ref.split(" ");
        int row = Integer.parseInt(refs[0]);
        int col = Integer.parseInt(refs[1]);
        boolean ints = false;
        if(!visited.containsKey(ref));
        {
            if (sym == '#')
            {
                ints = true;
            }
            if((this.board[row][col] == sym) || (Character.isDigit(this.board[row][col]) && ints))
            {
                visited.put(ref,sym);
                if(row - 1 >= 0)
                {
                    String up = (row-1) + " " + col;
                    if(this.board[row-1][col] == sym && !visited.containsKey(up))
                    {
                        countcharhelper(visited, up, sym);
                    }
                }
                if(row + 1 < this.rows)
                {
                    String down = (row+1) + " " + col;
                    if(this.board[row+1][col] == sym && !visited.containsKey(down))
                    {
                        countcharhelper(visited, down, sym);
                    }
                }
                if(col - 1 >= 0)
                {
                    String left = row + " " + (col-1);
                    if(this.board[row][col-1] == sym && !visited.containsKey(left))
                    countcharhelper(visited,left,sym);
                }
                if(col+1 <this.cols)
                {
                    String right = row + " " + (col+1);
                    if(this.board[row][col+1] == sym && !visited.containsKey(right))
                    countcharhelper(visited,right,sym);
                }
            }

        }
        return visited;
    }

    @Override
    public boolean isGoal() {
        if(this.isValid())
        {
            for(int row = 0; row<this.rows;row++)
            {
                char[] line = this.board[row];
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

    public int getRows(){return rows;}
    public int getCols(){return cols;}
    public int getSeas(){return seas;}
    public char[][] getBoard(){return board;}
}
