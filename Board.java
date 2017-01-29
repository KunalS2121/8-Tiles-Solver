/* ------------------------------------------------
 * 8 Tiles GUI
 *
 * Class: CS 342, Fall 2016
 * System: Windows 10, IntelliJ IDE
 * Author: Five
 * ------------------------------------------------- */
import java.util.*;


/**
 * Underlying Board Structure class which represents the board by a 2D grid.
 * Handles creation of random board and user selected board
 * Handles movement of tiles on the board
 */
public class Board
{
    private int[][] grid; //3x3 grid to store board pieces
    private int hVal; //Heuristic value of board
    private int[] movable; //Pieces able to be moved
    private final int SIZE = Constants.BOARD_SIZE; //Size of board obtained from Constants class


    public Board()
    {
        grid = loadBoard(); //Randomly loaded board;
        hVal = heuristicCalc(); //Calculate goodness of board
        movable = availableMoves();
    }

    public Board(int[] arr)
    {
        grid = loadBoard(arr);
        hVal = heuristicCalc(); //Calculate goodness of board
        movable = availableMoves();
    }


    public int[][] getGrid()
    {
        return grid;
    }
    public void setGrid(int[][] grid)
    {
        this.grid = grid;
    }
    public int gethVal()
    {
        return hVal;
    }
    public int[] getMovable()
    {
        return movable;
    }
    public int getSIZE()
    {
        return SIZE;
    }


    /**
     *  Loads in 3x3 grid with random, unique, value from given size range
     *  @return Randomly loaded grid
     */
    private int[][] loadBoard()
    {
        int[][] retGrid = new int[SIZE][SIZE];
        int[] nums = new int[SIZE*SIZE];
        boolean flag = true;
        int index;

        Random generator = new Random(System.currentTimeMillis());

        //Create number set that we'll be randomly loading into grid
        for(int i=0;i<(SIZE*SIZE);i++)
        {
            nums[i] = i;
        }

        //Loop through grid
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                //Loop until unique number is allocated to spot [i][j]
                while(flag)
                {
                    index = (generator.nextInt(9)); //Generate rand num from size range (0-9)
                    if(nums[index] !=-1) //If number is not 'used'
                    {
                        retGrid[i][j] =  nums[index];
                        nums[index] =-1; //Number is now 'used'
                        flag = false;
                    }
                }
                flag = true;
            }//End for j
        }//End for i

        return retGrid;
    }

    /**
     * Loads in 3x3 grid with user configuration value
     * @return Configured loaded grid
     */
    private int[][] loadBoard(int[] config)
    {
        int[][] retGrid = new int[SIZE][SIZE];

        for(int i=0;i<config.length;i++)
        {
            if(i<3)
            {
                retGrid[0][i] = config[i];
            }
            else if(i>=3 && i<6)
            {
                retGrid[1][i-3] = config[i];
            }
            else
            {
                retGrid[2][i-6] = config[i];
            }
        }

        return retGrid;
    }

    /**
     *  Calculates total heuristic value(goodness) of board
     *  @return Heuristic value of board
     */
    public int heuristicCalc()
    {
        int heuristic =0;
        int currVal;

        int dx;
        int dy;

        for(int x=0;x<3;x++)
        {
            for(int y=0;y<3;y++)
            {
                currVal = grid[x][y];
                switch(currVal)
                {
                    case 0 :
                        dx = Math.abs(x-2);
                        dy = Math.abs(y-2);
                        heuristic += dx+dy;
                        break;
                    case 1:
                        dx = Math.abs(x-0);
                        dy = Math.abs(y-0);
                        heuristic += dx+dy;
                        break;
                    case 2:
                        dx = Math.abs(x-0);
                        dy = Math.abs(y-1);
                        heuristic += dx+dy;
                        break;
                    case 3:
                        dx = Math.abs(x-0);
                        dy = Math.abs(y-2);
                        heuristic += dx+dy;
                        break;
                    case 4:
                        dx = Math.abs(x-1);
                        dy = Math.abs(y-0);
                        heuristic += dx+dy;
                        break;
                    case 5:
                        dx = Math.abs(x-1);
                        dy = Math.abs(y-1);
                        heuristic += dx+dy;
                        break;
                    case 6:
                        dx = Math.abs(x-1);
                        dy = Math.abs(y-2);
                        heuristic += dx+dy;
                        break;
                    case 7:
                        dx = Math.abs(x-2);
                        dy = Math.abs(y-0);
                        heuristic += dx+dy;
                        break;
                    case 8:
                        dx = Math.abs(x-2);
                        dy = Math.abs(y-1);
                        heuristic += dx+dy;
                        break;
                }//End switch
            }
        }

        return heuristic;
    }


    /**
     * Find open piece and identify north,south,east,and west pieces
     * @return array of all possible movable pieces
     */
    public int[] availableMoves()
    {
        int north =- 1; int south = -1;
        int east = -1; int west= -1;

        //Find the 'open' space coordinates
        int[] openSpace = findSpace();
        int x = openSpace[0];
        int y = openSpace[1];

        //North piece
        if(x>0)
        {
            north = grid[x-1][y];
        }
        //South piece
        if(x<(SIZE-1))
        {
            south = grid[x+1][y];
        }
        //East piece
        if(y<(SIZE-1))
        {
            east = grid[x][y+1];
        }
        //West piece
        if(y>0)
        {
            west = grid[x][y-1];
        }

        int[] moves = {north,south,east,west};

        return moves;
    }


    /**
     *  Finds the 'open' space in the board
     *  @return the coordinate of the open space via array containing {x,y}
     */
    private int[] findSpace()
    {
        //Array which will hold coordinate
        int[] coordinate = new int[2];

        //Loop through and look for space
        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                //Return coordinates of space
                if(grid[i][j]==0)
                {
                    coordinate[0]=i;
                    coordinate[1]=j;
                    return coordinate;
                }
            }
        }

        return coordinate; //Should never happen
    }


    /**
     * Checks to see if provided input is a valid move
     * @return True if input is in movable
     */
    public boolean checkMove(int input)
    {
        for(int i = 0; i< movable.length; i++)
        {
            if(movable[i] == input)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Move input to adjacent blank space
     * Update hVal and Moveable list after swapped
     */
    public void move(int input)
    {
        int x0=0;
        int y0=0;
        int xInput=0;
        int yInput=0;

        //Find the coordinates of 0 and provided input
        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                if(grid[i][j]==0) {x0=i; y0=j;}
                if(grid[i][j]==input){xInput=i; yInput=j;}
            }
        }

        //Swap 0 with the provided input
        grid[x0][y0] = grid[xInput][yInput];
        grid[xInput][yInput]=0;

        movable =availableMoves();
        hVal = heuristicCalc();

    }


    /**
     * Print the board
     */
    public void printBoard()
    {

        int count =0;
        int x0=-1;
        int y0=-1;

        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                if(grid[i][j]==0)
                {
                    x0=i;
                    y0=j;
                    System.out.print(" " + " ");
                }
                else
                {
                    System.out.print(grid[i][j]+ " ");
                }
            }
            System.out.println("");
        }
        // System.out.println("SPACE IS AT: {"+x0+","+y0+"}");
    }


    /**
     * Print pieces available to move
     */
    public void printMoveable()
    {
        System.out.print("Piece to move: ");
        for(int i = 0; i< movable.length; i++)
        {
            if(movable[i]!=-1)
            {
                System.out.print(movable[i] + " ");
            }
        }
        System.out.println("");
    }


    /**
     * Print heuristic value of board
     */
    public void printHeuristic()
    {
        System.out.println("Heuristic Value: " + hVal);
    }


    /**
     * Creates a copy of the specified board
     */
    public void copyBoard(Board b)
    {
        grid = copyGrid(b);
        hVal = b.gethVal();
        movable = copyMovable(b);

    }

    public int[] copyMovable(Board b)
    {
        int[] toCopy = b.getMovable();
        int[] newCopy = new int[4];

        for(int i=0;i<4;i++)
        {
            newCopy[i] = toCopy[i];
        }
        return newCopy;
    }

    public int[][] copyGrid(Board b)
    {
        int[][] toCopy = b.grid;
        int [][] newCopy = new int[SIZE][SIZE];

        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                newCopy[i][j] = toCopy[i][j];
            }
        }

        return newCopy;
    }

} //End class
