package pl.ofertownia.offer;

import org.springframework.stereotype.Service;
import pl.ofertownia.category.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
class OfferService {
    private final OfferRepository offerRepository;
    private final CategoryService categoryService;
    private final OfferMapper offerMapper;

    public OfferService(OfferRepository offerRepository, CategoryService categoryService, OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.categoryService = categoryService;
        this.offerMapper = offerMapper;
    }

    public List<OfferDto> findAll() {
        return offerRepository.findAll().stream()
                .map(offerMapper::map)
                .toList();
    }

    public List<OfferDto> findAllByTitle(String title) {
        return offerRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(offerMapper::map)
                .toList();
    }

    public long count() {
        return offerRepository.count();
    }

    public Optional<OfferDto> saveOffer(OfferToSaveDto offerToSaveDto) {
        return offerMapper.map(offerToSaveDto)
                .map(offerRepository::save)
                .map(offerMapper::map);
    }

    public Optional<OfferDto> findById(Long id) {
        return offerRepository.findById(id)
                .map(offerMapper::map);
    }

    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }
}
