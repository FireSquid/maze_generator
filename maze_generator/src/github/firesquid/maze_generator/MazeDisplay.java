package github.firesquid.maze_generator;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;


public class MazeDisplay extends Frame 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7527229839395439108L;
	private static final int WINDOW_WIDTH = 1080;
	private static final int WINDOW_HEIGHT = 1080;
	
	private long lastTimeDrawn;
	
	private MazeController controller;
	
	private BufferedImage buffer;
	
	public MazeDisplay(MazeController cont)
	{
		super("Maze Generator");
		controller = cont;
		initialize();
	}
	
	private void initialize()
	{
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
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
		if (lastTimeDrawn + msDelay > System.currentTimeMillis())
			return;
		
		lastTimeDrawn = System.currentTimeMillis();
		
		buffer = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = buffer.getGraphics();
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setPaint(Color.BLUE);
		controller.drawMazeSpaces(g2);
		
		if (this.getGraphics() != null)
		{
			this.paint(this.getGraphics());
		}
	}
	
	@Override
	public void update(Graphics g)
	{
		this.paint(g);
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		g.drawImage(buffer, 0, 0, this);
	}
}
