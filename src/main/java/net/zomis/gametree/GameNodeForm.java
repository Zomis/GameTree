package net.zomis.gametree;

public class GameNodeForm {
	
	private Integer tree;
	private String name;
	private String tags = "";
	private String parents;

	public Integer getTree() {
		return tree;
	}
	
	public String getName() {
		return name;
	}
	
	public String getParents() {
		return parents;
	}
	
	public String getTags() {
		return tags;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setParents(String parents) {
		this.parents = parents;
	}
	
	public void setTags(String tags) {
		this.tags = tags;
	}
	
	public void setTree(Integer tree) {
		this.tree = tree;
	}
	
}
