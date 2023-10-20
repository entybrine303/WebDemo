package java6.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
import java6.model.RegisterDao;
import java6.service.AccountService;

@Controller
@RequestMapping("accounts")
public class RegisterController {
	@Autowired
	private AccountService accountService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private IndexController indexController;

	@GetMapping("register")
	public String viewRegisterForm(ModelMap model) {
		model.addAttribute("account", new RegisterDao());

		indexController.checkUser(model);
		return "userUI/register";
	}
	
	@PostMapping("pRegister")
	public ModelAndView login(ModelMap model, 
			@Valid @ModelAttribute("account") RegisterDao dao, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("userUI/register");
		}
		if (!dao.getConfirmPassword().equals(dao.getPassword())) {
			model.addAttribute("mess", "Đăng kí thất bại! Mật khẩu không trùng khớp");
			return new ModelAndView("userUI/register");
		}
		
		Account entity = new Account();
		
		BeanUtils.copyProperties(dao, entity);
		
		entity.setIsAdmin(false);

		accountService.save(entity);

		model.addAttribute("mess", "Đăng kí thành công");

		return new ModelAndView("userUI/login");
		
	}
}
