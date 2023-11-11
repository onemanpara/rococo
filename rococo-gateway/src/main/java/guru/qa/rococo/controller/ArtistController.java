package guru.qa.rococo.controller;

import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.service.api.GrpcArtistClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    private static final Logger LOG = LoggerFactory.getLogger(ArtistController.class);

    private final GrpcArtistClient grpcArtistClient;

    @Autowired
    public ArtistController(GrpcArtistClient grpcArtistClient) {
        this.grpcArtistClient = grpcArtistClient;
    }

    @GetMapping("/{id}")
    public ArtistJson getArtist(@PathVariable UUID id) {
        return grpcArtistClient.getArtist(id);
    }

    @GetMapping
    public Page<ArtistJson> getAll(@RequestParam(required = false) String name, @PageableDefault Pageable pageable) {
        return grpcArtistClient.getAllArtist(name, pageable);
    }

}
