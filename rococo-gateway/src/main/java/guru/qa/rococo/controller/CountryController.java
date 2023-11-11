package guru.qa.rococo.controller;

import guru.qa.rococo.model.museum.CountryJson;
import guru.qa.rococo.service.api.GrpcMuseumClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/country")
public class CountryController {

    private static final Logger LOG = LoggerFactory.getLogger(CountryController.class);

    private final GrpcMuseumClient grpcMuseumClient;

    @Autowired
    public CountryController(GrpcMuseumClient grpcMuseumClient) {
        this.grpcMuseumClient = grpcMuseumClient;
    }

    @GetMapping
    public Page<CountryJson> getAll(@PageableDefault Pageable pageable) {
        return grpcMuseumClient.getAllCountry(pageable);
    }

}
