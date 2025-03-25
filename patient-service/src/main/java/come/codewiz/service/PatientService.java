package come.codewiz.service;

import com.codewiz.patient.*;
import com.codewiz.model.Patient;
import come.codewiz.repository.PatientRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService extends PatientServiceGrpc.PatientServiceImplBase {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void registerPatient(PatientRegistrationRequest request, StreamObserver<PatientRegistrationResponse> responseObserver) {
        // Debugging: Print incoming data
        System.out.println("Received gRPC Request:");
        System.out.println("First Name: " + request.getFirstName());
        System.out.println("Last Name: " + request.getLastName());
        System.out.println("Email: " + request.getEmail());
        System.out.println("Phone: " + request.getPhone());
        System.out.println("Address: " + request.getAddress());

        // Validate input
        if (request.getFirstName().isEmpty() || request.getLastName().isEmpty()) {
            System.out.println("ERROR: First Name or Last Name is empty!");
            responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                    .withDescription("First name and last name cannot be empty.")
                    .asRuntimeException());
            return;
        }

        // Save patient to database
        Patient patient = new Patient(
                null,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getAddress()
        );

        patient = patientRepository.save(patient);

        // Debugging: Confirm what was saved
        System.out.println("Saved Patient ID: " + patient.id());
        System.out.println("Saved First Name: " + patient.firstName());
        System.out.println("Saved Last Name: " + patient.lastName());

        // Return response
        responseObserver.onNext(PatientRegistrationResponse.newBuilder()
                .setPatientId(patient.id())
                .setMessage("Patient registered successfully.")
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPatientDetails(PatientDetailsRequest request, StreamObserver<PatientDetails> responseObserver) {
        Optional<Patient> patient = patientRepository.findById(request.getPatientId());

        if (patient.isPresent()) {
            var p = patient.get();
            responseObserver.onNext(PatientDetails.newBuilder()
                    .setPatientId(p.id())
                    .setFirstName(p.firstName())
                    .setLastName(p.lastName())
                    .setEmail(p.email())
                    .setPhone(p.phone())
                    .setAddress(p.address())
                    .build());
        } else {
            System.out.println("ERROR: Patient with ID " + request.getPatientId() + " not found.");
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Patient not found")
                    .asRuntimeException());
        }

        responseObserver.onCompleted();
    }
}
