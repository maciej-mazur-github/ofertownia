package pl.ofertownia.api.category;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.ofertownia.api.constraintviolationerror.ConstraintViolationError;
import pl.ofertownia.utils.UriBuilder;

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
    ResponseEntity<CategoryDetailsDto> saveCategory(@Valid @RequestBody CategoryToSaveDto categoryToSaveDto) {
        CategoryDetailsDto savedCategory = categoryService.saveCategory(categoryToSaveDto);
        return ResponseEntity.created(UriBuilder.getUri(savedCategory.getId())).body(savedCategory);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    List<ConstraintViolationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new ConstraintViolationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
    }
}
