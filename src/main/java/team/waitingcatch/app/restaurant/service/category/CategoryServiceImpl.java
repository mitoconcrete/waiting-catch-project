package team.waitingcatch.app.restaurant.service.category;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.entity.Category;
import team.waitingcatch.app.restaurant.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService, InternalCategoryService {
	private final CategoryRepository categoryRepository;

	@Override
	public void createCategory(CreateCategoryRequest request) {
		Category category = Category.create(request);
		categoryRepository.save(category);
	}

}
