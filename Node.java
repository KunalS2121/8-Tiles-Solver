
/* ------------------------------------------------
 * 8 Tiles GUI
 *
 * Class: CS 342, Fall 2016
 * System: Windows 10, IntelliJ IDE
 * Author: Five
 * ------------------------------------------------- */
import java.util.ArrayList;


/**
 * Node class which encapsulates the Board with extra information used for SearchTree algorithm
 * Includes an unique key for each unique board used for hashing
 */
class Node implements Comparable<Node>
{

    private Board board;//Game Board state
    private int goodness; //Heuristic value of Game Board
    private ArrayList<Node> children; //Game Board states for possible moves
    private String boardKey; //The board converted to a string
    private final int SIZE = Constants.BOARD_SIZE; //Size obtained from Constants class


    public Node(Board b)
    {
        board = b;
        goodness = b.gethVal();
        children = new ArrayList<>();
        boardKey = boardToString(b);
    }


    public Board getBoard()
    {
        return board;
    }
    public int getGoodness()
    {
        return goodness;
    }
    public void setGoodness(int goodness)
    {
        this.goodness = goodness;
    }
    public ArrayList<Node> getChildren()
    {
        return children;
    }
    public String getBoardKey()
    {
        return boardKey;
    }


    /**
     * Add a child node to the children list
     */
    public void addChild(Node child)
    {
        children.add(child);
    }


    /**
     * Implement compare to for lowest goodness value
     */
    public int compareTo(Node o)
    {
        if(this.goodness < o.goodness)
        {
            return -1;
        }
        else if(this.goodness > o.goodness)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }


    /**
     * Converts the board grid into a string
     */
    private String boardToString(Board b)
    {
        int[][] grid = b.getGrid();
        String s="";

        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                s+=grid[i][j];
            }
        }
        return s;
    }




}
