package net.zomis.gametree;

import java.sql.SQLException;
import java.util.function.Function;

import net.zomis.gametree.model.GameNode;
import net.zomis.gametree.model.GameTree;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EditController {
	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(EditController.class);

	@RequestMapping(value = "/edit/connection/remove", method = RequestMethod.POST)
	public @ResponseBody String remove(@RequestParam Integer tree, @RequestParam Integer from, @RequestParam Integer to) {
		return withSession(sess -> {
			logger.info("Remove connection! " + tree + " from " + from + " to " + to);
			GameNode node = (GameNode) sess.get(GameNode.class, to);
			GameNode nodeFrom = (GameNode) sess.get(GameNode.class, from);
			if (failOnTree(node, tree)) {
				return "wrong-tree";
			}
			if (failOnTree(nodeFrom, tree)) {
				return "wrong-tree";
			}
			if (!node.removeParent(nodeFrom)) {
				return "invalid-connection";
			}
			sess.beginTransaction();
			sess.update(node);
			sess.getTransaction().commit();
			return "ok";
		});
	}
	
	@RequestMapping(value = "/edit/connection/add", method = RequestMethod.POST)
	public @ResponseBody String add(@RequestParam Integer tree, @RequestParam Integer from, @RequestParam Integer to) {
		return withSession(sess -> {
			logger.info("Add connection! " + tree);
			GameNode node = (GameNode) sess.get(GameNode.class, to);
			GameNode nodeFrom = (GameNode) sess.get(GameNode.class, from);
			if (failOnTree(node, tree)) {
				return "wrong-tree";
			}
			if (!node.addParent(nodeFrom)) {
				return "impossible";
			}
			
			sess.beginTransaction();
			sess.update(node);
			sess.getTransaction().commit();
			return "ok";
		});
	}

	@RequestMapping(value = "/edit/node", method = RequestMethod.POST)
	public @ResponseBody String editNode(@RequestParam Integer tree, @RequestParam("node") Integer nodeId, 
			@RequestParam String name, @RequestParam String tags) {
		return withSession(sess -> {
			logger.info("Edit node! " + tree + ", " + nodeId + ", " + name + ", " + tags);
			GameNode node = (GameNode) sess.get(GameNode.class, nodeId);
			if (failOnTree(node, tree)) {
				return "wrong-tree";
			}
			node.setName(name);
			sess.beginTransaction();
			node.updateTags(tags, sess);
			sess.update(node);
			sess.getTransaction().commit();
			return "ok";
		});
	}
	
	private boolean failOnTree(GameNode node, Integer tree) {
		if (node == null) {
			return true;
		}
		return !node.getTree().getId().equals(tree);
	}

	private String withSession(Function<Session, String> object) {
		Session sess = sessionFactory.openSession();
		try {
			return object.apply(sess);
		}
		catch (Exception ex) {
			logException(ex);
			return "error: " + ex;
		}
		finally {
			sess.close();
		}
	}

	private void logException(Exception ex) {
		logger.error("Error occured in EditController", ex);
		if (ex instanceof SQLException) {
			SQLException sqlException = (SQLException) ex;
			logException(sqlException.getNextException());
		}
	}

	@RequestMapping(value = "/edit/node/add", method = RequestMethod.POST)
	public @ResponseBody String addNode(@RequestParam Integer tree, @RequestParam String name, @RequestParam String tags) {
		return withSession(sess -> {
			logger.info("Add node! " + tree + " with name " + name + " and tags " + tags);
			GameNode node = new GameNode();
			GameTree gameTree = (GameTree) sess.get(GameTree.class, tree);
			node.setTree(gameTree);
			
			node.setName(name);
			node.updateTags(tags, sess);
			sess.beginTransaction();
			sess.persist(node);
			sess.getTransaction().commit();
			return String.valueOf(node.getId());
		});
	}
	
	@RequestMapping(value = "/edit/node/pos", method = RequestMethod.POST)
	public @ResponseBody String editNodePos(@RequestParam Integer tree, @RequestParam("node") Integer nodeId,
			@RequestParam Integer x, @RequestParam Integer y) {
		return withSession(sess -> {
			logger.info("Edit node! " + tree + ", " + nodeId + ", " + x + ", " + y);
			GameNode node = (GameNode) sess.get(GameNode.class, nodeId);
			if (failOnTree(node, tree)) {
				return "wrong-tree";
			}
			node.setX(x);
			node.setY(y);
			sess.beginTransaction();
			sess.update(node);
			sess.getTransaction().commit();
			return "ok";
		});
	}
	
	@RequestMapping(value = "/edit/node/remove", method = RequestMethod.POST)
	public @ResponseBody String removeNode(@RequestParam Integer tree, @RequestParam Integer node) {
		return withSession(sess -> {
			logger.info("Remove node! " + tree + ", " + node);
			GameNode gameNode = (GameNode) sess.get(GameNode.class, node);
			if (failOnTree(gameNode, tree)) {
				return "wrong-tree";
			}
			sess.beginTransaction();
			gameNode.remove();
			gameNode.getTree().removeNode(gameNode);
			sess.delete(gameNode);
			sess.getTransaction().commit();
			return String.valueOf(node);
		});
	}
	
}
