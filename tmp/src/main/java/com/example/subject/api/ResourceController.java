package com.example.subject.api;

import com.example.subject.model.Resource;
import com.example.subject.service.ComponentService;
import com.example.subject.service.ResourceService;
import com.example.subject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/v1/subjects/{subjectTitle}/components/{componentType}/resources")
@CrossOrigin(origins = "http://localhost:3000")
public class ResourceController {
    
    private static final String SUBJECT_ERROR = "Subject not found";
    private static final String COMPONENT_ERROR = "Component not found";
    private static final String RESOURCE_ERROR = "Resource not found";
    private final ResourceService resourceService;
    private final SubjectService subjectService;
    private final ComponentService componentService;
    @Autowired
    public ResourceController(ResourceService resourceService, SubjectService subjectService, ComponentService componentService) {
        this.resourceService = resourceService;
        this.subjectService = subjectService;
        this.componentService = componentService;
    }

    @GetMapping
    public ResponseEntity<List<Resource>> getResources(@PathVariable("subjectTitle") String title,
                                       @PathVariable("componentType") String type) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(null);
        if(componentService.getComponentByType(title, type).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(null);
        /*return resourceService.getResources(title, type);*/
        return ResponseEntity.status(OK).body(resourceService.getResources(title, type));
    }

    @GetMapping(path = "title={resourceTitle}")
    public ResponseEntity<Resource> getResourceByTitle(@PathVariable("subjectTitle") String title,
                                       @PathVariable("componentType") String type,
                                       @PathVariable("resourceTitle") String resourceTitle) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(null);
        if(componentService.getComponentByType(title, type).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body(null);
        /*return resourceService.getResourceByTitle(title, type, resourceTitle)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, RESOURCE_ERROR));*/
        return resourceService.getResourceByTitle(title, type, resourceTitle)
                .map(resource -> ResponseEntity.status(OK).body(resource))
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(null));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> addResourceFile(@PathVariable("subjectTitle") String title,
                                @PathVariable("componentType") String type,
                                @RequestParam("file") MultipartFile file) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body("Subject not found".getBytes());
        if(componentService.getComponentByType(title, type).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body("Component not found".getBytes());
        if(resourceService.addResource(file, title, type) == 0)
            /*throw new ResponseStatusException(NOT_FOUND, RESOURCE_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body("Resource not found".getBytes());
        /*throw new ResponseStatusException(CREATED, "Resource added successfully");*/
        return ResponseEntity.status(CREATED).body("Resource added successfully".getBytes());
    }

    @GetMapping("/file={resourceTitle}")
    public ResponseEntity<byte[]> getResourceFile(@PathVariable("subjectTitle") String title,
                                          @PathVariable("componentType") String type,
                                          @PathVariable("resourceTitle") String resourceTitle) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);
        if(componentService.getComponentByType(title, type).isEmpty())
            throw new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR);
        Optional<Resource> resource = resourceService.getResourceByTitle(title, type, resourceTitle);
        if(resource.isEmpty())
            throw new ResponseStatusException(NOT_FOUND, RESOURCE_ERROR);
        Resource resource1 = resource.get();
        try{
            byte[] file = Files.readAllBytes(new File(resource1.getLocation()).toPath());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(resource1.getType()))
                    .body(file);
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, RESOURCE_ERROR);
        }
    }

    @DeleteMapping(path = "title={resourceTitle}")
    @PreAuthorize("hasAuthority('TEACHER') || hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> deleteResourceByTitle(@PathVariable("subjectTitle") String title,
                                      @PathVariable("componentType") String type,
                                      @PathVariable("resourceTitle") String resourceTitle) {
        if(subjectService.getSubjectByTitle(title).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, SUBJECT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body("Subject not found".getBytes());
        if(componentService.getComponentByType(title, type).isEmpty())
            /*throw new ResponseStatusException(NOT_FOUND, COMPONENT_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body("Component not found".getBytes());
        if(resourceService.deleteResourceByTitle(title, type, resourceTitle) == 0)
            /*throw new ResponseStatusException(NOT_FOUND, RESOURCE_ERROR);*/
            return ResponseEntity.status(NOT_FOUND).body("Resource not found".getBytes());
        /*throw new ResponseStatusException(NO_CONTENT, "Resource deleted successfully");*/
        return ResponseEntity.status(NO_CONTENT).body("Resource deleted successfully".getBytes());
    }
}
