package net.zomis.gametree.model;

import java.util.List;

import javax.persistence.OneToMany;

public class GameNode {

	@OneToMany
	private List<GameNode> parents;
	
	
	
}
