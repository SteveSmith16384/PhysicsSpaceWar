package com.scs.physicsspacewar.input;

public interface IInputDevice {

	int getID();
	
	boolean isLeftPressed();

	boolean isRightPressed();
	
	boolean isUpPressed();
	
	boolean isDownPressed();
	
	float getStickDistance();
	
	boolean isFirePressed();
	
	int getAngle();

	String toString();

}
