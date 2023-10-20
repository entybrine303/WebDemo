package java6.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java6.domain.Account;
import java6.model.AccountDao;
import java6.service.AccountService;

@Controller
@RequestMapping("admin/accounts")
public class AccountController {
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private IndexController indexController;

	void fillToTable(ModelMap model) {
		List<Account> list = accountService.findAll();
		model.addAttribute("accounts", list);
	}

	@GetMapping("view")
	public String view(ModelMap model) {
		String username=session.getAttribute("username").toString();
		Optional<Account> account=accountService.findById(username);
		
		if(account.get().getIsAdmin()==false) {
			return indexController.viewIndex(model);
		}
		
		fillToTable(model);
		model.addAttribute("account", new AccountDao());

		indexController.checkUser(model);
		return "adminUI/management-account";
	}
	

	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("account") AccountDao dao,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("adminUI/management-account");
		}

		if (!dao.getConfirmPassword().equals(dao.getPassword())) {
			model.addAttribute("mess", "Mật khẩu không trùng khớp");
			return new ModelAndView(view(model));
		}

		Account entity = new Account();
		BeanUtils.copyProperties(dao, entity);
		
		entity.setIsAdmin(true);

		accountService.save(entity);

		model.addAttribute("mess", "Tài khoản đã được lưu thành công");
		
		return new ModelAndView(view(model), model);
	}


	@GetMapping("edit/{username}")
	public ModelAndView edit(ModelMap model, @PathVariable("username") String username) {
fillToTable(model);
		Optional<Account> optional = accountService.findById(username);
		AccountDao dao = new AccountDao();

		if (optional.isPresent()) {
			Account entity = optional.get();
																			
			BeanUtils.copyProperties(entity, dao);
			
			dao.setPassword(null);

			model.addAttribute("account", dao);
			
			return new ModelAndView("/adminUI/management-account", model);
		}

		model.addAttribute("mess", "Không tìm thấy tài khoản");


		return new ModelAndView("/adminUI/management-account", model);
	}

	@GetMapping("delete/{username}")
	public ModelAndView delete(ModelMap model, @PathVariable("username") String username) throws IOException {
		fillToTable(model);
		Optional<Account> optional=accountService.findById(username);
		
		if (optional.isPresent()) {
			
			accountService.delete(optional.get());
			model.addAttribute("mess", "Tài khoản "+optional.get().getUsername()+" đã được xoá");
			
		}else {
			model.addAttribute("mess", "Không tìm thấy tài khoản");
		}
		
		return new ModelAndView(view(model),model);
	}
//
//	@GetMapping("/imgs/{filename:.+}")
//	@ResponseBody
//	public ResponseEntity<Resource> serverFile(@PathVariable String fileName) {
//		Resource file = storageService.loadAsResource(fileName);
//
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//				.body(file);
//	}

//	@GetMapping("search")
//	public String search(ModelMap model,
//			@RequestParam(name = "name", required = false)String name) {
//		List<account> list=null;
//		
//		if (StringUtils.hasText(name)) {
//			list=accountService.findByNameContaining(name);
//		}else {
//			list=accountService.findAll();
//		}
//		
//		model.addAttribute("accounts", list);
//		return "adminUI/accounts/search";
//	}
//	
//	@GetMapping("searchPaginated")
//	public String search(ModelMap model,
//			@RequestParam(name = "name", required = false) String name,
//			@RequestParam("page")Optional<Integer>page,
//			@RequestParam("size")Optional<Integer> size) {
//		
//		int currentPage=page.orElse(1);
//		int pageSize=size.orElse(5);
//		
//		Pageable pageable=PageRequest.of(currentPage-1, pageSize, Sort.by("name"));
//		Page<account> resultPage=null;
//		
//		if (StringUtils.hasText(name)) {
//			resultPage=accountService.findByNameContaining(name, pageable);
//			model.addAttribute("name", name);
//		}else {
//			resultPage=accountService.findAll(pageable);
//		}
//		
//		int totalPages=resultPage.getTotalPages();
//		if (totalPages>0) {
//			int start=Math.max(1, currentPage-2);
//			int end=Math.min(currentPage+2, totalPages);
//			
//			if (totalPages>5) {
//				if (end==totalPages) start=end-5;
//				else if(start==1) end=start+5;
//			}
//			List<Integer>pageNumbers=IntStream.rangeClosed(start, end)
//					.boxed().collect(Collectors.toList());
//			model.addAttribute("pageNumbers", pageNumbers);
//		}
//		model.addAttribute("productPage", resultPage);
//		
//		return "adminUI/accounts/searchPaginated";
//	}
}
