package pl.ofertownia.api.offer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ofertownia.utils.UriBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    ResponseEntity<List<OfferDto>> getAllOffers(@RequestParam(required = false) String title) {
        if (title != null) {
            return ResponseEntity.ok(offerService.findAllByTitle(title));
        }
        return ResponseEntity.ok(offerService.findAll());
    }

    @GetMapping("/count")
    ResponseEntity<Long> getOffersCount() {
        return ResponseEntity.ok(offerService.count());
    }

    @PostMapping
    ResponseEntity<OfferDto> saveOffer(@RequestBody OfferToSaveDto offerToSaveDto) {
        return offerService.saveOffer(offerToSaveDto)
                .map(offer -> ResponseEntity.created(UriBuilder.getUri(offer.getId())).body(offer))
                .orElse(ResponseEntity.notFound().header(
                        "reason", "Category " + offerToSaveDto.getCategory() + " not found").build());
    }

    @GetMapping("/{id}")
    ResponseEntity<OfferDto> getOfferDetails(@PathVariable Long id) {
        return offerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity
                        .notFound()
                        .header("reason", "Offer of id " + id + " not found")
                        .build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }
}
