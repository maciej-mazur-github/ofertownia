package pl.ofertownia.category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.javastart.restoffers.utils.UriBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/names")
    ResponseEntity<List<String>> getCategoryNames() {
        return ResponseEntity.ok(categoryService.findCategoryNames());
    }

    @GetMapping
    ResponseEntity<List<CategoryDetailsDto>> getAllCategoriesDetails() {
        return ResponseEntity.ok(categoryService.getAllCategoriesDetails());
    }

    @PostMapping
    ResponseEntity<CategoryDetailsDto> saveCategory(@RequestBody CategoryToSaveDto categoryToSaveDto) {
        CategoryDetailsDto savedCategory = categoryService.saveCategory(categoryToSaveDto);
        return ResponseEntity.created(UriBuilder.getUri(savedCategory.getId())).body(savedCategory);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
