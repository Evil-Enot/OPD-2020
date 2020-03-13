package database;

import database.models.Website;
import database.models.Word;
import database.utils.CSVUtils;
import database.utils.DatabaseUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class DatabaseImpl implements Database {

    private String url;
    private String username;
    private String password;

    /* package-private

        DO NOT CHANGE ACCESS MODIFIER!

        Use IDatabase.newInstance() to create database object
   */
    DatabaseImpl() {
        try {
            parseProperties();
            initDatabase();
        } catch (ClassNotFoundException e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public boolean putWebsitesFromCsv(String csvFile) {
        return putWebsites(CSVUtils.parseLines(csvFile));
    }

    @Override
    public boolean putWebsite(int companyId, String website) {
        String statement = "INSERT INTO websites (company_id, website) VALUES (?, ?)";
        return executeStatementWithParams(companyId, website, statement);
    }

    @Override
    public boolean putWebsite(Website website) {
        return putWebsite(website.getCompanyId(), website.getLink());
    }

    @Override
    public boolean putWebsites(List<Website> websites) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = DatabaseUtil.getWebsitesPreparedStatement(websites, connection)) {
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean putWord(int websiteId, String word) {
        String statement = "INSERT INTO words (website_id, word) VALUES (?, ?)";
        return executeStatementWithParams(websiteId, word, statement);
    }

    @Override
    public boolean putWord(Word word) {
        return putWord(word.getWebsiteId(), word.getWord());
    }

    @Override
    public boolean putWords(List<Word> words) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = DatabaseUtil.getWordsPreparedStatement(words, connection)) {
                preparedStatement.executeUpdate();

                return true;
            }
        } catch (Exception e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean clearWebsites() {
        String statement = "DELETE FROM websites";
        return executeStatement(statement);
    }

    @Override
    public boolean clearWords() {
        String statement = "DELETE FROM words";
        return executeStatement(statement);
    }

    @Override
    public int getWebsitesSize() {
        String query = "SELECT COUNT(*) FROM websites";
        return getSizeFromQuery(query);
    }

    @Override
    public int getWordsSize() {
        String query = "SELECT COUNT(*) FROM words";
        return getSizeFromQuery(query);
    }

    @Override
    public boolean exportDataToCSV(String filepath) {
        String query = "SELECT * FROM words";
        File file = new File(filepath);
        file.delete();
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                String header = "\"id\";\"website_id\";\"word\"";
                writer.append(header);
                ResultSet rset = statement.executeQuery(query);
                while (rset.next()) {
                    int id = rset.getInt(1);
                    int websiteId = rset.getInt(2);
                    String word = rset.getString(3);
                    writer.append(String.format("\n%d;%d;\"%s\"", id, websiteId, word));
                }

                writer.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public HashSet<Website> getWebsites() {
        String query = "SELECT * FROM websites";
        return getWebsitesByQuery(query);
    }

    @Override
    public HashSet<Website> getWebsites(String word) {
        String query = "SELECT * FROM websites WHERE company_id=(SELECT website_id FROM words WHERE word='"+word+"')";
        return getWebsitesByQuery(query);
    }

    @Override
    public HashSet<Website> getWebsites(int companyId) {
        String query = "SELECT * FROM websites WHERE company_id='" + companyId + "'";
        return getWebsitesByQuery(query);
    }

    @Override
    public HashSet<String> getWebsiteLink(int companyId) {
        String query = "SELECT * FROM websites WHERE company_id='" + companyId + "'";
        HashSet<String> set = new HashSet<>();
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rset = statement.executeQuery(query);
                while(rset.next()){
                    String link = rset.getString(3);
                    set.add(link);
                }
                return set;
            }
        } catch (Exception e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
            return set;
        }
    }

    @Override
    public HashSet<Word> getWords() {
        String query = "SELECT * FROM words";
        return getWords(query);
    }

    @Override
    public HashSet<Word> getWords(int websiteId) {
        String query = "SELECT * FROM words WHERE website_id = '"+websiteId+"'";
        return getWords(query);
    }

    @Override
    public Word getWord(int wordId) {
        Word word = null;
        String query = "SELECT * FROM words WHERE id = '" + wordId + "'";
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rset = statement.executeQuery(query);
                rset.next();
                String wordStr = rset.getString(3);
                int websiteId = rset.getInt(2);
                word = new Word(websiteId, wordStr);
                return word;
            }
        } catch (Exception e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
            return word;
        }
    }

    @Override
    public int getWordId(String word) {
        String query = "SELECT * FROM words WHERE word = '" + word + "'";
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rset = statement.executeQuery(query);
                rset.next();
                return rset.getInt(1);
            }
        } catch (Exception e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
            return -1;
        }
    }

    private void parseProperties() {
        Properties properties = new Properties();

        try (InputStream in = Files.newInputStream(Paths.get("src/main/java/database/properties/database.properties"))) {
            properties.load(in);

            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void initDatabase() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        try (Connection connection = getConnection()) {

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS websites ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , 'company_id' int(11) NOT NULL , 'website' TEXT NOT NULL)");
            statement.execute("CREATE TABLE IF NOT EXISTS words ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , 'website_id' int(11) NOT NULL , 'word' TEXT NOT NULL)");
        } catch (Exception e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    private boolean executeStatement(String statement) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    private boolean executeStatementWithParams(int subId, String content, String statement) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

                preparedStatement.setInt(1, subId);
                preparedStatement.setString(2, content);
                preparedStatement.executeUpdate();

                return true;
            }
        } catch (Exception e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    private int getSizeFromQuery(String query) {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rset = statement.executeQuery(query);
                rset.next();
                return rset.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private HashSet<Word> getWords(String query) {
        HashSet<Word> set = new HashSet<>();
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rset = statement.executeQuery(query);
                while (rset.next()) {
                    int websiteId = rset.getInt(2);
                    String word = rset.getString(3);
                    set.add(new Word(websiteId, word));
                }
                return set;
            }

        } catch (Exception e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
            return set;
        }
    }

    private HashSet<Website> getWebsitesByQuery(String query) {
        HashSet<Website> set = new HashSet<>();
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rset = statement.executeQuery(query);
                while (rset.next()) {
                    int companyId = rset.getInt(2);
                    String website = rset.getString(3);
                    set.add(new Website(companyId, website));
                }
                return set;
            }
        } catch (Exception e) {
            Logger log = Logger.getLogger(DatabaseImpl.class.getName());
            log.log(Level.SEVERE, e.getMessage(), e);
            return set;
        }
    }
}