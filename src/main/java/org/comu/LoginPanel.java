package org.comu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserDAO userDAO;

    public LoginPanel(UserDAO userDAO) {
        this.userDAO = userDAO;
        setTitle("Login Paneli");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        // Kullanıcı adı ve şifre giriş alanları
        panel.add(new JLabel("Kullanıcı Adı:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Şifre:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // Giriş butonu
        loginButton = new JButton("Giriş Yap");
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);

        // Giriş butonuna tıklama işlemi
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (userDAO.login(username, password)) {
                    JOptionPane.showMessageDialog(null, "Giriş Başarılı!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    dispose();

                    new MusteriPanel(); // Ana paneli aç
                } else {
                    JOptionPane.showMessageDialog(null, "Geçersiz kullanıcı adı veya şifre.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        new LoginPanel(userDAO);
    }
}

