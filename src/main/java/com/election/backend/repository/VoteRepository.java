package com.election.backend.repository;

import com.election.backend.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByNameIgnoreCaseAndAge(String name, int age);

    long countByParty(String party);
}
