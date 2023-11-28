package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.CountryEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

    Optional<CountryEntity> findByName(String countryName);

    @Nonnull
    Page<CountryEntity> findAll(@Nonnull Pageable pageable);

    List<CountryEntity> findAllByIdIn(@Nonnull Set<UUID> ids);

}