package com.example.user.controllers;

import com.example.user.models.Reminder;
import com.example.security.objects.Student;
import com.example.user.services.RemindersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("api/v1/")
public class RemindersController {
    private final RemindersService remindersService;

    @Autowired
    public RemindersController(RemindersService remindersService) {
        this.remindersService = remindersService;
    }

    @Operation(summary = "Get a specific reminder of a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the reminder.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Student.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Haven't found the reminder.",
                    content = @Content
            )
    })
    @GetMapping(value = {"/reminders/{username}/{id}"})
    public ResponseEntity<List<Reminder>> getRemindersByParams(@PathVariable String username, @PathVariable UUID id) {
        Map<String, Object> args = new HashMap<>();

        args.put("creatorUsername", username);
        args.put("id", id);

        List<Reminder> reminders = remindersService.getRemindersByParams(args);

        if (reminders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reminders, HttpStatus.OK);
    }

    @Operation(summary = "Get a list of reminders of a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found reminders",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Student.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Haven't found reminders",
                    content = @Content
            )
    })
    @GetMapping(value = {"/reminders/{username}"})
    public ResponseEntity<List<Reminder>> getRemindersOfLoggedUser(@PathVariable String username) {

        Map<String, Object> params = Map.of("creatorUsername", username);

        List<Reminder> reminders = remindersService.getRemindersByParams(params);

        if (reminders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reminders, HttpStatus.OK);
    }

    @Operation(summary = "Receive necessary data in order to add a new reminder in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource added successfully.",
                    content = @Content)
    })
    @PostMapping(value = "/reminders")
    public ResponseEntity<Void> saveReminder(@RequestBody Reminder reminder) {
        remindersService.saveReminder(reminder);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Soft deleting an existing reminder from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Haven't found a reminder to delete with those descriptions",
                    content = @Content),
            @ApiResponse(responseCode = "204", description = "Reminder soft deleted successfully",
                    content = @Content)
    })
    @DeleteMapping(value = "/reminders/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable UUID id) {
        if (!remindersService.getRemindersByParams(Map.of("id", id)).isEmpty()) {
            remindersService.removeReminder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Receive necessary data in order to update information about a reminder in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource updated successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Haven't found reminder that match the requirements",
                    content = @Content
            )
    })
    @PatchMapping(value = "/reminder/{id}")
    public ResponseEntity<Void> updateReminder(@PathVariable UUID id, @RequestBody Reminder reminder) {
        if (!remindersService.getRemindersByParams(Map.of("id", id)).isEmpty()) {
            System.out.println(reminder);
            remindersService.updateReminder(id, reminder);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
