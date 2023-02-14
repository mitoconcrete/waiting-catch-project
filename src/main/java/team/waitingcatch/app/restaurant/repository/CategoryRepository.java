package team.waitingcatch.app.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.restaurant.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findAllByParentId(Long parentId);
}
