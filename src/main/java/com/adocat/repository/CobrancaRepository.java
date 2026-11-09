
package com.adocat.repository;

import com.adocat.model.Cobranca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CobrancaRepository extends JpaRepository<Cobranca, Long> {

    List<Cobranca> findByAssinaturaIdOrderByDataCobrancaDesc(Long assinaturaId);

}
