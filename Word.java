// =============================================================================
// Class that represents the words used in the crossword puzzle,
// including hints and user inputs
// implements Comparable<word> for ease of sorting using java.util.Arrays
public class Word implements Comparable<Word> {
// =============================================================================

    // =============================================================================
    // FIELDS
    
    private char[] wordChars; //the actual char[] representation of this word
    public char[] userArray; //the char[] that stores the user input for this word
    private boolean vertical; //stores whether the word is positioned vertically within the grid or not
    String hint; //the hint associated with this word
    int hintNum; //the numbering of the hint associated with this word
    Pair initialCoordinates; //the coordinates of the first letter of this word on the grid
    // =============================================================================  

    // =============================================================================
     /**
     * The constructor, constructs instance of a word given its string representation
     *
     * @param toStore the String that represents the word represented by this object
     *
     **/
    public Word(String toStore) {
	toStore = toStore.toLowerCase();
	wordChars = toStore.toCharArray();
	userArray = new char[wordChars.length];
    }// Word ()
    // =============================================================================

    // =============================================================================
    public boolean checkMatch() {
     /**
     * Checks if the contents of userArray and wordChars are equal,
     * intended for use in checking completion of puzzle
     *
     * @returns A boolean indicating whether contents of arrays match or not
     *
     **/
	for (int i = 0; i < userArray.length; i++) {
	    if (userArray[i] != wordChars[i]) {
		return false;
	    }
	}
	return true;
    } // checkMatch ()
    // =============================================================================

    // =============================================================================
    public char[] getCharArray() {
     /**
     * Getter method for charArray
     *
     * @returns This instance's charArray
     *
     **/
	return wordChars; 
    } // getCharArray ()
    // =============================================================================

    // =============================================================================
    public boolean getVertical() {
     /**
     * The getter method for the words verticality
     *
     * @returns boolean indicating whether word is vertical or not
     *
     **/
	return vertical;
    } // getVertical ()
    // =============================================================================

    // =============================================================================
    public void setVertical() {
     /**
     * The setter method for making a word vertical
     *
     **/
	vertical = true;
    } // setVertical ()
    // =============================================================================

    // =============================================================================
    public void setHorizontal() {
     /**
     * The setter method for making a word horizontal
     *
     **/
	vertical = false;
    } // setHorizontal ()
    // =============================================================================

    // =============================================================================
     /**
     * Method that indicates whether two words share a char or not
     *
     * @param toCompare Word that will be compared to this specific instance
     * @returns Boolean indicating whether words share a char or not
     *
     **/
    //methods that checks and return boolean saying whether two words share a char
    public boolean sharesChar(Word toCompare) {
	for (int i = 0; i < this.wordChars.length; i++) {
	    for (int j = 0; j < toCompare.wordChars.length; j++) {
		if (this.wordChars[i] == toCompare.wordChars[j]) {
		    return true;
		}
	    }
	}
	return false;
    } // sharesChar ()
    // =============================================================================

    // =============================================================================
     /**
     * Implemented method to satisfy the Comparable interface
     *
     * @param o The Word object this will be compared to
     * @returns A negative int if this is greater than, 0 if equal to, and positive if less than
     *
     **/
    //Method implemented to easily sort the word array
    public int compareTo(Word o) {
	if(this.wordChars.length > o.wordChars.length) {
	    return -1;
	}
	else if (this.wordChars.length == o.wordChars.length) {
	    return 0;
	}
	else {
	    return 1;
	}
    } // compareTo ()
    // =============================================================================
	      

    // =============================================================================
    /**
     * Helper method for testing purposes
     *
     * @returns The string representation for this object
     *
     **/
    public String toString() {
	String toReturn = "Word: ";
	for(int i = 0; i < wordChars.length; i++) {
	    toReturn += wordChars[i];
	}
	if(hint != null) {
	    toReturn += "; Hint: " + this.hint;
	}
	return toReturn;
    } // toString()
    // =============================================================================

// =============================================================================
} // class Word
// =============================================================================
