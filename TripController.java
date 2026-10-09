package com.skytrip;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin
public class TripController {
    private final TripRepository repository;
    public TripController(TripRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Trip> all() { return repository.findAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Trip create(@RequestBody Trip trip) {
        validate(trip);
        return repository.save(trip);
    }

    @PutMapping("/{id}")
    public Trip update(@PathVariable Long id, @RequestBody Trip incoming) {
        Trip trip = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        validate(incoming);
        trip.setDestination(incoming.getDestination());
        trip.setStartDate(incoming.getStartDate());
        trip.setEndDate(incoming.getEndDate());
        trip.setTravelers(incoming.getTravelers());
        trip.setActivities(incoming.getActivities());
        trip.setExpenses(incoming.getExpenses());
        return repository.save(trip);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repository.deleteById(id);
    }

    private void validate(Trip trip) {
        if (trip.getDestination() == null || trip.getDestination().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o destino.");
        if (trip.getTravelers() < 1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número de viajantes inválido.");
    }
}
