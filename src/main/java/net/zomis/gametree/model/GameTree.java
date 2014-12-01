package net.zomis.gametree.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/*
Game guides system using tags. Upvote, downvote and edit (SE-style), nice tree overview of available stuff to work on.
 Can theoretically be stored as text files to see progress. Would be nice to have in a database.
 Game ---> Tags, StepNode, Tree
FIRST STEP: Add node, show tree, mouse over / click on a specific node, SECURITY
SECOND STEP: Mark which nodes you have solved
*/	

@Entity
public class GameTree {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer	id;
	
	@ManyToOne
	private TreeUser author;
	
	private String name;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date time;
	
	@OneToMany(targetEntity = GameNode.class, cascade = { }, mappedBy = "tree")
	private List<GameNode> nodes = new ArrayList<GameNode>();
	
	public String getName() {
		return name;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void addNode(GameNode node) {
		nodes.add(node);
	}
	
	public List<GameNode> getNodes() {
		return nodes;
	}

	public List<NodeConnection> findConnections() {
		List<NodeConnection> result = new ArrayList<>();
		for (GameNode node : this.nodes) {
			for (GameNode parent : node.getParents()) {
				result.add(new NodeConnection(parent, node));
			}
		}
		return result;
	}
	
	public List<NodePosition> findPositions() {
		if (true) {
			return actualPositions();
		}
		List<NodePosition> result = new ArrayList<>();
		
		Map<GameNode, Integer> depths = new HashMap<GameNode, Integer>();
		for (GameNode node : nodes) {
			depths.put(node, findDepth(node, depths));
		}
		
		Map<Integer, List<GameNode>> groupedByDepth = depths.keySet().stream().collect(Collectors.groupingBy(node -> depths.get(node)));
		
		for (Entry<Integer, List<GameNode>> ee : groupedByDepth.entrySet()) {
			ListIterator<GameNode> it = ee.getValue().listIterator();
			while (it.hasNext()) {
				int index = it.nextIndex();
				GameNode node = (GameNode) it.next();
				result.add(new NodePosition(node, index * 200 + 50, depths.get(node) * 70));
			}
		}
		
		return result;
	}

	private List<NodePosition> actualPositions() {
		Random random = new Random();
		return this.nodes.stream()
				.filter(node -> node != null)
				.peek(node -> node.fixNonNull(random))
				.map(node -> new NodePosition(node, node.getX(), node.getY())).collect(Collectors.toList());
	}

	private int findDepth(GameNode node, Map<GameNode, Integer> depths) {
		int calculatedDepth = 0;
		for (GameNode parent : node.getParents()) {
			Integer parentDepth = depths.get(parent);
			if (parentDepth == null) {
				parentDepth = findDepth(parent, depths);
			}
			calculatedDepth = Math.max(calculatedDepth, parentDepth);
		}
		calculatedDepth++;
		depths.put(node, calculatedDepth);
		return calculatedDepth;
	}

	public void removeNode(GameNode gameNode) {
		this.nodes.remove(gameNode);
	}
	
}
