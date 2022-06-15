package models;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Klass mudel (Üldine)
 */
public class Model {
    /**
     * Siin on kõik unikaalsed kategooriad mis andmebaasi failist leiti
     */
    private String[] categories;
    /**
     * Andmeaasi nimi kettal
     */
    private String dbName = "words.db";
    /**
     *  Andmebaasi ühenduse jaoks
     */
    private String dbUrl = "jdbc:sqlite:" + dbName;
    /**
     * Andmebaasi tabeli scores sisu (Edetabel)
     */
    private List<DataScores> dataScores;
    /**
     * Andmebaasi tabeli words sisu Sõnad
     */
    private List<DataWords> dataWords;
    /**
     * Andmebaasi ühendust algselt pole
     */
    Connection connection = null;

    /**
     * Konstruktor
     */
    public Model() {
        dataScores = new ArrayList<>(); // Teeme tühja edetabeli listi
        dataWords = new ArrayList<>(); // Teeme tühja sõnade listi
        //categories = new String[]{"Kõik kategooriad", "Kategooria 1", "Kategooria 2"}; // TESTIKS!
        scoreSelect(); // Loeme edetabeli dataScores listi, kui on!
        wordsSelect(); // Loeme sõnade tabeli dataWords listi.
    }
    // ANDMEBAASI ASJAD
    /**
     * Andmebaaasi ühenduseks
     * @return tagastab ühenduse või rakendus lõpetab töö
     */
    private Connection dbConnection() throws SQLException {
        if (connection != null) { // Kui ühendus on püsti
            connection.close(); // Sulge ühendus
        }
        connection = DriverManager.getConnection(dbUrl); // Tee ühendus
        return connection; // Tagasta ühendus
    }
    /**
     * SELECT lause edetabeli sisu lugemiseks ja info dataScores listi lisamiseks
     */
    public void scoreSelect() {
        String sql = "SELECT * FROM scores ORDER BY playertime DESC";
        try {
            Connection conn = this.dbConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            dataScores.clear(); // Tühjenda dataScores list vanadest andmetest
            while (rs.next()) {
                //int id = rs.getInt("id");
                String datetime = rs.getString("playertime");
                LocalDateTime playerTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String playerName = rs.getString("playername");
                String guessWord = rs.getString("guessword");
                String wrongCharacters = rs.getString("wrongcharacters");
                // Lisame tabeli kirje dataScores listi
                dataScores.add(new DataScores(playerTime, playerName, guessWord, wrongCharacters));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * SELECT lause tabeli words sisu lugemiseks ja info dataWords listi lisamiseks
     */
    public void wordsSelect() {
        String sql = "SELECT * FROM words ORDER BY category, word";
        List<String> categories = new ArrayList<>(); // NB! See on meetodi sisene muutuja categories!
        try {
            Connection conn = this.dbConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            dataWords.clear(); // Tühjenda dataScores list vanadest andmetest
            while (rs.next()) {
                //int id = rs.getInt("id");
                int id = rs.getInt("id");
                String word = rs.getString("word");
                String category = rs.getString("category");
                dataWords.add(new DataWords(id, word, category)); // Lisame tabeli kirje dataWords listi
                categories.add(category);
            }
            // https://howtodoinjava.com/java8/stream-find-remove-duplicates/
            List<String> unique = categories.stream().distinct().collect(Collectors.toList());
            setCorrectCategoryNames(unique); // Unikaalsed nimed Listist String[] listi categories
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // SETTERS
    /**
     * Paneb unikaalsed kategooriad ComboBox-i jaoks muutujasse
     * @param unique unikaalsed kategooriad
     */
    private void setCorrectCategoryNames(List<String> unique) {
        categories = new String[unique.size()+1]; // Vali kategooria. See on klassi sisene muutuja!
        categories[0] = "Kõik kategooriad";
        for(int x = 0; x < unique.size(); x++) {
            categories[x+1] = unique.get(x);
        }
    }

    // GETTERS
    /**
     * Tagasta kategooriad
     * @return tagastab String[] listi kategooria nimedega
     */
    public String[] getCategories() {
        return categories;
    }
    /**
     * Tagastab edetabeli listi
     * @return tagastab List&lt;DataScores&gt; listi edetabeli tabelis sisuga
     */
    public List<DataScores> getDataScores() {
        return dataScores;
    }
    /**
     * Tagastab sõnade listi
     * @return List
     */
    public List<DataWords> getDataWords() {
        return dataWords;
    }

    /**
     * Võtab kategooria ja kuvab vastavad sõnad listi ning võtab lambi sõna
     * @param category
     */
    public String getWordByCategory(String category){
        List<String> wordsList = new ArrayList<>();     //loob uue listi, mille seast valida
        Random random = new Random();                   // uuele listile random valiku tegemiseks
        String randomWord;                              // sõna, mis lõpuks kuvatakse
        if (category.equalsIgnoreCase("Kõik kategooriad")){                 //kui on kõik kategooriad, siis valib kõikide sõnade seast
            randomWord = dataWords.get(random.nextInt(dataWords.size())).getWord();
        } else {
            for(DataWords word : dataWords){
                if (category.equalsIgnoreCase(word.getCategory())){                 //kui on kindel kategooria, siis lisab wordsListi need sõnad
                    //System.out.println(word.getWord());
                    wordsList.add(word.getWord());
                }

            }
            randomWord = wordsList.get(random.nextInt(wordsList.size()));           // valib suvalise sõna wordsListist
        }

        return randomWord;                  // kuvab selle sõna
    }
}
