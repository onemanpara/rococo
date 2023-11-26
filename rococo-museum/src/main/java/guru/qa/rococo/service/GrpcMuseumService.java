package guru.qa.rococo.service;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.MuseumRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
                            MuseumResponse response = MuseumEntity.toGrpcMessage(museumEntity);
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        () -> responseObserver.onError(
                                NOT_FOUND.withDescription("Museum not found by id: " + museumId)
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
            MuseumResponse response = MuseumEntity.toGrpcMessage(museumEntity);
            responseBuilder.addMuseum(response);
        });
        responseBuilder.setTotalCount((int) museumPage.getTotalElements());

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void addMuseum(AddMuseumRequest addMuseumRequest, StreamObserver<MuseumResponse> responseObserver) {
        MuseumEntity entity = museumRepository.save(MuseumEntity.fromAddMuseumGrpcMessage(addMuseumRequest));
        responseObserver.onNext(MuseumEntity.toGrpcMessage(entity));
        responseObserver.onCompleted();
    }

    @Override
    public void updateMuseum(UpdateMuseumRequest request, StreamObserver<MuseumResponse> responseObserver) {
        UUID museumId = fromString(request.getId().toStringUtf8());

        museumRepository.findById(museumId)
                .ifPresentOrElse(
                        museumEntity -> {
                            museumEntity = MuseumEntity.fromUpdateMuseumGrpcMessage(request);
                            museumRepository.save(museumEntity);
                            responseObserver.onNext(MuseumEntity.toGrpcMessage(museumEntity));
                            responseObserver.onCompleted();
                        }, () -> responseObserver.onError(
                                NOT_FOUND.withDescription("Museum not found by id: " + museumId)
                                        .asRuntimeException()
                        )
                );
    }

    @Override
    public void getMuseumByIds(MuseumIdsRequest request, StreamObserver<AllMuseumByIdsResponse> responseObserver) {
        Set<UUID> museumIds = request.getIdList().stream()
                .map(byteString -> fromString(byteString.toStringUtf8()))
                .collect(Collectors.toSet());

        List<MuseumEntity> museums = museumRepository.findAllByIdIn(museumIds);

        AllMuseumByIdsResponse.Builder responseBuilder = AllMuseumByIdsResponse.newBuilder();
        museums.forEach(museumEntity -> {
            MuseumResponse response = MuseumEntity.toGrpcMessage(museumEntity);
            responseBuilder.addMuseum(response);
        });

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

}
