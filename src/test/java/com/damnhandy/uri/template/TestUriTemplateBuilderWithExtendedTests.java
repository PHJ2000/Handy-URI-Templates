package com.damnhandy.uri.template;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author <a href="ryan@damnhandy.com">Ryan J. McDonough</a>
 * Created by ryan on 11/14/15.
 */
public class TestUriTemplateBuilderWithExtendedTests extends AbstractUriTemplateBuilderTests
{

    static Stream<Arguments> testData() throws Exception
    {
        File file = new File("./uritemplate-test/extended-tests.json");
        return loadTestData(file);
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("testData")
    void test(Map<String, Object> vars, String template, Object expected, String testsuite) throws Exception
    {
        executeBuilderTest(vars, template, expected, testsuite);
    }
}
