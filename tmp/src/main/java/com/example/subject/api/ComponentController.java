package com.example.subject.api;

import com.example.subject.model.Component;
import com.example.subject.service.ComponentService;
import com.example.subject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public void addComponent(@PathVariable("subjectTitle") String title, @RequestBody Component component) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);

        if(componentService.addComponent(title, component) == 0)
            throw new ResponseStatusException(NOT_ACCEPTABLE, COMPONENT_BAD);
        throw new ResponseStatusException(CREATED, "Component added successfully");
    }

    @GetMapping(path = "type={type}")
    public Component getComponentByType(@PathVariable("subjectTitle") String title, @PathVariable("type") String type) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);

        return componentService.getComponentByType(title, type)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR));
    }

    @DeleteMapping(path = "type={type}")
    public void deleteComponentByType(@PathVariable("subjectTitle") String title, @PathVariable("type") String type) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);

        if(componentService.deleteComponentByType(title, type) == 0)
            throw new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR);
        throw new ResponseStatusException(NO_CONTENT, "Component deleted successfully");
    }

    @PutMapping(path = "type={type}")
    public void updateComponentByType(@PathVariable("subjectTitle") String title,
                                      @PathVariable("type") String type,
                                      @RequestBody Component component) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);
        if(componentService.getComponentByType(title, type).isEmpty())
            throw new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR);
        if(componentService.updateComponentByType(title, type, component) == 0)
            throw new ResponseStatusException(BAD_REQUEST, COMPONENT_BAD);
    }
}
