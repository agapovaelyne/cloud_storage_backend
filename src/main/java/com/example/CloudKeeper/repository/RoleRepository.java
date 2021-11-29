package com.example.CloudKeeper.repository;

import com.example.CloudKeeper.entity.Role;
import com.example.CloudKeeper.model.EnumRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
//    Optional<Role> findByName(EnumRoles name);
}
