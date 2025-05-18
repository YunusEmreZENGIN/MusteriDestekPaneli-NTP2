import org.comu.SupportTicket;
import org.comu.SupportTicketDAOInterface;

import java.util.ArrayList;
import java.util.List;

public class MockSupportTicketDAO implements SupportTicketDAOInterface {
    public List<SupportTicket> tickets = new ArrayList<>();

    @Override
    public void createTicket(SupportTicket ticket) {
        ticket.setId("mock-id-" + (tickets.size() + 1));
        tickets.add(ticket);
    }

    @Override
    public List<SupportTicket> listTickets() {
        return List.of();
    }

    @Override
    public void updateTicketStatus(String id, String newStatus) {
        for (SupportTicket t : tickets) {
            if (t.getId().equals(id)) {
                t.setStatus(newStatus);
                break;
            }
        }
    }

    @Override
    public void deleteTicket(String ticketId) {

    }

    @Override
    public SupportTicket getTicketById(String id) {
        return null;
    }

    // Diğer DAO metotları varsa eklenebilir
}
