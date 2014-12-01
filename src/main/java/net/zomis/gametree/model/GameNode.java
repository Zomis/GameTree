package net.zomis.gametree.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.Session;

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
	
	private Integer x = 100;
	
	private Integer y = 100;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean addParent(GameNode parent) {
		if (parent == null || parent.getTree() != getTree()) {
			return false;
		}
		this.parents.add(parent);
		return true;
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
	
	public String tagNames() {
		return tagNames("");
	}
	
	public String tagNames(String prefix) {
		return this.tags.stream().map(e -> prefix + e.getName()).collect(Collectors.joining(" "));
	}
	
	public List<NodeTag> tags() {
		return new ArrayList<NodeTag>(this.tags);
	}
	
	public GameTree getTree() {
		return tree;
	}

	public void updateTags(String tags, Session sess) {
		Set<String> newTags = new HashSet<String>(Arrays.asList(tags.split(" ")));
//		Set<String> oldTags = this.tags.stream().map(tag -> tag.getName()).collect(Collectors.toSet());
		
		this.tags.removeIf(tag -> !newTags.contains(tag.getName()));
		for (String tagName : newTags) {
			NodeTag tag = (NodeTag) sess.createQuery("select tag from NodeTag as tag where tag.name = :name and tag.tree.id = :tree")
				.setInteger("tree", this.tree.getId())
				.setString("name", tagName).uniqueResult();
			if (tag != null) {
				this.tags.add(tag);
			}
			else {
				tag = new NodeTag();
				tag.setName(tagName);
				sess.persist(tag);
				this.tags.add(tag);
			}
		}
	}

	public boolean removeParent(GameNode nodeFrom) {
		return this.parents.remove(nodeFrom);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameNode other = (GameNode) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public void remove() {
		this.tree.getNodes().forEach(node -> node.removeParent(this));
		
		this.parents.clear();
		this.tags.clear();
	}
	
	public Integer getX() {
		return x;
	}
	
	public Integer getY() {
		return y;
	}
	
	public void setX(Integer x) {
		this.x = x;
	}
	
	public void setY(Integer y) {
		this.y = y;
	}

	public void fixNonNull(Random random) {
		if (this.x == null) {
			x = random.nextInt(500);
		}
		if (this.y == null) {
			y = random.nextInt(500);
		}
	}
	
}
