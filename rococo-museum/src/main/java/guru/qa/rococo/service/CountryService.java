package guru.qa.rococo.service;

import guru.qa.rococo.data.CountryEntity;
import guru.qa.rococo.data.repository.CountryRepository;
import guru.qa.rococo.model.CountryJson;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CountryService {

    private static final Logger LOG = LoggerFactory.getLogger(CountryService.class);

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public @Nonnull Page<CountryJson> getAllCountry(@Nonnull Pageable pageable) {
        Page<CountryEntity> countryPage = countryRepository.findAll(pageable);
        List<CountryJson> countryJsonList = countryPage
                .getContent()
                .stream()
                .map(CountryJson::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(countryJsonList, pageable, countryPage.getTotalElements());
    }

}
