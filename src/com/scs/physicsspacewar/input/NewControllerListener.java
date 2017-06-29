package com.scs.physicsspacewar.input;

public interface NewControllerListener {

	void newController(IInputDevice input);

	void controllerRemoved(IInputDevice input);

}
