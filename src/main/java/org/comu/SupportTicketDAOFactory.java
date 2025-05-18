package org.comu;

import io.github.cdimascio.dotenv.Dotenv;

public class SupportTicketDAOFactory {

    public static SupportTicketDAOInterface createDAO() {
        Dotenv dotenv = Dotenv.load();
        String dbType = dotenv.get("DATABASE_TYPE");

        if ("mongodb".equalsIgnoreCase(dbType)) {
            return new SupportTicketMongoDAO();
        } else if ("mysql".equalsIgnoreCase(dbType)) {
            return new SupportTicketDAO();
        } else {
            throw new RuntimeException("Unsupported DATABASE_TYPE: " + dbType);
        }
    }
}
