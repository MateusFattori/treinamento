package br.com.fiap.treinamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.treinamento.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}

