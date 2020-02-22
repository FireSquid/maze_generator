package github.firesquid.maze_generator;

public class MazeSpaceState {
	
	public boolean Right;
	public boolean Down;
	public boolean Left;
	public boolean Up;
	public boolean Blocked;
	
	public MazeSpaceState(boolean r, boolean d, boolean l, boolean u)
	{
		Right = r;
		Down = d;
		Left = l;
		Up = u;
	}

	public boolean isRight()
	{
		return Right;
	}

	public MazeSpaceState setRight(boolean right)
	{
		Right = right;
		return this;
	}

	public boolean isDown()
	{
		return Down;
	}

	public MazeSpaceState setDown(boolean down)
	{
		Down = down;
		return this;
	}

	public boolean isLeft()
	{
		return Left;
	}

	public MazeSpaceState setLeft(boolean left) 
	{
		Left = left;
		return this;
	}

	public boolean isUp()
	{
		return Up;
	}

	public MazeSpaceState setUp(boolean up)
	{
		Up = up;
		return this;
	}
	
	public boolean isBlocked()
	{
		return (!Right && !Down && !Left && !Up);
	}
	
}
