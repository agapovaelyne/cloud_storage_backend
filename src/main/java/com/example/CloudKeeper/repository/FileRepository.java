package com.example.CloudKeeper.repository;

import com.example.CloudKeeper.entity.CloudFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<CloudFile, String> {

    List<CloudFile> findAllByUserId(long userId);

    @Query(value = "select * from storage s where s.user_id = ?1 order by s.id desc limit ?2", nativeQuery = true)
    List<CloudFile> findAllByUserIdWithLimit(long userId, int limit);

    Optional<CloudFile> findByUserIdAndName(long userId, String name);

    Optional<Long> removeByUserIdAndName(long userId, String name);
}
