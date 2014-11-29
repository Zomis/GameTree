package net.zomis.gametree.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TreeUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer	userid;

	private String name;
	
	public String getName() {
		return name;
	}
	
}
