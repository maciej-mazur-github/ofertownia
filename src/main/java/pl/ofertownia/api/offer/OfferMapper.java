package pl.ofertownia.api.offer;

import org.springframework.stereotype.Service;
import pl.ofertownia.api.category.Category;
import pl.ofertownia.api.category.CategoryRepository;

import java.util.Optional;

@Service
class OfferMapper {
    private final CategoryRepository categoryRepository;

    OfferMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    OfferDto map(Offer offer) {
        OfferDto offerDto = new OfferDto();
        offerDto.setId(offer.getId());
        offerDto.setTitle(offer.getTitle());
        offerDto.setDescription(offer.getDescription());
        offerDto.setImgUrl(offer.getImgUrl());
        offerDto.setPrice(offer.getPrice());
        offerDto.setCategory(offer.getCategory().getName());
        return offerDto;
    }

    Optional<Offer> map(OfferToSaveDto offerToSaveDto) {
        return categoryRepository.findByNameEqualsIgnoreCase(offerToSaveDto.getCategory())
                .map(category -> setOfferEntity(offerToSaveDto, category));
    }

    private Offer setOfferEntity(OfferToSaveDto offerToSaveDto, Category category) {
        Offer offer = new Offer();
        offer.setTitle(offerToSaveDto.getTitle());
        offer.setDescription(offerToSaveDto.getDescription());
        offer.setImgUrl(offerToSaveDto.getImgUrl());
        offer.setPrice(offerToSaveDto.getPrice());
        offer.setCategory(category);
        return offer;
    }
}
