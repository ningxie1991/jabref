package org.jabref.logic.database;

import java.util.Optional;
import java.util.stream.Stream;

import org.jabref.model.database.BibDatabaseMode;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.BibEntryTypesManager;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.types.StandardEntryType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DuplicateCheckTest {

    private BibEntry simpleArticle;
    private BibEntry unrelatedArticle;
    private BibEntry simpleInbook;
    private BibEntry simpleIncollection;
    private DuplicateCheck duplicateChecker;

    @BeforeEach
    public void setUp() {
        simpleArticle = new BibEntry(StandardEntryType.Article)
                .withField(StandardField.AUTHOR, "Single Author")
                .withField(StandardField.TITLE, "A serious paper about something")
                .withField(StandardField.YEAR, "2017");
        unrelatedArticle = new BibEntry(StandardEntryType.Article)
                .withField(StandardField.AUTHOR, "Completely Different")
                .withField(StandardField.TITLE, "Holy Moly Uffdada und Trallalla")
                .withField(StandardField.YEAR, "1992");
        simpleInbook = new BibEntry(StandardEntryType.InBook)
                .withField(StandardField.TITLE, "Alice in Wonderland")
                .withField(StandardField.AUTHOR, "Charles Lutwidge Dodgson")
                .withField(StandardField.CHAPTER, "Chapter One – Down the Rabbit Hole")
                .withField(StandardField.LANGUAGE, "English")
                .withField(StandardField.PUBLISHER, "Macmillan")
                .withField(StandardField.YEAR, "1865");
        simpleIncollection = new BibEntry(StandardEntryType.InCollection)
                .withField(StandardField.TITLE, "Innovation and Intellectual Property Rights")
                .withField(StandardField.AUTHOR, "Ove Grandstrand")
                .withField(StandardField.BOOKTITLE, "The Oxford Handbook of Innovation")
                .withField(StandardField.PUBLISHER, "Oxford University Press")
                .withField(StandardField.YEAR, "2004");
        duplicateChecker = new DuplicateCheck(new BibEntryTypesManager());
    }

    private static Stream<Arguments> getBibEntryPairs() {
        return Stream.of(
                // same entry type with same author
                Arguments.of(true, new BibEntry(StandardEntryType.Article).withField(StandardField.AUTHOR, "Billy Bob"),
                        new BibEntry(StandardEntryType.Article).withField(StandardField.AUTHOR, "Billy Bob")),
                // different entry types with same author
                Arguments.of(false, new BibEntry(StandardEntryType.Article).withField(StandardField.AUTHOR, "Billy Bob"),
                        new BibEntry(StandardEntryType.Book).withField(StandardField.AUTHOR, "Billy Bob")),
                // duplicate entries with more fields
                Arguments.of(true, new BibEntry(StandardEntryType.Article)
                                            .withField(StandardField.AUTHOR, "Billy Bob")
                                            .withField(StandardField.YEAR, "2005")
                                            .withField(StandardField.TITLE, "A title")
                                            .withField(StandardField.JOURNAL, "A"),
                                    new BibEntry(StandardEntryType.Article)
                                            .withField(StandardField.AUTHOR, "Billy Bob")
                                            .withField(StandardField.YEAR, "2005")
                                            .withField(StandardField.TITLE, "A title")
                                            .withField(StandardField.JOURNAL, "A")),
                // different JOURNAL value
                Arguments.of(true, new BibEntry(StandardEntryType.Article)
                                            .withField(StandardField.AUTHOR, "Billy Bob")
                                            .withField(StandardField.YEAR, "2005")
                                            .withField(StandardField.TITLE, "A title")
                                            .withField(StandardField.JOURNAL, "A"),
                                    new BibEntry(StandardEntryType.Article)
                                            .withField(StandardField.AUTHOR, "Billy Bob")
                                            .withField(StandardField.YEAR, "2005")
                                            .withField(StandardField.TITLE, "A title")
                                            .withField(StandardField.JOURNAL, "B")),
                // entryOne has NUMBER and entryTwo has VOLUME and both have same PAGES
                Arguments.of(true, new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "A")
                                .withField(StandardField.NUMBER, "1")
                                .withField(StandardField.PAGES, "334--337"),
                        new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "A")
                                .withField(StandardField.VOLUME, "21")
                                .withField(StandardField.PAGES, "334--337")),
                // duplicate entries
                Arguments.of(true, new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "A")
                                .withField(StandardField.NUMBER, "1")
                                .withField(StandardField.VOLUME, "21")
                                .withField(StandardField.PAGES, "334--337"),
                        new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "A")
                                .withField(StandardField.NUMBER, "1")
                                .withField(StandardField.VOLUME, "21")
                                .withField(StandardField.PAGES, "334--337")),
                // different VOLUME
                Arguments.of(true, new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "A")
                                .withField(StandardField.NUMBER, "1")
                                .withField(StandardField.VOLUME, "21")
                                .withField(StandardField.PAGES, "334--337"),
                        new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "A")
                                .withField(StandardField.NUMBER, "1")
                                .withField(StandardField.VOLUME, "22")
                                .withField(StandardField.PAGES, "334--337")),
                // different VOLUME and JOURNAL
                Arguments.of(true, new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "A")
                                .withField(StandardField.NUMBER, "1")
                                .withField(StandardField.VOLUME, "21")
                                .withField(StandardField.PAGES, "334--337"),
                        new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "B")
                                .withField(StandardField.NUMBER, "1")
                                .withField(StandardField.VOLUME, "22")
                                .withField(StandardField.PAGES, "334--337")),
                // empty JOURNAL
                Arguments.of(true, new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "")
                                .withField(StandardField.NUMBER, "1")
                                .withField(StandardField.VOLUME, "21")
                                .withField(StandardField.PAGES, "334--337"),
                        new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "")
                                .withField(StandardField.NUMBER, "1")
                                .withField(StandardField.VOLUME, "22")
                                .withField(StandardField.PAGES, "334--337")),
                // Different TITLE
                Arguments.of(false, new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "")
                                .withField(StandardField.NUMBER, "1")
                                .withField(StandardField.VOLUME, "21")
                                .withField(StandardField.PAGES, "334--337"),
                        new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "Another title")
                                .withField(StandardField.JOURNAL, "")
                                .withField(StandardField.NUMBER, "1")
                                .withField(StandardField.VOLUME, "22")
                                .withField(StandardField.PAGES, "334--337"))
        );
    }

    @ParameterizedTest
    @MethodSource("getBibEntryPairs")
    public void testDuplicateBibEntries(boolean expectedResult, BibEntry entryOne, BibEntry entryTwo) {
        assertEquals(expectedResult, duplicateChecker.isDuplicate(entryOne, entryTwo, BibDatabaseMode.BIBTEX));
    }

    private static Stream<Arguments> getBibEntryPairsForDuplicateScore() {
        return Stream.of(
                // duplicate entries
                Arguments.of(true, 1.01, 0.01, new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "A"),
                        new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "A")),
                // different journal
                Arguments.of(true, 0.75, 0.01, new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "A"),
                        new BibEntry(StandardEntryType.Article)
                                .withField(StandardField.AUTHOR, "Billy Bob")
                                .withField(StandardField.YEAR, "2005")
                                .withField(StandardField.TITLE, "A title")
                                .withField(StandardField.JOURNAL, "B"))
        );
    }

    @ParameterizedTest
    @MethodSource("getBibEntryPairsForDuplicateScore")
    public void testDuplicateScore(boolean expectedResult, double score, double delta, BibEntry entryOne, BibEntry entryTwo) {
        assertEquals(expectedResult, duplicateChecker.isDuplicate(entryOne, entryTwo, BibDatabaseMode.BIBTEX));
        assertEquals(score, DuplicateCheck.compareEntriesStrictly(entryOne, entryTwo), delta);
    }

    @Test
    public void testWordCorrelation() {
        String d1 = "Characterization of Calanus finmarchicus habitat in the North Sea";
        String d2 = "Characterization of Calunus finmarchicus habitat in the North Sea";
        String d3 = "Characterization of Calanus glacialissss habitat in the South Sea";

        assertEquals(1.0, (DuplicateCheck.correlateByWords(d1, d2)), 0.01);
        assertEquals(0.78, (DuplicateCheck.correlateByWords(d1, d3)), 0.01);
        assertEquals(0.78, (DuplicateCheck.correlateByWords(d2, d3)), 0.01);
    }

    @Test
    public void twoUnrelatedEntriesAreNoDuplicates() {
        assertFalse(duplicateChecker.isDuplicate(simpleArticle, unrelatedArticle, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void twoUnrelatedEntriesWithDifferentDoisAreNoDuplicates() {
        simpleArticle.setField(StandardField.DOI, "10.1016/j.is.2004.02.002");
        unrelatedArticle.setField(StandardField.DOI, "10.1016/j.is.2004.02.00X");

        assertFalse(duplicateChecker.isDuplicate(simpleArticle, unrelatedArticle, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void twoUnrelatedEntriesWithEqualDoisAreDuplicates() {
        simpleArticle.setField(StandardField.DOI, "10.1016/j.is.2004.02.002");
        unrelatedArticle.setField(StandardField.DOI, "10.1016/j.is.2004.02.002");

        assertTrue(duplicateChecker.isDuplicate(simpleArticle, unrelatedArticle, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void twoUnrelatedEntriesWithEqualPmidAreDuplicates() {
        simpleArticle.setField(StandardField.PMID, "12345678");
        unrelatedArticle.setField(StandardField.PMID, "12345678");

        assertTrue(duplicateChecker.isDuplicate(simpleArticle, unrelatedArticle, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void twoUnrelatedEntriesWithEqualEprintAreDuplicates() {
        simpleArticle.setField(StandardField.EPRINT, "12345678");
        unrelatedArticle.setField(StandardField.EPRINT, "12345678");

        assertTrue(duplicateChecker.isDuplicate(simpleArticle, unrelatedArticle, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void twoEntriesWithSameDoiButDifferentTypesAreDuplicates() {
        simpleArticle.setField(StandardField.DOI, "10.1016/j.is.2004.02.002");
        BibEntry duplicateWithDifferentType = (BibEntry) simpleArticle.clone();
        duplicateWithDifferentType.setType(StandardEntryType.InCollection);

        assertTrue(duplicateChecker.isDuplicate(simpleArticle, duplicateWithDifferentType, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void twoEntriesWithDoiContainingUnderscoresAreNotEqual() {
        simpleArticle.setField(StandardField.DOI, "10.1016/j.is.2004.02.002");
        // An underscore in a DOI can indicate a totally different DOI
        unrelatedArticle.setField(StandardField.DOI, "10.1016/j.is.2004.02.0_02");
        BibEntry duplicateWithDifferentType = unrelatedArticle;
        duplicateWithDifferentType.setType(StandardEntryType.InCollection);

        assertFalse(duplicateChecker.isDuplicate(simpleArticle, duplicateWithDifferentType, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void twoEntriesWithSameISBNButDifferentTypesAreDuplicates() {
        simpleArticle.setField(StandardField.ISBN, "0-123456-47-9");
        unrelatedArticle.setField(StandardField.ISBN, "0-123456-47-9");
        BibEntry duplicateWithDifferentType = unrelatedArticle;
        duplicateWithDifferentType.setType(StandardEntryType.InCollection);

        assertTrue(duplicateChecker.isDuplicate(simpleArticle, duplicateWithDifferentType, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void twoInbooksWithDifferentChaptersAreNotDuplicates() {
        twoEntriesWithDifferentSpecificFieldsAreNotDuplicates(simpleInbook, StandardField.CHAPTER,
                "Chapter One – Down the Rabbit Hole",
                "Chapter Two – The Pool of Tears");
    }

    @Test
    public void twoInbooksWithDifferentPagesAreNotDuplicates() {
        twoEntriesWithDifferentSpecificFieldsAreNotDuplicates(simpleInbook, StandardField.PAGES, "1-20", "21-40");
    }

    @Test
    public void twoIncollectionsWithDifferentChaptersAreNotDuplicates() {
        twoEntriesWithDifferentSpecificFieldsAreNotDuplicates(simpleIncollection, StandardField.CHAPTER, "10", "9");
    }

    @Test
    public void twoIncollectionsWithDifferentPagesAreNotDuplicates() {
        twoEntriesWithDifferentSpecificFieldsAreNotDuplicates(simpleIncollection, StandardField.PAGES, "1-20", "21-40");
    }

    private void twoEntriesWithDifferentSpecificFieldsAreNotDuplicates(final BibEntry cloneable,
                                                                       final Field field,
                                                                       final String firstValue,
                                                                       final String secondValue) {
        final BibEntry entry1 = (BibEntry) cloneable.clone();
        entry1.setField(field, firstValue);

        final BibEntry entry2 = (BibEntry) cloneable.clone();
        entry2.setField(field, secondValue);

        assertFalse(duplicateChecker.isDuplicate(entry1, entry2, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void inbookWithoutChapterCouldBeDuplicateOfInbookWithChapter() {
        final BibEntry inbook1 = (BibEntry) simpleInbook.clone();
        final BibEntry inbook2 = (BibEntry) simpleInbook.clone();
        inbook2.setField(StandardField.CHAPTER, "");

        assertTrue(duplicateChecker.isDuplicate(inbook1, inbook2, BibDatabaseMode.BIBTEX));
        assertTrue(duplicateChecker.isDuplicate(inbook2, inbook1, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void twoBooksWithDifferentEditionsAreNotDuplicates() {
        BibEntry editionOne = new BibEntry(StandardEntryType.Book);
        editionOne.setField(StandardField.TITLE, "Effective Java");
        editionOne.setField(StandardField.AUTHOR, "Bloch, Joshua");
        editionOne.setField(StandardField.PUBLISHER, "Prentice Hall");
        editionOne.setField(StandardField.DATE, "2001");
        editionOne.setField(StandardField.EDITION, "1");

        BibEntry editionTwo = new BibEntry(StandardEntryType.Book);
        editionTwo.setField(StandardField.TITLE, "Effective Java");
        editionTwo.setField(StandardField.AUTHOR, "Bloch, Joshua");
        editionTwo.setField(StandardField.PUBLISHER, "Prentice Hall");
        editionTwo.setField(StandardField.DATE, "2008");
        editionTwo.setField(StandardField.EDITION, "2");

        assertFalse(duplicateChecker.isDuplicate(editionOne, editionTwo, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void sameBooksWithMissingEditionAreDuplicates() {
        BibEntry editionOne = new BibEntry(StandardEntryType.Book);
        editionOne.setField(StandardField.TITLE, "Effective Java");
        editionOne.setField(StandardField.AUTHOR, "Bloch, Joshua");
        editionOne.setField(StandardField.PUBLISHER, "Prentice Hall");
        editionOne.setField(StandardField.DATE, "2001");

        BibEntry editionTwo = new BibEntry(StandardEntryType.Book);
        editionTwo.setField(StandardField.TITLE, "Effective Java");
        editionTwo.setField(StandardField.AUTHOR, "Bloch, Joshua");
        editionTwo.setField(StandardField.PUBLISHER, "Prentice Hall");
        editionTwo.setField(StandardField.DATE, "2008");

        assertTrue(duplicateChecker.isDuplicate(editionOne, editionTwo, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void sameBooksWithPartiallyMissingEditionAreDuplicates() {
        BibEntry editionOne = new BibEntry(StandardEntryType.Book);
        editionOne.setField(StandardField.TITLE, "Effective Java");
        editionOne.setField(StandardField.AUTHOR, "Bloch, Joshua");
        editionOne.setField(StandardField.PUBLISHER, "Prentice Hall");
        editionOne.setField(StandardField.DATE, "2001");

        BibEntry editionTwo = new BibEntry(StandardEntryType.Book);
        editionTwo.setField(StandardField.TITLE, "Effective Java");
        editionTwo.setField(StandardField.AUTHOR, "Bloch, Joshua");
        editionTwo.setField(StandardField.PUBLISHER, "Prentice Hall");
        editionTwo.setField(StandardField.DATE, "2008");
        editionTwo.setField(StandardField.EDITION, "2");

        assertTrue(duplicateChecker.isDuplicate(editionOne, editionTwo, BibDatabaseMode.BIBTEX));
    }

    @Test
    public void sameBooksWithDifferentEditionsAreNotDuplicates() {
        BibEntry editionTwo = new BibEntry(StandardEntryType.Book);
        editionTwo.setCitationKey("Sutton17reinfLrnIntroBook");
        editionTwo.setField(StandardField.TITLE, "Reinforcement learning:An introduction");
        editionTwo.setField(StandardField.PUBLISHER, "MIT Press");
        editionTwo.setField(StandardField.YEAR, "2017");
        editionTwo.setField(StandardField.AUTHOR, "Sutton, Richard S and Barto, Andrew G");
        editionTwo.setField(StandardField.ADDRESS, "Cambridge, MA.USA");
        editionTwo.setField(StandardField.EDITION, "Second");
        editionTwo.setField(StandardField.JOURNAL, "MIT Press");
        editionTwo.setField(StandardField.URL, "https://webdocs.cs.ualberta.ca/~sutton/book/the-book-2nd.html");

        BibEntry editionOne = new BibEntry(StandardEntryType.Book);
        editionOne.setCitationKey("Sutton98reinfLrnIntroBook");
        editionOne.setField(StandardField.TITLE, "Reinforcement learning: An introduction");
        editionOne.setField(StandardField.PUBLISHER, "MIT press Cambridge");
        editionOne.setField(StandardField.YEAR, "1998");
        editionOne.setField(StandardField.AUTHOR, "Sutton, Richard S and Barto, Andrew G");
        editionOne.setField(StandardField.VOLUME, "1");
        editionOne.setField(StandardField.NUMBER, "1");
        editionOne.setField(StandardField.EDITION, "First");

        assertFalse(duplicateChecker.isDuplicate(editionOne, editionTwo, BibDatabaseMode.BIBTEX));
    }
}
