package scopes;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:prototype-scope-context.xml"})
public class PrototypeScopeTest {

    @Autowired
    private FirstBookingFacade firstSingletonBookingFacade;

    @Autowired
    private SecondBookingFacade secondSingletonBookingFacade;

    @Test
    public void givenAProtoypeBeanDefinitionWhenWrappedInTwoFacadesThenTheWrappedBeansShouldNotBeIdentical() {
        Assert.assertNotEquals(firstSingletonBookingFacade.getBookingService(), secondSingletonBookingFacade.getBookingService());
    }

}
