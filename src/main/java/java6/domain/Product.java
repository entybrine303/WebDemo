package java6.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product implements Serializable{
	@Id
	@Column(columnDefinition = "nvarchar(10)")
	private String productId;
	@Column(columnDefinition = "nvarchar(200)")
	private String name;
	private int quantity;
	private double price;
	@Column(columnDefinition = "nvarchar(500)")
	private String img;
	@Column(columnDefinition = "nvarchar(500)")
	private String description;
	private String facture;
//	private double sale;
}
