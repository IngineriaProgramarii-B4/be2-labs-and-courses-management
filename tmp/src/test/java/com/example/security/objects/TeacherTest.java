package com.example.security.objects;
import com.example.security.objects.Teacher;
import com.example.subject.model.Subject;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class TeacherTest {

    @Test
    public void testSetTaughtSubjects_SetsTaughtSubjects() {
        Teacher teacher = new Teacher();
        Set<Subject> taughtSubjects = new HashSet<>();

        teacher.setTaughtSubjects(taughtSubjects);

        assertEquals(taughtSubjects, teacher.getTaughtSubjects());
    }

    @Test
    public void testGetTitle_ReturnsTitle() {
        String title = "Professor";
        Teacher teacher = new Teacher();
        teacher.setTitle(title);

        assertEquals(title, teacher.getTitle());
    }

}
