package pl.ofertownia.api.offer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByTitleContainingIgnoreCase(String title);
}
