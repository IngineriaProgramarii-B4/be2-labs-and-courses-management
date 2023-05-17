package com.example.demo;

import com.example.demo.objects.Seminar;
import com.example.demo.services.SeminarService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private SeminarService seminarService;

	@Test
	public void testRun() {
		// Adaugă seminarii
		seminarService.addSeminars("mate1");

		// Obține seminarul adăugat
		String seminarName = "mate1";
		List<Seminar> seminars = seminarService.getSeminarByName(seminarName);

		// Verifică că s-a adăugat corect un singur seminar cu numele "mate1"
		assertEquals(seminarName, seminars.get(0).getName());

		// Actualizează numele seminarului
		String newSeminarName = "pr1";
		seminarService.updateSeminar(seminars.get(0), newSeminarName);

		// Obține seminarul actualizat
		seminars = seminarService.getSeminarByName(newSeminarName);

		assertEquals(newSeminarName, seminars.get(0).getName());
	}
}