package transactions.jpa.entity;

import javax.persistence.Entity;

@Entity
public class InvestmentAccount extends Account {

	public InvestmentAccount() {
	}

	public InvestmentAccount(String name) {
		super(name);
	}

}
