/*
 * This file was automatically generated by EvoSuite
 * Sun May 30 12:32:29 GMT 2021
 */

package org.jabref.logic.bibtex.comparator;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jabref.logic.bibtex.comparator.BibtexStringComparator;
import org.jabref.model.entry.BibtexString;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class BibtexStringComparator_ESTest extends BibtexStringComparator_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      BibtexStringComparator bibtexStringComparator0 = new BibtexStringComparator(false);
      // Undeclared exception!
      try { 
        bibtexStringComparator0.compare((BibtexString) null, (BibtexString) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // Cannot invoke \"org.jabref.model.entry.BibtexString.getName()\"
         //
         verifyException("org.jabref.logic.bibtex.comparator.BibtexStringComparator", e);
      }
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      BibtexStringComparator bibtexStringComparator0 = new BibtexStringComparator(true);
      BibtexString bibtexString0 = new BibtexString("2\"H2AHwFF2tJh", "*Mff; Zi+)B}2r_#e");
      BibtexString bibtexString1 = new BibtexString("l06C", "~oCfW&YY5p]#un$|[%{");
      int int0 = bibtexStringComparator0.compare(bibtexString0, bibtexString1);
      assertEquals((-58), int0);
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      BibtexString bibtexString0 = new BibtexString("z", "z");
      BibtexString bibtexString1 = new BibtexString("", "z");
      BibtexStringComparator bibtexStringComparator0 = new BibtexStringComparator(false);
      int int0 = bibtexStringComparator0.compare(bibtexString0, bibtexString1);
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test3()  throws Throwable  {
      BibtexString bibtexString0 = new BibtexString("z", "z");
      BibtexString bibtexString1 = new BibtexString("", "z");
      BibtexStringComparator bibtexStringComparator0 = new BibtexStringComparator(true);
      int int0 = bibtexStringComparator0.compare(bibtexString0, bibtexString1);
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test4()  throws Throwable  {
      BibtexStringComparator bibtexStringComparator0 = new BibtexStringComparator(true);
      BibtexString bibtexString0 = new BibtexString("2\"H2AHwFF2tJh", "*Mff; Zi+)B}2r_#e");
      int int0 = bibtexStringComparator0.compare(bibtexString0, bibtexString0);
      assertEquals(0, int0);
  }
}