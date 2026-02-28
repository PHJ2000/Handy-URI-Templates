/*
 *
 *
 */
package com.damnhandy.uri.template.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.damnhandy.uri.template.MalformedUriTemplateException;
import com.damnhandy.uri.template.UriTemplateComponent;

/**
 * A TestCharacterScanner.
 *
 * @author <a href="ryan@damnhandy.com">Ryan J. McDonough</a>
 * @version $Revision: 1.1 $
 */
public class TestUriTemplateParser
{

   private static final Pattern URI_TEMPLATE_REGEX = Pattern.compile("\\{[^{}]+\\}");

   @Test
   public void testWithNoExpressions() throws Exception
   {
      String template = "http://example.com/";
      UriTemplateParser e = new UriTemplateParser();
      List<UriTemplateComponent> expressions = e.scan(template);
      Assertions.assertEquals(1, expressions.size());
      Assertions.assertEquals("http://example.com/", expressions.get(0).getValue());
   }

   @Test
   public void testGoodTemplate() throws Exception
   {
      String template = "http://example.com/{expr}/thing/{other}";
      UriTemplateParser e = new UriTemplateParser();
      List<UriTemplateComponent> expressions = e.scan(template);
      List<String> regExExpr = scanWithRegEx(template);
      Assertions.assertEquals(4, expressions.size());
      Assertions.assertEquals("http://example.com/", expressions.get(0).getValue());
      Assertions.assertEquals(regExExpr.get(0), expressions.get(1).getValue());
      Assertions.assertEquals("/thing/", expressions.get(2).getValue());
      Assertions.assertEquals(regExExpr.get(1), expressions.get(3).getValue());
   }


   @Test
   public void testGoodTemplateWithOperators() throws Exception
   {
      UriTemplateParser e = new UriTemplateParser();
      List<UriTemplateComponent> expressions = e.scan("http://example.com/{expr}/thing/{?other,thing}");
      Assertions.assertEquals(4, expressions.size());
      Assertions.assertEquals("http://example.com/", expressions.get(0).getValue());
      Assertions.assertEquals("{expr}", expressions.get(1).getValue());
      Assertions.assertEquals("/thing/", expressions.get(2).getValue());
      Assertions.assertEquals("{?other,thing}", expressions.get(3).getValue());
   }

   /**
    * Checking that we correctly catch an unbalanced expression
    *
    * @throws Exception
    */
   @Test
   public void testStartExpressionWithNoTermination() throws Exception
   {
      Assertions.assertThrows(MalformedUriTemplateException.class, () -> {
          UriTemplateParser e = new UriTemplateParser();
          List<UriTemplateComponent> expressions = e.scan("http://example.com/{expr/thing");
          Assertions.assertEquals(expressions.size(), 2);
          System.out.println(expressions);
      });
   }

   /**
    * Since the expression is never opened, we should not find any expressions.
    *
    * @throws Exception
    */
   @Test
   public void testStartExpressionWithTerminationButNoStartBrace() throws Exception
   {
      Assertions.assertThrows(MalformedUriTemplateException.class, () -> {
          UriTemplateParser e = new UriTemplateParser();
          List<UriTemplateComponent> expressions = e.scan("http://example.com/expr}/thing");
          Assertions.assertEquals(expressions.size(), 2);
          System.out.println(expressions);
      });
   }

   /**
    * Checking that we correctly catch an unbalanced expression
    *
    * @throws Exception
    */
   @Test
   public void testUnbalanceExpression() throws Exception
   {
      Assertions.assertThrows(MalformedUriTemplateException.class, () -> {
          UriTemplateParser e = new UriTemplateParser();
          List<UriTemplateComponent> expressions = e.scan("http://example.com/{expr/thing/{other}");
          Assertions.assertNotNull(expressions);
      });
   }


   private List<String> scanWithRegEx(String templateString)
   {
      Matcher matcher = URI_TEMPLATE_REGEX.matcher(templateString);
      List<String> expressionList = new LinkedList<String>();
      while (matcher.find())
      {
         String e = matcher.group();
         expressionList.add(e);
      }
      return expressionList;
   }
}
