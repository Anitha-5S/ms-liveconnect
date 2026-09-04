package com.c2.lc.ms.customer.entities.customer;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification")
public class NotificationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("n_notification_id")
    @Column(name = "n_notification_id",unique = true, nullable = false)
    private long notificationId;

    @SerializedName("n_user_id")
    @Column(name = "n_user_id")
    private long userId;

    @SerializedName("c_notification_type")
    @Column(name = "c_notification_type", length = 16)
    private String notificationType;

    @SerializedName("c_message")
    @Column(name = "c_message", length = 1024)
    private String message;

    @SerializedName("c_status")
    @Column(name = "c_status", length = 2)
    private String status;

    @SerializedName("t_created_timestamp")
    @Column(name = "t_created_timestamp")
    private LocalDateTime createdTimeStamp;

    @SerializedName("t_read_timestamp")
    @Column(name = "t_read_timestamp")
    private LocalDateTime readTimeStamp;

    @SerializedName("t_deleted_timestamp")
    @Column(name = "t_deleted_timestamp")
    private LocalDateTime deletedTimeStamp;

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationEntity that = (NotificationEntity) o;
        return notificationId == that.notificationId &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(notificationType, that.notificationType) &&
                Objects.equals(message, that.message) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdTimeStamp, that.createdTimeStamp) &&
                Objects.equals(readTimeStamp, that.readTimeStamp) &&
                Objects.equals(deletedTimeStamp, that.deletedTimeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationId, userId, notificationType, message, status, createdTimeStamp, readTimeStamp, deletedTimeStamp);
    }*/

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(LocalDateTime createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public LocalDateTime getReadTimeStamp() {
        return readTimeStamp;
    }

    public void setReadTimeStamp(LocalDateTime readTimeStamp) {
        this.readTimeStamp = readTimeStamp;
    }

    public LocalDateTime getDeletedTimeStamp() {
        return deletedTimeStamp;
    }

    public void setDeletedTimeStamp(LocalDateTime deletedTimeStamp) {
        this.deletedTimeStamp = deletedTimeStamp;
    }
}
