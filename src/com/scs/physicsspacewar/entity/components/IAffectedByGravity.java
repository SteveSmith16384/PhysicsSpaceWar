package com.scs.physicsspacewar.entity.components;

import org.jbox2d.common.Vec2;

public interface IAffectedByGravity extends IGetPosition {

	void applyForceToCenter(Vec2 vec);
}
