package guru.qa.rococo.service.api;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.model.PaintingJson;
import guru.qa.rococo.model.museum.MuseumJson;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static java.util.stream.Collectors.toSet;

@Component
public class GrpcPaintingClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcPaintingClient.class);

    @GrpcClient("grpcPaintingClient")
    private RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub rococoPaintingServiceStub;

    private final GrpcMuseumClient grpcMuseumClient;
    private final GrpcArtistClient grpcArtistClient;

    @Autowired
    public GrpcPaintingClient(GrpcMuseumClient grpcMuseumClient, GrpcArtistClient grpcArtistClient) {
        this.grpcMuseumClient = grpcMuseumClient;
        this.grpcArtistClient = grpcArtistClient;
    }

    public @Nonnull PaintingJson getPainting(UUID id) {
        PaintingRequest request = PaintingRequest.newBuilder()
                .setId(copyFromUtf8(id.toString()))
                .build();
        try {
            PaintingResponse response = rococoPaintingServiceStub.getPainting(request);
            PaintingJson painting = PaintingJson.fromGrpcMessage(response);
            enrichPaintingData(painting);
            return painting;
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запрашиваемая картина с id " + id + " не найдена", e);
            } else {
                LOG.error("### Error while calling gRPC server", e);
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
            }
        }
    }

    public @Nonnull Page<PaintingJson> getAllPainting(@Nullable String title, Pageable pageable) {
        AllPaintingRequest.Builder builder = AllPaintingRequest.newBuilder()
                .setPage(pageable.getPageNumber())
                .setSize(pageable.getPageSize());
        if (title != null) {
            builder.setTitle(title);
        }
        AllPaintingRequest request = builder.build();

        try {
            AllPaintingResponse response = rococoPaintingServiceStub.getAllPainting(request);
            List<PaintingJson> paintingJsonList = response.getPaintingList()
                    .stream()
                    .map(PaintingJson::fromGrpcMessage)
                    .toList();
            enrichPaintingData(paintingJsonList);
            return new PageImpl<>(paintingJsonList, pageable, response.getTotalCount());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull Page<PaintingJson> getAllPaintingByArtist(UUID id, Pageable pageable) {
        AllPaintingByArtistIdRequest request = AllPaintingByArtistIdRequest.newBuilder()
                .setArtistId(copyFromUtf8(id.toString()))
                .setPage(pageable.getPageNumber())
                .setSize(pageable.getPageSize())
                .build();
        try {
            AllPaintingResponse response = rococoPaintingServiceStub.getAllPaintingByArtistId(request);
            List<PaintingJson> paintingJsonList = response.getPaintingList()
                    .stream()
                    .map(PaintingJson::fromGrpcMessage)
                    .toList();
            enrichPaintingData(paintingJsonList);
            return new PageImpl<>(paintingJsonList, pageable, response.getTotalCount());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull PaintingJson addPainting(PaintingJson painting) {
        AddPaintingRequest request = PaintingJson.toGrpcMessage(painting);
        PaintingResponse response = rococoPaintingServiceStub.addPainting(request);
        PaintingJson addedPainting = PaintingJson.fromGrpcMessage(response);
        enrichPaintingData(addedPainting);
        return addedPainting;
    }

    public @Nonnull PaintingJson updatePainting(PaintingJson painting) {
        AddPaintingRequest paintingData = PaintingJson.toGrpcMessage(painting);
        UpdatePaintingRequest request = UpdatePaintingRequest.newBuilder()
                .setId(ByteString.copyFromUtf8(painting.getId().toString()))
                .setPaintingData(paintingData)
                .build();
        PaintingResponse response = rococoPaintingServiceStub.updatePainting(request);
        PaintingJson addedPainting = PaintingJson.fromGrpcMessage(response);
        enrichPaintingData(addedPainting);
        return addedPainting;
    }

    private void enrichPaintingData(PaintingJson painting) {
        MuseumJson museum = grpcMuseumClient.getMuseum(painting.getMuseum().id());
        ArtistJson artist = grpcArtistClient.getArtist(painting.getArtist().id());
        painting.setMuseum(museum);
        painting.setArtist(artist);
    }

    private void enrichPaintingData(List<PaintingJson> paintingJsonList) {
        Set<UUID> museumIds = paintingJsonList.stream()
                .map(painting -> painting.getMuseum().id())
                .collect(toSet());
        Set<UUID> artistIds = paintingJsonList.stream()
                .map(painting -> painting.getArtist().id())
                .collect(toSet());


        List<MuseumJson> museums = grpcMuseumClient.getMuseumByIds(museumIds);
        List<ArtistJson> artists = grpcArtistClient.getArtistByIds(artistIds);

        paintingJsonList.forEach(museum -> {
            UUID museumId = museum.getId();
            Optional<MuseumJson> matchingMuseum = museums.stream()
                    .filter(museumJson -> museumJson.id().equals(museumId))
                    .findFirst();
            matchingMuseum.ifPresent(museum::setMuseum);
        });
        paintingJsonList.forEach(artist -> {
            UUID artistId = artist.getId();
            Optional<ArtistJson> matchingArtist = artists.stream()
                    .filter(artistJson -> artistJson.id().equals(artistId))
                    .findFirst();
            matchingArtist.ifPresent(artist::setArtist);
        });
    }
}
