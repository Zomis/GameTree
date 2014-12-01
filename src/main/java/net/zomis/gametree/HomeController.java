package net.zomis.gametree;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.zomis.gametree.model.GameNode;
import net.zomis.gametree.model.GameTree;
import net.zomis.gametree.model.NodeTag;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes
public class HomeController {
	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/threads", method = RequestMethod.GET)
	public String threads(Model model, HttpServletRequest request) {
		model.addAttribute("threads", Thread.getAllStackTraces());
		return "threads";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, HttpServletRequest request) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		return "home";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createApply(@ModelAttribute("tree") GameTree tree, Model model) {
		logger.info("Created new tree! " + tree + " with name " + tree.getName());
		Session session = sessionFactory.openSession();
		model.addAttribute("tree", tree);
		session.beginTransaction();
		session.persist(tree);
		session.getTransaction().commit();
		session.close();
		return "created";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@RequestParam("tree") Integer treeId, Model model, HttpServletRequest request) {
		logger.info("Create new node!");
		model.addAttribute("node", new GameNodeForm());
		model.addAttribute("treeId", treeId);
		return "add";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addApply(@ModelAttribute("node") GameNodeForm nodeData, Model model) {
		logger.info("Created new node! " + nodeData + " with name " + nodeData.getName());
		Session session = sessionFactory.openSession();
		GameTree tree = (GameTree) session.get(GameTree.class, nodeData.getTree());
		GameNode node = new GameNode();
		node.setTree(tree);
		
		node.setName(nodeData.getName());
		for (String parentId : nodeData.getParents().split(" ")) {
			try {
				GameNode parentNode = (GameNode) session.get(GameNode.class, Integer.parseInt(parentId));
				if (parentNode != null) {
					node.addParent(parentNode);
				}
			}
			catch (NumberFormatException ex) {
			}
		}
		
		for (String tagId : nodeData.getTags().split(" ")) {
			if (tagId.isEmpty()) {
				continue;
			}
			
			@SuppressWarnings("unchecked")
			List<NodeTag> nodeTag = session.createQuery("select nodeTag from NodeTag as nodeTag where nodeTag.name = :tag")
				.setString("tag", tagId)
				.list();
			NodeTag tag;
			if (nodeTag.isEmpty()) {
				session.beginTransaction();
				tag = new NodeTag();
				tag.setName(tagId);
				session.persist(tag);
				session.getTransaction().commit();
			}
			else {
				tag = nodeTag.iterator().next();
			}
			
			node.addTag(tag);
		}
		
		model.addAttribute("nodeAdded", node);
		model.addAttribute("justAdded", true);
		model.addAttribute("node", new GameNodeForm());
		model.addAttribute("treeId", tree.getId());
		session.beginTransaction();
		tree.addNode(node);
		session.update(tree);
		session.getTransaction().commit();
		session.close();
		return "add";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(Locale locale, Model model, HttpServletRequest request) {
		logger.info("Create new!", locale);
		return new ModelAndView("create", "tree", new GameTree());
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(Locale locale, @RequestParam Boolean edit, @PathVariable Integer id, Model model, HttpServletRequest request) {
		logger.info("View!", locale);
		Session session = sessionFactory.openSession();
		GameTree tree = (GameTree) session.get(GameTree.class, id);
		for (GameNode node : tree.getNodes()) {
			node.tagNames(); // fetch node tags
		}
		model.addAttribute("nodes", tree.getNodes());
		model.addAttribute("nodePositions", tree.findPositions());
		model.addAttribute("connections", tree.findConnections());
		model.addAttribute("tree", tree);
		model.addAttribute("treeId", tree.getId());
		model.addAttribute("editmode", edit);
		session.close();
		return "view";
	}

	@RequestMapping(value = "/db", method = RequestMethod.GET)
	public String db(Locale locale, Model model, HttpServletRequest request) {
		Session sess = sessionFactory.openSession();
		logger.info("Session is " + sess);
		sess.close();
		return "home";
	}

}
