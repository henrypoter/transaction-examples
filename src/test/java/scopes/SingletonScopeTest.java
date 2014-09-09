package scopes;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:singleton-scope-context.xml"})
public class SingletonScopeTest {

    @Autowired
    private FirstBookingFacade firstSingletonBookingFacade;

    @Autowired
    private SecondBookingFacade secondSingletonBookingFacade;

    @Test
    public void givenASingletonBeanDefinitionWhenWrappedInTwoFacadesThenTheWrappedBeansShouldBeIdentical() {
        Assert.assertEquals(getHashCode(firstSingletonBookingFacade), getHashCode(secondSingletonBookingFacade));
    }

    private int getHashCode(BookingFacade bookingFacade) {
        return bookingFacade.getBookingService().hashCode();
    }

}
