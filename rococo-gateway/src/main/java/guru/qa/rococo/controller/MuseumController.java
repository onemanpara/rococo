package guru.qa.rococo.controller;

import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.service.api.GrpcMuseumClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/museum")
public class MuseumController {

    private final GrpcMuseumClient grpcMuseumClient;

    @Autowired
    public MuseumController(GrpcMuseumClient grpcMuseumClient) {
        this.grpcMuseumClient = grpcMuseumClient;
    }

    @GetMapping("/{id}")
    public MuseumJson getMuseum(@PathVariable UUID id) {
        return grpcMuseumClient.getMuseum(id);
    }

    @GetMapping
    public Page<MuseumJson> getAllMuseum(@RequestParam(required = false) String title, @PageableDefault Pageable pageable) {
        return grpcMuseumClient.getAllMuseum(title, pageable);
    }

    @PostMapping
    public MuseumJson addMuseum(@Valid @RequestBody MuseumJson museum) {
        return grpcMuseumClient.addMuseum(museum);
    }

    @PatchMapping
    public MuseumJson updateMuseum(@Valid @RequestBody MuseumJson museum) {
        return grpcMuseumClient.updateMuseum(museum);
    }

}
