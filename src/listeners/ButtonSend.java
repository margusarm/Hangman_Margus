package listeners;

import models.Model;
import views.View;

import javax.swing.*;
import java.awt.*;
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
        //FIXME kogu see jura, mis siin all kirjas on, tuleks pisut paremini kokku võtta
        //JOptionPane.showMessageDialog(null, "Kes vajutas nuppu/Enter: " + view.getTxtChar().getText().toUpperCase());
        /*
         * @author Margus Arm
         * @date 15/06/2022 - 23:00
         * töötab ka nii, et requestFocus jääb algusesse...
         */
        view.getTxtChar().requestFocus(); // Peale selle nupu klikkimist anna fookus tekstikastile
        String guessStringChar = view.getTxtChar().getText().toUpperCase();
        char guessChar = guessStringChar.charAt(0);
        String guessWord = model.getRandomWord();
        String[] wordCharArray = guessWord.split(""); //tee arvatav sõna arrayks
        boolean miss = true;

        if (model.getMissedWordsList().contains(guessStringChar)){ // kontrollib, kas pakutud ja mööda pandud tähte on juba pakutud.
            JOptionPane.showMessageDialog(null,"Sa oled juba pakkunud tähte \""+guessStringChar+"\"","Korduv pakkumine",JOptionPane.ERROR_MESSAGE);
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

        if (miss){
            model.getMissedWordsList().add(guessStringChar);
            view.getLblWrongInfo().setForeground(Color.RED);
        }

        //String test = model.getRandomWord();
        //System.out.println(test);
        //System.out.println(model.getHiddenWord());

        String wordUpdate = model.wordSpacer(model.getHiddenWord().toString()); //võtab peidetud tähed uuesti ja paneb tühikud vahele
        view.getLblGuessWord().setText(wordUpdate);                 // paneb arvatud tähtedega sõna uuesti labelile
        String missedWordsStream = model.getMissedWordsList().toString().replace("[","").replace("]",""); // võtab [] ära, sest nii on ilusam
        model.setMissedWordsCount(model.getMissedWordsList().size());
        view.getLblWrongInfo().setText("Valesti " + model.getMissedWordsCount() + " täht(e). " + missedWordsStream); // uuendab, mitu sõna mööda pandud ja näitab sõnu
        //System.out.println(model.getMissedWordsList().size());
        view.getTxtChar().setText("");

        if (!model.getHiddenWord().toString().contains("_")){ //kontrollib, ega võitnud juba ei ole
            JOptionPane.showMessageDialog(null,"Võitsid, ei olegi jobu!", "WINNER WINNER WINNER",JOptionPane.INFORMATION_MESSAGE);
            view.setEndGame();
        }

        if (model.getMissedWordsCount()>= 7){
            JOptionPane.showMessageDialog(null,"Kaotasid, jobu!","LOSER LOSER LOSER",JOptionPane.ERROR_MESSAGE);
            view.setEndGame();
        }
    }
}
