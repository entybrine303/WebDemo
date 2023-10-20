package java6.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java6.domain.Account;
import java6.domain.Product;
import java6.model.LoginDao;
import java6.service.AccountService;
import java6.service.ProductService;

@Controller
@RequestMapping()
public class IndexController {
	@Autowired
	private ProductService productService;

	@Autowired
	private HttpSession session;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private AccountService accountService;
	

	@GetMapping("msm.com")
	public String viewIndex(ModelMap model) {
		try {
			List<Product> products = productService.findAll();
			System.out.println(session.getAttribute("username"));

			model.addAttribute("products", products);

//			Kiểm tra có sản phẩm nào của các hãng dưới đây để fill lên index hay ko
			model.addAttribute("iPhone", checkFacture("iPhone"));
			model.addAttribute("samsung", checkFacture("Samsung"));
			model.addAttribute("xiaomi", checkFacture("Xiaomi"));

			checkUser(model);

		} catch (Exception e) {
			e.printStackTrace();

			model.addAttribute("mess", e.getMessage());
		}

		return "userUI/index";
	}

	@GetMapping("detailProduct/{productId}")
	public ModelAndView detailProduct(ModelMap model, @PathVariable("productId") String productId) {
		try {
			Optional<Product> product = productService.findById(productId);
			Product product2 = product.get();
			model.addAttribute("product", product2);

			List<Product> products = productService.findAll();
			model.addAttribute("products", products);

			checkUser(model);
		} catch (Exception e) {
			e.printStackTrace();

			model.addAttribute("mess", e.getMessage());
		}

		return new ModelAndView("userUI/detail-product", model);
	}

	public boolean checkFacture(String nameFacture) {
		String sql = "SELECT COUNT(*) FROM product WHERE facture = '" + nameFacture + "'";
		int count = jdbcTemplate.queryForObject(sql, Integer.class);

		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
	

	public void checkUser(ModelMap model) {
//    	Lấy dữ liệu
		Object username = session.getAttribute("username");
		model.addAttribute("username", username);

		if (username != null) {
//		Truyền username vào phần tk của header 
			model.addAttribute("username_a", username.toString());
			
//			Kiểm tra vai trò của người dùng
			Optional<Account> accounts = accountService.findById(username.toString());
			model.addAttribute("isAdmin", accounts.get().getIsAdmin());

		} else {
			model.addAttribute("username_a", "Tài khoản");
		}

	}

}
