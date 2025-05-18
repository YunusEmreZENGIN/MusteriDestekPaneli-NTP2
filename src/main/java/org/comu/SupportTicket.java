package org.comu;

import java.util.Date;

public class SupportTicket {
    private String id;
    private String customerId;
    private String category;
    private String status; // "Yeni", "Çözülmüş", "Devam Ediyor"
    private String description;
    private Date createdAt;
    private Date updatedAt;

    // Yapıcı metod
    public SupportTicket(String id, String customerId, String category, String status, String description) {
        this.id = id;
        this.customerId = customerId;
        this.category = category;
        this.status = status;
        this.description = description;
    }

    // Boş yapıcı (JDBC için önerilir)
    public SupportTicket() {}

    // Getter ve Setter metodları
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
