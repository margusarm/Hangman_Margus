package listeners;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;


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
        boolean result;
        String text;

        try {
            text = e.getDocument().getText(0, e.getLength()); //võtab textifieldi, mille listener see button on ja annab selle väärtuse stringile text
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
        //kuidas tsekkida, kas string koosneb ainult tähestikumärkidest
        //https://www.tutorialkart.com/java/how-to-check-if-string-contains-only-alphabets-in-java/#:~:text=To%20check%20if%20String%20contains%20only%20alphabets%20in%20Java%2C%20call,alphabets%20(uppercase%20or%20lowercase).

        if (text.matches("[a-zA-Z]+") && e.getDocument().getLength() > 0) result = true; // kui kõik tingimused on täidetud, siis enableb buttoni
        else result = false;

        //System.out.println(text);
        button.setEnabled(result);
    }


}

