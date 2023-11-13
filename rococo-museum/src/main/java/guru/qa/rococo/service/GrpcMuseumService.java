package guru.qa.rococo.service;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.MuseumRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static io.grpc.Status.NOT_FOUND;
import static java.util.UUID.fromString;

@GrpcService
public class GrpcMuseumService extends RococoMuseumServiceGrpc.RococoMuseumServiceImplBase {

    private final MuseumRepository museumRepository;

    public GrpcMuseumService(MuseumRepository museumRepository) {
        this.museumRepository = museumRepository;
    }

    @Override
    public void getMuseum(MuseumRequest request, StreamObserver<MuseumResponse> responseObserver) {
        UUID museumId = fromString(request.getId().toStringUtf8());

        museumRepository.findById(museumId)
                .ifPresentOrElse(
                        museumEntity -> {
                            MuseumResponse response = MuseumResponse.newBuilder()
                                    .setId(ByteString.copyFromUtf8(museumEntity.getId().toString()))
                                    .setTitle(museumEntity.getTitle())
                                    .setDescription(museumEntity.getDescription())
                                    .setPhoto(ByteString.copyFrom(museumEntity.getPhoto()))
                                    .setGeo(getGeoFromEntity(museumEntity))
                                    .build();

                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        () -> responseObserver.onError(
                                NOT_FOUND.withDescription("Can't find museum by id: " + museumId)
                                        .asRuntimeException()
                        )
                );
    }

    @Override
    public void getAllMuseum(AllMuseumRequest request, StreamObserver<AllMuseumResponse> responseObserver) {
        String title = request.getTitle();
        int page = request.getPage();
        int size = request.getSize();
        PageRequest pageable = PageRequest.of(page, size);

        Page<MuseumEntity> museumPage = museumRepository.findAllByTitleContainsIgnoreCase(title, pageable);

        AllMuseumResponse.Builder responseBuilder = AllMuseumResponse.newBuilder();
        museumPage.forEach(museumEntity -> {
            MuseumResponse museumResponse = MuseumResponse.newBuilder()
                    .setId(ByteString.copyFromUtf8(museumEntity.getId().toString()))
                    .setTitle(museumEntity.getTitle())
                    .setDescription(museumEntity.getDescription())
                    .setPhoto(ByteString.copyFrom(museumEntity.getPhoto()))
                    .setGeo(getGeoFromEntity(museumEntity))
                    .build();
            responseBuilder.addMuseum(museumResponse);
        });
        responseBuilder.setTotalCount((int) museumPage.getTotalElements());

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void addMuseum(AddMuseumRequest addMuseumRequest, StreamObserver<MuseumResponse> responseObserver) {
        MuseumEntity entity = museumRepository.save(MuseumEntity.fromAddGrpcMessage(addMuseumRequest));
        responseObserver.onNext(MuseumEntity.toGrpcMessage(entity));
        responseObserver.onCompleted();
    }

    @Override
    public void updateMuseum(UpdateMuseumRequest request, StreamObserver<MuseumResponse> responseObserver) {
        UUID museumId = fromString(request.getId().toStringUtf8());

        museumRepository.findById(museumId)
                .ifPresentOrElse(
                        museumEntity -> {
                            AddMuseumRequest museumData = request.getMuseumData();
                            museumEntity.setTitle(museumData.getTitle());
                            museumEntity.setDescription(museumData.getDescription());
                            museumEntity.setPhoto(museumData.getPhoto().toByteArray());
                            museumEntity.setCity(museumData.getGeo().getCity());
                            museumEntity.setGeoId(UUID.fromString(museumData.getGeo().getCountry().getId().toStringUtf8()));
                            museumRepository.save(museumEntity);
                            responseObserver.onNext(MuseumEntity.toGrpcMessage(museumEntity));
                            responseObserver.onCompleted();
                        }, () -> responseObserver.onError(
                                NOT_FOUND.withDescription("Can't find museum by id: " + museumId)
                                        .asRuntimeException()
                        )
                );
    }

    private Geo getGeoFromEntity(MuseumEntity museum) {
        return Geo.newBuilder()
                .setCity(museum.getCity())
                .setCountry(CountryId.newBuilder().setId(ByteString.copyFromUtf8(museum.getGeoId().toString())))
                .build();
    }

}
