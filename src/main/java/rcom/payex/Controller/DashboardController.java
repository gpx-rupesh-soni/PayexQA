package rcom.payex.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rcom.payex.model.User;

/**
 * Handles requests for the application home page.
 */
@Controller
public class DashboardController {
	String mainLayout = "master";
	private static Logger logger = LogManager.getLogger(DashboardController.class);

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	public boolean isLoged(HttpSession session) {
		return session.getAttribute("user") != null;
	}
	

	public boolean hasRole(HttpSession session, String role) {
		return session.getAttribute("role").toString().equalsIgnoreCase(role);
	}

	public String getRole(HttpSession session) {
		logger.info("Role is :"+ ""+session.getAttribute("role"));
		return session.getAttribute("role").toString();
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model,HttpSession session,
			@RequestParam(value = "t", required = false, defaultValue = "1") int tab,
			@RequestParam(value = "p", required = false, defaultValue = "1") int pageNo) {
		
		if(!isLoged(session)) return "redirect:/login";
		else return "redirect:/dashboard";
	
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard(Locale locale, Model model,HttpSession session,
			@RequestParam(value = "t", required = false, defaultValue = "d") String tab,
			@RequestParam(value = "date", required = false, defaultValue = "today") String date
			) {

		if(!isLoged(session)) return "redirect:/login";
		User user=(User) session.getAttribute("user");
		logger.info("next due date  "+date);		
		model.addAttribute("layout", "dashboard/index");
		
		return mainLayout;
	}
	
}
