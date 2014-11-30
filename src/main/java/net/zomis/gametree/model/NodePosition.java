package net.zomis.gametree.model;

public class NodePosition {
	
	private GameNode node;
	private int x;
	private int y;

	public NodePosition(GameNode node, int x, int y) {
		this.node = node;
		this.x = x;
		this.y = y;
	}
	
	public int getId() {
		return node.getId();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

}
