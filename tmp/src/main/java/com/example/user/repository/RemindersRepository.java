package com.example.user.repository;

import com.example.user.models.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RemindersRepository extends JpaRepository<Reminder, UUID> {

    @Query("select a from Reminder a where (?1 is null or a.creatorUsername=?1) and (cast(?2 as uuid) is null or a.id = ?2) and (a.isDeleted=false)")
    List<Reminder> findRemindersByParams(String creatorUsername, UUID id);

    @Modifying
    @Query("update Reminder a set a.creatorId = COALESCE(?2, a.creatorId), " +
            "a.creatorUsername = COALESCE(?3, a.creatorUsername), " +
            "a.dueDateTime = COALESCE(?4, a.dueDateTime), " +
            "a.title = COALESCE(?5, a.title), " +
            "a.description = COALESCE(?6, a.description) " +
            "where a.id = ?1"
    )
    void updateReminder(UUID uuid, UUID creatorId, String creatorUsername, LocalDateTime dueDateTime, String title, String description);

    @Modifying
    @Query("update Reminder a set a.isDeleted = true where a.id = ?1")
    void remove(UUID uuid);
}
