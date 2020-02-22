package github.firesquid.maze_generator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Main {

	public static void main(String[] args) {
		
		MazeController mazeController = new MazeController(50, 50);
		MazeDisplay mdWindow = new MazeDisplay(mazeController);
		mdWindow.setVisible(true);
		
		
		// Step Generation Timer
		/*
		int delay = 1;
		ActionListener StepGen = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				mazeController.stepGeneration();
				
				if (mazeController.genIsComplete())
				{
					((Timer)e.getSource()).stop();
					mdWindow.repaintBuffer(0);
				}
				else
				{
					mdWindow.repaintBuffer(33);
				}
			}
		};
		
		new Timer(delay, StepGen).start();
		*/
		
		// Fully Generate Maze
		/*
		mazeController.fullGeneration();
		mdWindow.repaintBuffer(0);
		*/
		
		// Full Generation Timer
		int delay = 200;
		ActionListener FullGen = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				mazeController.fullGeneration();
				mdWindow.repaintBuffer(0);				
				mazeController.reset(); 
			}
		};
		
		new Timer(delay, FullGen).start();
		

	}

}
