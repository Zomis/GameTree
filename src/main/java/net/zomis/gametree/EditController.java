package net.zomis.gametree;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

import net.zomis.gametree.model.GameNode;
import net.zomis.gametree.model.GameTree;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public @ResponseBody String remove(@RequestParam(value="tree", required = false) Integer treeId, Model model, HttpServletRequest request) {
		logger.info("Remove connection! " + treeId + ", " + model.asMap());
		
		return "ok-" + model.asMap().get("from");
	}
	
	@RequestMapping(value = "/edit/connection/add", method = RequestMethod.POST)
	public @ResponseBody String add(@RequestParam("tree") Integer treeId, Model model, HttpServletRequest request) {
		logger.info("Add connection! " + treeId + ", " + model.asMap());
		return "add-" + treeId;
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
		return !node.getTree().getId().equals(tree);
	}

	private String withSession(Function<Session, String> object) {
		Session sess = sessionFactory.openSession();
		try {
			return object.apply(sess);
		}
		catch (Exception ex) {
			logger.error("error performing " + object, ex);
			return "error: " + ex;
		}
		finally {
			sess.close();
		}
	}

	}
	
}
