package pl.ofertownia.api.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameEqualsIgnoreCase(String categoryName);
}
