package net.zomis.gametree.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
		return this.tags.stream().map(e -> e.getName()).collect(Collectors.joining());
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
	
}
