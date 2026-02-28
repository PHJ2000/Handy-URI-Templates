package com.damnhandy.uri.template;

import com.damnhandy.uri.template.conformance.AbstractUriTemplateConformanceTest;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

/**
 * @author <a href="ryan@damnhandy.com">Ryan J. McDonough</a>
 * Created by ryan on 11/14/15.
 */
public abstract class AbstractUriTemplateBuilderTests extends AbstractUriTemplateConformanceTest
{

    /**
     * Executes a builder conformance test case.
     *
     * @param variables
     * @param template
     * @param expected
     * @param testsuite
     * @throws Exception
     */
    protected static void executeBuilderTest(Map<String, Object> variables, String template, Object expected, String testsuite) throws Exception
    {
        UriTemplate original = UriTemplate.fromTemplate(template);
        UriTemplateBuilder originalBuilder = UriTemplate.buildFromTemplate(original);
        UriTemplateComponent[] components = originalBuilder.getComponents();

        UriTemplateBuilder builder = UriTemplate.createBuilder();
        for(UriTemplateComponent c : components)
        {
            builder.addComponent(c);
        }
        UriTemplate rebuiltTemplate = builder.build();

        Assertions.assertEquals(original.getTemplate(), rebuiltTemplate.getTemplate());
    }
}
