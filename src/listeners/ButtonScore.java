package listeners;

import models.Model;
import views.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klass nupu Edetabel jaoks
 */
public class ButtonScore implements ActionListener {
    /**
     * Model
     */
    private final Model model;
    /**
     * View
     */
    private final View view;
    /**
     * Päise String[] list
     */
    private final String[] header = new String[] {"Kuupäev", "Nimi", "Sõna", "Tähed"};
    private final DefaultTableModel dtm = new DefaultTableModel(header, 0);
    /**
     * JTable
     */
    private final JTable table = new JTable(dtm);

    /**
     * Siia Dialog-i aknasse pannakse edetabeli tabel
     */
    private JDialog dialogScore; // Siin näidatakse edetabelit eraldi aknas

    /**
     * Konstruktor
     * @param model mudel
     * @param view vaade
     */
    public ButtonScore(Model model, View view) {
        this.model = model;
        this.view = view;
        model.setDtm(dtm);
        createDialog();
    }

    /**
     * Kui klikitakse nupul Edetabel
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //JOptionPane.showMessageDialog(null, "Keegi klikkis edetabeli nuppu!");
        model.scoreSelect(); // Loeme kogu sisu uuesti listi
        //createScoreboard(); // Teeb dialoogi akna
        view.updateTable(); // Uuendmae andmeid dialogi akna tabelis
        dialogScore.setVisible(true); // Teeme dialoogiakna nähtavaks
        view.getTxtChar().requestFocus(); // Peale selle nupu klikkimist anna fookus tekstikastile
    }

    /**
     * Teeb dialog-i akna ja paneb tabeli sinna. Kuna seda on vaja korduvalt kasutada,
     * peab ta olemasolema.
     */
    private void createDialog() {
        dialogScore = new JDialog();
        dialogScore.setPreferredSize(new Dimension(500, 200));

        dialogScore.add(new JScrollPane(table));
        dialogScore.setTitle("Edetabel");
        dialogScore.pack();
        dialogScore.setLocationRelativeTo(view);
        dialogScore.setModal(true);
    }

}
