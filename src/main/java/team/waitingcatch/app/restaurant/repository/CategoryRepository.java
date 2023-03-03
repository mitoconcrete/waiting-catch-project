package team.waitingcatch.app.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import team.waitingcatch.app.restaurant.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findAllByParentId(Long parentId);

	List<Category> findAllByIdIn(List<Long> categoryId);

	@Query(value = "select c.name from Category c where c.id in(:categoryIds)")
	List<String> findNameByIdIn(@Param("categoryIds") List<Long> categoryIds);
}
