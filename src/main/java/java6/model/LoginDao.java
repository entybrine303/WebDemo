package java6.model;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDao{
	private String username;
	@Length(min = 3)
	private String password;
	
	private Boolean remember=false;
}
