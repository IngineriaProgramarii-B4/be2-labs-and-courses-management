
package com.example.subject.service;

import com.example.firebase.FirebaseStorageStrategy;
import com.example.subject.dao.CourseDao;
import com.example.subject.model.Component;
import com.example.subject.model.Resource;
import com.example.subject.model.Subject;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import java.util.logging.Logger;

@Service
@Transactional
public class SubjectService {
    private final CourseDao courseDao;
    private static final String RENAME_FAILURE = "RENAME FAILURE";
    private static final Logger logger = Logger.getLogger(SubjectService.class.getName());

    private final FirebaseStorageStrategy firebaseStorageStrategy;

    @Autowired
    public SubjectService(@Qualifier("postgres") CourseDao courseDao, FirebaseStorageStrategy firebaseStorageStragetegy) {
        this.courseDao = courseDao;
        this.firebaseStorageStrategy = firebaseStorageStragetegy;
    }

    public boolean validateSubject(Subject subject){
        if(subject.getTitle().isEmpty() || subject.getIsDeleted())
            return false;
        List<Subject> subjects = getAllSubjects();
        for(Subject subject1 : subjects)
            if(subject1.getTitle().equals(subject.getTitle())) //only non-deleted subjects
                return false;
        return validateComponents(subject);
    }

    public boolean validateTitle(String title){
        List<Subject> subjects = courseDao.selectAllSubjects();
        for(Subject subject : subjects)
            if(subject.getTitle().equals(title)) //only non-deleted subjects
                return true;
        return false;
    }

    public boolean validateUpdate(String title, String title1) {
        if(title.equals(title1))
            return true;
        for(Subject subject : courseDao.selectAllSubjects()) //only non-deleted subjects
            if(subject.getTitle().equals(title1))
                return false;
        return true;
    }

    public boolean validateComponents(Subject subject) {
        Set<String> componentTypes = new HashSet<>();
        for (Component component : subject.getComponentList()) {
            if (!componentTypes.add(component.getType())) {
                return false; // found a duplicate component type
            }
        }
        return true; // all component types are unique
    }

    public int addSubject(Subject subject) {
        if(!validateSubject(subject))
            return 0;

        return courseDao.insertSubject(subject);
    }

    public List<Subject> getAllSubjects() {
        return courseDao.selectAllSubjects();
    }

    public Optional<Subject> getSubjectByTitle(String title) {
        if(validateTitle(title))
            return courseDao.selectSubjectByTitle(title);
        else return Optional.empty();
    }

    public List<Subject> getSubjectsByYearAndSemester(int year, int semester) {
        return courseDao.getSubjectsByYearAndSemester(year, semester);
    }

    public int deleteSubjectByTitle(String title) {
        ComponentService componentService = new ComponentService(courseDao, firebaseStorageStrategy);

        if (!validateTitle(title))
            return 0;

        for (Component component : courseDao.getComponents(title)) {
            String type = component.getType();
            componentService.deleteComponentByType(title, type);
        }
        Optional<Subject> subject = courseDao.selectSubjectByTitle(title);
        Resource oldImage;
        if(subject.isEmpty()) {
            return courseDao.deleteSubjectByTitle(title);
        }
        oldImage = subject.get().getImage();
        if (oldImage != null) {
            try {
                firebaseStorageStrategy.deleteFile(oldImage.getLocation());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return courseDao.deleteSubjectByTitle(title);
    }

    public int updateSubjectByTitle(String title, Subject subject) {
        if(!validateUpdate(title, subject.getTitle()))
            return 0;

        List<Component> components = courseDao.getComponents(title);
        List<Resource> allResources = new ArrayList<>();
        List<byte[]> allResourcesBytes = new ArrayList<>();
        int nrResources = 0;
        for (Component component : components) {
            List<Resource> compResource = courseDao.getResourcesForComponentType(title, component.getType());
            allResources.addAll(compResource);
            nrResources += compResource.size();
            for(Resource resource : compResource){
                try {
                    allResourcesBytes.add(firebaseStorageStrategy.download(resource.getLocation()));
                    firebaseStorageStrategy.deleteFile(resource.getLocation());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for(int i = 0; i < nrResources; i++){
            Resource resource = allResources.get(i);
            byte[] resourceBytes = allResourcesBytes.get(i);
            String resourceLocation = resource.getLocation();
            String resourceLocationUpdated = resourceLocation.substring(
                    resourceLocation.indexOf("/") + 1
            );
            resourceLocationUpdated = subject.getTitle() + "/" + resourceLocationUpdated;
            resourceLocationUpdated = resourceLocationUpdated.substring(
                    0,
                    resourceLocationUpdated.lastIndexOf("/")
            );
            try {
                firebaseStorageStrategy.uploadBytes(resourceBytes, resource.getTitle(), resourceLocationUpdated);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Optional<Subject> subject1 = courseDao.selectSubjectByTitle(title);
        Resource oldImage = null;
        if(subject1.isPresent())
            oldImage = subject1.get().getImage();
        if (oldImage != null) {
            try {
                byte[] imageBytes = firebaseStorageStrategy.download(oldImage.getLocation());
                firebaseStorageStrategy.deleteFile(oldImage.getLocation());
                firebaseStorageStrategy.uploadBytes(imageBytes, oldImage.getTitle(), subject.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return courseDao.updateSubjectByTitle(title, subject);
    }

    public int uploadSubjectImage(String title, MultipartFile image) {
        Optional<Subject> subjectMaybe = getSubjectByTitle(title);
        if(subjectMaybe.isEmpty())
            return 0;

        Resource oldImage = subjectMaybe.get().getImage();
        String oldImageLocation = "";
        if (oldImage != null) {
            oldImageLocation = oldImage.getLocation();
        }

        String fileName = title + "/" + image.getOriginalFilename();
        String fileType = image.getContentType();
        Resource resource = new Resource(image.getOriginalFilename(), fileName, fileType);
        if(courseDao.saveImageToSubject(title, resource) == 0)
            return 0;
        try {
            firebaseStorageStrategy.deleteFile(oldImageLocation);
            firebaseStorageStrategy.upload(image, image.getOriginalFilename(), title);
            return 1;
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Exception: ", e);
            return 0;
        }
    }
}