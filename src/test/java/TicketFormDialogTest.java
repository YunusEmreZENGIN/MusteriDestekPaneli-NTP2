


import org.comu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class TicketFormDialogTest {

    private MockSupportTicketDAO mockDao;

    @BeforeEach
    public void setUp() {
        mockDao = new MockSupportTicketDAO();
    }

    @Test
    public void testCreateNewTicket() {
        // Yeni ticket formu oluştur
        TicketFormDialog dialog = new TicketFormDialog(null, mockDao, null);

        // Form alanlarını doldur
        setText(dialog, "customerIdField", "CUST001");
        setCombo(dialog, "categoryCombo", "Teknik Destek");
        setCombo(dialog, "statusCombo", "Yeni");
        setTextArea(dialog, "descriptionArea", "Bağlantı problemi yaşıyorum.");

        // Simüle et: "Kaydet" butonuna basılmış gibi
        clickButton(dialog, "saveButton");

        // Mock DAO’ya kayıt eklendi mi kontrol et
        assertEquals(1, mockDao.getTickets().size());

        SupportTicket saved = mockDao.getTickets().get(0);
        assertEquals("CUST001", saved.getCustomerId());
        assertEquals("Teknik Destek", saved.getCategory());
        assertEquals("Yeni", saved.getStatus());
        assertEquals("Bağlantı problemi yaşıyorum.", saved.getDescription());
    }

    // Yardımcı metotlar — testin GUI ile çalışmasını sağlar
    private void setText(TicketFormDialog dialog, String fieldName, String value) {
        try {
            JTextField field = (JTextField) dialog.getClass().getDeclaredField(fieldName).get(dialog);
            field.setText(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setTextArea(TicketFormDialog dialog, String fieldName, String value) {
        try {
            JTextArea area = (JTextArea) dialog.getClass().getDeclaredField(fieldName).get(dialog);
            area.setText(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setCombo(TicketFormDialog dialog, String fieldName, String value) {
        try {
            JComboBox<?> combo = (JComboBox<?>) dialog.getClass().getDeclaredField(fieldName).get(dialog);
            combo.setSelectedItem(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void clickButton(TicketFormDialog dialog, String fieldName) {
        try {
            JButton button = (JButton) dialog.getClass().getDeclaredField(fieldName).get(dialog);
            for (ActionListener listener : button.getActionListeners()) {
                listener.actionPerformed(new java.awt.event.ActionEvent(button, ActionEvent.ACTION_PERFORMED, null));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
