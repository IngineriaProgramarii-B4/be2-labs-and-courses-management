package com.example.subject.api;
import com.example.subject.model.Evaluation;
import com.example.subject.service.ComponentService;
import com.example.subject.service.EvaluationService;
import com.example.subject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "api/v1/subjects/{subjectTitle}/evaluationMethods")
@CrossOrigin(origins = "http://localhost:3000")
public class EvaluationController {

    private static final String SUBJECT_ERROR = "Subject not found";
    private static final String COMPONENT_ERROR = "Component not found";
    private static final String EVALUATION_ERROR = "Evaluation method not found";

    private final EvaluationService evaluationService;
    private final SubjectService subjectService;
    private final ComponentService componentService;
    @Autowired
    public EvaluationController(EvaluationService evaluationService, SubjectService subjectService, ComponentService componentService) {
        this.evaluationService = evaluationService;
        this.subjectService = subjectService;
        this.componentService = componentService;
    }

    @GetMapping
    public ResponseEntity<List<Evaluation>> getEvaluationMethods(@PathVariable("subjectTitle") String title) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(null);
        return ResponseEntity.status(OK).body(evaluationService.getEvaluationMethods(title));
    }

    @GetMapping(path = "component={component}")
    public ResponseEntity<Evaluation> getEvaluationMethodByComponent(@PathVariable("subjectTitle") String title,
                                                     @PathVariable("component") String component) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(null);
        if(componentService.getComponentByType(title, component).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(null);
        /*return evaluationService.getEvaluationMethodByComponent(title, component)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, EVALUATION_ERROR));*/
        return evaluationService.getEvaluationMethodByComponent(title, component)
                .map(evaluation -> ResponseEntity.status(OK).body(evaluation))
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(null));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> addEvaluationMethod(@PathVariable("subjectTitle") String title,
                                    @RequestBody Evaluation evaluation) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(SUBJECT_ERROR.getBytes());
        if(componentService.getComponentByType(title, evaluation.getComponent()).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(COMPONENT_ERROR.getBytes());
        if(evaluationService.addEvaluationMethod(title, evaluation) == 0)
            /*throw new ResponseStatusException(NOT_ACCEPTABLE, "Evaluation method is invalid");*/
            return ResponseEntity.status(NOT_ACCEPTABLE).body("Evaluation method is invalid".getBytes());
        /*throw new ResponseStatusException(CREATED, "Evaluation method added successfully");*/
        return ResponseEntity.status(CREATED).body("Evaluation method added successfully".getBytes());
    }

    @DeleteMapping(path = "component={component}")
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> deleteEvaluationMethodByComponent(@PathVariable("subjectTitle") String title,
                                                  @PathVariable("component") String component) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(SUBJECT_ERROR.getBytes());
        if(componentService.getComponentByType(title, component).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(COMPONENT_ERROR.getBytes());
        if(evaluationService.deleteEvaluationMethodByComponent(title, component) == 0)
            /*throw new ResponseStatusException(NOT_FOUND, EVALUATION_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(EVALUATION_ERROR.getBytes());
        /*throw new ResponseStatusException(NO_CONTENT, "Evaluation method deleted successfully");*/
        return ResponseEntity.status(NO_CONTENT).body("Evaluation method deleted successfully".getBytes());
    }

    @PutMapping(path = "component={component}")
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> updateEvaluationMethodByComponent(@PathVariable("subjectTitle") String title,
                                                  @PathVariable("component") String component,
                                                  @RequestBody Evaluation evaluation) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(SUBJECT_ERROR.getBytes());
        if(componentService.getComponentByType(title, component).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(COMPONENT_ERROR.getBytes());
        if(evaluationService.updateEvaluationMethodByComponent(title, component, evaluation) == 0) {
            /*throw new ResponseStatusException(NOT_FOUND, EVALUATION_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(EVALUATION_ERROR.getBytes());
        }
        /*throw new ResponseStatusException(NO_CONTENT, "Evaluation method updated successfully");*/
        return ResponseEntity.status(NO_CONTENT).body("Evaluation method updated successfully".getBytes());
    }
}
