package com.example.CloudKeeper.repository;

import com.example.CloudKeeper.entity.CloudFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<CloudFile, String> {

    List<CloudFile> findAllByUserId(long userId);

    CloudFile findByUserIdAndName(long userId, String name);

    Long removeByUserIdAndName(long userId, String name);
}
