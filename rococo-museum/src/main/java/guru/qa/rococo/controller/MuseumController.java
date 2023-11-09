package guru.qa.rococo.controller;

import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.service.MuseumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MuseumController {

    private static final Logger LOG = LoggerFactory.getLogger(MuseumController.class);

    private final MuseumService museumService;

    @Autowired
    public MuseumController(MuseumService museumService) {
        this.museumService = museumService;
    }

    @GetMapping("/api/museum/{id}")
    public MuseumJson getMuseum(@PathVariable UUID id) {
        return museumService.getMuseum(id);
    }

    @GetMapping("/api/museum")
    public Page<MuseumJson> getAll(@RequestParam(required = false) String title,
                                   @PageableDefault Pageable pageable) {
        return museumService.getAllMuseum(title, pageable);
    }

}
