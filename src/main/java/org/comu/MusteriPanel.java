package org.comu;

import io.github.cdimascio.dotenv.Dotenv;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
public class MusteriPanel extends JFrame {

    private JTextField idField, adField, soyadField, emailField, telefonField;
    private JTable musteriTable;
    private DefaultTableModel tableModel;
    private MusteriDAO dao;

    public MusteriPanel() {
        setTitle("Müşteri Destek Paneli");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Dotenv yüklemesi
        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .load();

        String dbType = dotenv.get("DB_TYPE");

        if (dbType == null) {
            JOptionPane.showMessageDialog(this, "DB_TYPE .env dosyasında tanımlı değil!");
            System.exit(1);
        }
        if (dbType.equalsIgnoreCase("mysql")) {
            dao = new MusteriDAO_MySQL(); // MySQL DAO
        } else if (dbType.equalsIgnoreCase("mongodb")) {
            String uri = dotenv.get("MONGO_URI");
            String dbName = dotenv.get("MONGO_DB");

            if (uri == null || dbName == null) {
                JOptionPane.showMessageDialog(this, "MONGO_URI veya MONGO_DB .env dosyasında eksik!");
                System.exit(1);
            }

            MongoClient mongoClient = MongoClients.create(uri);
            MongoDatabase database = mongoClient.getDatabase(dbName);

            dao = new MusteriDAOMongoDB(database);
        } else {
            JOptionPane.showMessageDialog(this, "Desteklenmeyen DB_TYPE: " + dbType);
            System.exit(1);
        }



        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        idField = new JTextField(); idField.setEnabled(false);
        adField = new JTextField();
        soyadField = new JTextField();
        emailField = new JTextField();
        telefonField = new JTextField();

        formPanel.add(new JLabel("ID:")); formPanel.add(idField);
        formPanel.add(new JLabel("Ad:")); formPanel.add(adField);
        formPanel.add(new JLabel("Soyad:")); formPanel.add(soyadField);
        formPanel.add(new JLabel("Email:")); formPanel.add(emailField);
        formPanel.add(new JLabel("Telefon:")); formPanel.add(telefonField);

        panel.add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Ad", "Soyad", "Email", "Telefon"}, 0);
        musteriTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(musteriTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton ekleBtn = new JButton("Ekle");
        JButton guncelleBtn = new JButton("Güncelle");
        JButton silBtn = new JButton("Sil");
        JButton listeleBtn = new JButton("Listele");
        JButton supportTicketPanelBtn = new JButton("Destek Talep Paneli Aç");
        JButton reportPanelBtn = new JButton("Rapor Paneli Aç");

        buttonPanel.add(ekleBtn);
        buttonPanel.add(guncelleBtn);
        buttonPanel.add(silBtn);
        buttonPanel.add(listeleBtn);
        buttonPanel.add(supportTicketPanelBtn);
        buttonPanel.add(reportPanelBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Buton işlemleri
        ekleBtn.addActionListener(e -> {
            Musteri m = new Musteri(adField.getText(), soyadField.getText(), emailField.getText(), telefonField.getText());
            dao.ekle(m);
            listele();
            temizle();
        });

        guncelleBtn.addActionListener(e -> {
            String id = idField.getText();
            if (!id.isEmpty()) {
                Musteri m = new Musteri(id, adField.getText(), soyadField.getText(), emailField.getText(), telefonField.getText());
                dao.guncelle(m);
                listele();
                temizle();
            }
        });

        silBtn.addActionListener(e -> {
            String id = idField.getText();
            if (!id.isEmpty()) {
                dao.sil(id);
                listele();
                temizle();
            }
        });

        listeleBtn.addActionListener(e -> listele());

        musteriTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int secili = musteriTable.getSelectedRow();
                if (secili >= 0) {
                    idField.setText(tableModel.getValueAt(secili, 0).toString());
                    adField.setText(tableModel.getValueAt(secili, 1).toString());
                    soyadField.setText(tableModel.getValueAt(secili, 2).toString());
                    emailField.setText(tableModel.getValueAt(secili, 3).toString());
                    telefonField.setText(tableModel.getValueAt(secili, 4).toString());
                }
            }
        });

        supportTicketPanelBtn.addActionListener(e -> openSupportTicketPanel(dbType));
        reportPanelBtn.addActionListener(e -> openReportPanel());

        add(panel);
        setVisible(true);
    }

    private void openSupportTicketPanel(String dbType) {
        JFrame frame;
        if (dbType.equalsIgnoreCase("mysql")) {
            frame = new SupportTicketFrame(new SupportTicketDAO());
        } else {
            frame = new SupportTicketFrame(new SupportTicketMongoDAO());
        }
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openReportPanel() {
        JFrame reportFrame = new JFrame("Rapor Paneli");
        reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reportFrame.add(new ReportPanel(new SupportTicketDAO())); // Gerekirse burada da dbType kontrolü eklenebilir
        reportFrame.pack();
        reportFrame.setLocationRelativeTo(null);
        reportFrame.setVisible(true);
    }

    private void listele() {
        tableModel.setRowCount(0);
        List<Musteri> musteriler = dao.listele();
        for (Musteri m : musteriler) {
            tableModel.addRow(new Object[]{m.getId(), m.getAd(), m.getSoyad(), m.getEmail(), m.getTelefon()});
        }
    }

    private void temizle() {
        idField.setText("");
        adField.setText("");
        soyadField.setText("");
        emailField.setText("");
        telefonField.setText("");
    }

    public static void main(String[] args) {
        new MusteriPanel();
    }
}
