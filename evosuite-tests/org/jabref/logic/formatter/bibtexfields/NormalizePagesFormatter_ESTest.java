/*
 * This file was automatically generated by EvoSuite
 * Fri May 28 16:06:00 GMT 2021
 */

package org.jabref.logic.formatter.bibtexfields;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jabref.logic.formatter.bibtexfields.NormalizePagesFormatter;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class NormalizePagesFormatter_ESTest extends NormalizePagesFormatter_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      NormalizePagesFormatter normalizePagesFormatter0 = new NormalizePagesFormatter();
      String string0 = normalizePagesFormatter0.format("1 - 2");
      assertEquals("1--2", string0);
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      NormalizePagesFormatter normalizePagesFormatter0 = new NormalizePagesFormatter();
      // Undeclared exception!
      try { 
        normalizePagesFormatter0.format((String) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("java.util.Objects", e);
      }
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      NormalizePagesFormatter normalizePagesFormatter0 = new NormalizePagesFormatter();
      String string0 = normalizePagesFormatter0.format("\u2013|\u2014");
      assertEquals("--|--", string0);
  }

  @Test(timeout = 4000)
  public void test3()  throws Throwable  {
      NormalizePagesFormatter normalizePagesFormatter0 = new NormalizePagesFormatter();
      String string0 = normalizePagesFormatter0.format("Vietnamese");
      assertEquals("Vietnamese", string0);
  }

  @Test(timeout = 4000)
  public void test4()  throws Throwable  {
      NormalizePagesFormatter normalizePagesFormatter0 = new NormalizePagesFormatter();
      String string0 = normalizePagesFormatter0.format("");
      assertEquals("", string0);
  }

  @Test(timeout = 4000)
  public void test5()  throws Throwable  {
      NormalizePagesFormatter normalizePagesFormatter0 = new NormalizePagesFormatter();
      String string0 = normalizePagesFormatter0.getDescription();
      assertEquals("Normalize pages to BibTeX standard.", string0);
  }

  @Test(timeout = 4000)
  public void test6()  throws Throwable  {
      NormalizePagesFormatter normalizePagesFormatter0 = new NormalizePagesFormatter();
      String string0 = normalizePagesFormatter0.getKey();
      assertEquals("normalize_page_numbers", string0);
  }

  @Test(timeout = 4000)
  public void test7()  throws Throwable  {
      NormalizePagesFormatter normalizePagesFormatter0 = new NormalizePagesFormatter();
      String string0 = normalizePagesFormatter0.getName();
      assertEquals("Normalize page numbers", string0);
  }

  @Test(timeout = 4000)
  public void test8()  throws Throwable  {
      NormalizePagesFormatter normalizePagesFormatter0 = new NormalizePagesFormatter();
      String string0 = normalizePagesFormatter0.getExampleInput();
      assertEquals("1 - 2", string0);
  }
}