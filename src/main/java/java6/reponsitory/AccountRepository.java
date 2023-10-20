package java6.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import java6.domain.Account;

public interface AccountRepository extends JpaRepository<Account, String>{

}
