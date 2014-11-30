package net.zomis.gametree.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class GameNode {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	private GameTree tree;
	
	@ManyToMany(targetEntity = GameNode.class)
	private List<GameNode> parents = new ArrayList<>();
	
	@ManyToMany(targetEntity = NodeTag.class)
	private List<NodeTag> tags = new ArrayList<>();
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addParent(GameNode parent) {
		this.parents.add(parent);
	}

	public void addTag(NodeTag tag) {
		tags.add(tag);
	}

	@Override
	public String toString() {
		return id + ": " + name;
	}

	public void setTree(GameTree tree) {
		this.tree = tree;
	}
	
	public Integer getId() {
		return id;
	}

	public List<GameNode> getParents() {
		return this.parents;
	}
	
}
