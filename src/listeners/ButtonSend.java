package listeners;

import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klass nupu Saada täht jaoks
 */
public class ButtonSend implements ActionListener {
    /**
     * Mudel
     */
    private Model model;
    /**
     * View
     */
    private View view;

    /**
     * Konstuktor
     *
     * @param model Model
     * @param view  View
     */
    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Kui kliikida nupul Saada täht
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //JOptionPane.showMessageDialog(null, "Kes vajutas nuppu/Enter: " + view.getTxtChar().getText().toUpperCase());

        view.getTxtChar().requestFocus(); // Peale selle nupu klikkimist anna fookus tekstikastile
        String guessStringChar = view.getTxtChar().getText().toUpperCase();
        char guessChar = guessStringChar.charAt(0);
        String guessWord = model.getRandomWord();


        String[] wordCharArray = guessWord.split(""); //tee arvatav sõna arrayks
        for (int i = 1; i < wordCharArray.length - 1; i++) {
            if (wordCharArray[i].equalsIgnoreCase(guessStringChar)) {
                //System.out.println("great success!");
                model.getHiddenWord().setCharAt(i, guessChar); // avaldab ära arvatud tähed ja määrab uue stringi
                //model.guessedWords(i, guessChar);
            }
        }

        //String test = model.getRandomWord();
        //System.out.println(test);
        //System.out.println(model.getHiddenWord());

        String wordUpdate = model.wordSpacer(model.getHiddenWord().toString()); //võtab peidetud tähed uuesti ja paneb tühikud vahele
        view.getLblGuessWord().setText(wordUpdate);                             // paneb arvatud tähtedega sõna uuesti labelile
    }
}
