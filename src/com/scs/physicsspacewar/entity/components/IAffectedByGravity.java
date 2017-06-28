package com.scs.physicsspacewar.entity.components;

import org.jbox2d.common.Vec2;

public interface IAffectedByGravity extends IGetPosition {

	float getMass();
	
	void applyForceToCenter(Vec2 vec);
	
}
