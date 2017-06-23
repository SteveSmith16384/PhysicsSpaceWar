package com.scs.physicsspacewar.entity.components;

public interface IProcessable {

	void preprocess(long interpol);
	
	void postprocess(long interpol);
	
}
