package guru.qa.rococo.service;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.GetAristRequest;
import guru.qa.grpc.rococo.grpc.GetAristResponse;
import guru.qa.grpc.rococo.grpc.RococoArtistServiceGrpc;
import guru.qa.rococo.data.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import guru.qa.rococo.ex.NotFoundException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Optional;

import static java.util.UUID.fromString;

@GrpcService
public class GrpcArtistService extends RococoArtistServiceGrpc.RococoArtistServiceImplBase {

    private final ArtistRepository artistRepository;

    public GrpcArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public void getArtist(GetAristRequest request, StreamObserver<GetAristResponse> responseObserver) {
        Optional<ArtistEntity> artistSource = artistRepository.findById(fromString(request.getUuid().toStringUtf8()));
        if (artistSource.isEmpty()) {
            throw new NotFoundException("Can`t find artist by id: " + request.getUuid());
        }
        ArtistEntity artist = artistSource.get();

        GetAristResponse response = GetAristResponse.newBuilder()
                .setName(artist.getName())
                .setBiography(artist.getBiography())
                .setPhoto(ByteString.copyFrom(artist.getPhoto()))
                .setUuid(ByteString.copyFromUtf8(artist.getId().toString()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
