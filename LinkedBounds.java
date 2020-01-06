// =============================================================================
// Helper data structure for printing "faces" of the "cube", this is used to change
// the display when the arrow buttons are pressed in the class UserInterface
public class LinkedBounds {
// =============================================================================

    // =============================================================================
    // FIELDS
    
    public int minX; //minimum xBoundary
    public int minY; //minimum yBoundary
    
    // the following fields store other instances of this class that are linked to this instance
    public LinkedBounds leftLink;
    public LinkedBounds rightLink;
    public LinkedBounds bottomLink;
    public LinkedBounds topLink;
    // =============================================================================

    // =============================================================================
    /**
     * The constructor, stores paramaters in instance
     *
     * @param x The minX boundary to store
     * @param y The minY boundary to store
     *
     **/
    public LinkedBounds(int x, int y) {
	minX = x;
	minY = y;
    }// LinkedBounds ()
    // =============================================================================

    // =============================================================================
    /**
     * Sets the linked boundaries to the arguments passed in
     *
     * @param left The left link to store
     * @param right The right link to store
     * @param top The top link to store
     * @param bottom The bottom link to store
     *
     **/
    public void setLinks(LinkedBounds left, LinkedBounds right, LinkedBounds top, LinkedBounds bottom) {
	leftLink = left;
	rightLink = right;
	topLink = top;
	bottomLink = bottom;
    } // setLinks ()
    // =============================================================================

// =============================================================================
}// class LinkedBounds
// =============================================================================
