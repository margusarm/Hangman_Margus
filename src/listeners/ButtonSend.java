package listeners;

import models.DataScores;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Locale;

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
        //FIXME kogu see jura, mis siin all kirjas on, tuleks pisut paremini kokku võtta
        //FIXME sümboleid ei tohiks saada pakkuda.
        //JOptionPane.showMessageDialog(null, "Kes vajutas nuppu/Enter: " + view.getTxtChar().getText().toUpperCase());
        /*
         * @author Margus Arm
         * @date 15/06/2022 - 23:00
         * töötab ka nii, et requestFocus jääb algusesse...
         */
        view.getTxtChar().requestFocus(); // Peale selle nupu klikkimist anna fookus tekstikastile
        String guessStringChar = view.getTxtChar().getText().toUpperCase();
        char guessChar = guessStringChar.charAt(0);
        String guessWord = model.getRandomWordUpperCase();
        String[] wordCharArray = guessWord.split(""); //tee arvatav sõna arrayks
        boolean miss = true;

        if (model.getMissedWordsList().contains(guessStringChar)) { // kontrollib, kas pakutud ja mööda pandud tähte on juba pakutud.
            JOptionPane.showMessageDialog(null, "Sa oled juba pakkunud tähte \"" + guessStringChar + "\"", "Korduv pakkumine", JOptionPane.ERROR_MESSAGE);
            miss = false;   //peab seda ka panema, muid loeb ikka tähe ära
        } else {
            for (int i = 1; i < wordCharArray.length - 1; i++) {
                if (wordCharArray[i].equalsIgnoreCase(guessStringChar)) {
                    //System.out.println("great success!");
                    model.getHiddenWord().setCharAt(i, guessChar); // avaldab ära arvatud tähed ja määrab uue stringi
                    //model.guessedWords(i, guessChar);
                    miss = false;
                }
            }
        }

        if (miss) {
            model.getMissedWordsList().add(guessStringChar);
            view.getLblWrongInfo().setForeground(Color.RED);
        }

        //String test = model.getRandomWord();
        //System.out.println(test);
        //System.out.println(model.getHiddenWord());

        String wordUpdate = model.wordSpacer(model.getHiddenWord().toString()); //võtab peidetud tähed uuesti ja paneb tühikud vahele
        view.getLblGuessWord().setText(wordUpdate);                 // paneb arvatud tähtedega sõna uuesti labelile
        String missedWordsStream = model.getMissedWordsList().toString().replace("[", "").replace("]", ""); // võtab [] ära, sest nii on ilusam
        model.setMissedWordsCount(model.getMissedWordsList().size());
        view.getLblWrongInfo().setText("Valesti " + model.getMissedWordsCount() + " täht(e). " + missedWordsStream); // uuendab, mitu sõna mööda pandud ja näitab sõnu
        //System.out.println(model.getMissedWordsList().size());
        view.getTxtChar().setText("");

        if (!model.getHiddenWord().toString().contains("_")) { //kontrollib, ega võitnud juba ei ole
            ImageIcon iconWinner = new ImageIcon("src/images/winner.png");
            String nameWinner = (String) JOptionPane.showInputDialog(view, "Sisesta palun oma nimi: ", "Võitsid mängu", JOptionPane.INFORMATION_MESSAGE, iconWinner, null, "");
            //String nameWinner = JOptionPane.showInputDialog(view."Sisesta nimi","Võitsid",JOptionPane.INFORMATION_MESSAGE);
            if (nameWinner.length() < 2) {
                JOptionPane.showMessageDialog(view, "Nimi peab olema vähemalt kaks tähemärki");
                nameWinner = (String) JOptionPane.showInputDialog(view, "Sisesta palun oma nimi: ", "Võitsid mängu", JOptionPane.INFORMATION_MESSAGE, iconWinner, null, "");
            }
            DataScores winnerScore = new DataScores(LocalDateTime.now(), nameWinner, model.getRandomWord(), missedWordsStream); //loob uue objekti. sobib sama, millega andmeid sqlist võetakse
            model.scoreInsert(winnerScore); //sisestab sqli. modelisse on tehtud uus meetod sisestamiseks
            model.getDataScores().add(winnerScore); //lisab viimase score ka datascores listi. teine variant on teha SQLi uue päringu, aga listi lisamine koormab vähem sql serverit ja annab sama tulemuse kasutajale
            view.setEndGame();
        }

        if (model.getMissedWordsCount() >= 7) {
            ImageIcon iconLoser = new ImageIcon("src/images/loser.png");
            JOptionPane.showMessageDialog(view, "Kahjuks kaotasid.", "Mäng läbi", JOptionPane.ERROR_MESSAGE, iconLoser);

            view.setEndGame();
        }
    }
}
