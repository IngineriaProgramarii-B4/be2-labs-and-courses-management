package com.example.subject.dao;


import com.example.subject.model.*;

import java.util.List;
import java.util.Optional;

public interface CourseDao {

    /*
      SUBJECT
     */
    int insertSubject(Subject subject);

    List<Subject> selectAllSubjects();

    Optional<Subject> selectSubjectByTitle(String title);

    List<Subject> getSubjectsByYearAndSemester(int year, int semester);

    int deleteSubjectByTitle(String title);

    int updateSubjectByTitle(String title, Subject subject);

    int saveImageToSubject(String title, Resource image);

    /*
      Component
     */

    int addComponent(String title, Component component);

    List<Component> getComponents(String title);

    Optional<Component> getComponentByType(String title, String type);

    int deleteComponentByType(String title, String type);

    int updateComponentByType(String title, String type, Component component);

    /*
      RESOURCE FOR Component
     */

    List<Resource> getResourcesForComponentType(String title, String type);

    Optional<Resource> getResourceByTitleForComponentType(String subjectTitle, String componentType, String resourceTitle);

    int addResourceForComponentType(String title, String type, Resource resource);

    int updateResourceByTitleForComponentType(String subjectTitle, String componentType, String resourceTitle, Resource resource);

    int deleteResourceByTitleForComponentType(String subjectTitle, String componentType, String resourceTitle);

    /*
      EVALUATION
     */

    int addEvaluationMethod(String title, Evaluation evaluationMethod);
    Optional<Evaluation> getEvaluationMethodByComponent(String title, String component);
    List<Evaluation> getEvaluationMethods(String title);
    int deleteEvaluationMethod(String title, String component);
    int updateEvaluationMethodByComponent(String title, String component, Evaluation evaluationMethod);
}
