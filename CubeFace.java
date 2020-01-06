public class CubeFace {

    public Character[][] grid;
    public CubeFace leftLink;
    public CubeFace rightLink;
    public CubeFace bottomLink;
    public CubeFace topLink;

    public CubeFace(Character[][] toStore) {
	grid = toStore;
    }

    //Sets the linked faces to arguments passed in
    public void setLinks(CubeFace left, CubeFace right, CubeFace top, CubeFace bottom) {
	leftLink = left;
	rightLink = right;
	topLink = top;
	bottomLink = bottom;
    }
}
