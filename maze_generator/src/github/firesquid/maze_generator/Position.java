package github.firesquid.maze_generator;

public class Position {
	
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
	
	public Position addPosition(Position other)
	{
		return addPosition(this, other);
	}
	
	public static Position addPosition(Position posA, Position posB)
	{
		return new Position(posA.X + posB.X, posA.Y + posB.Y);
	}
	
	public Position subPosition(Position other)
	{
		return subPosition(this, other);
	}
	
	public static Position subPosition(Position posA, Position posB)
	{
		return new Position(posA.X - posB.X, posA.Y - posB.Y);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Position)
		{
			Position other = (Position)obj;
			
			return ((X == other.X) && (Y == other.Y));
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("(%d, %d)", X, Y);
	}
	
}
