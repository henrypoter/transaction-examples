package transactions.xa;

import transactions.jpa.entity.Account;

public interface AccountDao {

	/**
	 * Save account into database.
	 * 
	 * @param account
	 *            - account to be saved.
	 */
	void save(Account account);

	/**
	 * counts all the accounts.
	 * 
	 * @return - the number of accounts.
	 */
	Integer getAllCount();

}