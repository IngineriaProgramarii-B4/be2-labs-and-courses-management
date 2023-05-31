package com.example.user.services;

import com.example.user.models.Reminder;
import com.example.user.repository.RemindersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RemindersService {
    private final RemindersRepository remindersRepository;

    public RemindersService(RemindersRepository remindersRepository) {
        this.remindersRepository = remindersRepository;
    }

    public List<Reminder> getRemindersByParams(Map<String, Object> params) {
        //String creatorUsername = (String)params.get("creatorUsername");
        UUID id = (UUID) params.get("id");
        UUID creatorId = (UUID) params.get("creatorId");
        return remindersRepository.findRemindersByParams(id, creatorId);
    }

    public void saveReminder(Reminder reminder) {
        remindersRepository.save(reminder);
    }

    @Transactional
    public void removeReminder(UUID uuid) {
        remindersRepository.remove(uuid);
    }

    @Transactional
    public void updateReminder(UUID id, Reminder reminder) {
        remindersRepository.updateReminder(id, reminder.getCreatorId(), reminder.getCreatorUsername(), reminder.getDueDateTime(), reminder.getTitle(), reminder.getDescription());
    }
}

