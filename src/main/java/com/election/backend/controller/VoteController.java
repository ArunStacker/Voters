package com.election.backend.controller;

import com.election.backend.model.Vote;
import com.election.backend.service.VoteService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174" }) // Allow React (Vite)
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/vote")
    public ResponseEntity<?> vote(@Valid @RequestBody Vote vote) { // Added @Valid
        try {
            Vote savedVote = voteService.castVote(vote);
            return ResponseEntity.ok("Thank you for your vote!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Handle Validation Errors (e.g. Age < 18)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.append(errorMessage).append(". ");
        });
        return ResponseEntity.badRequest().body(errors.toString());
    }

    @GetMapping("/stats")
    public ResponseEntity<Long> getTotalVotes() {
        return ResponseEntity.ok(voteService.getTotalVotes());
    }

    @GetMapping("/results")
    public ResponseEntity<?> getResults() {
        LocalDateTime now = LocalDateTime.now();
        // Result publish on Jan 25, 2026 at 10:00 AM
        LocalDateTime resultDate = LocalDateTime.of(2026, 1, 25, 10, 0, 0);

        // TEMPORARY UNLOCK FOR VERIFICATION
        if (now.isBefore(resultDate)) {
            return ResponseEntity.status(403).body("Results will be published on January 25, 2026 at 10:00 AM");
        }

        return ResponseEntity.ok(voteService.getResults());
    }
}
