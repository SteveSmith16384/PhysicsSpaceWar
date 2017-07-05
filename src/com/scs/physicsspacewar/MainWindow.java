package com.scs.physicsspacewar;

import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

	public BufferStrategy BS;

	public MainWindow(Main_SpaceWar main) {
		this.setSize(Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);
		this.setVisible(true);
		this.addKeyListener(main);

		this.createBufferStrategy(2);
		BS = this.getBufferStrategy();
	}





}
