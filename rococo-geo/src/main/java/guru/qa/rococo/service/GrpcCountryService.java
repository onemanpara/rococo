package guru.qa.rococo.service;

import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.data.CountryEntity;
import guru.qa.rococo.data.repository.CountryRepository;
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
public class GrpcCountryService extends RococoCountryServiceGrpc.RococoCountryServiceImplBase {
    private final CountryRepository countryRepository;

    public GrpcCountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void getCountry(CountryId request, StreamObserver<CountryResponse> responseObserver) {
        UUID countryId = fromString(request.getId().toStringUtf8());

        countryRepository.findById(countryId)
                .ifPresentOrElse(
                        countryEntity -> {
                            CountryResponse response = CountryEntity.toGrpcMessage(countryEntity);
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        () -> responseObserver.onError(
                                NOT_FOUND.withDescription("Country not found by id: " + countryId)
                                        .asRuntimeException()
                        )
                );
    }

    @Override
    public void getCountryByName(CountryName request, StreamObserver<CountryResponse> responseObserver) {
        String countryName = request.getName();

        countryRepository.findByName(countryName)
                .ifPresentOrElse(
                        countryEntity -> {
                            CountryResponse response = CountryEntity.toGrpcMessage(countryEntity);
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        () -> responseObserver.onError(
                                NOT_FOUND.withDescription("Country not found by name: " + countryName)
                                        .asRuntimeException()
                        )
                );
    }

    @Override
    public void getAllCountry(AllCountryRequest request, StreamObserver<AllCountryResponse> responseObserver) {
        int page = request.getPage();
        int size = request.getSize();
        PageRequest pageable = PageRequest.of(page, size);

        Page<CountryEntity> countryPage = countryRepository.findAll(pageable);
        AllCountryResponse.Builder responseBuilder = AllCountryResponse.newBuilder();
        countryPage.forEach(countryEntity -> {
            CountryResponse response = CountryEntity.toGrpcMessage(countryEntity);
            responseBuilder.addCountry(response);
        });
        responseBuilder.setTotalCount((int) countryPage.getTotalElements());

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCountriesByIds(CountryIdsRequest request, StreamObserver<AllCountryByIdsResponse> responseObserver) {
        Set<UUID> countryIds = request.getIdList().stream()
                .map(byteString -> fromString(byteString.toStringUtf8()))
                .collect(Collectors.toSet());

        List<CountryEntity> countries = countryRepository.findAllByIdIn(countryIds);

        AllCountryByIdsResponse.Builder responseBuilder = AllCountryByIdsResponse.newBuilder();
        countries.forEach(countryEntity -> {
            CountryResponse response = CountryEntity.toGrpcMessage(countryEntity);
            responseBuilder.addCountry(response);
        });

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

}
