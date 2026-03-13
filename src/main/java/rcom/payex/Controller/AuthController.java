package rcom.payex.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rcom.payex.model.User;
import rcom.payex.service.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class AuthController {
	String mainLayout = "master";

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}

	@Autowired
	UserService userService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */

	public boolean isLoged(HttpSession session) {
		return session.getAttribute("user") != null;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginGet(Locale locale, Model model, RedirectAttributes redirectAttrs) {
		
		model.addAttribute("layout", "login");
		return mainLayout;
	}

	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPost(Locale locale, Model model, HttpSession session, RedirectAttributes redirectAttrs,
			@ModelAttribute("user") User user) {

		user.setRole("support");
		User tempUser = userService.authUser(user);

		if (tempUser != null) {
			session.setAttribute("user", tempUser);
			session.setAttribute("role", tempUser.getRole());
			session.setAttribute("op", tempUser.getOperation());
		}else{
			redirectAttrs.addFlashAttribute("msg", "Wrong UserId Or Password");
			return "redirect:/login";
		}		
		
		return "redirect:/";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Locale locale, Model model, HttpSession session) {
		session.removeAttribute("user");
		session.removeAttribute("role");
		session.invalidate();
		return "redirect:/";
	}
}
