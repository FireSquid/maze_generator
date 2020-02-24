/**
 * Project:		firesquid.maze_generator
 * Filename:	MazeSpaceState.java
 * Developer:	Peter Reynolds
 * Date:		February 22, 2020
 * 
 * 
 * structure to hold the state of a spaced
 * each boolean determines if there is an opening to an adjacent space
 */

package github.firesquid.maze_generator;

public class MazeSpaceState {
	
	// stores which openings the space has
	public boolean Right;
	public boolean Down;
	public boolean Left;
	public boolean Up;
	
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
	
	// check if the space is completely closed off
	public boolean isBlocked()
	{
		return (!Right && !Down && !Left && !Up);
	}
	
}
