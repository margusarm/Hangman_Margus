package models;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
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
    private Connection connection = null;
    /**
     * See on tegelik dtm mida kasutatakse tabeli puhul.
     */
    private DefaultTableModel dtm = new DefaultTableModel();
    /**
     * Sõna, mis tekib, kui uus mäng vajutada.
     * Sama vorming, mis andmebaasis.
     */
    private String randomWord;
    /**
     * randomWord läbivate suurtähtedega. enamik osa rakendusest kasutab seda
     */
    private String randomWordUpperCase;
    /**
     * Peidetud randomWord stiilis P______D
     * Peab jätma ilma tühikuteta, et tähti lihtsam avada oleks.
     */
    private StringBuilder hiddenWord;
    /**
     * Valesti arvatud tähed listina.
     */
    private List<String> missedWordslist = new ArrayList<>();
    /**
     * Valesti arvatud tähtede arv.
     */
    private int missedWordsCount;

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
     * Võtab objekti DataScore ja lisab selle andmebaasi
     * @param winnerScore
     */
    public void scoreInsert(DataScores winnerScore){
        //TODO lisa see, et kui tabelit pole, siis tekitatakse
        String sql = "INSERT INTO scores (playertime,playername,guessword,wrongcharacters) VALUES (?,?,?,?)";
        try{
            Connection conn = this.dbConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            DateTimeFormatter formatSQL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String playerTime = winnerScore.getGameTime().format(formatSQL);
            ps.setString(1,playerTime);
            ps.setString(2,winnerScore.getPlayerName());
            ps.setString(3,winnerScore.getGuessWord());
            ps.setString(4,winnerScore.getMissingLetters());
            ps.executeUpdate();
            scoreSelect();
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
    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
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
     * Tagastab andmebaasist valitud suvalise sõna.
     * Sõna vorming on sama, mis andmebaasis
     * @return
     */
    public String getRandomWord() {
        return randomWord;
    }

    /**
     * Tagastab suvalise valitud sõna kategooriast.
     * Sõna vorming on läbivalt suurtähtedega.
     * @return
     */
    public String getRandomWordUpperCase() {
        return randomWordUpperCase;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }


    /**
     * Tagastab suvalise valitud sõna selliselt, et keskmised sõnad on peidetud "_" alla.
     * @return
     */
    public StringBuilder getHiddenWord() {
        return hiddenWord;
    }

    /**
     * Tagastab valesti arvatud tähed listina.
     * @return
     */
    public List<String> getMissedWordsList() {
        return missedWordslist;
    }

    /**
     * Tagastab valesti arvatud tähtede arvu.
     * @return
     */
    public int getMissedWordsCount() {
        return missedWordsCount;
    }


    //SETTERS

    /**
     * Määrab valesti arvatud töhtede arvu.
     * @param missedWordsCount
     */
    public void setMissedWordsCount(int missedWordsCount) {
        this.missedWordsCount = missedWordsCount;
    }

    /**
     * Võtab kategooria ja kuvab vastavad sõnad listi ning võtab suvalise sõna.
     * @param category
     */
    public void setRandomWordByCategory(String category){
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
        this.randomWord = randomWord;
        this.randomWordUpperCase = randomWord.toUpperCase();                  // kuvab selle sõna ja teeb suured tähed
        hideWord(); // tekitab ka peidetud sõna
    }

    /**
     * Määrab hiddenWordi. Peidab tähed ära.
     */
    public void hideWord() {
        StringBuilder newWord = new StringBuilder(this.randomWordUpperCase); // see peaks töötama ka lihtsalt stringiga, aga ei hakka tagasi muutma.
        for (int i = 0; i < this.randomWordUpperCase.length(); i++) { // käib stringi kõik tähed va esimene ja viimane, ning muudab nad alakriipsuks
                newWord.setCharAt(i, '_');

        }
        /* Siin versioon, mis avab juba esimese ja viimase tähe

        for (int i = 1; i < this.randomWordUpperCase.length() - 1; i++) { // käib stringi kõik tähed va esimene ja viimane, ning muudab nad alakriipsuks
            char toCheck = newWord.charAt(i);
            char firstChar = newWord.charAt(0);
            char lastChar = newWord.charAt(newWord.length()-1);
            if (toCheck != firstChar && toCheck !=lastChar){ //jätab viimase ja esimese tähega samad sõnad ka mittepeidetuks
                newWord.setCharAt(i, '_');
            }

        }
        */
        this.hiddenWord = newWord;
    }

    /**
     * Lisab sisestatud stringile tühikud vahele.
     * @param word
     * @return
     */
    public String wordSpacer(String word) {
        String[] wordCharArray = word.split("");        //teeb sõna arrayks
        StringJoiner join = new StringJoiner(" "); // äkki peaks kaks tühikut panema.
        String spacedWord;
        for (String w : wordCharArray){                     //käib kõik tähed ükshaaval läbi ja lisab tühiku.
            join.add(w);
            //System.out.println(w);
        }
        return join.toString();
    }

    /**
     * Muudab hiddenWordis i indeksiga tähele väärtuseks sisendi c.
     * Siin rakenduses kasutatakse selleks, et avada hiddenWordis tähti.
     * @param i
     * @param c
     */
    public void guessedLetters(int i, char c){
        this.hiddenWord.setCharAt(i,c);
    }
}

