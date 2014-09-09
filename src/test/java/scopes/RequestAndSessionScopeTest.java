package scopes;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:session-request-scope-context.xml"})
public class RequestAndSessionScopeTest {

    @Autowired
    private SessionScopedService sessionScopedService;

    @Autowired
    private RequestScopedService requestScopedService;

    @Autowired
    private MockHttpSession mockHttpSession;

    @Autowired
    private MockHttpServletRequest mockHttpServletRequest;

    @Test
    public void givenASessionScopeBeanWhenConstructorValueSetAsSessionAttributeThenBeanShouldBeCreatedWithThisParameter() {
        final String serviceName = "sessionService";
        mockHttpSession.setAttribute("serviceName", serviceName);
        Assert.assertEquals(serviceName, sessionScopedService.getName());
    }

    @Test
    public void givenARequestScopeBeanWhenConstructorValueSetAsRequestAttributeThenBeanShouldBeCreatedWithThisParameter() {
        final String serviceName = "requestService";
        mockHttpServletRequest.setParameter("serviceName", serviceName);
        Assert.assertEquals(serviceName, requestScopedService.getName());
    }
}
