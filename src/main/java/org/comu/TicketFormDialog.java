package org.comu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;
import java.awt.*;

public class TicketFormDialog extends JDialog {
    private JTextField customerIdField;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> statusCombo;  // Burayı JComboBox yaptık
    private JTextArea descriptionArea;

    private JButton saveButton, cancelButton;
    private SupportTicketDAOInterface dao;
    private SupportTicket ticket;

    private static final String[] CATEGORIES = {"Teknik Destek", "Satış", "Genel", "Diğer"};
    private static final String[] STATUSES = {"Yeni","Çözülecek", "Çözülmüş"};  // Durum seçenekleri

    public TicketFormDialog(Frame owner, SupportTicketDAOInterface dao, SupportTicket ticket) {
        super(owner, true);
        this.dao = dao;
        this.ticket = ticket;

        setTitle(ticket == null ? "Yeni Talep Ekle" : "Talep Güncelle");
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Müşteri ID:"));
        customerIdField = new JTextField();
        formPanel.add(customerIdField);

        formPanel.add(new JLabel("Kategori:"));
        categoryCombo = new JComboBox<>(CATEGORIES);
        formPanel.add(categoryCombo);

        formPanel.add(new JLabel("Durum:"));
        statusCombo = new JComboBox<>(STATUSES);  // Durum combo box eklendi
        formPanel.add(statusCombo);

        formPanel.add(new JLabel("Açıklama:"));
        descriptionArea = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        formPanel.add(descScroll);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("Kaydet");
        cancelButton = new JButton("İptal");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Eğer ticket varsa, alanları doldur
        if (ticket != null) {
            customerIdField.setText(ticket.getCustomerId());
            categoryCombo.setSelectedItem(ticket.getCategory());
            statusCombo.setSelectedItem(ticket.getStatus());  // Burada da seçili durumu ayarla
            descriptionArea.setText(ticket.getDescription());
        }

        saveButton.addActionListener(e -> {
            if (customerIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Müşteri ID boş olamaz!");
                return;
            }
            if (statusCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Durum seçimi boş olamaz!");
                return;
            }

            if (ticket == null) {
                SupportTicket newTicket = new SupportTicket();
                newTicket.setCustomerId(customerIdField.getText().trim());
                newTicket.setCategory((String) categoryCombo.getSelectedItem());
                newTicket.setStatus((String) statusCombo.getSelectedItem());  // Status combo box'tan al
                newTicket.setDescription(descriptionArea.getText().trim());
                dao.createTicket(newTicket);
            } else {
                ticket.setCustomerId(customerIdField.getText().trim());
                ticket.setCategory((String) categoryCombo.getSelectedItem());
                ticket.setStatus((String) statusCombo.getSelectedItem());  // Status combo box'tan al
                ticket.setDescription(descriptionArea.getText().trim());
                dao.updateTicketStatus(ticket.getId(), ticket.getStatus());
            }
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());
    }
}
