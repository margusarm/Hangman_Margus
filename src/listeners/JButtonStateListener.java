package listeners;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


//http://www.java2s.com/Tutorials/Java/Swing_How_to/JTextField/Enable_or_disable_a_JButton_based_on_JTextField_content.htm
public class JButtonStateListener implements DocumentListener {
    JButton button;

    public JButtonStateListener(JButton button) {
        this.button = button;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
    disableIfEmpty(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    disableIfEmpty(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    disableIfEmpty(e);
    }

    public void disableIfEmpty(DocumentEvent e) {
        button.setEnabled(e.getDocument().getLength() > 0);
    }
}
