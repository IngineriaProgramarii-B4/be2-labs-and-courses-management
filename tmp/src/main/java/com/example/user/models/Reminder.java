package com.example.user.models;

import com.example.security.objects.DBObject;
import com.example.security.objects.Student;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "reminders")
public class Reminder extends DBObject {
    private UUID creatorId;
    private String creatorUsername;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime dueDateTime;
    private String title;
    private String description;

    public Reminder() {

    }

    public Reminder(Student student, UUID id, String dueDateTime, String title, String description) {
        this.creatorId = student.getId();
        this.creatorUsername = student.getUsername();
        this.id = id;
        this.dueDateTime = LocalDateTime.parse(dueDateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"  ));
        this.title = title;
        this.description = description;
    }

    public Reminder(Student student, String dueDateTime, String title, String description) {
        this.creatorId = student.getId();
        creatorUsername = student.getUsername();
        this.dueDateTime = LocalDateTime.parse(dueDateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"  ));
        this.title = title;
        this.description = description;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Reminder reminder = (Reminder) o;
        return Objects.equals(creatorId, reminder.creatorId) && Objects.equals(creatorUsername, reminder.creatorUsername) && Objects.equals(id, reminder.id) && Objects.equals(dueDateTime, reminder.dueDateTime) && Objects.equals(title, reminder.title) && Objects.equals(description, reminder.description);
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "creatorId=" + creatorId +
                ", creatorUsername='" + creatorUsername + '\'' +
                ", id=" + id +
                ", dueDateTime=" + dueDateTime +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
