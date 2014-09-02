package transactions.jpa.entity;

import javax.persistence.Entity;

@Entity
public class BankAccount extends Account {

	public BankAccount() {

	}

	public BankAccount(String name) {
		super(name);
	}

}
