package transactions.xa;

import javax.persistence.PersistenceException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import transactions.jpa.entity.BankAccount;
import transactions.jpa.entity.InvestmentAccount;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:xa-context.xml" })
@EnableTransactionManagement
public class XATransactionsTest {

	private static final Logger logger = LoggerFactory
			.getLogger(XATransactionsTest.class);

	@Autowired
	private InterAccountService interAccountService;

	@Autowired
	private BankAccountDao bankAccountDao;

	@Autowired
	private InvestmentAccountDao investmentAccountDao;

	@Test
	public void testBothEntityShouldBeInserted() {
		interAccountService.save(new BankAccount(), new InvestmentAccount());
		Assert.assertEquals(1, bankAccountDao.getAllCount().intValue());
		Assert.assertEquals(1, investmentAccountDao.getAllCount().intValue());
	}

	@Test
	public void testBothEntityShouldNotBeInserted() {
		try {
			interAccountService.save(new BankAccount(), new InvestmentAccount(
					"moreThanEightCharacters"));
			Assert.fail();
		} catch (PersistenceException e) {
			Assert.assertEquals(0, bankAccountDao.getAllCount().intValue());
			Assert.assertEquals(0, investmentAccountDao.getAllCount()
					.intValue());
		}
	}

}
