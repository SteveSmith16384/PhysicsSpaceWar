package com.scs.physicsspacewar;

import com.scs.physicsspacewar.input.IInputDevice;

public class Player {
	
	public int score, id_ZB;
	public IInputDevice input;

	private static int nextId = 0;
	
	public Player(IInputDevice _input) {
		super();
		
		id_ZB = nextId++;
		input = _input;
	}

}
