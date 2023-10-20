package java6.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customerId;
	@Column(columnDefinition = "nvarchar(150)")
	private String name;
	@Column(columnDefinition = "nvarchar(150)")
	private String email;
	@Column(length = 10)
	private String phone;
	@Column(columnDefinition = "nvarchar(1000)")
	private String locate;
	@Temporal(TemporalType.DATE)
	private Date OderDate;
}
