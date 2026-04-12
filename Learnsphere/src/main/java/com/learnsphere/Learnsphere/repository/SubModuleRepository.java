package com.learnsphere.Learnsphere.repository;

import com.learnsphere.Learnsphere.model.SubModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubModuleRepository extends JpaRepository<SubModule, Long> {
    List<SubModule> findByModuleId(Long moduleId);
}