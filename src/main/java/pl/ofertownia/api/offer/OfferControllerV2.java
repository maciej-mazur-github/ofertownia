package pl.ofertownia.api.offer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.ofertownia.api.constraintviolationerror.ConstraintViolationError;
import pl.ofertownia.utils.UriBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v2/offers")
public class OfferControllerV2 {
    private final OfferService offerService;

    public OfferControllerV2(OfferService offerService) {
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
    ResponseEntity<OfferDto> saveOffer(@Valid @RequestBody OfferToSaveDto offerToSaveDto) {
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    List<ConstraintViolationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new ConstraintViolationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
    }
}
