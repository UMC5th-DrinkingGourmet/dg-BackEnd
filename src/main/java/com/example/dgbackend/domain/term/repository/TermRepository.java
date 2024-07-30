package com.example.dgbackend.domain.term.repository;

import com.example.dgbackend.domain.term.Term;
import com.example.dgbackend.domain.term.TermType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    Term findByTermType(TermType termType);

}
