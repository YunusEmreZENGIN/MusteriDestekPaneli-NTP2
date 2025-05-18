package org.comu;

import java.util.List;
public interface SupportTicketDAOInterface {
    void createTicket(SupportTicket ticket);
    List<SupportTicket> listTickets();
    void updateTicketStatus(String ticketId, String newStatus);
    void deleteTicket(String ticketId);
    SupportTicket getTicketById(String id);
}
