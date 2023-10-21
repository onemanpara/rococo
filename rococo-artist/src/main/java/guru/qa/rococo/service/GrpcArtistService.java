package guru.qa.rococo.service;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.data.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import guru.qa.rococo.ex.NotFoundException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static java.util.UUID.fromString;

@GrpcService
public class GrpcArtistService extends RococoArtistServiceGrpc.RococoArtistServiceImplBase {

    private final ArtistRepository artistRepository;

    public GrpcArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public void getArtist(GetArtistRequest request, StreamObserver<GetArtistResponse> responseObserver) {
        Optional<ArtistEntity> artistSource = artistRepository.findById(fromString(request.getUuid().toStringUtf8()));
        if (artistSource.isEmpty()) {
            throw new NotFoundException("Can`t find artist by id: " + request.getUuid());
        }
        ArtistEntity artist = artistSource.get();

        GetArtistResponse response = GetArtistResponse.newBuilder()
                .setName(artist.getName())
                .setBiography(artist.getBiography())
                .setPhoto(ByteString.copyFrom(artist.getPhoto()))
                .setUuid(ByteString.copyFromUtf8(artist.getId().toString()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getArtistsWithPagination(GetArtistsWithPaginationRequest request, StreamObserver<GetArtistsWithPaginationResponse> responseObserver) {
        String name = request.getName();
        int page = request.getPage();
        int size = request.getSize();

        PageRequest pageable = PageRequest.of(page, size);

        Page<ArtistEntity> artistPage = artistRepository.findAllByNameContainsIgnoreCase(name, pageable);

        GetArtistsWithPaginationResponse.Builder responseBuilder = GetArtistsWithPaginationResponse.newBuilder();
        for (ArtistEntity artist : artistPage) {
            GetArtistResponse artistResponse = GetArtistResponse.newBuilder()
                    .setName(artist.getName())
                    .setBiography(artist.getBiography())
                    .setPhoto(ByteString.copyFrom(artist.getPhoto()))
                    .setUuid(ByteString.copyFromUtf8(artist.getId().toString()))
                    .build();
            responseBuilder.addArtists(artistResponse);
        }
        responseBuilder.setTotalCount((int) artistPage.getTotalElements());

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

}
