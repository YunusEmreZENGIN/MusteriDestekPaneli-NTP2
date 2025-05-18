package org.comu;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SupportTicketFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton addButton, updateButton, deleteButton;

    private SupportTicketDAOInterface dao; // MySQL veya MongoDAO olarak atanacak

    public SupportTicketFrame(SupportTicketDAOInterface dao) {
        this.dao = dao;

        setTitle("Destek Talepleri");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Müşteri ID", "Kategori", "Durum", "Açıklama"}, 0);
        table = new JTable(tableModel);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);

        addButton = new JButton("Ekle");
        updateButton = new JButton("Güncelle");
        deleteButton = new JButton("Sil");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Buton işlemleri
        addButton.addActionListener(e -> openTicketDialog(null)); // Yeni kayıt
        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Lütfen güncellemek için bir satır seçin.");
                return;
            }
            String id = tableModel.getValueAt(selectedRow, 0).toString();
            SupportTicket ticket = dao.getTicketById(id);
            openTicketDialog(ticket);
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Lütfen silmek için bir satır seçin.");
                return;
            }
            String id = tableModel.getValueAt(selectedRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Seçili kaydı silmek istediğinize emin misiniz?", "Silme Onayı", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dao.deleteTicket(id);
                refreshTable();
            }
        });
    }

    private void refreshTable() {
        List<SupportTicket> tickets = dao.listTickets();
        tableModel.setRowCount(0);
        for (SupportTicket t : tickets) {
            tableModel.addRow(new Object[]{t.getId(), t.getCustomerId(), t.getCategory(), t.getStatus(), t.getDescription()});
        }
    }

    private void openTicketDialog(SupportTicket ticket) {
        TicketFormDialog dialog = new TicketFormDialog(this, dao, ticket);
        dialog.setVisible(true);
        refreshTable();
    }

    public static void main(String[] args) {
        // Burada istediğin DAO'yu oluşturup ver
        SupportTicketDAOInterface dao = new SupportTicketDAO(); // veya new SupportTicketMongoDAO();
        SwingUtilities.invokeLater(() -> new SupportTicketFrame(dao).setVisible(true));
    }
}
