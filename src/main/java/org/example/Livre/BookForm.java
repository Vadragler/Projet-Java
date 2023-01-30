package org.example.Livre;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookForm extends JFrame {
    private JTextField titleField, authorField, publisherField;

    public BookForm() {
        setTitle("Saisie de livre");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Titre :"));
        titleField = new JTextField();
        panel.add(titleField);
        panel.add(new JLabel("Auteur :"));
        authorField = new JTextField();
        panel.add(authorField);
        panel.add(new JLabel("Editeur :"));
        publisherField = new JTextField();
        panel.add(publisherField);

        add(panel);
    }

    public String getTitle() {
        return titleField.getText();
    }

    public String getAuthor() {
        return authorField.getText();
    }

    public String getPublisher() {
        return publisherField.getText();
    }
}
