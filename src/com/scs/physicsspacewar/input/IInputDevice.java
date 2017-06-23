package com.scs.physicsspacewar.input;

public interface IInputDevice {

	boolean isLeftPressed();

	boolean isRightPressed();
	
	boolean isJumpPressed();

	boolean isFirePressed();

	float getStickDistance();
	
}
