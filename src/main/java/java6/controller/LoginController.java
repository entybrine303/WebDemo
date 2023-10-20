package java6.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java6.domain.Account;
import java6.model.LoginDao;
import java6.service.AccountService;

@Controller
@RequestMapping("accounts")
public class LoginController {
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private  HttpServletRequest request;

	@Autowired
	private IndexController indexController;
	
	@GetMapping("login")
	public String viewLoginForm(ModelMap model) {
		// Lưu lại đường dẫn trước khi ấn vào đăng nhập 
        String referer = request.getHeader("referer");
        request.getSession().setAttribute("referer", referer);
		
		model.addAttribute("account", new LoginDao());
		
		indexController.checkUser(model);
		return "/userUI/login";
	}
	
	@PostMapping("pLogin")
	public ModelAndView login(ModelMap model, 
			@Valid @ModelAttribute("account") LoginDao dao, BindingResult result) {
		indexController.checkUser(model);
		if (result.hasErrors()) {
			return new ModelAndView("userUI/login", model);
		}
		
		Account account=accountService.login(dao.getUsername(), dao.getPassword());
		
		if (account==null) {
			model.addAttribute("mess", "Invalid username or password");
			return new ModelAndView("userUI/login", model);
		}
		
		session.setAttribute("username", account.getUsername());
		
		 // Lấy đường dẫn trang trước đó từ session
        String referer = (String) request.getSession().getAttribute("referer");
        if (referer != null && !referer.isEmpty()) {
            // Xóa thông tin đã lưu trong session
            request.getSession().removeAttribute("referer");
            // Chuyển hướng về trang trước đó
            return new ModelAndView("redirect:" + referer);
        }
		
		return new ModelAndView("userUI/index");
	}
	

	@GetMapping("logout")
	public ModelAndView logout(ModelMap model) {
		session.setAttribute("username", null);
		
		 // Chuyển hướng về trang hiện tại (load lại trang)
        String referer = request.getHeader("referer");
		return new ModelAndView("redirect:" + referer);
	}
}
