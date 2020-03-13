package database;

import database.models.Website;
import database.models.Word;

import java.util.List;
import java.util.HashSet;

public interface Database {

    /**
     * Creates a database instance
     *
     * @return database instance
     */
    static Database newInstance() {
        return new DatabaseImpl();
    }

    /**
     * Parses a CSV file and inserts the result of parsing into a database
     *
     * @param csvFile path to the CSV file
     * @return true, if the insert was successful,
     * false, if it was not possible to insert
     */
    boolean putWebsitesFromCsv(String csvFile);

    /**
     * Inserts a website into the database,
     * if such an entry already exists, then does not insert anything
     *
     * @param companyId id of company
     * @param website   website link
     * @return true, if the insert was successful,
     * false, if it was not possible to insert
     */
    boolean putWebsite(int companyId, String website);

    /**
     * Inserts a website into the database,
     * if such an entry already exists, then does not insert anything
     *
     * @param website site object containing company id and link
     * @return true, if the insert was successful,
     * false, if it was not possible to insert
     */
    boolean putWebsite(Website website);

    /**
     * Inserts a websites into the database,
     * if such an entry already exists, then does not insert anything
     *
     * @param websites list of websites
     * @return true, if the insert was successful,
     * false, if it was not possible to insert
     */
    boolean putWebsites(List<Website> websites);

    /**
     * Inserts a word into the database,
     * if such an entry already exists, then does not insert anything
     *
     * @param websiteId id of website
     * @param word      word to insert
     * @return true, if the insert was successful,
     * false, if it was not possible to insert
     */
    boolean putWord(int websiteId, String word);

    /**
     * Inserts a word into the database,
     * if such an entry already exists, then does not insert anything
     *
     * @param word word object containing website id and word to insert
     * @return true, if the insert was successful,
     * false, if it was not possible to insert
     */
    boolean putWord(Word word);

    /**
     * Inserts a word into the database,
     * if such an entry already exists, then does not insert anything
     *
     * @param words list of words
     * @return true, if the insert was successful,
     * false, if it was not possible to insert
     */
    boolean putWords(List<Word> words);

    /**
     * Clears all columns of the 'websites' table
     *
     * @return true, if the cleaning was successful,
     * false, if it was not possible to clean
     */
    boolean clearWebsites();

    /**
     * Clears all columns of the 'words' table
     *
     * @return true, if the cleaning was successful,
     * false, if it was not possible to clean
     */
    boolean clearWords();

    /**
     * Returns the number of entries in the "websites" table
     *
     * @return Websites table size
     */
    int getWebsitesSize();

    /**
     * Exports data from database and inserts it in the new CSV file
     *
     * @return true, if the export was successful,
     * false, if it was not possible to insert
     */
    boolean exportDataToCSV(String filepath);

    /**
     * Returns the number of entries in the "words" table
     *
     * @return Words table size
     */
    int getWordsSize();

    /**
     * Returns a list containing all the websites from the "websites" table
     *
     * @return List of websites from websites table
     */
    HashSet<Website> getWebsites();

    /**
     * Returns a list containing the website in which such word last occurred from the "words" table
     *
     * @param word      word by which to find a website
     * @return List of Website objects
     */
    HashSet<Website> getWebsites(String word);

    /**
     * Returns a list of site objects containing the link of website and company ID
     *
     * @param companyId     websites' company ID in "websites" table
     * @return list of website objects with website link and company id
     */
    HashSet<Website> getWebsites(int companyId);

    /**
     * Returns a list of string containing the links of websites by its company ID
     *
     * @param companyId      websites' company ID in "websites" table
     * @return list of Strings with websites link
     */
    HashSet<String> getWebsiteLink(int companyId);

    /**
     * Returns a list containing all found words from "words" table
     *
     * @return list of Word objects
     */
    HashSet<Word> getWords();

    /**
     * Returns a list containing all found words from the specified website
     *
     * @param websiteId     id of website
     * @return list of Word objects
     */
    HashSet<Word> getWords(int websiteId);

    /**
     * Returns a word ID from the "words" table
     *
     * @param word    word which id is required
     * @return word ID
     */
    int getWordId(String word);
    /**
     * Returns a word object by its id in "words" table
     *
     * @param wordId     id of required word
     * @return Word object
     */
    Word getWord(int wordId);
}
