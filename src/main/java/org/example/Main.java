package org.example;

import org.example.Connection.ConnectionJDBC;
import org.example.Livre.BookForm;

public class Main {
    public static void main(String[] args) {
        //ConnectionJDBC.conncetionJDBC();
        BookForm bookForm = new BookForm();
        bookForm.setVisible(true);
    }


}