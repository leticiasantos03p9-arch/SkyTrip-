package com.skytrip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AtividadeRepository extends JpaRepository<Atividade,Long>{List<Atividade> findByViagemIdOrderByIdAsc(Long viagemId); void deleteByViagemId(Long viagemId);}