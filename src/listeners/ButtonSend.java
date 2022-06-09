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
     * @param model Model
     * @param view View
     */
    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Kui kliikida nupul Saada täht
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //JOptionPane.showMessageDialog(null, "Kes vajutas nuppu/Enter: " + view.getTxtChar().getText().toUpperCase());

        view.getTxtChar().requestFocus(); // Peale selle nupu klikkimist anna fookus tekstikastile
    }
}
