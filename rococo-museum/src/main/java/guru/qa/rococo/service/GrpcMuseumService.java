package guru.qa.rococo.service;

import com.google.protobuf.ByteString;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.data.CountryEntity;
import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.CountryRepository;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.ex.NotFoundException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static java.util.UUID.fromString;

@GrpcService
public class GrpcMuseumService extends RococoMuseumServiceGrpc.RococoMuseumServiceImplBase {

    private final MuseumRepository museumRepository;
    private final CountryRepository countryRepository;

    public GrpcMuseumService(MuseumRepository museumRepository, CountryRepository countryRepository) {
        this.museumRepository = museumRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public void getMuseum(GetMuseumRequest request, StreamObserver<GetMuseumResponse> responseObserver) {
        Optional<MuseumEntity> museumSource = museumRepository.findById(fromString(request.getUuid().toStringUtf8()));
        if (museumSource.isEmpty()) {
            throw new NotFoundException("Can`t find museum by id: " + request.getUuid());
        }
        MuseumEntity museum = museumSource.get();
        Geo geo = getGeoFromEntity(museum);

        GetMuseumResponse response = GetMuseumResponse.newBuilder()
                .setUuid(ByteString.copyFromUtf8(museum.getId().toString()))
                .setTitle(museum.getTitle())
                .setDescription(museum.getDescription())
                .setPhoto(ByteString.copyFrom(museum.getPhoto()))
                .setGeo(geo)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMuseumWithPagination(GetMuseumsWithPaginationRequest request, StreamObserver<GetMuseumsWithPaginationResponse> responseObserver) {
        String title = request.getTitle();
        int page = request.getPage();
        int size = request.getSize();

        PageRequest pageable = PageRequest.of(page, size);

        Page<MuseumEntity> museumPage = museumRepository.findAllByTitleContainsIgnoreCase(title, pageable);

        GetMuseumsWithPaginationResponse.Builder responseBuilder = GetMuseumsWithPaginationResponse.newBuilder();
        for (MuseumEntity museum : museumPage) {
            Geo geo = getGeoFromEntity(museum);
            GetMuseumResponse museumResponse = GetMuseumResponse.newBuilder()
                    .setUuid(ByteString.copyFromUtf8(museum.getId().toString()))
                    .setTitle(museum.getTitle())
                    .setDescription(museum.getDescription())
                    .setPhoto(ByteString.copyFrom(museum.getPhoto()))
                    .setGeo(geo)
                    .build();
            responseBuilder.addMuseum(museumResponse);
        }
        responseBuilder.setTotalCount((int) museumPage.getTotalElements());

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCountries(GetCountriesRequest request, StreamObserver<GetCountriesResponse> responseObserver) {
        int page = request.getPage();
        int size = request.getSize();

        PageRequest pageable = PageRequest.of(page, size);

        Page<CountryEntity> countryPage = countryRepository.findAll(pageable);
        GetCountriesResponse.Builder responseBuilder = GetCountriesResponse.newBuilder();
        for (CountryEntity countryEntity : countryPage) {
            Country country = getCountryFromEntity(countryEntity);
            responseBuilder.addCountry(country);
        }
        responseBuilder.setTotalCount((int) countryPage.getTotalElements());

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private Geo getGeoFromEntity(MuseumEntity museum) {
        Country country = Country.newBuilder()
                .setUuid(ByteString.copyFromUtf8(museum.getCountry().getId().toString()))
                .setName(museum.getCountry().getName())
                .build();
        return Geo.newBuilder()
                .setCity(museum.getCity())
                .setCountry(country)
                .build();
    }

    private Country getCountryFromEntity(CountryEntity country) {
        return Country.newBuilder()
                .setUuid(ByteString.copyFromUtf8(country.getId().toString()))
                .setName(country.getName())
                .build();
    }

}
