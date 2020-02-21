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
	
	private int RIGHT = 0, DOWN = 1, LEFT = 2, UP = 3;
	private Position[] orthoPositions = {new Position(1,0), new Position(0,1), new Position(-1,0), new Position(0,-1)};
	
	public MazeController(int Width, int Height)
	{
		width = Width;
		height = Height;
		
		start = new Position(0, 0);
		end = new Position(Width - 1, Height - 1);
		
		generationPosList.add(start);
		
		random = new Random();
		
		for (int X=0; X<Width; X++)
		{
			for (int Y=0; Y<Width; Y++)
			{
				mazeSpaces.add((X == start.getX() && Y == start.getY()) ? (MazeSpaceState.NO_OPENINGS) : (MazeSpaceState.BLOCKED));
			}
		}
		
		Position posA = new Position(3, 7);
		Position posB = new Position(5, -2);
	}
	
	public void DrawMazeSpaces(Graphics2D g2)
	{
		if (!visible)
			return;
		
		for (int x=0; x<width; x++)
			for (int y=0; y<height; y++)
			{
				int X = x*16 + 128;
				int Y = y*16 + 128;
				
				if (mazeSpaces.get(x*height + y) == MazeSpaceState.BLOCKED)
				{
					g2.fillRect(X, Y, 14, 14);
				}
				
				if (mazeSpaces.get(x*height + y) != MazeSpaceState.RIGHT && mazeSpaces.get(x*height + y) != MazeSpaceState.RIGHT_DOWN)
				{
					g2.fillRect(X + 14, Y, 2, 16);
				}
				if (mazeSpaces.get(x*height + y) != MazeSpaceState.DOWN && mazeSpaces.get(x*height + y) != MazeSpaceState.RIGHT_DOWN)
				{
					g2.fillRect(X, Y + 14, 16, 2);
				}
				
				if (x == 0)
				{
					g2.fillRect(X - 2, Y, 2, 16);
				}
				if (y == 0)
				{
					g2.fillRect(X, Y - 2, 16, 2);
				}
				
				if (x == start.getX() && y == start.getY())
				{
					g2.setPaint(Color.GREEN);
					g2.fillRect(X + 3, Y + 3, 8, 8);
					g2.setPaint(Color.BLUE);
				}
				if (x == end.getX() && y == end.getY())
				{
					g2.setPaint(Color.RED);
					g2.fillRect(X + 3, Y + 3, 8, 8);
					g2.setPaint(Color.BLUE);
				}
			}
		
		g2.setPaint(Color.ORANGE);
		for (Position genPos : generationPosList)
		{
			int X = genPos.getX()*16 + 128;
			int Y = genPos.getY()*16 + 128;
			g2.fillRect(X + 3, Y + 3, 8, 8);
		}
	}
	
	public int[] StepGeneration()
	{
		if (generationComplete)
			return new int[] {0, 0, 0, 0};
		
		Position usedPosition = generationPosList.get(random.nextInt(generationPosList.size()));		
		Position[] availableDirections = GetMatchingOrthoPositions(usedPosition, new MazeSpaceState[] {MazeSpaceState.BLOCKED});		
		Position orthoPos = availableDirections[random.nextInt(availableDirections.length)];
		Position stepPosition = usedPosition.addPosition(orthoPos);
		
		if (orthoPos.equals(orthoPositions[RIGHT]))
		{
			mazeSpaces.set(stepPosition.getX()*height + stepPosition.getY(), MazeSpaceState.NO_OPENINGS);
			
			if (mazeSpaces.get(usedPosition.getX()*height + usedPosition.getY()) != MazeSpaceState.DOWN)
				mazeSpaces.set(usedPosition.getX()*height + usedPosition.getY(), MazeSpaceState.RIGHT);
			else
				mazeSpaces.set(usedPosition.getX()*height + usedPosition.getY(), MazeSpaceState.RIGHT_DOWN);
		}
		else if (orthoPos.equals(orthoPositions[DOWN]))
		{
			mazeSpaces.set(stepPosition.getX()*height + stepPosition.getY(), MazeSpaceState.NO_OPENINGS);
			
			if (mazeSpaces.get(usedPosition.getX()*height + usedPosition.getY()) != MazeSpaceState.RIGHT)
				mazeSpaces.set(usedPosition.getX()*height + usedPosition.getY(), MazeSpaceState.DOWN);
			else
				mazeSpaces.set(usedPosition.getX()*height + usedPosition.getY(), MazeSpaceState.RIGHT_DOWN);
		}
		else if (orthoPos.equals(orthoPositions[LEFT]))
		{
			if (mazeSpaces.get(stepPosition.getX()*height + stepPosition.getY()) != MazeSpaceState.DOWN)
				mazeSpaces.set(stepPosition.getX()*height + stepPosition.getY(), MazeSpaceState.RIGHT);
			else
				mazeSpaces.set(stepPosition.getX()*height + stepPosition.getY(), MazeSpaceState.RIGHT_DOWN);
		}
		else if (orthoPos.equals(orthoPositions[UP]))
		{
			if (mazeSpaces.get(stepPosition.getX()*height + stepPosition.getY()) != MazeSpaceState.RIGHT)
				mazeSpaces.set(stepPosition.getX()*height + stepPosition.getY(), MazeSpaceState.DOWN);
			else
				mazeSpaces.set(stepPosition.getX()*height + stepPosition.getY(), MazeSpaceState.RIGHT_DOWN);
		}
		
		
		UpdateGenPosList(stepPosition);
		
		return (new int[] {
				stepPosition.getX()*16 + 128 - 32,
				stepPosition.getY()*16 + 128 - 32,
				64,
				64
		});
	}
	
	private Position[] GetMatchingOrthoPositions(Position pos, MazeSpaceState[] tgtStates)
	{
		ArrayList<Position> validOrthoPostions = new ArrayList<Position>();
		
		for (Position orthoPos : orthoPositions)
		{
			Position checkPos = orthoPos.addPosition(pos);
			
			if (checkPos.getX() >= 0 && checkPos.getX() < width && checkPos.getY() >= 0 && checkPos.getY() < height)
			{
				if (Arrays.stream(tgtStates).anyMatch(x -> x == mazeSpaces.get(checkPos.getX()*height + checkPos.getY())))
				{
					validOrthoPostions.add(orthoPos);
				}
			}
		}
		
		return validOrthoPostions.toArray(new Position[validOrthoPostions.size()]);
	}
	
	private void UpdateGenPosList(Position center)
	{
		if (mazeSpaces.get(center.getX()*height + center.getY()) != MazeSpaceState.BLOCKED && GetMatchingOrthoPositions(center, new MazeSpaceState[] {MazeSpaceState.BLOCKED}).length > 0)
		{
			if (!generationPosList.contains(center))
				generationPosList.add(center);
		}
		
		for (Position orthoPos : orthoPositions)
		{			
			
			Position checkPos = orthoPos.addPosition(center);
			
			if (checkPos.getX() >= 0 && checkPos.getX() < width && checkPos.getY() >= 0 && checkPos.getY() < height)
			{
				if (mazeSpaces.get(checkPos.getX()*height + checkPos.getY()) != MazeSpaceState.BLOCKED && GetMatchingOrthoPositions(checkPos, new MazeSpaceState[] {MazeSpaceState.BLOCKED}).length > 0)
				{
					if (!generationPosList.contains(checkPos))
						generationPosList.add(checkPos);
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
	
}