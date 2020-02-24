/**
 * Project:		firesquid.maze_generator
 * Filename:	Main.java
 * Developer:	Peter Reynolds
 * Date:		February 22, 2020
 * 
 * 
 * Main class
 */

package github.firesquid.maze_generator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) {
		
		// Initialize the maze generator and display
		MazeController mazeController = new MazeController(50, 50);
		MazeDisplay mdWindow = new MazeDisplay(mazeController);
		mdWindow.setVisible(true);
		
		
		// Step Generation Timer
		///*
		int delay = 1;
		ActionListener StepGen = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Do one generation step
				mazeController.stepGeneration();
				
				// repaint the display
				if (mazeController.genIsComplete())
				{
					// stop the timer and immediately repaint the display
					((Timer)e.getSource()).stop();
					mdWindow.repaintBuffer(0);
				}
				else
				{
					// repaint the display every 16 ms
					mdWindow.repaintBuffer(16);
				}
			}
		};
		
		// start the timer
		new Timer(delay, StepGen).start();
		//*/
		
		// Fully Generate Maze
		/*
		mazeController.fullGeneration();
		mdWindow.repaintBuffer(0);
		 */
		
		
		// Full Generation Timer
		/*
		int delay = 500;
		ActionListener FullGen = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// generate the maze, display it, and reset the maze
				mazeController.fullGeneration();
				mdWindow.repaintBuffer(0);				
				mazeController.reset(); 
			}
		};
		
		// start the timer
		new Timer(delay, FullGen).start();
		*/
		

	}

}
