package guru.qa.rococo.service;

import guru.qa.rococo.data.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.model.ArtistJson;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ArtistService {

    private static final Logger LOG = LoggerFactory.getLogger(ArtistService.class);

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public @Nonnull
    ArtistJson getArtist(@Nonnull UUID id) {
        return ArtistJson.fromEntity(getRequiredArtist(id));
    }

    public @Nonnull Page<ArtistJson> getAllArtist(@Nonnull String name, @Nonnull Pageable pageable) {
        Page<ArtistEntity> artistPage = artistRepository.findAllByNameContainsIgnoreCase(name, pageable);
        List<ArtistJson> artistJsonList = artistPage
                .getContent()
                .stream()
                .map(ArtistJson::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(artistJsonList, pageable, artistPage.getTotalElements());
    }

    @Nonnull
    ArtistEntity getRequiredArtist(@Nonnull UUID id) {
        Optional<ArtistEntity> artist = artistRepository.findById(id);
        if (artist.isEmpty()) {
            throw new NotFoundException("Can`t find artist by id: " + id);
        }
        return artist.get();
    }
}
