package com.example.subject.service;

import com.example.subject.dao.CourseDao;
import com.example.subject.model.Component;
import com.example.subject.model.Evaluation;
import com.example.subject.model.Resource;
import com.example.subject.model.Subject;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ComponentService {
    private final CourseDao courseDao;

    @Autowired
    public ComponentService(@Qualifier("postgres") CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public boolean validateComponent(String title, Component component) {
        if(component.getNumberWeeks() <= 0 || component.isDeleted()) return false;
        if(!validateType(component.getType()))
            return false;
        Optional<Subject> subject = courseDao.selectSubjectByTitle(title);
        if(subject.isEmpty()) return false;
        for(Component comp : courseDao.getComponents(title))
            if(comp.getType().equals(component.getType()))
                return false;
        return true;
    }

    public boolean validateComponentToUpdate(String title, String type, Component component) {
        if(component.getNumberWeeks() <= 0 || component.isDeleted()) return false;
        if(!Objects.equals(component.getType(), type))
            return false;
        Optional<Subject> subject = courseDao.selectSubjectByTitle(title);
        return subject.isPresent();
    }
    public boolean validateType(String type){
        return Objects.equals(type, "Course") || Objects.equals(type, "Seminar") || Objects.equals(type, "Laboratory");
    }

    public int addComponent(String title, Component component) {
        if(validateComponent(title, component)) {
            return courseDao.addComponent(title, component);
        }
        return 0;
    }

    public List<Component> getComponents(String title) {
        return courseDao.getComponents(title);
    }

    public Optional<Component> getComponentByType(String title, String type) {
        if(validateType(type))
            return courseDao.getComponentByType(title, type);
        return Optional.empty();
    }

    public int deleteComponentByType(String title, String type) {
        ResourceService resourceService = new ResourceService(courseDao);

        if(!validateType(type))
            return 0;

        Optional<Subject> subject = courseDao.selectSubjectByTitle(title);
        if(subject.isEmpty()) return 0;

        List<Resource> resources = courseDao.getResourcesForComponentType(title, type);
        for (Resource resource : resources) {
            String resourceTitle = resource.getTitle();
            resourceService.deleteResourceByTitle(title, type, resourceTitle);
        }

        Optional<Evaluation> evaluation = courseDao.getEvaluationMethodByComponent(title, type);
        if (evaluation.isEmpty())
            return courseDao.deleteComponentByType(title, type);
        return courseDao.deleteEvaluationMethod(title, type) & courseDao.deleteComponentByType(title, type);
    }

    public int updateComponentByType(String title, String type, Component component) {
        return validateComponentToUpdate(title, type, component) ? courseDao.updateComponentByType(title, type, component) : 0;
    }
}
