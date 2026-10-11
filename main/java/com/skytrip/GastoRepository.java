package com.skytrip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GastoRepository extends JpaRepository<Gasto,Long>{List<Gasto> findByViagemIdOrderByIdAsc(Long viagemId); void deleteByViagemId(Long viagemId);}