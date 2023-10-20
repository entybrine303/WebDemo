package java6.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDao {
	private String productId;
	private String name;
	private int quantity;
	private double price;
	
	private String img;
	private MultipartFile imgFile;
	
	private String description;

	private String facture;
//	private double sale;
}
