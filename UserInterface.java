// =============================================================================
// IMPORTS

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
// =============================================================================

// =============================================================================
// This class provides the main method that is responsible for ultimately running the program.
// This class also handles the user interface using java.awt and java.swing functionality.
// Finally, this class handles file input for the list of words.
public class UserInterface extends JFrame{
// =============================================================================

    // =============================================================================
    // FIELDS

    //The following felds are JComponent-related declarations
    private JMenuBar menuBar;
    private JMenu file, help, instructions;
    private JMenuItem close, view, about, showInstructions, completePuzzle;
    private JTextArea hints;
    private JPanel display;
    private JButton down, up, right, left, editWord, victoryCheck;
    private JComboBox<String> directionSelect;
    private JComboBox<Integer> numberSelect;
    private int WIDTH = 920, HEIGHT = 640;

    private static Word[] wordList; //The list of words parsed from the inputted File
    private static Crossword puzzle; //The instance of Crossword that holds the puzzle grid and Words
    private static Character[][] userGrid; //Copy of puzzle grid, but with characters that user has inputted
    private static Word currentWord; //Word currently selected by user for editing

    //The following fields store the bounds used to display the different grid sections
    private static LinkedBounds initialBounds; //boundaries of first grid section displayed
    private static LinkedBounds currentBounds; //boundaries of current grid section being displayed
    
    // =============================================================================
     /**
     * The main method. Reads word list from input file and initialzes all necessary
     * aspects of the program, before displaying the user interface.
     *
     * @param args Arguments entered in the command line
     *
     **/
    public static void main(String[] args) {
	//Handle the file input
	ArrayList<Word> list = new ArrayList<Word>();
	
	Scanner s = new Scanner(System.in);
	s = s.useDelimiter("\n");
	while (s.hasNext()) {
	    try {
		Scanner line = new Scanner(s.next());
		line = line.useDelimiter(" : ");
		Word toAppend = new Word(line.next());
		try {
		    toAppend.hint = line.next();
		    list.add(toAppend);
		} catch (NoSuchElementException e) {
		    System.err.println("ERROR: Could not find correct elements in file. Please remember to input a text file in format specified on the README!");
		    System.exit(1);
		}
	    } catch (NoSuchElementException e) {
		System.err.println("ERROR: Could not find correct elements in file. Please remember to input a text file in format specified on the README!");
		System.exit(1);
	    }
	}
	Word[] temp = new Word[0];
	wordList = list.toArray(temp);
	
	//create instance of crossword puzzle
	puzzle = new Crossword(wordList);
	
	//initialize display boundaries
	initializeBounds();
	
	//initialize userGrid
	userGrid = new Character[puzzle.grid.length][puzzle.grid.length];
	for(int i = 0; i < userGrid.length; i++) {
	    for(int j = 0; j < userGrid[i].length; j++) {
		userGrid[i][j] = new Character((char)0);
	    }
	}

	//initialize userInterface
	new UserInterface();
    }// main ()
    // =============================================================================
    
    // =============================================================================
     /**
     * The constructor, calls all methods for initializing the JComponent displays.
     *
     **/
    public UserInterface() {
        super("3D Crossword");
        sendMenuBar();
        sendDisplay();
        sendButtons();
	sendWordSelection();
        sendUI(this);
    }// UserInterface ()
    // =============================================================================
    
    // =============================================================================
     /**
     * Method responsible for handling the JComboBoxes used for selecting words.
     * Links the options in the second box to be dependent on the selection of first box.
     *
     **/
    private void sendWordSelection() {
	String[] selections = new String[]{"Vertical", "Horizontal"};
	directionSelect = new JComboBox<String>(selections);
	directionSelect.setBounds(605, 505, 100, 100);
	directionSelect.setSelectedItem("Vertical");
	
	numberSelect = new JComboBox<Integer>();
	numberSelect.setBounds(705, 505, 75, 100);
	
	//Have second combo box depend on the selection for first combo box
	directionSelect.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
		    ArrayList<Integer> tempInts = new ArrayList<Integer>(1);
		    for(int i = 0; i < puzzle.storedWords.length && puzzle.storedWords[i] != null; i++) {
			Word temp = puzzle.storedWords[i];
			//If selection is vertical get hint numbers of vertical words, otherwise if horizontal get hint nums of horizontal words
			if(directionSelect.getSelectedItem().equals("Vertical")) {
			    if(temp.getVertical() == true) {
				tempInts.add(temp.hintNum);
			    }
			}
			else if(directionSelect.getSelectedItem().equals("Horizontal")) {
			    if(temp.getVertical() == false) {
				tempInts.add(temp.hintNum);
			    }
			}
		    }
		    Integer[] ints = new Integer[0];
		    ints = tempInts.toArray(ints);
		    numberSelect.removeAllItems();
		    for(int i = 0; i < ints.length; i++) {
			numberSelect.addItem(ints[i]);
		    }
		}
	    });

	//Lets user select word direction
	add(directionSelect);
	//Lets user select word hint number, given a direction
	add(numberSelect);
    }// sendWordSelection ()
    // =============================================================================

    // =============================================================================
     /**
     * Method responsible for sending the programs menu bar. Handles information about
     * program, use instructions, closing, and has an option to auto-complete puzzle,
     * for quicker testing.
     **/
    private void sendMenuBar(){

        menuBar = new JMenuBar();
        file = new JMenu(" File ");
        close = new JMenuItem("Close");
        help = new JMenu(" Help ");
	instructions = new JMenu(" Instructions ");
	showInstructions = new JMenuItem("Show Instructions");
	completePuzzle = new JMenuItem("Complete The Puzzle For Me");

        view = new JMenuItem("View Source Code");
        about = new JMenuItem("About 3D Crossword");

        setJMenuBar(menuBar);
        menuBar.add(file);
        menuBar.add(help);
	menuBar.add(instructions);

	//Set behavior to close program on click
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

	//Alloes user to look at the source code!
        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "View What?",
                        "You Silly Lemur", JOptionPane.OK_OPTION);
            }
        });

	//Display information about the program
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "This is a project for CS112.\nCreated by Internazionale.",
                        "About 3D Crossword", JOptionPane.OK_OPTION);
            }
        });

	//Displays instruction on how to use program
	showInstructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "Instructions: \nSwitch through the different faces of the puzzle by using the arrow buttons! \nUse the selections in the bottom right to select a word, and press the button to edit it! \nIf you think you have completed the puzzle, press the button on the top right.\nIf you get stuck (or want to test quicker), check File in the menu bar. ;)",
                        "Instructions", JOptionPane.OK_OPTION);
            }
        });

	//Auto-completes the puzzle, for convenience's sake
	completePuzzle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                userGrid = Arrays.copyOf(puzzle.grid, puzzle.grid.length);
		for(int i = 0; i < puzzle.storedWords.length && puzzle.storedWords[i] != null; i++) {
		    puzzle.storedWords[i].userArray = Arrays.copyOf(puzzle.storedWords[i].getCharArray(), puzzle.storedWords[i].getCharArray().length);
		}
		display.repaint();
            }
        });

	//adds all elements to the UI
        file.add(close);
	file.add(completePuzzle);
        help.add(view);
        help.add(about);
	instructions.add(showInstructions);
    } // sendMenuBar ()
    // =============================================================================

    // =============================================================================
    /**
     * Method responsible for handling buttons. Handles the switching of sections diplayed
     * when arrow buttons are pressed. Handles the user input of guessed words. Handles
     * checking if the puzzle is complete.
     **/
    private void sendButtons() {

	//Switches perspective to left
        left = new JButton(" < ");
        left.setBounds(10, 245, 50, 100);
        left.setFont(new Font("SansSerif", Font.PLAIN, 25));
        left.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
		if(currentBounds.leftLink != null) {
		    currentBounds = currentBounds.leftLink;
		    display.repaint();
		}
            }
        });
        add(left);

	//Switches perspective to right
        right = new JButton(" > ");
        right.setBounds(555, 245, 50, 100);
        right.setFont(new Font("SansSerif", Font.PLAIN, 25));
        right.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
		if(currentBounds.rightLink != null) {
		    currentBounds = currentBounds.rightLink;
		    display.repaint();
		}
            }
        });
        add(right);

	//Switches perspective to top
        up = new JButton(" ^ ");
        up.setBounds(260, 5, 100, 50);
        up.setFont(new Font("SansSerif", Font.PLAIN, 25));
        up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
		if(currentBounds.topLink != null) {
		    currentBounds = currentBounds.topLink;
		    display.repaint();
		}
            }
        });
        add(up);

	//Switches perspective to bottom
        down = new JButton(" v ");
        down.setBounds(260, 525, 100, 50);
        down.setFont(new Font("SansSerif", Font.PLAIN, 25));
        down.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
		if(currentBounds.bottomLink != null) {     
		    currentBounds = currentBounds.bottomLink;
		    display.repaint();
		}
            }
        });
        add(down);

	//Allows user to edit a selected word
	editWord = new JButton("Edit Word");
	editWord.setBounds(785, 530, 100, 50);
	editWord.setFont(new Font("SansSerif", Font.PLAIN, 15));
	editWord.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent actionEvent) {
		//Tells user to select a word if no word is selected
		if(directionSelect.getSelectedItem() == null || numberSelect.getSelectedItem() == null) {
		    JOptionPane.showMessageDialog(display, "Please select the word you want to edit in the drop down menus to the left.", "Whoops!", JOptionPane.OK_OPTION);
		}
		else {
		    Word[] toCheck = puzzle.storedWords;
		    Word toEdit = null;
		    
		    //Find the selected word
		    for(int i = 0; i < toCheck.length && toCheck[i] != null; i++) {
			if(directionSelect.getSelectedItem() == "Vertical") {
			    if(toCheck[i].getVertical() == true && toCheck[i].hintNum == numberSelect.getItemAt(numberSelect.getSelectedIndex()).intValue()) {
				toEdit = toCheck[i];
			    }
			}
			else {
			    if(toCheck[i].getVertical() == false && toCheck[i].hintNum == numberSelect.getItemAt(numberSelect.getSelectedIndex()).intValue()) {
				toEdit = toCheck[i];
			    }
			}
		    }

		    //Handle user input erroes
		    String entry = JOptionPane.showInputDialog("Please input your guess:");
		    if (entry != null) {
			entry = entry.toLowerCase();
			char[] temp = entry.toCharArray();
			while (temp.length != toEdit.getCharArray().length) {
			    JOptionPane.showMessageDialog(display, "Please enter a word of the correct length: " + toEdit.getCharArray().length + ".", "Whoops!", JOptionPane.OK_OPTION);
			    entry = JOptionPane.showInputDialog("Please input your guess:");
			    if (entry != null) {
				entry = entry.toLowerCase();
				temp = entry.toCharArray();
			    }
			}
			if (entry != null) {
			    //Store what user has inputted for specific word
			    toEdit.userArray = temp;
			    int tempX = toEdit.initialCoordinates.x;
			    int tempY = toEdit.initialCoordinates.y;
			    
			    //Store user entry onto user grid, for printing
			    for(int i = 0; i < toEdit.userArray.length; i++) {
				if(toEdit.getVertical() == true) {
				    userGrid[tempY + i][tempX] = toEdit.userArray[i];
				}
				else {
				    userGrid[tempY][tempX + i] = toEdit.userArray[i];
				}
			    }
			}
		    }
		}
	        display.repaint();
	    }
	});
      	add(editWord);

	//Handles checking if puzzle is complete
	victoryCheck = new JButton("I Think I Won!");
	victoryCheck.setBounds(635, 10, 250, 50);
        victoryCheck.setFont(new Font("SansSerif", Font.PLAIN, 15));
        victoryCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
		//if user has won, inform them
		if(puzzle.checkVictory()) {
		    JOptionPane.showMessageDialog(display, "Coungratulations, you have completed the puzzle! Exit the program and input a different list of words to play again, or continue editing this puzzle.", "Victory!", JOptionPane.OK_OPTION);
		}
		else {
		    JOptionPane.showMessageDialog(display, "The puzzle is not yet complete. Keep on trying!", "Nope!", JOptionPane.OK_OPTION);
		}
            }
        });

	add(victoryCheck);
    }// sendButtons ()
    // =============================================================================

    
    // =============================================================================
     /**
     * Method responsible for handling hint and grid display, represented by a JTextArea
     * and a JFrame respectively. Also creates the frame in which graphics are handled.
     **/
    private void sendDisplay(){
	//create grid display
	display = new graphicsFrame();
	display.setBounds(85, 65, 450, 450);
	
        add(display);

	//create hint display
        hints = new JTextArea("HINTS");
        hints.setMargin(new Insets(0, 10, 0, 0));
        hints.setEditable(false);
        hints.setFont(new Font("Verdana", Font.PLAIN, 10));
	hints.append("\nTest");
	hints.setText(puzzle.getHints());
	hints.setLineWrap(true);
	//make it panable
	JScrollPane pane = new JScrollPane(hints);
	pane.setBounds(635, 65, 250, 450);

	add(pane);

    } // sendDisplay ()
    // =============================================================================

    // =============================================================================
     /**
     * Method responsible for sending the UI Frame, going through the java.awt formalities.
     * Responsible for final size and look of the program.
     **/
    private void sendUI(UserInterface app){
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(WIDTH, HEIGHT);
        app.setResizable(false);
        app.setLayout(null);
        app.setBackground(Color.GRAY);
        app.setLocationRelativeTo(null);
        app.setVisible(true);
    } // sendUI ()
    // =============================================================================

    // =============================================================================
     /**
     * Method that initializes the programs LinkedBounds from its grid.
     * These are the boundaries used to display different sections of the grid,
     * each corresponding to a "face" of the "cube".
     **/
    private static void initializeBounds() {
	int size = puzzle.grid.length / 3;
	
	//create each face using the grid
	LinkedBounds middleBounds = new LinkedBounds(size, size);
	LinkedBounds topBounds = new LinkedBounds(size, 0);
	LinkedBounds bottomBounds = new LinkedBounds(size, size * 2);
	LinkedBounds leftBounds = new LinkedBounds(0, size);
	LinkedBounds rightBounds = new LinkedBounds(size * 2, size);

	//set up links between the faces
	middleBounds.setLinks(leftBounds, rightBounds, topBounds, bottomBounds);
	topBounds.setLinks(leftBounds, rightBounds, null, middleBounds);
	bottomBounds.setLinks(leftBounds, rightBounds, middleBounds, null);
	leftBounds.setLinks(null, middleBounds, topBounds, bottomBounds);
	rightBounds.setLinks(middleBounds, null, topBounds, bottomBounds);

	initialBounds = middleBounds;
	currentBounds = middleBounds;
    } // initializeBounds ()
    // =============================================================================

    // =============================================================================
    /**
     * Method that checks whether a Word's initial coordinates are included in the current
     * display bounds.
     *
     * @param toCheck Word whose initial coordinates will be checked
     *
     * @returns Boolean indicating whether word is in bounds or not
     *
     **/
    private static boolean startsInBounds(Word toCheck) {
	int x = toCheck.initialCoordinates.x;
	int y = toCheck.initialCoordinates.y;
	if(x >= currentBounds.minX && x < currentBounds.minX + (puzzle.grid.length / 3)) {
	    if(y >= currentBounds.minY && y < currentBounds.minY + (puzzle.grid.length / 3)) {
		return true;
	    }
	}
	return false;
    }// startsInBounds ()
    // =============================================================================

    // =============================================================================
    //internal class that provides a custom component for the graphical display
    class graphicsFrame extends JPanel {
    // =============================================================================
	
	// =============================================================================
	// FIELDS
	public final int width; //graphical display width
	public final int height; //graphical display height
	// =============================================================================

	// =============================================================================
	/**
	 * Constructor. Sets the prefferedSize of display, and its width and height.
	 *
	 **/
	public graphicsFrame() {
	    super.setPreferredSize(new Dimension(365, 385));
	    width = 450;
	    height = 450;
	} // graphicsFrame ()
	// =============================================================================
	
	// =============================================================================
	/**
	 * paintComponent method, responsible for painting the graphics onto the graphical component.
	 * This handles the painting of grid squares, hint number positions, and the user-inputted words.
	 *
	 **/
	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.setColor(Color.WHITE);
	    g.fillRect(0, 0, width, height);

	    g.setColor(Color.BLACK);
	    g.setFont(new Font("Verdana", Font.PLAIN, 10));

	    	//divide area into squares queal to grid, then run through grid. If there is a char on a position, draw a square and that char on the equivalent position in that area.
	    int tempX = currentBounds.minX;
	    int tempY = currentBounds.minY;
	    int displaySize = puzzle.grid.length/3;
	    int rowCounter = 0;
	    
	    //paints the grid squares
	    for(int i = tempY; i < tempY + displaySize; i++) {
		int colCounter = 0;
	    	for(int j = tempX; j < tempX + displaySize; j++) {
		    if(puzzle.grid[i][j].charValue() != 0) {
			g.drawRect((width/displaySize) * colCounter, (height/displaySize) * rowCounter, width/displaySize, height/displaySize);
		    }
		    colCounter++;
		}
		rowCounter++;
	    }
	    
	    //places the hint numbers
	    Word[] toCheck = puzzle.storedWords;
	    for(int i = 0; i < toCheck.length && toCheck[i] != null; i++) {
		if(startsInBounds(toCheck[i])) {
		    //Place number on grid position
		    int x = toCheck[i].initialCoordinates.x;
		    int y = toCheck[i].initialCoordinates.y;
		    g.drawString("" + toCheck[i].hintNum, (width/displaySize) * (x - currentBounds.minX), (width/displaySize) * (y - currentBounds.minY) + 10);
		}
	    }

	    //places guessed characters
	    g.setFont(new Font("Verdana", Font.PLAIN, 13));
	    rowCounter = 0;
	    for(int i = tempY; i < tempY + displaySize; i++) {
		int colCounter = 0;
	    	for(int j = tempX; j < tempX + displaySize; j++) {
		    if(userGrid[i][j].charValue() != 0) {
			g.drawString("" + userGrid[i][j].charValue(), (width/displaySize) * (colCounter) + 6, (width/displaySize) * (rowCounter) + 18);
		    }
		    colCounter++;
		}
		rowCounter++;
	    }
	}// paintComponent ()
	// =============================================================================
	
    // =============================================================================
    }// class graphicsFrame
    // =============================================================================
    
// =============================================================================
}// class UserInterface
// =============================================================================
