package java6.controller;

import java.util.Optional;

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
import java6.model.ChangePasswordDao;
import java6.model.RegisterDao;
import java6.service.AccountService;

@Controller
@RequestMapping("accounts")
public class ChangePassController {
	@Autowired
	private AccountService accountService;

	@Autowired
	private HttpSession session;

	@Autowired
	private IndexController indexController;
	
	@GetMapping("changePassword")
	public String viewChangePassForm(ModelMap model) {
		indexController.checkUser(model);
		
		try {
			Account account=new Account();
			
			account.setUsername(session.getAttribute("username").toString());
			model.addAttribute("account", account);
			model.addAttribute("disableInput", true);
		} catch (Exception e) {
			return "userUI/index";		
			}
		
		
		
		
		return "userUI/change-pass";
	}
	
	@PostMapping("pChangePassword")
	public ModelAndView changePassword(ModelMap model, 
			@Valid @ModelAttribute("account") ChangePasswordDao dao, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView(viewChangePassForm(model));
		}
		if (!dao.getConfirmPassword().equals(dao.getNewPassword())) {
			model.addAttribute("mess", "Đổi mật khẩu thất bại");
			return new ModelAndView(viewChangePassForm(model));
		}
		
		Account entity = new Account();
		Optional<Account> acc=accountService.findById(session.getAttribute("username").toString());
		
		BeanUtils.copyProperties(dao, entity);
		
		entity.setPassword(dao.getNewPassword());
		entity.setIsAdmin(acc.get().getIsAdmin());
		
		accountService.save(entity);

		model.addAttribute("mess", "Đổi mật khẩu thành công");
		return new ModelAndView(viewChangePassForm(model));
		
	}
}
