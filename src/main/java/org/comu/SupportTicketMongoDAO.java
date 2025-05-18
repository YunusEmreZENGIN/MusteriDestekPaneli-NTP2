package org.comu;

import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class SupportTicketMongoDAO implements SupportTicketDAOInterface{

    private final MongoCollection<Document> collection;

    public SupportTicketMongoDAO() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("musteridestekpanel");
        collection = database.getCollection("destek_talepleri");
    }

    public void createTicket(SupportTicket ticket) {
        Document doc = new Document("customer_id", ticket.getCustomerId())
                .append("category", ticket.getCategory())
                .append("status", ticket.getStatus())
                .append("description", ticket.getDescription())
                .append("created_at", new Date())
                .append("updated_at", new Date());

        collection.insertOne(doc);
    }

    public List<SupportTicket> listTickets() {
        List<SupportTicket> tickets = new ArrayList<>();
        FindIterable<Document> docs = collection.find();

        for (Document doc : docs) {
            SupportTicket ticket = new SupportTicket();
            ticket.setId(doc.getObjectId("_id").toString());
            ticket.setCustomerId(doc.getString("customer_id"));
            ticket.setCategory(doc.getString("category"));
            ticket.setStatus(doc.getString("status"));
            ticket.setDescription(doc.getString("description"));
            ticket.setCreatedAt(doc.getDate("created_at"));
            ticket.setUpdatedAt(doc.getDate("updated_at"));
            tickets.add(ticket);
        }

        return tickets;
    }

    public void updateTicketStatus(String ticketId, String status) {
        collection.updateOne(eq("_id", new org.bson.types.ObjectId(ticketId)),
                new Document("$set", new Document("status", status).append("updated_at", new Date())));
    }

    public void deleteTicket(String ticketId) {
        collection.deleteOne(eq("_id", new org.bson.types.ObjectId(ticketId)));
    }

    @Override
    public SupportTicket getTicketById(String id) {
        Document query = new Document("_id", new ObjectId(id));
        Document doc = collection.find(query).first();
        if (doc != null) {
            return new SupportTicket(
                    doc.getObjectId("_id").toHexString(),
                    doc.getString("customer_id"),
                    doc.getString("category"),
                    doc.getString("status"),
                    doc.getString("description")
            );
        }
        return null;
    }

    // Ek sorgular vs. burada da yazılabilir (kategoriye göre, duruma göre vs.)
}
