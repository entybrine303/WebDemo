package java6.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer{
	private int customerId;
	private String name;
	private String email;
	private String phone;
	private String locate;
	private Date OderDate;
}
