package org.comu;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class ReportPanel extends JPanel {

    private JComboBox<String> reportSelector;
    private JTextArea reportArea;
    private SupportTicketDAO ticketDAO;

    public ReportPanel(SupportTicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
        setLayout(new BorderLayout());

        // Rapor türü seçimi için ComboBox
        reportSelector = new JComboBox<>(new String[]{"Son 30 Gündeki Çözülen Talepler", "Teknik Destek Kategorisindeki Talepler"});
        reportSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        // Raporu göstermek için JTextArea
        reportArea = new JTextArea(15, 40);
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);

        // Panel düzeni
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Rapor Seç:"));
        topPanel.add(reportSelector);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        // Grafik butonları
        JButton pieChartButton = new JButton("Kategori Dağılımı (Pie Chart)");
        pieChartButton.addActionListener(e -> {
            List<SupportTicket> tickets = ticketDAO.listTickets();
            PieChartExample chart = new PieChartExample(tickets);
            chart.setVisible(true);
        });

        JButton barChartButton = new JButton("Çözülmüş Talepler (Bar Chart)");
        barChartButton.addActionListener(e -> {
            List<SupportTicket> resolvedTickets = ticketDAO.getResolvedTicketsLast30Days();
            BarChartExample chart = new BarChartExample(resolvedTickets);
            chart.setVisible(true);
        });

// Butonları topPanel'e ekle
        topPanel.add(pieChartButton);
        topPanel.add(barChartButton);

        // Başlangıçta rapor oluşturulacak
        generateReport();
    }

    private void generateReport() {
        String selectedReport = (String) reportSelector.getSelectedItem();
        StringBuilder reportContent = new StringBuilder();

        if ("Son 30 Gündeki Çözülen Talepler".equals(selectedReport)) {
            // Son 30 gündeki çözülen talepler
            List<SupportTicket> resolvedTickets = ticketDAO.getResolvedTicketsLast30Days();
            reportContent.append("Son 30 Gündeki Çözülen Talepler:\n\n");
            for (SupportTicket ticket : resolvedTickets) {
                reportContent.append("ID: ").append(ticket.getId()).append("\n")
                        .append("Kategori: ").append(ticket.getCategory()).append("\n")
                        .append("Açıklama: ").append(ticket.getDescription()).append("\n")
                        .append("Durum: ").append(ticket.getStatus()).append("\n\n");
            }
        } else if ("Teknik Destek Kategorisindeki Talepler".equals(selectedReport)) {
            // Teknik destek kategorisindeki talepler
            List<SupportTicket> techSupportTickets = ticketDAO.getTechnicalSupportTickets();
            reportContent.append("Teknik Destek Kategorisindeki Talepler:\n\n");
            for (SupportTicket ticket : techSupportTickets) {
                reportContent.append("ID: ").append(ticket.getId()).append("\n")
                        .append("Kategori: ").append(ticket.getCategory()).append("\n")
                        .append("Açıklama: ").append(ticket.getDescription()).append("\n")
                        .append("Durum: ").append(ticket.getStatus()).append("\n\n");
            }
        }

        reportArea.setText(reportContent.toString());
    }
}
