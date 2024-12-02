package ltweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ltweb.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}