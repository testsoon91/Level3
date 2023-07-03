package com.sparta.blog.repository;

import com.sparta.myblog.entity.Myblog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyblogRepository extends JpaRepository<Myblog, Long> {
    List<Myblog> findAllByOrderByModifiedAtDesc();

}
