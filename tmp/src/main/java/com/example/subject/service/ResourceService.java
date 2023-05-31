package com.example.subject.service;

import com.example.firebase.FirebaseStorageStrategy;
import com.example.subject.dao.CourseDao;
import com.example.subject.model.Component;
import com.example.subject.model.Resource;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import java.util.logging.Logger;

@Service
@Transactional
public class ResourceService {
    private final CourseDao courseDao;
    private static final Logger logger = Logger.getLogger(ResourceService.class.getName());
    private final FirebaseStorageStrategy firebaseStorageStrategy;

    @Autowired
    public ResourceService(@Qualifier("postgres") CourseDao courseDao,
                           FirebaseStorageStrategy firebaseStorageStrategy) {
        this.courseDao = courseDao;
        this.firebaseStorageStrategy = firebaseStorageStrategy;
    }

    public boolean validateExistingResource(String title, String type, Resource resource){
        if (resource.getTitle().isEmpty() || resource.getLocation().isEmpty())
            return false;
        for(Component comp : courseDao.getComponents(title))
            if(comp.getType().equals(type))
                for(Resource res : courseDao.getResourcesForComponentType(title, type))
                    if(res.getTitle().equals(resource.getTitle()))
                        return true;
        return false;
    }

    public boolean validateNewResource(String title, String type, Resource resource){
        if (resource.getTitle().isEmpty() || resource.getLocation().isEmpty())
            return false;
        for(Resource res : courseDao.getResourcesForComponentType(title, type))
            if(res.getTitle().equals(resource.getTitle())||res.getLocation().equals(resource.getLocation()))
                return false;
        for(Component comp : courseDao.getComponents(title))
            if(comp.getType().equals(type))
                return true;
        return false;
    }

    public int addResource(MultipartFile file, String title, String type){
        String fileName = title + "/" + type + "/" + file.getOriginalFilename();
        Resource resource = new Resource(
                file.getOriginalFilename(), fileName, file.getContentType()
        );
        if(!validateNewResource(title, type, resource))
            return 0;
        if(courseDao.addResourceForComponentType(title, type, resource) == 0)
            return 0;
        try {
            firebaseStorageStrategy.upload(file, file.getOriginalFilename(), title + "/" + type);
            return 1;
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Exception: ", e);
            return 0;
        }
    }

    public List<Resource> getResources(String title, String type) {
        return courseDao.getResourcesForComponentType(title, type);
    }

    public Optional<Resource> getResourceByTitle(String title, String type, String resourceTitle) {
        return courseDao.getResourceByTitleForComponentType(title, type, resourceTitle);
    }

    public int deleteResourceByTitle(String title, String type, String resourceTitle) {
        Optional<Resource> optionalResource = courseDao.getResourceByTitleForComponentType(title, type, resourceTitle);
        if (optionalResource.isEmpty()) {
            return 0;
        }
        Resource resource = optionalResource.get();

        if (validateExistingResource(title, type, resource)) {
            try {
                firebaseStorageStrategy.deleteFile(resource.getLocation());
            } catch (Exception e) {
                logger.log(java.util.logging.Level.SEVERE, "Exception: ", e);
                return 0;
            }
            return courseDao.deleteResourceByTitleForComponentType(title, type, resourceTitle);
        }
        return 0;
    }
}
