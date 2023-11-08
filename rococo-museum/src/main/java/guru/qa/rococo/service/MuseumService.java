package guru.qa.rococo.service;

import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.model.MuseumJson;
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
public class MuseumService {

    private static final Logger LOG = LoggerFactory.getLogger(MuseumService.class);

    private final MuseumRepository museumRepository;

    @Autowired
    public MuseumService(MuseumRepository museumRepository) {
        this.museumRepository = museumRepository;
    }

    public @Nonnull
    MuseumJson getMuseum(@Nonnull UUID id) {
        return MuseumJson.fromEntity(getRequiredMuseum(id));
    }

    public @Nonnull Page<MuseumJson> getAllMuseum(@Nonnull String title, @Nonnull Pageable pageable) {
        Page<MuseumEntity> museumPage = museumRepository.findAllByTitleContainsIgnoreCase(title, pageable);
        List<MuseumJson> museumJsonList = museumPage
                .getContent()
                .stream()
                .map(MuseumJson::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(museumJsonList, pageable, museumPage.getTotalElements());
    }

    @Nonnull
    MuseumEntity getRequiredMuseum(@Nonnull UUID id) {
        Optional<MuseumEntity> museum = museumRepository.findById(id);
        if (museum.isEmpty()) {
            throw new NotFoundException("Can`t find museum by id: " + id);
        }
        return museum.get();
    }
}
