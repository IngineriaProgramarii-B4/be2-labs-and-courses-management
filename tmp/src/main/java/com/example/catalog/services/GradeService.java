package com.example.catalog.services;

import com.example.catalog.models.Grade;
import com.example.catalog.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// <-------------------------------- FROM CATALOG ----------------------------------> //

@Service
public class GradeService {
    private final GradeRepository repository;

    @Autowired
    public GradeService(GradeRepository repository) {
        this.repository = repository;
    }

    public Grade getGradeById(UUID id){
        return repository.getGradeById(id).orElse((Grade)null);
    }

    public List<Grade> getGrades(){
        return repository.findAll();
    }
}
