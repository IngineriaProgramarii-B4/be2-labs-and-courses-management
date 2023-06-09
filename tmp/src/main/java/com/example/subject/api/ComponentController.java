package com.example.subject.api;

import com.example.subject.model.Component;
import com.example.subject.service.ComponentService;
import com.example.subject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/v1/subjects/{subjectTitle}/components")
@CrossOrigin(origins = "http://localhost:3000")
public class ComponentController {

    private static final String SUBJECT_ERROR = "Subject not found";
    private static final String COMPONENT_ERROR = "Component not found";
    private static final String COMPONENT_BAD = "Component is invalid";

    private final ComponentService componentService;
    private final SubjectService subjectService;
    @Autowired
    public ComponentController(ComponentService componentService, SubjectService subjectService){
        this.componentService = componentService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<Component> getComponents(@PathVariable("subjectTitle") String title) {
        return componentService.getComponents(title);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> addComponent(@PathVariable("subjectTitle") String title, @RequestBody Component component) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            return ResponseEntity.status(NOT_FOUND).body(SUBJECT_ERROR.getBytes());

        if(componentService.addComponent(title, component) == 0)
            return ResponseEntity.status(NOT_ACCEPTABLE).body(COMPONENT_BAD.getBytes());
        return ResponseEntity.status(CREATED).body("Component added successfully".getBytes());
    }

    @GetMapping(path = "type={type}")
    public ResponseEntity<Component> getComponentByType(@PathVariable("subjectTitle") String title, @PathVariable("type") String type) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            return ResponseEntity.status(NOT_FOUND).body(null);

        return componentService.getComponentByType(title, type)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(null));
    }

    @DeleteMapping(path = "type={type}")
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> deleteComponentByType(@PathVariable("subjectTitle") String title, @PathVariable("type") String type) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            return ResponseEntity.status(NOT_FOUND).body(SUBJECT_ERROR.getBytes());

        if(componentService.deleteComponentByType(title, type) == 0)
            return ResponseEntity.status(NOT_FOUND).body(COMPONENT_ERROR.getBytes());
        return ResponseEntity.status(NO_CONTENT).body("Component deleted successfully".getBytes());
    }

    @PutMapping(path = "type={type}")
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> updateComponentByType(@PathVariable("subjectTitle") String title,
                                      @PathVariable("type") String type,
                                      @RequestBody Component component) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            return ResponseEntity.status(NOT_FOUND).body(SUBJECT_ERROR.getBytes());
        if(componentService.getComponentByType(title, type).isEmpty())
            return ResponseEntity.status(NOT_FOUND).body(COMPONENT_ERROR.getBytes());
        if(componentService.updateComponentByType(title, type, component) == 0)
            return ResponseEntity.status(BAD_REQUEST).body(COMPONENT_BAD.getBytes());

        return ResponseEntity.status(NO_CONTENT).body("Component updated successfully".getBytes());
    }
}
