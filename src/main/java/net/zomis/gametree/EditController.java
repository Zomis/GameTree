package net.zomis.gametree;

import javax.servlet.http.HttpServletRequest;

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
	public @ResponseBody String editNode(@RequestParam Integer tree, @RequestParam Integer node, 
			@RequestParam String name, @RequestParam String tags) {
		logger.info("Edit node! " + tree + ", ");
		
		return String.format("ok-%s-%s-%s-%s", tree, node, name, tags);
	}
	
}
