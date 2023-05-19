package com.example.subject.service;

import com.example.subject.dao.CourseDao;
import com.example.subject.model.Component;
import com.example.subject.model.Evaluation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EvaluationService {

    private final CourseDao courseDao;

    @Autowired
    public EvaluationService(@Qualifier("postgres")  CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public boolean validateEval(String title, Evaluation evaluation){
        double sum = 0.;
        for(Evaluation eval : courseDao.getEvaluationMethods(title)) {
            if(!eval.getComponent().equals(evaluation.getComponent()) && !eval.getIsDeleted()) {
                sum += Math.floor(eval.getValue() * 100) / 100;
                sum = Math.floor(sum * 100) / 100;
            }
        }
        double finalSum = Math.floor((sum + evaluation.getValue()) * 100) / 100;
        if(finalSum > 1.0 || evaluation.getIsDeleted())
            return false;
        for(Component comp : courseDao.getComponents(title))
            if(comp.getType().equals(evaluation.getComponent()))
                return true;
        return false;
    }

    public boolean validateComponent(String title, String component){
        for(Component comp : courseDao.getComponents(title))
            if(comp.getType().equals(component))
                return true;
        return false;
    }

    public List<Evaluation> getEvaluationMethods(String title){
        return courseDao.getEvaluationMethods(title);
    }

    public Optional<Evaluation> getEvaluationMethodByComponent(String title, String component){
        if(validateComponent(title, component))
            return courseDao.getEvaluationMethodByComponent(title, component);
        return Optional.empty();
    }

    public int addEvaluationMethod(String title, Evaluation evaluation){
        Optional<Evaluation> eval = courseDao.getEvaluationMethodByComponent(title, evaluation.getComponent());
        if(validateEval(title, evaluation) && eval.isEmpty()){
            return courseDao.addEvaluationMethod(title, evaluation);
        }
        return 0;
    }

    public int deleteEvaluationMethodByComponent(String title, String component){
        if(validateComponent(title, component))
            return courseDao.deleteEvaluationMethod(title, component);
        return 0;
    }

    public int updateEvaluationMethodByComponent(String title, String component, Evaluation evaluation){
        if(validateEval(title, evaluation))
            return courseDao.updateEvaluationMethodByComponent(title, component, evaluation);
        return 0;
    }
}
