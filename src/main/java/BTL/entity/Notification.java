/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.entity;

import java.sql.Timestamp;

/**
 *
 * @author vanminh
 */
public class Notification {
    private int notificationId;
    private int userId;
    private String title;
    private String message;
    private String image;
    private Timestamp createdAt;

    public Notification() {
    }

    // Constructor dùng để insert (không cần id và timestamp)
    public Notification(int userId, String title, String message, String image) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.image = image;
    }

    // Constructor đầy đủ để lấy dữ liệu từ DB
    public Notification(int notificationId, int userId, String title, String message, String image, Timestamp createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.image = image;
        this.createdAt = createdAt;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Notification{" + "notificationId=" + notificationId + ", userId=" + userId + ", title=" + title + ", message=" + message + ", image=" + image + ", createdAt=" + createdAt + '}';
    }
    
}