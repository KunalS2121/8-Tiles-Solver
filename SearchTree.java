/* ------------------------------------------------
 * 8 Tiles GUI
 *
 * Class: CS 342, Fall 2016
 * System: Windows 10, IntelliJ IDE
 * Author: Five
 * ------------------------------------------------- */
import java.util.*;


/**
 * SearchTree class which implements an A* like algorithm in order to automatically
 * solve any given board or find best possible solution to an impossible board.
 */
public class SearchTree
{
    private HashMap uniqueList; // <String,Node> Hash mapping of unique (already seen) nodes
    private PriorityQueue<Node> leafList; //Min Heap of leaf nodes (children)
    private ArrayList<Board> solvedBoards; //List of all solved boards.
    private PriorityQueue<Node> bestBoard; //Min heap of best board based upon hueristic value

    public SearchTree()
    {
        uniqueList = new HashMap();
        leafList = new PriorityQueue<>();
        solvedBoards = new ArrayList<>();
        bestBoard = new PriorityQueue<>();
    }


    public ArrayList<Board> getSolvedBoards()
    {
        return solvedBoards;
    }

    /**
     * Deploys A* like algorithim in order to solve the puzzle
     * Solves puzzle or displays best found solution
     * Returns false is unsolvable and true if solvable
     */
    public boolean autoSolve(Board startBoard)
    {
        System.out.println("");
        System.out.println("Auto Solution Engaged! (:");
        System.out.println("");

        //Add starting board to leaf and unique list
        Node start = new Node(startBoard);
        addLeaf(start);
        addToUnique(start);
        int count=1;


        PriorityQueue<Node> bestBoard = new PriorityQueue<>();

        //Loop until we solve puzzle
        while(leafList.peek()!=null && leafList.peek().getGoodness()!=0)
        {
            //Obtain board off of mean heap
            Node currNode = getMinLeaf();
            Board currBoard = currNode.getBoard();

            //Process board's possible moves
            processChildren(currNode);
            bestBoard.add(currNode);
            solvedBoards.add(currBoard);
            count++;
        }
        if(leafList.peek()!=null)
        {
            for(int i=0;i<solvedBoards.size();i++)
            {
                System.out.println("");
                System.out.println(i + ".");
                Board tmp = solvedBoards.get(i);
                tmp.printBoard();
                tmp.printHeuristic();
            }
            Board finalBoard = leafList.peek().getBoard();
            solvedBoards.add(finalBoard);
            System.out.println("");
            System.out.println(count +". ");
            finalBoard.printBoard();
            finalBoard.printHeuristic();
            return true;

        }
        else if(leafList.peek()==null)
        {
            System.out.println("All " + count + " puzzles have been tried");
            System.out.println("That puzzle is impossible to solve. Best board was: ");
            Node tmp = bestBoard.poll();
            Board finalBoard = tmp.getBoard();
            finalBoard.printBoard();
            finalBoard.printHeuristic();
            return false;
        }
        return true;
    }


    /**
     * Takes board state and takes all its possible moves and stores each new state
     * Will STORE NEW STATE ONLY IF IT IS UNIQUE. so all states in leafList are unique
     * @return original board state with children nodes added to it
     */
    private void processChildren(Node parent)
    {
        Board board = parent.getBoard();
        int[] possibleMoves= board.getMovable();

        //Make each possible move and store into parent's children list
        for(int i=0;i<possibleMoves.length;i++)
        {
            int input = possibleMoves[i];
            if(input !=-1)
            {
                Board tmp = new Board();
                tmp.copyBoard(board);

                tmp.move(input);

                Node child = new Node(tmp);
                // boolean check = inUnique(child);
                if(!inUnique(child))
                {
                    parent.addChild(child);
                    addToUnique(child);
                    addLeaf(child);
                }
            }
        }
    }

    /**
     * Checks if key (int[][] grid) is already mapped within the hmap
     * @return True if it is, else
     */
    public boolean inUnique(Node node)
    {
        String key = node.getBoardKey();
        return uniqueList.containsKey(key);

    }


    /**
     * Adds a Node to the unique list of nodes
     */
    public void addToUnique(Node node)
    {
        String key = node.getBoardKey();
        uniqueList.put(key,node);

    }


    /**
     * Adds a child node to the min heap of child nodes
     */
    public void addLeaf(Node node)
    {
        leafList.add(node);
    }


    /**
     * Removes and returns the head of the child list
     */
    public Node getMinLeaf()
    {
        return leafList.poll();
    }

    public Node seeMinLeaf()
    {
        return leafList.peek();
    }


    /**
     * Returns best board from the min queue of boards
     */
    public Board getBestBoard() {
        return bestBoard.poll().getBoard();
    }


}
