package com.learnsphere.Learnsphere.controller;

import com.learnsphere.Learnsphere.model.Course;
import com.learnsphere.Learnsphere.model.SubModule;
import com.learnsphere.Learnsphere.model.Module;

import com.learnsphere.Learnsphere.repository.ModuleRepository;
import com.learnsphere.Learnsphere.repository.SubModuleRepository;
import com.learnsphere.Learnsphere.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired private CourseService courseService;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private SubModuleRepository subModuleRepository;

    @GetMapping
    public List<Course> getAll() {
        return courseService.getAllCourses(); // Students, Instructors, and Admins can view [cite: 122]
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Course create(@RequestBody Course course, Authentication auth) {
        // auth.getName() provides the email we extracted in JwtFilter
        return courseService.createCourse(course, auth.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public String delete(@PathVariable Long id, Authentication auth) {
        // Pass the role to service to check ownership [cite: 137]
        String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        courseService.deleteCourse(id, auth.getName(), role);
        return "Course deleted successfully";
    }

    // Add a Module to a Course
    @PostMapping("/{courseId}/modules")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Module addModule(@PathVariable Long courseId, @RequestBody Module module) {
        return courseService.addModule(courseId, module);
    }

    // Add a Sub-Module to a Module
    @PostMapping("/modules/{moduleId}/submodules")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public SubModule addSubModule(@PathVariable Long moduleId, @RequestBody SubModule subModule) {
        return courseService.addSubModule(moduleId, subModule);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Course updateCourse(@PathVariable Long id, @RequestBody Course course) {
        return courseService.updateCourse(id, course);
    }

    @PutMapping("/modules/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Module updateModule(@PathVariable Long id, @RequestBody Module module) {
        return courseService.updateModule(id, module);
    }

    @PutMapping("/submodules/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public SubModule updateSubModule(@PathVariable Long id, @RequestBody SubModule subModule) {
        return courseService.updateSubModule(id, subModule);
    }

    // 1. Updated Delete Module
    @DeleteMapping("/modules/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteModule(@PathVariable Long id) {
        // Use the lowercase 'moduleRepository' instance here!
        if (!moduleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        moduleRepository.deleteById(id);
        return ResponseEntity.ok("Module deleted successfully");
    }

    // 2. Updated Delete Sub-Module
    @DeleteMapping("/submodules/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteSubModule(@PathVariable Long id) {
        // Use the lowercase 'subModuleRepository' instance here!
        if (!subModuleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        subModuleRepository.deleteById(id);
        return ResponseEntity.ok("Lesson deleted successfully");
    }
}