package java6.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java6.domain.Account;
import java6.domain.Product;
import java6.model.ProductDao;
import java6.reponsitory.ProductReponsitory;
import java6.service.AccountService;
import java6.service.ProductService;
import java6.service.StorageService;
import java6.utils.ImageUploader;

@Controller
@RequestMapping("admin/products")
public class ProductController {
	@Autowired
	ProductService productService;

	@Autowired
	StorageService storageService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private IndexController indexController;
	
	void fillToTable(ModelMap model) {
		List<Product> list = productService.findAll();

		model.addAttribute("products", list);
	}
	
	@GetMapping("view")
	public String view(ModelMap model) {
		String username=session.getAttribute("username").toString();
		Optional<Account> account=accountService.findById(username);
		
		if(account.get().getIsAdmin()==false) {
			return indexController.viewIndex(model);
		}
		
		fillToTable(model);

		model.addAttribute("product", new ProductDao());

		indexController.checkUser(model);
		return "adminUI/management-product";
	}

	@GetMapping("edit/{productId}")
	public ModelAndView edit(ModelMap model, @PathVariable("productId") String productId) {
		fillToTable(model);
		Optional<Product> optional = productService.findById(productId);
		ProductDao dao = new ProductDao();

		if (optional.isPresent()) {
			Product entity = optional.get();

			BeanUtils.copyProperties(entity, dao);

			model.addAttribute("product", dao);

			return new ModelAndView("/adminUI/management-product", model);
		}

		model.addAttribute("mess", "Không tìm thấy sản phẩm");

		return new ModelAndView("/adminUI/management-product", model);
	}

	@GetMapping("delete/{productId}")
	public ModelAndView delete(ModelMap model, @PathVariable("productId") String productId) throws IOException {
		fillToTable(model);
		Optional<Product> optional=productService.findById(productId);
		
		if (optional.isPresent()) {
		
			
			productService.delete(optional.get());
			model.addAttribute("mess", "Sản phẩm "+optional.get().getName()+" đã được xoá");
			
		}else {
			model.addAttribute("mess", "Không tìm thấy sản phẩm");
		}

		return new ModelAndView(view(model),model);
	}

	@GetMapping("/imgs/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serverFile(@PathVariable String fileName) {
		Resource file = storageService.loadAsResource(fileName);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("product") ProductDao dao,
			@RequestParam("imgFile") MultipartFile imageFile ,BindingResult result) throws IOException, ServletException {
		fillToTable(model);
		indexController.checkUser(model);
		if (result.hasErrors()) {
			model.addAttribute("mess", "Sai dữ liệu");
			
			return new ModelAndView("/adminUI/management-product", model);		
		}
		
		Product entity = new Product();
		BeanUtils.copyProperties(dao, entity);
		
		try {
             String uploadedImagePath= ImageUploader.saveImage(imageFile, imageFile.getOriginalFilename());
            if (uploadedImagePath != null) {
            	System.out.println("Image uploaded successfully");
            	
                entity.setImg(uploadedImagePath);
//                model.addAttribute("img", uploadedImagePath);
            } else {
            	System.out.println("Image uploaded failed");
            }
            System.out.println(uploadedImagePath);
        } catch (IOException e) {
            System.out.println("Image upload failed. " + e.getMessage());
        }
		
		productService.save(entity);
		

		model.addAttribute("mess", "Product is saved");
		model.addAttribute("product", entity);

		return new ModelAndView("/adminUI/management-product", model);
	}

	@PostMapping("search")
	public String search(ModelMap model,
			@RequestParam("nameSearch") String name) {
		indexController.checkUser(model);
		
		List<Product> list=null;
		
		if (StringUtils.hasText(name)) {
			list=productService.findByNameContaining(name);
		}else {
			list=productService.findAll();
		}
		model.addAttribute("countResult", list.size());
		
		model.addAttribute("products", list);
		return "userUI/search";
	}
	
	@GetMapping("searchPaginated")
	public String search(ModelMap model,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam("page")Optional<Integer>page,
			@RequestParam("size")Optional<Integer> size) {
		
		int currentPage=page.orElse(1);
		int pageSize=size.orElse(5);
		
		Pageable pageable=PageRequest.of(currentPage, pageSize, Sort.by("name"));
		Page<Product> resultPage=null;
		
		if (StringUtils.hasText(name)) {
			resultPage=productService.findByNameContaining(name, pageable);
			model.addAttribute("name", name);
		}else {
			resultPage=productService.findAll(pageable);
		}
		
		int totalPages=resultPage.getTotalPages();
		
		if (totalPages>0) {
			int start=Math.max(1, currentPage-2);
			int end=Math.min(currentPage+2, totalPages);
			
			if (totalPages>5) {
				if (end==totalPages) start=end-5;
				else if(start==1) end=start+5;
			}
			List<Integer>pageNumbers=IntStream.rangeClosed(start, end)
					.boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}
		model.addAttribute("productPage", resultPage);
		
		return "admin/products/searchPaginated";
	}
	

}
