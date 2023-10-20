package guru.qa.rococo.controller;

import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.service.api.GrpcArtistClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ArtistController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final GrpcArtistClient grpcArtistClient;

    @Autowired
    public ArtistController(GrpcArtistClient grpcArtistClient) {
        this.grpcArtistClient = grpcArtistClient;
    }

    @GetMapping("/api/artist/{id}")
    public ArtistJson getArtist(@PathVariable UUID id) {
        return grpcArtistClient.getArtist(id);
    }

}
