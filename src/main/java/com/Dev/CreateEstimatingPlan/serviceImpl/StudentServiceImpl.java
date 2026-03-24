package com.Dev.CreateEstimatingPlan.serviceImpl;

import com.Dev.CreateEstimatingPlan.entity.students.Student;
import com.Dev.CreateEstimatingPlan.exception.GroupNotFoundException;
import com.Dev.CreateEstimatingPlan.repository.studentsRepository.StudentRepository;
import com.Dev.CreateEstimatingPlan.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repository;

    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @Override
    public List<Student> findByGroupNames(List<String> groupNames) {
        List<Student> students = repository.findByGroupNames(groupNames);
        if (students.isEmpty()) {
            throw new GroupNotFoundException("Група яка вказана в списку : " + groupNames + " не існує в базі даних");
        }
        return students;
    }

}
