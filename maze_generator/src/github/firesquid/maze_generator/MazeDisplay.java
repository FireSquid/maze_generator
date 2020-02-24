/**
 * Project:		firesquid.maze_generator
 * Filename:	MazeDisplay.java
 * Developer:	Peter Reynolds
 * Date:		February 22, 2020
 * 
 * 
 * Displays the maze
 */

package github.firesquid.maze_generator;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;


public class MazeDisplay extends Frame 
{

	
	private static final long serialVersionUID = 7527229839395439108L;
	
	// window dimensions
	private static final int WINDOW_WIDTH = 1080;
	private static final int WINDOW_HEIGHT = 1080;
	
	// last time the window was repainted
	private long lastTimeDrawn;
	
	// the maze generator
	private MazeController controller;
	
	private BufferedImage buffer;
	
	// construct the display with a link to a maze generator
	public MazeDisplay(MazeController cont)
	{
		super("Maze Generator");
		controller = cont;
		initialize();
	}
	
	// initializes the display
	private void initialize()
	{
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		// allow the window to be closed
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent)
			{
				System.exit(0);
			}
		});
		
		lastTimeDrawn = System.currentTimeMillis();
	}
	
	public void repaintBuffer(long msDelay)
	{
		// cancel the repainting if not enough time has past
		if (lastTimeDrawn + msDelay > System.currentTimeMillis())
			return;
		
		lastTimeDrawn = System.currentTimeMillis();
		
		// create a new buffer to draw to
		buffer = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		
		// get graphics objects from it
		Graphics g = buffer.getGraphics();		
		Graphics2D g2 = (Graphics2D) g;
		
		// clear the buffer image
		g.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		// draw the maze to the buffer graphics
		controller.drawMazeSpaces(g2);
		
		// paint the buffer to the frame
		this.repaint();
	}
	
	@Override
	public void update(Graphics g)
	{
		this.paint(g);
	}
	
	@Override
	public void paint(Graphics g)
	{
		// draw the buffer onto the window
		g.drawImage(buffer, 0, 0, this);
	}
}
