package guru.qa.rococo.service;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.data.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static io.grpc.Status.NOT_FOUND;
import static java.util.UUID.fromString;

@GrpcService
public class GrpcArtistService extends RococoArtistServiceGrpc.RococoArtistServiceImplBase {

    private final ArtistRepository artistRepository;

    public GrpcArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public void getArtist(ArtistRequest request, StreamObserver<ArtistResponse> responseObserver) {
        UUID artistId = fromString(request.getId().toStringUtf8());

        artistRepository.findById(artistId)
                .ifPresentOrElse(
                        artistEntity -> {
                            ArtistResponse response = ArtistResponse.newBuilder()
                                    .setName(artistEntity.getName())
                                    .setBiography(artistEntity.getBiography())
                                    .setPhoto(ByteString.copyFrom(artistEntity.getPhoto()))
                                    .setId(ByteString.copyFromUtf8(artistEntity.getId().toString()))
                                    .build();

                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        () -> responseObserver.onError(
                                NOT_FOUND.withDescription("Can't find artist by id: " + artistId)
                                        .asRuntimeException()
                        )
                );
    }

    @Override
    public void getAllArtist(GetAllArtistRequest request, StreamObserver<GetAllArtistResponse> responseObserver) {
        String name = request.getName();
        int page = request.getPage();
        int size = request.getSize();

        PageRequest pageable = PageRequest.of(page, size);

        Page<ArtistEntity> artistPage = artistRepository.findAllByNameContainsIgnoreCase(name, pageable);

        GetAllArtistResponse.Builder responseBuilder = GetAllArtistResponse.newBuilder();
        for (ArtistEntity artist : artistPage) {
            ArtistResponse artistResponse = ArtistResponse.newBuilder()
                    .setName(artist.getName())
                    .setBiography(artist.getBiography())
                    .setPhoto(ByteString.copyFrom(artist.getPhoto()))
                    .setId(ByteString.copyFromUtf8(artist.getId().toString()))
                    .build();
            responseBuilder.addArtists(artistResponse);
        }
        responseBuilder.setTotalCount((int) artistPage.getTotalElements());

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void addArtist(AddArtistRequest addArtistRequest, StreamObserver<ArtistResponse> responseObserver) {
        ArtistEntity entity = artistRepository.save(ArtistEntity.fromGrpcMessage(addArtistRequest));
        responseObserver.onNext(ArtistEntity.toGrpcMessage(entity));
        responseObserver.onCompleted();
    }

    @Override
    public void updateArtist(UpdateArtistRequest request, StreamObserver<ArtistResponse> responseObserver) {
        UUID artistId = fromString(request.getId().toStringUtf8());

        artistRepository.findById(artistId)
                .ifPresentOrElse(
                        artistEntity -> {
                            AddArtistRequest artistData = request.getArtistData();
                            artistEntity.setName(artistData.getName());
                            artistEntity.setBiography(artistData.getBiography());
                            artistRepository.save(artistEntity);
                            responseObserver.onNext(ArtistEntity.toGrpcMessage(artistEntity));
                            responseObserver.onCompleted();
                        }, () -> responseObserver.onError(
                                NOT_FOUND.withDescription("Can't find artist by id: " + artistId)
                                        .asRuntimeException()
                        )
                );
    }

}
