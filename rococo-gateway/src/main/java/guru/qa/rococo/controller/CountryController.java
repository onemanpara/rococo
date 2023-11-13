package guru.qa.rococo.controller;

import guru.qa.rococo.model.museum.CountryJson;
import guru.qa.rococo.service.api.GrpcCountryClient;
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

    private final GrpcCountryClient grpcCountryClient;

    @Autowired
    public CountryController(GrpcCountryClient grpcCountryClient) {
        this.grpcCountryClient = grpcCountryClient;
    }

    @GetMapping
    public Page<CountryJson> getAll(@PageableDefault Pageable pageable) {
        return grpcCountryClient.getAllCountry(pageable);
    }

}
