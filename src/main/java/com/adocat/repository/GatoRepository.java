package com.adocat.repository;

import com.adocat.model.Gato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatoRepository extends JpaRepository<Gato, Long> {
}