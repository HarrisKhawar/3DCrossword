// =============================================================================
// IMPORTS

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
// =============================================================================

// =============================================================================
// The class the represents the grid of the crossword puzzle,
// responsible for initializing grid and checking win conditions.
// Contains the instances of the Word class used throught the program.
public class Crossword {
// =============================================================================

    // =============================================================================
    //FIELDS
    
    static Character[][] grid; //the grid that contains the crossword
    static Word[] storedWords; //stores which words have been placed on the grid
    static int placedCounter = 0; //counts how many words have been placed on the grid
    static int hintCounter = 1; //counts what number should be displayed in the hint for each word
    
    //The following fields count how many chars have been placed on the different parts of the grid
    static int topCounter;
    static int leftCounter;
    static int rightCounter;
    static int bottomCounter;
    // =============================================================================

    // =============================================================================
    /**
     * The constructor, constructs instance of Crossword given a list of Words,
     * initializing the grid from the list.
     *
     * @param wordList Array containing the words used to initialize the grid
     *
     **/
    public Crossword(Word[] wordList) {

	grid = new Character[60][60];
	Arrays.sort(wordList);
	initializeGrid(wordList);
    }// Crossword ()
    // =============================================================================

    // =============================================================================
     /**
     * Initializes the grid griven a list of Words
     *
     * @param list Array containing the words used to initialize the grid
     *
     **/
    public static void initializeGrid (Word[] list) {
	//Set the "plus grid" to (char)0
	for(int i = grid.length/3; i < (grid.length/3) * 2; i++) {
	    for(int j = 0; j < grid.length; j++) {
		grid[i][j] = new Character ((char)0);
	    }
	}
	for(int i = 0; i < grid.length; i++) {
	    for(int j = grid.length/3; j < (grid.length/3) * 2; j++) {
		grid[i][j] = new Character ((char)0);
	    }
	}
	
	// Place first word in grid
	storedWords = new Word[list.length];
	Word first = list[0];
	Pair initialCoordinates = new Pair(30, 30);
	placeWord(initialCoordinates, first, true);
	list = removeAtIndex(0, list);
	// Look through list, place largest word with shared letter on grid - efficiency: perhaps store the grid's available letters somewhere in the programs
	int i = 0;
	//Loop is gonna keep looping through the list until there are no more words that can be placed
	while(i < list.length) {
	    Word toAttempt = list[i];
	    boolean placed = attemptToPlace(toAttempt);
	    i++;
	    if (placed) {
		list = removeAtIndex(i - 1, list);
		i = 0;
	    }
	}
    }// initializeGrid ()
    // =============================================================================

    // =============================================================================
     /**
     * Method that attemptsToPlace a word on the grid. Looks at all available positions,
     * chooses the one with the best score.
     *
     * @param toPlace Word to attempt to place in the grid
     *
     * @returns Boolean indicating whether attempt was sucessful or not
     *
     **/
    public static boolean attemptToPlace(Word toPlace) {//this returns a boolean beacuse we need to know if the word was placed or not
	Pair bestPlacePosition = null;
	int score = -1000;
	boolean isVertical = true;
	for(int i = 0; i <= placedCounter - 1; i++) {//This loop runs through the list of already placed words
	    Word temp = storedWords[i];
		if (toPlace.sharesChar(temp)) {
		    //Find coordinates of shared char.
		    Pair sharedCoordinates;
		    Pair toPlaceCoordinates = null;
		    boolean foundCoordinates = false;
		    char[] toPlaceChars = toPlace.getCharArray();
		    char[] tempChars = temp.getCharArray();
		    int tempScore = -1000;
		    for (int k = 0; k < toPlaceChars.length; k++) {//This loop runs through the array of the word you want to place    
			for (int j = 0; j < tempChars.length; j++) { //This loops through the array of the word you are testing against
			    if (toPlaceChars[k] == tempChars[j]) {
				//Get coordinates to begin placing new word depending on whether other word is vertical or horizontal.
				boolean verticality;
				if (temp.getVertical() == true) {//this means the word you are attempting to place will be horizontal
				    verticality = false;
				    sharedCoordinates = new Pair(temp.initialCoordinates.x, temp.initialCoordinates.y + j);
				    toPlaceCoordinates = new Pair(sharedCoordinates.x - k, sharedCoordinates.y);
				}
				else {//this means the word you are attempting to place will be vertical
				    verticality = true;
				    sharedCoordinates = new Pair(temp.initialCoordinates.x + j, temp.initialCoordinates.y);
				    toPlaceCoordinates = new Pair(sharedCoordinates.x, sharedCoordinates.y - k);
				}
				tempScore = getScore(toPlaceCoordinates, toPlace, verticality);
			    }
			    //Keep track of the position with the best score
			    if (tempScore > -1000 && tempScore > score) {
				bestPlacePosition = toPlaceCoordinates;
				isVertical = !temp.getVertical();
				score = tempScore;
			    }
			}
		    }
		}
	    }
	//Place word in best position and return successful attempt
	if(bestPlacePosition != null) {
	    placeWord(bestPlacePosition, toPlace, isVertical);
	    return true;
	}
	//Return failed attempt
	else {
 	    return false;
	}
    }// attemptToPlace ()
    // =============================================================================

    // =============================================================================
     /**
     * Method that places a word on a grid given a pair of coordinates,
     * assumes that placement is legal since this is acconuted for in attemptToPlace().
     * Also handles the setting of each words respective hint number.
     *
     * @param coordinates Position in grid where the Word's first char should be placed
     * @param word Word to be placed on grid
     * @param vertical Boolean indicating whether word is to be placed vertically or not
     *
     **/
    private static void placeWord(Pair coordinates, Word word, boolean vertical) {
	char[] toPlace = word.getCharArray();
	int col = coordinates.x;
	int row = coordinates.y;
	if (vertical) {
	    //Place the word
	    for (int i = 0; i < toPlace.length; i++) {
		if(grid[row + 1][col].charValue() == 0) {
		    incrementCharCounter(new Pair(col, row + i));
		}
		grid[row + i][col] = new Character(toPlace[i]);
	    }
	    word.setVertical();
	    //check if word shares initial position with another placed word
	    boolean sharedPlace = false;
	    for(int i = 0; i < storedWords.length && storedWords[i] != null; i++) {
		//if so, make their hintNums the same
		if(coordinates.x == storedWords[i].initialCoordinates.x && coordinates.y == storedWords[i].initialCoordinates.y) {
		    word.hintNum = storedWords[i].hintNum;
		    sharedPlace = true;
		}
	    }
	    //if starting position is not shared, set hintNum to hintCounter and increment it
	    if(sharedPlace == false) {
		word.hintNum = hintCounter;
		hintCounter++;
	    }
	    word.hint = word.hintNum + ". " + word.hint;
	}
	else {
	    //Place the word
	    for (int i = 0; i < toPlace.length; i++) {
		if(grid[row][col + i].charValue() == 0) {
		    incrementCharCounter(new Pair(col + i, row));
		}
		grid[row][col + i] = new Character(toPlace[i]);
	    }
	    word.setHorizontal();
	    //check if word shares initial position with another placed word
	    boolean sharedPlace = false;
	    for(int i = 0; i < storedWords.length && storedWords[i] != null; i++) {
		//if so, make their hintNums the same
		if(coordinates.x == storedWords[i].initialCoordinates.x && coordinates.y == storedWords[i].initialCoordinates.y) {
		    word.hintNum = storedWords[i].hintNum;
		    sharedPlace = true;
		}
	    }
	    //if starting position is not shared, set hintNum to next number and increment it
	    if(sharedPlace == false) {
		word.hintNum = hintCounter;
		hintCounter++;
	    }
	    word.hint = word.hintNum + ". " + word.hint;
	}
	word.initialCoordinates = coordinates;
	storedWords[placedCounter] = word;
	placedCounter++;
    }//placeWord ()
    // =============================================================================

    // =============================================================================
     /**
     * Method that uses an algorithm to calculate a score for a word,
     * given it is placed in the given position.
     * Scoring parameters: Number of surrounding chars(negative score),
     * number of words crossed(positive score), number of chars already stored in grid section(negative score)
     *
     * @param coordinates Position in grid where the Word's first char would be placed
     * @param word Word that would be placed on grid
     * @param vertical Boolean indicating whether word would be placed vertically or not
     *
     * @returns An integer score for the word. The higher the score is, the more likely it is to be
     *          placed in that position
     *
     **/
    public static int getScore(Pair coordinates, Word word, boolean vertical) {
	int col = coordinates.x;
	int row = coordinates.y;
	int score = 10;
	boolean valid = true;
	char[] toPlace = word.getCharArray();
	int adjacentLetters = 0;
	int crosses = 0;
	int tempPlacedChars = 5; //set to this value to counteract the weighting towards first area expanded to
	if (vertical) {//Word to place is vertical
	    for (int i = 0; i < toPlace.length; i++) {//Looping through the word toGetScore
		//Right now this specific condition will create a bug that if the user enters a list with a lot of the same word they might overwrite each other
		//Invalidate if words exits grid
		if (row + i > grid.length - 1 || row < 0) {
		    valid = false;
		}
		if (!valid) {
		    break;
		}

		//Invalidate if word goes into empty corners
		if(grid[row][col] == null || grid[row + i][col] == null) {
		    valid = false;
		}
		if (!valid) {
		    break;
		}

		//Invalidate if there are letters before starting position
		if(row != 0) {
		    if(grid[row - 1][col] != null && grid[row - 1][col].charValue() != 0) {
			valid = false;
		    }
		}
		
		//Invalidate if the char to be placed does not match placed char
		if (grid[row + i][col].charValue() != 0 && toPlace[i] != grid[row + i][col]) {
		    valid = false;
		}

		//Increment number of crosses if it crosses a word
		if (grid[row + i][col].charValue() == toPlace[i]) {
		    crosses++;
		}

		//Increment number of adjacent letters
		Pair temp = new Pair(col, row + i);
		adjacentLetters += getNumAdjacent(temp);
		if(adjacentLetters > 6) {
		    valid = false;
		}

		//Keep track of number of areas in letters if position is not in middle
		if((temp.x >= grid.length / 3 && temp.x < (grid.length / 3) * 2) && (temp.y >= grid.length / 3 && temp.y < (grid.length / 3) * 2)) {
		    tempPlacedChars = getCharCounter(temp);
		}
	    }
	    //Invalidate if the are letters after end position
	    if(!(row + toPlace.length >= grid.length - 1)) {
		if(grid[row + toPlace.length][col] != null && grid[row + toPlace.length][col].charValue() != 0) {
		    valid = false;
		}
	    }
	}
	else {//Word to place is horizontal
	    for (int i = 0; i < toPlace.length; i++) {
		//Invalidate if word exits grid
		if (col + i > grid[i].length - 1 || col < 0) {
		    valid = false;
		}
		if (!valid) {
		    break;
		}

		//Invalidate if word goes into empty corners
		if(grid[row][col] == null || grid[row][col + i] == null) {//Invalidate if word goes into one of the empty corners
		    valid = false;
		}
		if (!valid) {
		    break;
		}

		//Invalidate if there are letters before starting position
		if(col != 0) {
		    if(grid[row][col - 1] != null && grid[row][col - 1].charValue() != 0) {
			valid = false;
		    }
		}
		//Right now this specific condition will create a bug that if the user enters a list with a lot of the same word they might overwrite each other

		//Invalidate if the char to be placed does not match placed char
		if (grid[row][col + i].charValue() != 0 && toPlace[i] != grid[row][col + i]) {
		    valid = false;
		}

		//Increment number of crosses if it crosses a word
		if (grid[row][col + i].charValue() == toPlace[i]) {
		    crosses++;
		}

		//Increment number of adjacent letters
		Pair temp = new Pair(col + i, row);
		adjacentLetters += getNumAdjacent(temp);
		if(adjacentLetters > 6) {
		    valid = false;
		}
		
		//Keep track of number of areas in letters if position is not in middle
		if((temp.x >= grid.length / 3 && temp.x < (grid.length / 3) * 2) && (temp.y >= grid.length / 3 && temp.y < (grid.length / 3) * 2)) {
		    tempPlacedChars = getCharCounter(temp);
		}
     	    }
	    //Invalidate if the are letters after end position
	    if(!(col + toPlace.length - 1 >= grid.length - 1)) {
		if(grid[row][col + toPlace.length] != null && grid[row][col + toPlace.length].charValue() != 0) {
		    valid = false;
		}
	    }
	}
	if (valid == true) {
	    //Scoring parameters: number of adjacent letters, number of words crossed, number of chars already placed in area
	    score = score - (adjacentLetters * 20) + (100 * crosses) + (100 - 2 * tempPlacedChars);

	    return score;
	}
	else {
	    score = -1000;
	    return score;
	}
    }// getScore()
    // =============================================================================

    // =============================================================================
     /**
     * Method that takes in a position and returns the number of adjacent placed letters.
     * Used in the getScore() method
     *
     * @param postiion Position in grid for which adjacent tiles will be checked
     * 
     * @returns Int representing number of adjacent tiles with letters
     *
     **/
    public static int getNumAdjacent(Pair position) {
	int row = position.y;
	int col = position.x;
	
	int upBound = row - 1;
	int downBound = row + 1;
	int leftBound = col - 1;
	int rightBound = col + 1;

	//Make sure bound do not exit array bounds
	if (row == 0) {
	    upBound = 0;
	}
	else if (row == grid.length - 1) {
	    downBound = grid.length - 1;
	}
	if (col == 0) {
	    leftBound = 0;
	}
	else if (col == grid[0].length - 1) {
	    rightBound = 0;
	}

	//Count adjacent letters
	int counter = 0;
	for (int i = upBound; i <= downBound; i++) {
	    for (int j = leftBound; j <= rightBound; j++) {
		if(grid[i][j] != null && grid[i][j].charValue() != (char)0) {
		    if(!(i == row && j == col)) {
		    counter++;
		    }
		}
	    }
	}
	return counter;
    }// getNumAdjacent ()
    // =============================================================================

    // =============================================================================
     /**
     * Method increments the charCounter of a grid region, given a position in that grid
     *
     * @param position Position in grid for which regional charCounter should be incremented
     *
     **/
    //increments the placed char counter for the different positions in the grid
    //inteded for use in the placeWord() method
    private static void incrementCharCounter(Pair position) {
	int col = position.x;
	int row = position.y;
	if(col < grid.length / 3) {
	    leftCounter++;
	}
	if(col >= (grid.length / 3) * 2) {
	    rightCounter++;
	}
	if(row < grid.length / 3) {
	    topCounter++;
	}
	if(row >= (grid.length / 3) * 2) {
	    bottomCounter++;
	}
    }// incrementCharCounter ()
    // =============================================================================

    // =============================================================================
     /**
     * Method returns the charCounter of a grid region, given a position in that grid
     *
     * @param position Position in grid for which regional charCounter should returned
     *
     * @return int The placed character counter for the region containing position, or 0 if region is middle
     **/
    //returns placedCharCounter for different positions in the grid
    //inteded for use in the getScore() method
    private static int getCharCounter(Pair position) {
	int col = position.x;
	int row = position.y;
	if(col < grid.length / 3) {
	    return leftCounter;
	}
	else if(col >= (grid.length / 3) * 2) {
	    return rightCounter;
	}
	if(row < grid.length / 3) {
	    return topCounter;
	}
	else if(row >= (grid.length / 3) * 2) {
	    return bottomCounter;
	}
	else{
	    return 0;
	}
    }//incrementCharCounter()
    // =============================================================================

    // =============================================================================
     /**
     * Method that returns a String represtnation of all the hints of words stored in the puzzle
     *
     * @returns String that represents the hints of placed words
     *
     **/
    public String getHints() {
	String toReturn = "Hints:";
	String verticals = "\n\nVertical:";
	String horizontals = "\n\nHorizontal:";

	//Loop through stored words and get their hints
	for (int i = 0; i < storedWords.length && storedWords[i] != null; i++) {
	    if(storedWords[i].getVertical()) {//word is vertical
		verticals += "\n" + storedWords[i].hint;
	    }
	    else {//word is horizontal
		horizontals += "\n" + storedWords[i].hint;
	    }
	}
	toReturn += verticals;
	toReturn += horizontals;
	return toReturn;
    }// getHints ()
    // =============================================================================

    // =============================================================================
     /**
     * Method that checks whether the user inputs match each words actual char representation
     *
     * @returns boolean that indicates whether puzzle has been completed or not
     *
     **/
    public boolean checkVictory() {
	boolean toReturn = true;
	for(int i = 0; i < storedWords.length && storedWords[i] != null; i++) {
	    Word temp = storedWords[i];
		for(int j = 0; j < temp.userArray.length; j++) {
		    //If user array is not equal to char array, set return to false
		    if(temp.userArray != null && temp.userArray[j] != temp.getCharArray()[j]) {
			toReturn = false;
		}
	    }
	}
	return toReturn;
    }//checkVictory ()
    // =============================================================================

    // =============================================================================
     /**
     * Helper method for removing an indexed value from a Word[]
     *
     * @param index Int representing index to be removed
     * @param myArray Array which will have value at index removed
     *
     * @returns Word array with value at index removed
     *
     **/
    //Helper method for reomvingAtIndex
    public static Word[] removeAtIndex(int index, Word[] myArray) {
	Word[] toReturn = new Word[myArray.length - 1];
	int returnIndex = 0;
	for (int i = 0; i < myArray.length; i++) {
	    if (i != index) {
		toReturn[returnIndex] = myArray[i];
		returnIndex++;
	    }
	}
	return toReturn;
    }// removeAtIndex ()
    // =============================================================================

    // =============================================================================
     /**
     * Helper method that prints the grid, used for testing purposes
     *
     *
     **/
    public static void printGrid() {
	for (int i = 0; i < grid.length; i++) {
	    for (int j = 0; j < grid.length; j++) {
		if (grid[i][j] == null) {
		    System.out.print("-");
		}
		else if(grid[i][j].charValue() == (char)0)  {
		    System.out.print(" ");
		}
		else System.out.print(grid[i][j]);
	    }
	    System.out.println();
	}
    }// printGrid ()
    // =============================================================================

// ============================================================================= 
}// class Crossword
// =============================================================================
