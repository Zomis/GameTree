package net.zomis.gametree;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.zomis.gametree.model.GameTree;

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
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(Locale locale, Model model, HttpServletRequest request) {
		logger.info("Create new!", locale);
		return new ModelAndView("create", "tree", new GameTree());
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(Locale locale, @PathVariable Integer id, Model model, HttpServletRequest request) {
		logger.info("View!", locale);
		Session session = sessionFactory.openSession();
		GameTree tree = (GameTree) session.get(GameTree.class, id);
		model.addAttribute("tree", tree);
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
