package pl.ofertownia.api.category;

import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {
    CategoryDetailsDto map(Category category) {
        CategoryDetailsDto categoryDetailsDto = new CategoryDetailsDto();
        categoryDetailsDto.setId(category.getId());
        categoryDetailsDto.setName(category.getName());
        categoryDetailsDto.setDescription(category.getDescription());
        categoryDetailsDto.setOffers(category.getOffers().size());
        return categoryDetailsDto;
    }

    Category map(CategoryToSaveDto categoryToSaveDto) {
        Category category = new Category();
        category.setName(categoryToSaveDto.getName());
        category.setDescription(categoryToSaveDto.getDescription());
        return category;
    }
}
