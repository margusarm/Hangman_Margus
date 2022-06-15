package listeners;

import models.Model;
import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.StringJoiner;

/**
 * Klass nupu Uus mäng jaoks
 */
public class ButtonNew implements ActionListener {
    /**
     * Model
     */
    private Model model;
    /**
     * View
     */
    private View view;

    /**
     * Konstruktor
     *
     * @param model Model
     * @param view  View
     */
    public ButtonNew(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Kui klikitakse nupul Uus mäng
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //JOptionPane.showMessageDialog(null, "Klikiti nupul Uus mäng");

        view.setStartGame();
        view.getTxtChar().requestFocus(); // Peale selle nupu klikkimist anna fookus teksti kastile


        //String word = model.getDataWords().get(0).getWord(); // valib sõna
        String category = view.getCmbCategory().getSelectedItem().toString(); //vaatab, mis kategooria on valitud
        String randomWord = model.getWordByCategory(category); //määrab valitud kategooria järgi suvalise sõna
        String hiddenWord = wordHider(randomWord);
        String spacedWord = wordSpacer(hiddenWord);

        view.getLblGuessWord().setText(spacedWord); //paneb sõna

        //System.out.println(spacedWord);
    }

    private String wordHider(String word) {
        word = word.toUpperCase();
        StringBuilder newWord = new StringBuilder(word); // see peaks töötama ka lihtsalt stringiga, aga ei hakka tagasi muutma.
        for (int i = 1; i < word.length() - 1; i++) { // käib stringi kõik tähed va esimene ja viimane, ning muudab nad alakriipsuks
            newWord.setCharAt(i, '_');
        }
        return newWord.toString();
    }

    private String wordSpacer(String word) {
        String[] wordCharArray = word.split("");        //teeb sõna arrayks
        StringJoiner join = new StringJoiner(" "); // äkki peaks kaks tühikut panema.
        String spacedWord;
        for (String w : wordCharArray){                     //käib kõik tähed ükshaaval läbi ja lisab tühiku.
            join.add(w);
            //System.out.println(w);
        }
        return join.toString();
    }
}
