package com.learnsphere.Learnsphere.service;

import com.learnsphere.Learnsphere.model.Course;
import com.learnsphere.Learnsphere.model.User;
import com.learnsphere.Learnsphere.model.SubModule;
import com.learnsphere.Learnsphere.model.Module;

import com.learnsphere.Learnsphere.repository.CourseRepository;
import com.learnsphere.Learnsphere.repository.ModuleRepository;
import com.learnsphere.Learnsphere.repository.SubModuleRepository;
import com.learnsphere.Learnsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired private CourseRepository courseRepository;
    @Autowired private UserRepository userRepository;

    @Autowired private ModuleRepository moduleRepository;
    @Autowired private SubModuleRepository subModuleRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course createCourse(Course course, String email) {
        User instructor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id, String email, String role) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Business Logic: Admins can delete anything, Instructors only their OWN [cite: 159]
        if (role.equals("ADMIN") || course.getInstructor().getEmail().equals(email)) {
            courseRepository.delete(course);
        } else {
            throw new RuntimeException("Unauthorized to delete this course");
        }
    }

    public Module addModule(Long courseId, Module module) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        module.setCourse(course);
        return moduleRepository.save(module);
    }

    public SubModule addSubModule(Long moduleId, SubModule subModule) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        subModule.setModule(module);
        return subModuleRepository.save(subModule);
    }

    // Update Course
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setTitle(courseDetails.getTitle());
        course.setDescription(courseDetails.getDescription());
        return courseRepository.save(course);
    }

    // Update Module
    public Module updateModule(Long id, Module moduleDetails) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        module.setTitle(moduleDetails.getTitle());
        return moduleRepository.save(module);
    }

    // Update SubModule
    public SubModule updateSubModule(Long id, SubModule subModuleDetails) {
        SubModule subModule = subModuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubModule not found"));
        subModule.setTitle(subModuleDetails.getTitle());
        subModule.setContent(subModuleDetails.getContent());
        return subModuleRepository.save(subModule);
    }
}