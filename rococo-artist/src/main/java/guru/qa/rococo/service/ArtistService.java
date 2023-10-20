package guru.qa.rococo.service;

import guru.qa.rococo.data.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.model.ArtistJson;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

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

    @Nonnull
    ArtistEntity getRequiredArtist(@Nonnull UUID id) {
        Optional<ArtistEntity> artist = artistRepository.findById(id);
        if (artist.isEmpty()) {
            throw new NotFoundException("Can`t find artist by id: " + id);
        }
        return artist.get();
    }
}
