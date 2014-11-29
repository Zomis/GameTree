package net.zomis.gametree;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
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

	@RequestMapping(value = "/db", method = RequestMethod.GET)
	public String db(Locale locale, Model model, HttpServletRequest request) {
		Session sess = sessionFactory.openSession();
		logger.info("Session is " + sess);
		sess.close();
		return "home";
	}

}
