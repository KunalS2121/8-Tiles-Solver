/* ------------------------------------------------
 * 8 Tiles GUI
 *
 * Class: CS 342, Fall 2016
 * System: Windows 10, IntelliJ IDE
 * Author: Five
 * ------------------------------------------------- */
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import static java.lang.System.exit;


/**
 * GUI class which creates functionality for the tile board based upon the actual underlying board
 * Handles the creation of the tile board as well as movement of tiles
 */
public class ButtonPanel extends JPanel
{
    private Board gameBoard; //Underlying board from which the GUI is based
    private JPanel gridPanel; //3x3 grid of tiles
    private int[] inputArr; //User's input of choosing board
    private int numInput; //Number of input the user has entered
    private int solIterator; //Iterator for the autoSolution

    public ButtonPanel(boolean user)
    {
        //Constructor and set to BorderLayout
        super();
        this.setLayout(new BorderLayout());

        //Construct the grid which will contain the tiles
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(Constants.BOARD_SIZE,Constants.BOARD_SIZE));

        //Add the grid to the overarching JPanel
        this.add(gridPanel,BorderLayout.CENTER);
        gameBoard = null;

        //Create random board or let the user set-up the board
        if(!user)
        {
            initRandBoard();
        }
        else
        {
            numInput = 0;
            inputArr = new int[Constants.BOARD_SIZE*Constants.BOARD_SIZE];
            initUserBoard();
        }

    }


    /**
     * Returns the underlying Board structure
     */
    public Board getGameBoard() {
        return gameBoard;
    }


    /**
     * Create randomly generated board and corresponding grid GUI
     */
    private void initRandBoard()
    {
        gameBoard = new Board();
        JButton tile;

        //Loop through board grid and create corresponding buttons
        int[][] tempGrid = gameBoard.getGrid();
        for(int i=0;i<Constants.BOARD_SIZE;i++)
        {
            for(int j=0;j<Constants.BOARD_SIZE;j++)
            {
                int tileNum = tempGrid[i][j];
                if(tileNum!=0)
                {
                   tile = new JButton(Integer.toString(tileNum));
                }
                else
                {
                    tile = new JButton(" ");
                }

                tile.addActionListener(new MoveHandler());
                gridPanel.add(tile);
            }
        }

        //Add options of the Solve and displaying Hueristic Value
        addGameOptions();
    }


    /**
     * Create user initilized board and corresponding grid GUI
     */
    private void initUserBoard()
    {
        //Add nine blank buttons to the grid GUI
        for(int i=0;i<(Constants.BOARD_SIZE*Constants.BOARD_SIZE);i++)
        {
            JButton tile = new JButton("Click Me [" + i + "]");
            tile.addActionListener(new CreationHandler());
            gridPanel.add(tile);
        }
    }


    /**
     * Removes all and Redraws the grid GUI based upon lastest
     * update of the underlying game board
     * @param user : Whether or not there is user input (should happen at most once)
     */
    private void updatePanel(boolean user)
    {
        gridPanel.removeAll();
        this.removeAll();

        //Create a new board based upon user input if flag is set
        if(user)
        {
            gameBoard = new Board(inputArr);
        }

        //Goto grid and create corresponding buttons
        int[][] tempGrid = gameBoard.getGrid();
        JButton tile;

        for(int i=0;i<Constants.BOARD_SIZE;i++)
        {
            for(int j=0;j<Constants.BOARD_SIZE;j++)
            {
                int tileNum = tempGrid[i][j];
                if(tileNum!=0)
                {
                    tile = new JButton(Integer.toString(tileNum)); //Create corresponding JButton
                }
                else
                {
                    tile = new JButton(" "); //Since tileNum is zero, make the button blank
                }
                tile.addActionListener(new MoveHandler()); //Set the action listener to be the move handler
                gridPanel.add(tile);
            }
        }

        gridPanel.revalidate();
        this.add(gridPanel,BorderLayout.CENTER);
        addGameOptions();
        this.revalidate();
    }


    /**
     * Add an auto solve option and a display for the Heuristic Value of the current board
     */
    private void addGameOptions()
    {

        //Create Solve Button and Heuristic Label
        JButton solveGame = new JButton("SOLVE!!");
        String score = Integer.toString(gameBoard.gethVal());
        JLabel hValDisplay = new JLabel("Board Score: " + score);

        solveGame.addActionListener(new SolutionHandler());

        //Add to the JPanel and Refresh the Panel
        this.add(solveGame,BorderLayout.NORTH);
        this.add(hValDisplay,BorderLayout.SOUTH);
        this.revalidate();
    }


    /**
     * Update the user input array based upon the button that is chosen
     */
    private void userInputUpdate(String chosen)
    {
        for(int i=0;i<9;i++)
        {
            if(chosen.contains(Integer.toString(i)))
            {
                inputArr[i] = numInput;
                numInput++;
            }
        }
    }


    /**
     * ActionListener class which handles the auto solution of the board
     */
    private class SolutionHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            //Create SearchTree and auto solve
            SearchTree solution = new SearchTree();

            //Auto solve and get the list of taken steps
            ArrayList<Board> solvedList = solution.getSolvedBoards();
            boolean isSolved = solution.autoSolve(gameBoard);
            solIterator = 0;

            //Timer used to update GUI every 300ms
            final Timer timer = new Timer(300, null);

            //ActionListener for timer used to update board GUI every 300ms
            ActionListener listener = new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e) {

                    //Get a board from the list and display it
                    if (solIterator < solvedList.size()) {
                        gameBoard = solvedList.get(solIterator);
                        updatePanel(false);
                        solIterator++;

                    }
                    //If iterated through entire list...
                    else
                    {
                        //Stop the timer
                        timer.stop();

                        //If solved, display puzzle has been solved
                        if(isSolved)
                        {
                            JOptionPane.showMessageDialog(ButtonPanel.this, "Puzzle has been solved!");
                        }
                        //If not solved, display best board that was found
                        else
                        {
                            JOptionPane.showMessageDialog(ButtonPanel.this, "All solutions have been tried. There is no solution. " +
                                    "Here is the best board!");
                            gameBoard =solution.getBestBoard();
                            updatePanel(false);
                        }
                    }
                }
            }; //End ActionListener

            //Start the timer
            timer.addActionListener(listener);
            timer.start();
        }
    }


    /**
     * ActionListener class which handles user set-up of board
     */
    private class CreationHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            //If chosen tile has not been assigned, assign it to a number
            String chosenButton = event.getActionCommand();
            if(chosenButton.contains("Click Me"))
            {
                JButton temp = (JButton)event.getSource();
                temp.setText(Integer.toString(numInput));
                userInputUpdate(chosenButton);
            }
            //If user has picked all tiles, create new board based upon the input and display it
            if(numInput == 9)
            {
                updatePanel(true);
            }
            //If user has already assigned the clicked tile, display error message
            if(!(chosenButton.contains("Click Me")))
            {
                JOptionPane.showMessageDialog(ButtonPanel.this, event.getActionCommand() + " Has Already Been Set");
            }
        }
    }


    /**
     * ActionListener class which handles movement of a tile on the grid
     */
    private class MoveHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            //Cannot move blank tile
            if(event.getActionCommand().equals(" "))
            {
                JOptionPane.showMessageDialog(ButtonPanel.this, event.getActionCommand() + " Is Not A Valid Move");
            }
            else
            {
                //Obtain tile chosen and check if valid
                int toMove = Integer.parseInt(event.getActionCommand());
                //If valid move...
                if(gameBoard.checkMove(toMove))
                {
                    //Move on underlying board structure
                    gameBoard.move(toMove);
                    System.out.println("");
                    gameBoard.printBoard();
                    gameBoard.printHeuristic();

                    //Update GUI respectivley
                    ButtonPanel.this.updatePanel(false);

                    //If hVal after move is zero, display game won message
                    if(gameBoard.gethVal()==0)
                    {
                        JOptionPane.showMessageDialog(ButtonPanel.this, "YOU'VE SOLVED THE PUZZLE!");
                        //exit(0);
                    }
                }
                //Not a valid move, display error message
                else
                {
                    JOptionPane.showMessageDialog(ButtonPanel.this, event.getActionCommand() + " Is Not A Valid Move");
                }
            }
        }
    }



} //End ButtonPanel
