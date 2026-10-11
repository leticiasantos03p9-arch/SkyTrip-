package com.skytrip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ViagemRepository extends JpaRepository<Viagem,Long>{List<Viagem> findByPessoaIdOrderByIdDesc(Long pessoaId);}