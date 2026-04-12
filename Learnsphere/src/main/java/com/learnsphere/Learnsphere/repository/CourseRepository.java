package com.learnsphere.Learnsphere.repository;

import com.learnsphere.Learnsphere.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}