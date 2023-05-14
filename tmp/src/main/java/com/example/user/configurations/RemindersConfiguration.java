package com.example.user.configurations;

import com.example.user.models.Reminder;
import com.example.security.objects.Student;
import com.example.user.repository.RemindersRepository;
import com.example.security.repositories.StudentsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
public class RemindersConfiguration {

//    @Bean
//    CommandLineRunner commandLineRunnerForReminders(StudentsRepository studentsRepository, RemindersRepository remindersRepository) {
//        Student tempStudent = new Student(
//                "Duluta",
//                "George",
//                "george.duluta@yahoo.com",
//                "sfdgsdf",
//                2,
//                1,
//                "234",
//                new HashSet<>(Arrays.asList("RC", "LFAC", "BD", "AG", "QC")));
//
//        Student tempStudent2 = new Student(
//                "logged",
//                "user",
//                "reminderOwner@yahoo.com",
//                "loggeduser",
//                3,
//                2,
//                "£425",
//                null
//        );
//
//        studentsRepository.save(tempStudent);
//
//        studentsRepository.save(tempStudent2);
//
//        return args -> {
//            Reminder reminder1 = new Reminder(
//                    tempStudent,
//                    "25.04.2023 14:30",
//                    "Examen la pedagogie",
//                    "Examen maine la pedagogie la ora 16 in C2"
//            );
//
//            Reminder reminder2 = new Reminder(
//                    tempStudent,
//                    "06.06.2023 11:30",
//                    "Examen-IP",
//                    "Primul examen din sesiune"
//            );
//
//            Reminder reminder3 = new Reminder(
//                    tempStudent2,
//                    "08.06.2023 10:00",
//                    "Examen-OF",
//                    "Al doilea examen din sesiune"
//            );
//
//            remindersRepository.saveAll(List.of(reminder1, reminder2, reminder3));
//        };
//    }
}
