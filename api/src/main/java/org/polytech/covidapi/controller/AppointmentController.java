package org.polytech.covidapi.controller;

import java.util.List;
import java.util.Optional;
import org.polytech.covidapi.model.Appointment;
import org.polytech.covidapi.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import io.github.bucket4j.*;
import org.springframework.web.server.ResponseStatusException;
import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/api")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private Bucket bucket;

    final String remainning = "X-Rate-Limit-Remaining";
    final String retryAfter = "X-Rate-Limit-Retry-After-Seconds";

    public AppointmentController(AppointmentService appointmentService, Bucket bucket) {
        this.appointmentService = appointmentService;
        this.bucket = bucket;
    }

     @GetMapping(value="/private/appointments")
     public Iterable<Appointment> getAllAppointment(){
        return (Iterable<Appointment>) appointmentService.findAll();
    }

    //Permet d'obtenir des infos sur le bucket
    @CrossOrigin(exposedHeaders = {remainning, retryAfter})
    @GetMapping(value = "/public/appointments/infos")
    public ResponseEntity<Object> infos() {
        HttpHeaders headers = new HttpHeaders();
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(2);

        if(probe.isConsumed()) {
            headers.add("X-Rate-Limit-Remaining", Long.toString(probe.getRemainingTokens()));
            return ResponseEntity.ok()
                    .headers(headers)
                    .build();
        }

        long waitingTimeSec = probe.getNanosToWaitForRefill() / 1_000_000_000;
        headers.add("X-Rate-Limit-Retry-After-Seconds",String.valueOf(waitingTimeSec));
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .headers(headers)
                .build();
    }

    @GetMapping("/public/appointment/{id}")
    public Optional<Appointment> getOneacteur(@PathVariable int id){
        return appointmentService.findById(id);
    }

    @Timed(value = "saveAppointment.time", description = "Time taken to save an appointment")
    @PostMapping(path = "/public/appointment")
    public Appointment save(@RequestBody Appointment newappointment) {
        return appointmentService.save(newappointment);
    }

    @GetMapping("/public/appointmentbycenter/{id}")
    public List<Appointment> getAppointmentsbyCenter(@PathVariable int id){
        if(bucket.tryConsume(1)) {
            return appointmentService.getAppointementByCenterId(id);        }
        else {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Trop de requêtes");
        }
    }

    // On crée une autre route pour les admins qui ne sera pas soumis au rate limite
    @GetMapping("/private/appointmentbycenter/{id}")
    public List<Appointment> getAppointmentsbyCenterAdmin(@PathVariable int id){
        return appointmentService.getAppointementByCenterId(id);
    }

    @DeleteMapping("/private/appointment/{id}")

    public void delete(@PathVariable int id){
        appointmentService.delete(id);
    }

    @GetMapping(value="/public/maxappointment")
    public int max(){
        return appointmentService.max();
    }
}
