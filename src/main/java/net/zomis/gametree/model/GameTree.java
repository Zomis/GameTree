package net.zomis.gametree.model;

import java.util.Date;
import java.util.List;

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
	private Integer	gameid;
	
	@ManyToOne
	private TreeUser author;
	
	private String name;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date time;
	
	@OneToMany(targetEntity = GameNode.class)
	private List<GameNode> nodes;
	
	public String getName() {
		return name;
	}
	
	public Integer getGameid() {
		return gameid;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
