package guru.qa.rococo.controller;

import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.service.api.GrpcMuseumClient;
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

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final GrpcMuseumClient grpcMuseumClient;

    @Autowired
    public MuseumController(GrpcMuseumClient grpcMuseumClient) {
        this.grpcMuseumClient = grpcMuseumClient;
    }

    @GetMapping("/api/museum/{id}")
    public MuseumJson getMuseum(@PathVariable UUID id) {
        return grpcMuseumClient.getMuseum(id);
    }

    @GetMapping("/api/museum")
    public Page<MuseumJson> getAll(@RequestParam(required = false) String name, @PageableDefault Pageable pageable) {
        return grpcMuseumClient.getAllMuseum(name, pageable);
    }

}
