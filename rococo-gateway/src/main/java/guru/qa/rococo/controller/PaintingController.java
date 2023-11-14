package guru.qa.rococo.controller;

import guru.qa.rococo.model.PaintingJson;
import guru.qa.rococo.service.api.GrpcPaintingClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/painting")
public class PaintingController {

    private final GrpcPaintingClient grpcPaintingClient;

    @Autowired
    public PaintingController(GrpcPaintingClient grpcPaintingClient) {
        this.grpcPaintingClient = grpcPaintingClient;
    }

    @GetMapping("/{id}")
    public PaintingJson getPainting(@PathVariable UUID id) {
        return grpcPaintingClient.getPainting(id);
    }

    @GetMapping
    public Page<PaintingJson> getAll(@RequestParam(required = false) String title, @PageableDefault Pageable pageable) {
        return grpcPaintingClient.getAllPainting(title, pageable);
    }

    @PostMapping
    public PaintingJson addPainting(@Valid @RequestBody PaintingJson painting) {
        return grpcPaintingClient.addPainting(painting);
    }

    @PatchMapping
    public PaintingJson updatePainting(@Valid @RequestBody PaintingJson painting) {
        return grpcPaintingClient.updatePainting(painting);
    }

    @GetMapping("/author/{id}")
    public Page<PaintingJson> getPaintingByArtist(@PathVariable UUID id, @PageableDefault Pageable pageable) {
        return grpcPaintingClient.getAllPaintingByArtist(id, pageable);
    }

}
