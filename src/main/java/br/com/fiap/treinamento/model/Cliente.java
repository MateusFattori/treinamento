package br.com.fiap.treinamento.model;

import java.time.LocalDate;


import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import br.com.fiap.treinamento.validation.FidelidadeCliente;


@Data
@Entity
@Table (name = "Cliente")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "{cliente.nome.not.blank}")
    private String nome;
    
    @NotBlank(message = "{cliente.cpf.not.blank}")
    @Size(min= 11, max = 11, message = "{cliente.cpf.size}")
    private String cpf;

    @NotBlank(message = "{cliente.email.not.blank}")
    private String email;

    @NotBlank(message = "{cliente.senha.not.blank}")
    private String senha;

    private String genero;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dt_nascimento;

    @NotBlank(message = "{cliente.telefone.not.blank}")
    @Size(min= 14, max = 15, message = "{cliente.telefone.size}")
    private String telefone;

    private int pontos;

    @FidelidadeCliente(message = "{cliente.tipo.tipocliente}")
    private String fidelidade; // FILIADO | NÃOFILIADO

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dt_filiacao;

    private String perfilCompra;
    
    private String classificacao;


    
}
