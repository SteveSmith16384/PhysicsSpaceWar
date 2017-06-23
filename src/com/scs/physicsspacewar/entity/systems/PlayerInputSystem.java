package com.scs.physicsspacewar.entity.systems;

import com.scs.physicsspacewar.entity.components.IPlayerControllable;

public class PlayerInputSystem {

	public PlayerInputSystem() {
	}


	public void process(IPlayerControllable avatar) {
		avatar.processInput();
	}
	
}
