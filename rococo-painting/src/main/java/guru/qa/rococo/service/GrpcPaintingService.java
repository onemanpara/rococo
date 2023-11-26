package guru.qa.rococo.service;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.data.PaintingEntity;
import guru.qa.rococo.data.repository.PaintingRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static io.grpc.Status.NOT_FOUND;
import static java.util.UUID.fromString;

@GrpcService
public class GrpcPaintingService extends RococoPaintingServiceGrpc.RococoPaintingServiceImplBase {

    private final PaintingRepository paintingRepository;

    public GrpcPaintingService(PaintingRepository paintingRepository) {
        this.paintingRepository = paintingRepository;
    }

    @Override
    public void getPainting(PaintingRequest request, StreamObserver<PaintingResponse> responseObserver) {
        UUID paintingId = fromString(request.getId().toStringUtf8());

        paintingRepository.findById(paintingId)
                .ifPresentOrElse(
                        paintingEntity -> {
                            PaintingResponse response = PaintingEntity.toGrpcMessage(paintingEntity);
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        () -> responseObserver.onError(
                                NOT_FOUND.withDescription("Painting not found by id: " + paintingId)
                                        .asRuntimeException()
                        )
                );
    }

    @Override
    public void getAllPainting(AllPaintingRequest request, StreamObserver<AllPaintingResponse> responseObserver) {
        String title = request.getTitle();
        int page = request.getPage();
        int size = request.getSize();
        PageRequest pageable = PageRequest.of(page, size);

        Page<PaintingEntity> paintingPage = paintingRepository.findAllByTitleContainsIgnoreCase(title, pageable);
        AllPaintingResponse response = getAllPaintingResponseFromEntities(paintingPage);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addPainting(AddPaintingRequest addPaintingRequest, StreamObserver<PaintingResponse> responseObserver) {
        PaintingEntity entity = paintingRepository.save(PaintingEntity.fromAddPaintingGrpcMessage(addPaintingRequest));
        responseObserver.onNext(PaintingEntity.toGrpcMessage(entity));
        responseObserver.onCompleted();
    }

    @Override
    public void updatePainting(UpdatePaintingRequest request, StreamObserver<PaintingResponse> responseObserver) {
        UUID paintingId = fromString(request.getId().toStringUtf8());

        paintingRepository.findById(paintingId)
                .ifPresentOrElse(
                        paintingEntity -> {
                            paintingEntity = PaintingEntity.fromUpdatePaintingGrpcMessage(request);
                            paintingRepository.save(paintingEntity);
                            responseObserver.onNext(PaintingEntity.toGrpcMessage(paintingEntity));
                            responseObserver.onCompleted();
                        }, () -> responseObserver.onError(
                                NOT_FOUND.withDescription("Painting not found by id: " + paintingId)
                                        .asRuntimeException()
                        )
                );
    }

    @Override
    public void getAllPaintingByArtistId(AllPaintingByArtistIdRequest request, StreamObserver<AllPaintingResponse> responseObserver) {
        UUID artistId = UUID.fromString(request.getArtistId().toStringUtf8());
        int page = request.getPage();
        int size = request.getSize();
        PageRequest pageable = PageRequest.of(page, size);

        Page<PaintingEntity> paintingPage = paintingRepository.findAllByArtistId(artistId, pageable);
        AllPaintingResponse response = getAllPaintingResponseFromEntities(paintingPage);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private AllPaintingResponse getAllPaintingResponseFromEntities(Page<PaintingEntity> paintingPage) {
        AllPaintingResponse.Builder responseBuilder = AllPaintingResponse.newBuilder();
        paintingPage.forEach(paintingEntity -> {
            PaintingResponse response = PaintingEntity.toGrpcMessage(paintingEntity);
            responseBuilder.addPainting(response);
        });
        return responseBuilder.setTotalCount((int) paintingPage.getTotalElements()).build();
    }
}
