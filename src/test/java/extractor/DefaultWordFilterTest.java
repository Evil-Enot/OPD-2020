package extractor;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultWordFilterTest {

    @Test
    void filterTest() {
        Collection<String> wordSForFiltrationSet = new HashSet<>();
        wordSForFiltrationSet.add("ГаллОграмма..");
        wordSForFiltrationSet.add("в");
        wordSForFiltrationSet.add("Музее,,");
        wordSForFiltrationSet.add("к");
        wordSForFiltrationSet.add("11>");
        wordSForFiltrationSet.add("сентября");
        wordSForFiltrationSet.add(",");
        Collection<String> expected = new HashSet<>();
        expected.add("галлограмма");
        expected.add("музее");
        expected.add("11");
        expected.add("сентября");
        Collection<String> filteredSet = new DefaultWordFilter().filter(wordSForFiltrationSet);
        assertEquals(expected, filteredSet);
    }

    @Test
    void unnecessaryWordsFilterTest() {
        Collection<String> wordSForFiltrationSet = new HashSet<>();
        wordSForFiltrationSet.add("Галлограмма");
        wordSForFiltrationSet.add("в");
        wordSForFiltrationSet.add("Музее");
        wordSForFiltrationSet.add("к");
        wordSForFiltrationSet.add("11");
        wordSForFiltrationSet.add("сентября");
        Collection<String> expected = new HashSet<>();
        expected.add("Галлограмма");
        expected.add("Музее");
        expected.add("11");
        expected.add("сентября");
        new DefaultWordFilter().unnecessaryWordsFilter(wordSForFiltrationSet);
        assertEquals(expected, wordSForFiltrationSet);
    }

    @Test
    void deleteBlankLinesTest() {
        Collection<String> wordSForFiltrationSet1 = new HashSet<>();
        wordSForFiltrationSet1.add("Игорь");
        wordSForFiltrationSet1.add("");
        new DefaultWordFilter().deleteBlankLines(wordSForFiltrationSet1);
        assertEquals(wordSForFiltrationSet1.size(), 1);
        Collection<String> wordSForFiltrationSet2 = new HashSet<>();
        wordSForFiltrationSet2.add("Игорь");
        new DefaultWordFilter().deleteBlankLines(wordSForFiltrationSet1);
        assertEquals(wordSForFiltrationSet2.size(), 1);
    }

    @Test
    void punctuationMarkFilterTest() {
        Collection<String> wordSForFiltrationSet = new HashSet<>();
        wordSForFiltrationSet.add("нога,");
        wordSForFiltrationSet.add("404..");
        wordSForFiltrationSet.add("Зебры:");
        wordSForFiltrationSet.add(".");
        Collection<String> expected = new HashSet<>();
        expected.add("нога");
        expected.add("404");
        expected.add("Зебры");
        expected.add("");
        Collection<String> newSet = new DefaultWordFilter().punctuationMarkFilter(wordSForFiltrationSet);
        assertEquals(newSet, expected);
    }

    @Test
    void linkFilteringTest() {
        Collection<String> wordsForFiltration = new HashSet<>();
        wordsForFiltration.add("нога");
        wordsForFiltration.add("https://www.google.com");
        wordsForFiltration.add("404");
        wordsForFiltration.add("http://www.google.com");
        wordsForFiltration.add("Зебры");
        wordsForFiltration.add("www.google.com");

        Collection<String> expected = new HashSet<>();
        expected.add("нога");
        expected.add("404");
        expected.add("Зебры");
        new DefaultWordFilter().removingLinks(wordsForFiltration);
        assertEquals(expected, wordsForFiltration);
    }
}