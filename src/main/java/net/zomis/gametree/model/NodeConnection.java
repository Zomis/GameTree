package net.zomis.gametree.model;

public class NodeConnection {

	private final GameNode from;
	private final GameNode to;
	
	public NodeConnection(GameNode from, GameNode to) {
		this.from = from;
		this.to = to;
	}
	
	public Integer getFrom() {
		return from.getId();
	}
	
	public int getTo() {
		return to.getId();
	}
	
}
