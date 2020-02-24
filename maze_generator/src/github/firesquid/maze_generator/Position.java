/**
 * Project:		firesquid.maze_generator
 * Filename:	Position.java
 * Developer:	Peter Reynolds
 * Date:		February 22, 2020
 * 
 * 
 * structure that holds a position with x-y coordinates
 */

package github.firesquid.maze_generator;

public class Position {
	
	// coordinates of the position
	private int X;
	private int Y;
	
	public Position(int x, int y) {
		super();
		X = x;
		Y = y;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}
	
	// add the coordinates of two positions and return the resulting position
	public Position addPosition(Position other)
	{
		return addPosition(this, other);
	}
	
	// add the coordinates of two positions and return the resulting position
	public static Position addPosition(Position posA, Position posB)
	{
		return new Position(posA.X + posB.X, posA.Y + posB.Y);
	}
	
	// subtract the coordinates of two positions and return the resulting position
	public Position subPosition(Position other)
	{
		return subPosition(this, other);
	}
	
	// subtract the coordinates of two positions and return the resulting position
	public static Position subPosition(Position posA, Position posB)
	{
		return new Position(posA.X - posB.X, posA.Y - posB.Y);
	}

	// two positions are equivalent if they have the same coordinates
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Position)
		{
			Position other = (Position)obj;
			
			return ((X == other.X) && (Y == other.Y));
		}
		return false;
	}

	// return the position as a pair of coordinates in string format
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("(%d, %d)", X, Y);
	}
	
}
