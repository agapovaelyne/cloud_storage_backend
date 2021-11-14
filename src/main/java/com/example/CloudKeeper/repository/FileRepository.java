package com.example.CloudKeeper.repository;

import com.example.CloudKeeper.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, String> {

    List<File> findAllByUserId(long userId);
}
