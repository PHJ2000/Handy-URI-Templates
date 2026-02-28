/*
 * Copyright 2012, Ryan J. McDonough
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.damnhandy.uri.template.conformance;

import com.damnhandy.uri.template.UriTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.provider.Arguments;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:ryan@damnhandy.com">Ryan J. McDonough</a>
 */
public abstract class AbstractUriTemplateConformanceTest
{

    /**
     * <p>
     * Loads the test data from the JSON file and generated the parameter list.
     * </p>
     *
     * @param file
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    protected static Stream<Arguments> loadTestData(File file) throws Exception
    {
        InputStream in = new FileInputStream(file);
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> testsuites = mapper.readValue(in, new TypeReference<Map<String, Object>>()
            {
            });

            List<Arguments> params = new ArrayList<>();
            for (Map.Entry<String, Object> entry : testsuites.entrySet())
            {
                String name = entry.getKey();
                Map<String, Object> testsuite = (Map<String, Object>) entry.getValue();
                Map<String, Object> variables = (Map<String, Object>) testsuite.get("variables");
                List<List<Object>> testcases = (List<List<Object>>) testsuite.get("testcases");

                for (List<Object> test : testcases)
                {
                    params.add(Arguments.of(variables, test.get(0), test.get(1), name));
                }
            }
            return params.stream();
        }
        finally
        {
            in.close();
        }


    }

    /**
     * Executes a conformance test case.
     *
     * @param variables
     * @param template
     * @param expected
     * @param testsuite
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    protected static void executeConformanceTest(Map<String, Object> variables, String template, Object expected, String testsuite) throws Exception
    {
        UriTemplate t = UriTemplate.fromTemplate(template);
        String actual;
        try
        {
            actual = t.expand(variables);
            if (expected instanceof String)
            {
                Assertions.assertEquals(expected, actual, testsuite + "->  Template: " + template);
            }
            else if (expected instanceof Collection)
            {
                List<String> combinations = (List<String>) expected;
                boolean match = false;
                for (String combo : combinations)
                {
                    if (combo.equalsIgnoreCase(actual))
                    {
                        match = true;
                        break;
                    }
                }
                Assertions.assertTrue(match, testsuite + "->  Template: " + template + " returned " + actual + " and did not match any combination");
            }
        }
        catch (Exception e)
        {
            Assertions.fail(testsuite + "->  Template: " + template + " returned " + e.getMessage() + " and did not match any combination");
        }

    }
}
