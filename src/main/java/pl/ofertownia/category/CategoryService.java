package pl.ofertownia.category;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<String> findCategoryNames() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .toList();
    }

    public List<CategoryDetailsDto> getAllCategoriesDetails() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::map)
                .toList();
    }

    public CategoryDetailsDto saveCategory(CategoryToSaveDto categoryToSaveDto) {
        Category category = categoryMapper.map(categoryToSaveDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.map(savedCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
