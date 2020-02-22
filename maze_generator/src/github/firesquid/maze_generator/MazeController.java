package github.firesquid.maze_generator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MazeController
{
	
	private ArrayList<MazeSpaceState> mazeSpaces = new ArrayList<MazeSpaceState>();
	
	private int width, height;
	private boolean visible=true;
	private boolean generationComplete=false;
	
	private Random random;
	
	private Position start, end;
	
	private ArrayList<Position> generationPosList = new ArrayList<Position>();
	
	private final int RIGHT = 0, DOWN = 1, LEFT = 2, UP = 3, BLOCKED = 4;
	private Position[] orthoPositions = {new Position(1,0), new Position(0,1), new Position(-1,0), new Position(0,-1)};
	
	public MazeController(int Width, int Height)
	{
		width = Width;
		height = Height;
		
		start = new Position(0, 0);
		end = new Position(Width - 1, Height - 1);
		
		generationPosList.add(start);
		
		random = new Random();
		
		for (int X=0; X<width; X++)
		{
			for (int Y=0; Y<height; Y++)
			{
				mazeSpaces.add(new MazeSpaceState(false, false, false, false));
			}
		}
	}
	
	public void reset()
	{
		generationComplete = false;
		generationPosList.add(start);
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
		
		g2.setPaint(Color.BLUE);
		g2.fillRect(128, 128, width*16, height*16);
		
		for (int x=0; x<width; x++)
			for (int y=0; y<height; y++)
			{
				int X = x*16 + 128;
				int Y = y*16 + 128;
				
				g2.setPaint(Color.WHITE);
				if (getMazeSpace(x, y).isRight())
				{
					g2.fillRect(X + 1, Y + 1, 30, 14);
				}
				if (getMazeSpace(x, y).isDown())
				{
					g2.fillRect(X + 1, Y + 1, 14, 30);
				}
				
				if (x == start.getX() && y == start.getY())
				{
					g2.setPaint(Color.GREEN);
					g2.fillRect(X + 3, Y + 3, 8, 8);
				}
				if (x == end.getX() && y == end.getY())
				{
					g2.setPaint(Color.RED);
					g2.fillRect(X + 3, Y + 3, 8, 8);
				}
			}
		
		/*g2.setPaint(Color.ORANGE);
		for (Position genPos : generationPosList)
		{
			int X = genPos.getX()*16 + 128;
			int Y = genPos.getY()*16 + 128;
			g2.fillRect(X + 3, Y + 3, 8, 8);
		}*/
	}
	
	public void stepGeneration()
	{
		if (generationComplete)
			return;
		
		Position usedPosition = generationPosList.get(random.nextInt(generationPosList.size()));		
		Position[] availableDirections = getMatchingOrthoPositions(usedPosition, BLOCKED);		
		Position orthoPos = availableDirections[random.nextInt(availableDirections.length)];
		Position stepPosition = usedPosition.addPosition(orthoPos);
		
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
		
		
		updateGenPosList(stepPosition);
	}
	
	public void fullGeneration()
	{
		while (!generationComplete)
		{
			stepGeneration();
		}
	}
	
	private Position[] getMatchingOrthoPositions(Position pos, int state)
	{
		ArrayList<Position> validOrthoPostions = new ArrayList<Position>();
		
		for (Position orthoPos : orthoPositions)
		{
			Position checkPos = orthoPos.addPosition(pos);
			
			if (checkPos.getX() >= 0 && checkPos.getX() < width && checkPos.getY() >= 0 && checkPos.getY() < height)
			{
				
				MazeSpaceState checkState = getMazeSpace(checkPos);
				
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
				default:
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
	
	private void updateGenPosList(Position center)
	{
		System.out.println("Updating Position " + center);
		
		if (!getMazeSpace(center).isBlocked() && getMatchingOrthoPositions(center, BLOCKED).length > 0)
		{
			if (!generationPosList.contains(center))
			{
				generationPosList.add(center);
			}
		}
		
		for (Position orthoPos : orthoPositions)
		{			
			
			Position checkPos = orthoPos.addPosition(center);
			
			if (checkPos.getX() >= 0 && checkPos.getX() < width && checkPos.getY() >= 0 && checkPos.getY() < height)
			{
				if (!getMazeSpace(checkPos).isBlocked() && getMatchingOrthoPositions(checkPos, BLOCKED).length > 0)
				{
					if (!generationPosList.contains(checkPos))
					{
						generationPosList.add(checkPos);
					}
				}
				else
				{
					if (generationPosList.contains(checkPos))
					{
						generationPosList.remove(checkPos);
					}					
					
				}
			}
		}
		
		generationComplete = (generationPosList.size() == 0);
	}
	
	public boolean genIsComplete()
	{
		return generationComplete;
	}
	
	private MazeSpaceState getMazeSpace(int x, int y)
	{
		return mazeSpaces.get(x*height + y);
	}
	
	private MazeSpaceState getMazeSpace(Position pos)
	{
		return mazeSpaces.get(pos.getX()*height + pos.getY());
	}
	
	private MazeSpaceState setMazeSpace(int x, int y, MazeSpaceState mss)
	{
		return mazeSpaces.set(x*height + y, mss);
	}
	
	private MazeSpaceState setMazeSpace(Position pos, MazeSpaceState mss)
	{
		return mazeSpaces.set(pos.getX()*height + pos.getY(), mss);
	}
	
}