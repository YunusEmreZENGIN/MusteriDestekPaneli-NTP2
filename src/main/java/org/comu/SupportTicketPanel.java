package org.comu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
public class SupportTicketPanel extends JPanel {

    private JTextField categoryField, descriptionField;
    private JComboBox<String> statusComboBox;
    private JTable ticketTable;
    private DefaultTableModel tableModel;

    private JButton createTicketButton;
    private JButton updateStatusButton;
    private JButton deleteTicketButton;

    private SupportTicketDAOInterface ticketDAO;

    public SupportTicketPanel(SupportTicketDAOInterface ticketDAO) {
        this.ticketDAO = ticketDAO;
        setLayout(new BorderLayout());

        // Form panel for ticket details
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        categoryField = new JTextField();
        descriptionField = new JTextField();
        statusComboBox = new JComboBox<>(new String[]{"Yeni", "Devam Ediyor", "Çözülmüş"});

        formPanel.add(new JLabel("Kategori:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Açıklama:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Durum:"));
        formPanel.add(statusComboBox);

        // 💡 Sınıf seviyesinde tanımlanan butonlar burada başlatılıyor:
        createTicketButton = new JButton("Destek Talebi Oluştur");
        updateStatusButton = new JButton("Durum Güncelle");
        deleteTicketButton = new JButton("Destek Talebi Sil");

        // ActionListeners
        createTicketButton.addActionListener(e -> {
            String category = categoryField.getText();
            String description = descriptionField.getText();
            String status = statusComboBox.getSelectedItem().toString();
            SupportTicket ticket = new SupportTicket(null, "customerId", category, status, description);
            ticketDAO.createTicket(ticket);
            loadTickets();
        });

        updateStatusButton.addActionListener(e -> {
            int selectedRow = ticketTable.getSelectedRow();
            if (selectedRow >= 0) {
                String ticketId = tableModel.getValueAt(selectedRow, 0).toString();
                String newStatus = statusComboBox.getSelectedItem().toString();
                ticketDAO.updateTicketStatus(ticketId, newStatus);
                loadTickets();
            }
        });

        deleteTicketButton.addActionListener(e -> {
            int selectedRow = ticketTable.getSelectedRow();
            if (selectedRow >= 0) {
                String ticketId = tableModel.getValueAt(selectedRow, 0).toString();
                ticketDAO.deleteTicket(ticketId);
                loadTickets();
            }
        });

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Kategori", "Durum", "Açıklama"}, 0);
        ticketTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ticketTable);

        // Add components
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createTicketButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(deleteTicketButton);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        loadTickets();
    }

    private void loadTickets() {
        tableModel.setRowCount(0);
        List<SupportTicket> tickets = ticketDAO.listTickets();
        for (SupportTicket ticket : tickets) {
            tableModel.addRow(new Object[]{
                    ticket.getId(),
                    ticket.getCategory(),
                    ticket.getStatus(),
                    ticket.getDescription()
            });
        }
    }
    public void reload() {
        loadTickets();
    }

}
