package com.skytrip;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PessoaRepository extends JpaRepository<Pessoa,Long>{Optional<Pessoa> findByEmailIgnoreCase(String email);}