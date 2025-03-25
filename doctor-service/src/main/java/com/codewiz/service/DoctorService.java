package com.codewiz.service;

import com.codewiz.doctor.*;
import com.codewiz.repository.DoctorRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class DoctorService extends DoctorServiceGrpc.DoctorServiceImplBase {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void registerDoctor(DoctorRegistrationRequest request, StreamObserver<DoctorRegistrationResponse> responseObserver) {
        var doctor = new com.codewiz.model.Doctor(
                null,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getSpeciality(),
                request.getCentreName(),
                request.getLocation()
        );

        doctor = doctorRepository.save(doctor);
        responseObserver.onNext(DoctorRegistrationResponse.newBuilder().setDoctorId(doctor.id()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getDoctorDetails(DoctorDetailsRequest request, StreamObserver<DoctorDetailsResponse> responseObserver) {
        var doctor = doctorRepository.findById(request.getDoctorId());

        if(doctor.isPresent()) {
            var d = doctor.get();

            responseObserver.onNext(DoctorDetailsResponse.newBuilder()
                    .setDoctorId(d.id())
                    .setFirstName(d.firstName())
                    .setLastName(d.lastName())
                    .setEmail(d.email())
                    .setPhone(d.phone())
                    .setSpeciality(d.specialty())
                    .setCentreName(d.centreName())
                    .setLocation(d.location())
                    .build());
        }
        else {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Doctor not found").asRuntimeException());
        }
        responseObserver.onCompleted();
    }
}
