package com.codewiz.service;

import com.codewiz.appointment.BookAppointmentRequest;
import com.codewiz.appointment.BookAppointmentResponse;
import com.codewiz.doctor.DoctorDetailsRequest;
import com.codewiz.doctor.DoctorDetailsResponse;
import com.codewiz.doctor.DoctorServiceGrpc;
import com.codewiz.model.Appointment;
import com.codewiz.patient.PatientDetails;
import com.codewiz.patient.PatientDetailsRequest;
import com.codewiz.patient.PatientServiceGrpc;
import com.codewiz.repository.AppointmentRepository;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorServiceGrpc.DoctorServiceBlockingStub doctorServiceBlockingStub;

    @Mock
    private PatientServiceGrpc.PatientServiceBlockingStub patientServiceBlockingStub;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void testBookAppointmentSuccess() {
        // ARRANGE
        // Building the object for BookAppointmentRequest
        var doctorId = "1";
        var patientId = "1";
        var location = "Nairobi";
        var appointmentDate = LocalDate.now();
        var appointmentTime = LocalTime.now();
        var reason = "General checkup";

        // building the object itself
        BookAppointmentRequest request = BookAppointmentRequest.newBuilder()
                .setDoctorId(Long.parseLong(doctorId))
                .setPatientId(Long.parseLong(patientId))
                .setAppointmentDate(String.valueOf(appointmentDate))
                .setAppointmentTime(String.valueOf(appointmentTime))
                .setReason(reason)
                .build();

        // Create dummy responses for doctor and patient details.
        DoctorDetailsResponse doctorDetailsResponse = DoctorDetailsResponse.newBuilder()
                .setDoctorId(Long.parseLong(doctorId))
                .setFirstName("Samwel")
                .setLastName("Mwawasi")
                .setLocation("Kyosk")
                .build();

        PatientDetails patientDetailsResponse = PatientDetails.newBuilder()
                .setFirstName("Jeff")
                .setLastName("kysosk")
                .setEmail("SamwelJeff@gamil.com")
                .setPatientId(Long.parseLong("1"))
                .build();

        // Stubbing the dependencies
        when(doctorServiceBlockingStub.getDoctorDetails(any(DoctorDetailsRequest.class)))
                .thenReturn(doctorDetailsResponse);
        when(patientServiceBlockingStub.getPatientDetails(any(PatientDetailsRequest.class)))
                .thenReturn(patientDetailsResponse);

        // Simulate saving the appointment in the repository by returning an Appointment with an id.
        Appointment savedAppointment = new Appointment(
                Long.parseLong("1"),
                Long.parseLong(patientId),
                "Jeff Kysosk",
                Long.parseLong(doctorId),
                "Samwel Mwawasi",
                location,
                appointmentDate,
                appointmentTime,
                reason);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        // Capturing the response using a TestStreamObserver.
        TestStreamObserver<BookAppointmentResponse> responseObserver = new TestStreamObserver<>();

        // ACT
        appointmentService.bookAppointment(request, responseObserver);

        // ASSERT
        // Verify that the repository.save was called once
        ArgumentCaptor<Appointment> appointmentArgumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository, times(1)).save(appointmentArgumentCaptor.capture());

        Appointment capturedAppointment = appointmentArgumentCaptor.getValue();
        // assertEquals(patientId, capturedAppointment.patientId());
        assertEquals(1L, capturedAppointment.patientId());
        assertEquals(1L, capturedAppointment.doctorId());;
        assertEquals(reason, capturedAppointment.reason());

        // Verify that responseObserver recieved the correct response.
        List<BookAppointmentResponse> responses = responseObserver.getValues();
        assertEquals(1, responses.size());
        assertEquals(1L, responseObserver.getValues().getFirst().getAppointmentId());;
        assertNull(responseObserver.getError());
    }

    @Test
    void testGetAppointmentAvailabilitySuccess() {
        // ARRANGE


        // ACT


        // ASSERT
    }

    /**
     * A simple implememntation of StreamObserver for testing purposes.
     */
    private  static class TestStreamObserver<T> implements StreamObserver<T> {
        private final List<T> values = new ArrayList<>();
        private Throwable error;
        private boolean completed = false;

        @Override
        public void onNext(T value) {
            values.add(value);
        }

        @Override
        public void onError(Throwable t) {
            this.error = t;

        }


        @Override
        public void onCompleted() {
            completed = true;

        }
        public List<T> getValues() {
            return values;
        }

        public Throwable getError() {
            return error;
        }

        public boolean isCompleted() {
            return completed;
        }


    }
}