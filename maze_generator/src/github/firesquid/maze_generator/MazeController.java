/**
 * Project:		firesquid.maze_generator
 * Filename:	MazeController.java
 * Developer:	Peter Reynolds
 * Date:		February 22, 2020
 * 
 * 
 * Maze generator class.
 * Handles the generation and display of a maze.
 */

package github.firesquid.maze_generator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class MazeController
{
	// array holding the states of each position in the maze
	private ArrayList<MazeSpaceState> mazeSpaces = new ArrayList<MazeSpaceState>();

	private int width, height;
	private boolean visible=true;
	
	// set to true when the maze is done generating
	private boolean generationComplete=false;
	
	// random number generator
	private Random random;
	
	// start and end positions of the maze
	private Position start, end;
	
	// array of positions in the maze that can be further generated from
	private ArrayList<Position> generationPosList = new ArrayList<Position>();
	
	// array of orthagonal positions
	private final int RIGHT = 0, DOWN = 1, LEFT = 2, UP = 3, BLOCKED = 4;
	private Position[] orthoPositions = {new Position(1,0), new Position(0,1), new Position(-1,0), new Position(0,-1)};
	
	public MazeController(int Width, int Height)
	{
		width = Width;
		height = Height;
		
		// set start to the top left corner and the end to the bottom right
		start = new Position(0, 0);
		end = new Position(Width - 1, Height - 1);
		
		// add the start position to the list of positions that can be generated from
		generationPosList.add(start);
		
		// initialize the random number generator
		random = new Random();
		
		// initialize the array to have all spaces filled in
		for (int X=0; X<width; X++)
		{
			for (int Y=0; Y<height; Y++)
			{
				mazeSpaces.add(new MazeSpaceState(false, false, false, false));
			}
		}
	}
	
	// reset the maze controller
	public void reset()
	{
		// reset necessary variables
		generationComplete = false;
		generationPosList.add(start);
		
		// reset the spaces to all be filled in
		for (int X=0; X<width; X++)
		{
			for (int Y=0; Y<height; Y++)
			{
				setMazeSpace(X, Y, new MazeSpaceState(false, false, false, false));
			}
		}
	}
	
	public void drawMazeSpaces(Graphics2D g2)
	{
		if (!visible)
			return;
		
		// fill in the maze background
		g2.setPaint(Color.BLUE);
		g2.fillRect(128, 128, width*16, height*16);
		
		for (int x=0; x<width; x++)
			for (int y=0; y<height; y++)
			{
				int X = x*16 + 128;
				int Y = y*16 + 128;
				
				// draw cut out rectanges on the path for every space with an opening to the right or bottom
				g2.setPaint(Color.WHITE);
				if (getMazeSpace(x, y).isRight())
				{
					g2.fillRect(X + 1, Y + 1, 30, 14);
				}
				if (getMazeSpace(x, y).isDown())
				{
					g2.fillRect(X + 1, Y + 1, 14, 30);
				}
				
				// draw green and red squares on the start and end positions respectively
				if (x == start.getX() && y == start.getY())
				{
					g2.setPaint(Color.GREEN);
					g2.fillRect(X + 4, Y + 4, 8, 8);
				}
				if (x == end.getX() && y == end.getY())
				{
					g2.setPaint(Color.RED);
					g2.fillRect(X + 4, Y + 4, 8, 8);
				}
			}
		
		// draw orange squares on spaces for available generations
		/*g2.setPaint(Color.ORANGE);
		for (Position genPos : generationPosList)
		{
			int X = genPos.getX()*16 + 128;
			int Y = genPos.getY()*16 + 128;
			g2.fillRect(X + 3, Y + 3, 8, 8);
		}*/
	}
	
	// generate one new space in the maze
	public void stepGeneration()
	{
		if (generationComplete)
			return;
		
		// choose a random position form the available positions for further generations
		Position usedPosition = generationPosList.get(random.nextInt(generationPosList.size()));
		
		// check which spaces have not been generated yet
		Position[] availableDirections = getMatchingOrthoPositions(usedPosition, BLOCKED);
		
		// choose a random direction to generate in
		Position orthoPos = availableDirections[random.nextInt(availableDirections.length)];
		Position stepPosition = usedPosition.addPosition(orthoPos);
		
		// update the maze spaces based on the direction generation occurred in
		if (orthoPos.equals(orthoPositions[RIGHT]))
		{
			setMazeSpace(usedPosition, getMazeSpace(usedPosition).setRight(true));
			setMazeSpace(stepPosition, getMazeSpace(stepPosition).setLeft(true));
		}
		else if (orthoPos.equals(orthoPositions[DOWN]))
		{
			setMazeSpace(usedPosition, getMazeSpace(usedPosition).setDown(true));
			setMazeSpace(stepPosition, getMazeSpace(stepPosition).setUp(true));
		}
		else if (orthoPos.equals(orthoPositions[LEFT]))
		{
			setMazeSpace(usedPosition, getMazeSpace(usedPosition).setLeft(true));
			setMazeSpace(stepPosition, getMazeSpace(stepPosition).setRight(true));
		}
		else if (orthoPos.equals(orthoPositions[UP]))
		{
			setMazeSpace(usedPosition, getMazeSpace(usedPosition).setUp(true));
			setMazeSpace(stepPosition, getMazeSpace(stepPosition).setDown(true));
		}
		
		// update the generatable positions list
		updateGenPosList(stepPosition);
	}
	
	// continuously generate the array until generation finishes
	public void fullGeneration()
	{
		while (!generationComplete)
		{
			stepGeneration();
		}
	}
	
	// find positions orthagonal to the one given that are in a specific state
	private Position[] getMatchingOrthoPositions(Position pos, int state)
	{
		// list of orthagonal directions that will be returned
		ArrayList<Position> validOrthoPostions = new ArrayList<Position>();
		
		// loop through each of the orthagonal positions
		for (Position orthoPos : orthoPositions)
		{
			// get the position to check for a certain state
			Position checkPos = orthoPos.addPosition(pos);
			
			// verify that the position is within the maze
			if (checkPos.getX() >= 0 && checkPos.getX() < width && checkPos.getY() >= 0 && checkPos.getY() < height)
			{
				// get the state of the position that is being checked
				MazeSpaceState checkState = getMazeSpace(checkPos);
				
				// check each direction
				switch (state)
				{
				case RIGHT:
				{
					if (checkState.isRight())
						validOrthoPostions.add(orthoPos);
				}
					break;
				case DOWN:
				{
					if (checkState.isDown())
						validOrthoPostions.add(orthoPos);
				}
					break;
				case LEFT:
				{
					if (checkState.isLeft())
						validOrthoPostions.add(orthoPos);
				}
					break;
				case UP:
				{
					if (checkState.isUp())
						validOrthoPostions.add(orthoPos);
				}
					break;
				default:	// check if all sides of the space are closed in the default case
				{
					if (checkState.isBlocked())
						validOrthoPostions.add(orthoPos);
				}
					break;
				}
			}
		}
		
		return validOrthoPostions.toArray(new Position[validOrthoPostions.size()]);
	}
	
	// update which positions can still be generated from
	private void updateGenPosList(Position center)
	{
		
		// check that the space is not blocked and does have blocked spaces next to it
		if (!getMazeSpace(center).isBlocked() && getMatchingOrthoPositions(center, BLOCKED).length > 0)
		{
			// add that position to the generation list if it is not already
			if (!generationPosList.contains(center))
			{
				generationPosList.add(center);
			}
		}
		
		// check all the spaces adjacent to the center
		for (Position orthoPos : orthoPositions)
		{			
			// get the space to check
			Position checkPos = orthoPos.addPosition(center);
			
			// verify that the position to check is within the maze
			if (checkPos.getX() >= 0 && checkPos.getX() < width && checkPos.getY() >= 0 && checkPos.getY() < height)
			{
				// check that the space is not blocked and has blocked spaces next to it
				if (!getMazeSpace(checkPos).isBlocked() && getMatchingOrthoPositions(checkPos, BLOCKED).length > 0)
				{
					// add the position to the generation list if it isn't already
					if (!generationPosList.contains(checkPos))
					{
						generationPosList.add(checkPos);
					}
				}
				else
				{
					// if the space is blocked or doesn't have any blocked spaces next to it then remove it from the generation list 
					if (generationPosList.contains(checkPos))
					{
						generationPosList.remove(checkPos);
					}					
					
				}
			}
		}
		
		// genertaion is compelete if there are no more spaces to generate from
		generationComplete = (generationPosList.size() == 0);
	}
	
	// checks if generation is complete
	public boolean genIsComplete()
	{
		return generationComplete;
	}
	
	// get the state of a space from x-y coords
	private MazeSpaceState getMazeSpace(int x, int y)
	{
		return mazeSpaces.get(x*height + y);
	}
	
	// get the state of a space from a position
	private MazeSpaceState getMazeSpace(Position pos)
	{
		return mazeSpaces.get(pos.getX()*height + pos.getY());
	}
	
	// set the state of a space with x-y coords
	private MazeSpaceState setMazeSpace(int x, int y, MazeSpaceState mss)
	{
		return mazeSpaces.set(x*height + y, mss);
	}
	
	// set the state of a space with a position
	private MazeSpaceState setMazeSpace(Position pos, MazeSpaceState mss)
	{
		return mazeSpaces.set(pos.getX()*height + pos.getY(), mss);
	}
	
}