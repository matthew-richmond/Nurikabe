import java.util.LinkedList;

/**
 * Nurikabe
 * Node to store information needed for Nurikabe Game
 * @author Matthew Richmond on 4/4/2017.
 * @version 1.0
 */
public class Node
{
    private LinkedList<Node> neighbors;
    private String symbol;
    private int row;
    private int col;
    public Node(String symbol,int row, int col)
    {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
        this.neighbors = new LinkedList<>();
    }

    /**
     * adds the current node and the refered node to each others node lists
     * @param neighbor
     */
    public void addneighbor(Node neighbor)
    {
        this.neighbors.add(neighbor);
        neighbor.neighbors.add(this);
    }
    public String getSymbol()
    {
        return this.symbol;
    }
    public LinkedList<Node> getNeighbors(){return this.neighbors;}
    public int[] getCoords()
    {
        int[] coord = {this.row,this.col};
        return(coord);
    }
    public String toString()
    {
        return this.symbol;
    }
}
