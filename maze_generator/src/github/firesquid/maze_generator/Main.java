package github.firesquid.maze_generator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Main {

	public static void main(String[] args) {
		
		MazeController mazeController = new MazeController(60, 50);
		MazeDisplay mdWindow = new MazeDisplay(mazeController);
		mdWindow.setVisible(true);
		
		int delay = 1;
		ActionListener StepGen = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int[] reDrawCoords = mazeController.StepGeneration();
				
				if (mazeController.genIsComplete())
				{
					((Timer)e.getSource()).stop();
					mdWindow.redraw(0);
				}
				else
				{
					mdWindow.redraw(33);
				}
			}
		};
		
		new Timer(delay, StepGen).start();;

	}

}
