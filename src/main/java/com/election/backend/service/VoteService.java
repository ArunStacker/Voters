package com.election.backend.service;

import com.election.backend.model.Vote;
import com.election.backend.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public Vote castVote(Vote vote) {
        // Check if user already voted
        if (voteRepository.existsByNameIgnoreCaseAndAge(vote.getName(), vote.getAge())) {
            throw new RuntimeException("You have already voted!");
        }
        return voteRepository.save(vote);
    }

    public Map<String, Long> getResults() {
        Map<String, Long> results = new HashMap<>();
        String[] parties = { "DMK", "AIADMK", "TVK", "NTK", "PMK", "BJP" };
        for (String party : parties) {
            results.put(party, voteRepository.countByParty(party));
        }
        return results;
    }

    public long getTotalVotes() {
        return voteRepository.count();
    }
}
