

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * A class to represent a single configuration in the Nurikabe puzzle.
 * Re-wrote this too many times.
 * @author Sean Strout @ RITCS
 * @author Matthew Richmond
 */
public class NurikabeConfig implements Configuration {
    private int rows;
    private int cols;
    private int seas;
    private int islands;
    private int total_islands;
    private Node lastAdd;
    private Node[][] board;


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
    public NurikabeConfig(String filename) throws FileNotFoundException
    {
        try (Scanner in = new Scanner(new File(filename)))
        {
            String[] rowcol = in.nextLine().split(" ");
            this.rows = Integer.parseInt(rowcol[0]);
            this.cols = Integer.parseInt(rowcol[1]);
            this.seas = 0;
            this.islands = 0;
            this.lastAdd = null;
            this.board = new Node[this.rows][this.cols];
            for(int r = 0; r<this.rows; r++)
            {
                String[] line = in.nextLine().trim().split(" ");
                Node[] nodeline = new Node[line.length];
                int i = 0;
                for(String x: line)
                {
                    if (Character.isDigit(x.charAt(0)))
                    {
                        this.islands++;
                        this.total_islands+=(Integer.parseInt(x));
                    }
                    Node node = new Node(x,r,i);
                    nodeline[i] = node;
                    i++;
                }
                this.board[r] = nodeline;
            }
            this.lastAdd = null;
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
        this.lastAdd = other.lastAdd;
        this.rows = other.rows;
        this.cols = other.cols;
        this.islands = other.islands;
        this.total_islands = other.total_islands;
        this.seas = other.seas;
        this.board = new Node[other.rows][other.cols];
        for(int r = 0; r<other.rows;r++)
        {
            for (int c = 0; c<other.cols;c++)
            {
                String sym = other.board[r][c].getSymbol();
                int row = other.board[r][c].getCoords()[0];
                int col = other.board[r][c].getCoords()[1];
                this.board[r][c] = new Node(sym,row,col);
                if(r != 0)
                {
                    this.board[r][c].addneighbor(this.board[r-1][c]);
                }
                if (c != 0)
                {
                    this.board[r][c].addneighbor(this.board[r][c -1]);
                }
            }
        }
    }

    @Override
    public Collection<Configuration> getSuccessors()
    {
        List<Configuration> succ = new LinkedList<>();
        int newaddr = 0;
        int newaddc = 0;
        if (this.lastAdd == null)
        {
            for(int r = 0; r<this.rows; r++)
            {
                for (int c = 0; c<this.cols; c++)
                {
                    if(this.board[r][c].getSymbol().equals("."))
                    {
                        this.lastAdd = this.board[r][c];
                        newaddr = r;
                        newaddc = c;
                        break;
                    }

                }break;
            }
        }
        else
            {
            int[] lastaddcoords = this.lastAdd.getCoords();
            int[] newaddcoords = findnextempycoords(lastaddcoords);
            newaddr = newaddcoords[0];
            newaddc = newaddcoords[1];
        }
        NurikabeConfig suc1 = new NurikabeConfig(this);
        NurikabeConfig suc2 = new NurikabeConfig(this);
        suc1.board[newaddr][newaddc] = new Node("@",newaddr,newaddc);
        suc1.lastAdd = suc1.board[newaddr][newaddc];
        suc1.seas += 1;
        suc2.board[newaddr][newaddc] = new Node("#",newaddr,newaddc);
        suc2.islands+=1;
        suc2.lastAdd = suc2.board[newaddr][newaddc];
        if(newaddr != 0)
        {
            suc1.board[newaddr][newaddc].addneighbor(suc1.board[newaddr-1][newaddc]);
            suc2.board[newaddr][newaddc].addneighbor(suc2.board[newaddr-1][newaddc]);
        }
        if (newaddc != 0)
        {
            suc1.board[newaddr][newaddc].addneighbor(suc1.board[newaddr][newaddc -1]);
            suc2.board[newaddr][newaddc].addneighbor(suc2.board[newaddr][newaddc -1]);
        }
        if(newaddr != this.rows-1)
        {
            suc1.board[newaddr][newaddc].addneighbor(suc1.board[newaddr+1][newaddc]);
            suc2.board[newaddr][newaddc].addneighbor(suc2.board[newaddr+1][newaddc]);
        }
        if (newaddc != this.cols-1)
        {
            suc1.board[newaddr][newaddc].addneighbor(suc1.board[newaddr][newaddc +1]);
            suc2.board[newaddr][newaddc].addneighbor(suc2.board[newaddr][newaddc +1]);
        }
        succ.add(suc1);
        succ.add(suc2);
        return succ;
    }

    public int[] findnextempycoords(int[] startcor)
    {
        for (int r = startcor[0]; r<this.rows; r++)
        {
            for(int c = 0; c<this.cols;c++)
            {
                if(this.board[r][c].getSymbol().equals("."))
                {
                    int[] coords = {r,c};
                    return coords;
                }
            }
        }
        return null;
    }
    @Override
    public boolean isValid()
    {
        boolean complete = true;
        if(this.islands>this.total_islands)
            return false;
        if(this.lastAdd != null)
        {
            if (this.lastAdd.getSymbol().equals("@")) {
                int[] coords = this.lastAdd.getCoords();
                int r = coords[0];
                int c = coords[1];
                if(r != 0)
                {
                    if (c != 0)
                    {
                        if(this.board[r-1][c].getSymbol().equals("@"))
                        {
                            if(this.board[r][c-1].getSymbol().equals("@"))
                            {
                                if(this.board[r-1][c-1].getSymbol().equals("@"))
                                    return false;
                            }
                        }
                    }
                }
            }
            else if(this.lastAdd.getSymbol().equals("#"))
            {
                if (!islandDFS(this.lastAdd))
                    return false;
            }
            for(int r = 0; r<this.rows; r++)
            {
                for(int c = 0; c<this.cols; c++)
                {
                    if(this.board[r][c].getSymbol().equals("."))
                    {
                        complete = false;
                    }
                }
            }
            if(complete)
            {
                if(this.islands!=this.total_islands)
                    return false;
                for(int r = 0; r<this.rows; r++)
                {
                    for(int c = 0; c<this.cols; c++)
                    {
                        if(this.board[r][c].getSymbol().matches("^-?\\d+$"))
                        {
                            int val = Integer.parseInt(this.board[r][c].getSymbol());
                            if(DFS(this.board[r][c],"#") != val)
                                return false;
                        }
                    }
                }
                Node[] line = this.board[0];
                for(Node x: line)
                {
                    if(x.getSymbol().equals("@"))
                    {
                        if(DFS(x,"@") != this.seas)
                            return false;
                    }
                }

            }
        }
        return true;
    }

    private boolean islandDFS(Node startnode)
    {
        List<Node> nodelist = new LinkedList<>();
        nodelist.add(startnode);
        int isl = 0;
        List<Node> visited = IDFShelper(nodelist,startnode.getNeighbors());
        {
            for(Node x: visited)
            {
                if(x.getSymbol().matches("^-?\\d+$"))
                {
                    isl++;
                    if (visited.size() > Integer.parseInt(x.getSymbol())) {
                        return false;
                    }
                }
            }
            if(isl>1)
                return false;

        }
        return true;
    }
    private List<Node> IDFShelper(List<Node> nodelist, List<Node> neighbors)
    {
        for(Node x: neighbors)
        {
            if(!nodelist.contains(x) &&(x.getSymbol().matches("^-?\\d+$") || x.getSymbol().equals("#")))
            {
                nodelist.add(x);
                IDFShelper(nodelist,x.getNeighbors());
            }
        }
        return nodelist;
    }
    private int DFS(Node startnode,String sym)
    {
        List<Node> nodelist = new LinkedList<>();
        nodelist.add(startnode);
        DFShelper(nodelist,startnode.getNeighbors(),sym);
        return nodelist.size();
    }

    private List<Node> DFShelper(List<Node> nodelist, List<Node> neighbors, String sym)
    {
        for(Node x: neighbors)
        {
            boolean ints = false;
            if(sym == "#")
            {
                ints = true;
            }
            if((!nodelist.contains(x)) && (x.getSymbol().equals((sym)) || (x.getSymbol().matches("^-?\\d+$")&&ints)))
            {
                nodelist.add(x);
                DFShelper(nodelist,x.getNeighbors(),sym);
            }
        }
        return nodelist;
    }

    @Override
    public boolean isGoal()
    {
        if(this.isValid())
        {
            for(int r = 0; r<this.rows; r++)
            {
                for (int c = 0; c<this.cols; c++)
                {
                    if(this.board[r][c].getSymbol().equals("."))
                        return false;
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
        for(int r = 0; r<this.rows; r++)
        {
            Node[] line = this.board[r];
            for(Node x: line)
            {
                ret +=x.getSymbol() + " ";
            }
            ret.trim();
            ret+= "\n";
        }
        return ret.trim();
    }

    public int getRows(){return rows;}
    public int getCols(){return cols;}
    public int getSeas(){return seas;}
    public Node[][] getBoard(){return board;}
}
