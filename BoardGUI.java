/* ------------------------------------------------
 * 8 Tiles GUI
 *
 * Class: CS 342, Fall 2016
 * System: Windows 10, IntelliJ IDE
 * Author: Five
 * ------------------------------------------------- */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.lang.System.exit;


/**
 * Overarching GUI class which contains the start screen with options to start, set-up, or
 * exit the game. Also contains the sliding board GUI.
 */
public class BoardGUI
{
    private JPanel gamePanel; //Panel containing entire game and grid board
    private ButtonPanel boardPanel; //Grid board which is the overlying GUI for the board


    public BoardGUI()
    {
        //Create new panel which will contain the start screen and then the tile board
        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setSize(400,400);
        boardPanel = null;


        //Display the start screen to the user
        displayStartScreen();

        //Create overarching JFrame which will contain the gamePanel
        JFrame gameFrame = new JFrame();
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setSize(400,400);
        gameFrame.add(gamePanel);
        gameFrame.setVisible(true);

    }


    /**
     * Creates a new StartScreen class and adds it to the gamePanel
     */
    private void displayStartScreen()
    {
        StartScreen begin = new StartScreen();
        gamePanel.add(begin, BorderLayout.CENTER);
    }


    /**
     * Removes start screen and replaces it with the tile board
     * Creates boardPanel(tile board) based upon if the user wanted to Start or Set-Up
     * If user is TRUE, then the user can set up the board themselves
     */
    private void setUpGame(boolean user)
    {
        gamePanel.removeAll();

        //Create new ButtonPanel based on user input
        boardPanel = new ButtonPanel(user);

        //Create exit button for the game
        JButton exitGame = new JButton("EXIT");
        exitGame.addActionListener(new ExitHandler());

        //Add tile board and exit button to the gamePanel
        gamePanel.add(boardPanel,BorderLayout.CENTER);
        gamePanel.add(exitGame,BorderLayout.SOUTH);

        SwingUtilities.updateComponentTreeUI(gamePanel);
    }


    /**
     * JPanel class which has Start, Set-Up, and Exit options
     */
    private class StartScreen extends JPanel
    {
        public StartScreen()
        {
            super();
            this.setLayout(new GridLayout(3,1));
            JButton startGame = new JButton("START");
            JButton exitGame = new JButton("EXIT");
            JButton setGame = new JButton ("SET BOARD");

            startGame.addActionListener(new StartHandler());
            setGame.addActionListener(new StartHandler());
            exitGame.addActionListener(new ExitHandler());

            this.add(startGame);
            this.add(setGame);
            this.add(exitGame);
            this.setVisible(true);

        }
    }


    /**
     * ActionListener class which handles Start and Set-Up options
     * Sets up tile board based upon if the user selected Start or Set-Up
     */
    private class StartHandler implements ActionListener
    {
        public void actionPerformed (ActionEvent event)
        {
            String command = event.getActionCommand();
            if(command.equalsIgnoreCase("start"))
            {
                setUpGame(false);
            }
            else
            {
                setUpGame(true);
            }
        }
    }


    /**
     * ActionListener class which handles Exit option
     * Prints exit message and exits game
     */
    private class ExitHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            JOptionPane.showMessageDialog(gamePanel,"Thanks for playing!!");
            exit(0);
        }
    }

} //End BoardGUI
