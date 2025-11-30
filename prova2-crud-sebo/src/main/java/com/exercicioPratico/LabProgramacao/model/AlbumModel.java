package com.exercicioPratico.LabProgramacao.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Year; //para captarmos ano atual

/**
 * CAMADA DE DOMÍNIO (Domain Layer / Entity)
 * 
 * Esta classe representa a entidade "Album" no banco de dados.
 * É mapeada para a tabela "albuns" no MySQL através do JPA (Java Persistence API).
 * 
 * As anotações JPA (@Entity, @Table, @Id, etc.) instruem o framework sobre:
 * - Como criar a estrutura da tabela no banco
 * - Como mapear objetos Java para registros SQL
 * - Quais validações aplicar automaticamente
 * 
 * As anotações de validação (@NotBlank, @Size, etc.) garantem integridade dos dados
 * antes mesmo de chegar ao banco de dados.
 */
@Entity
@Table(name = "albuns")
public class AlbumModel {

    /**
     * ID único do álbum (chave primária).
     * 
     * @GeneratedValue com strategy IDENTITY significa que o MySQL
     * gera automaticamente o ID usando AUTO_INCREMENT.
     * O JPA não precisa gerenciar a geração do ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título do álbum.
     * 
     * @NotBlank: não pode ser null, vazio ("") ou apenas espaços em branco
     * @Size: máximo de 150 caracteres para evitar campos muito longos
     */
    @NotBlank(message = "O título não pode estar em branco")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
    private String titulo;

    /**
     * Nome da banda/artista.
     * 
     * @NotBlank: campo obrigatório
     * @Size: máximo de 100 caracteres
     */
    @NotBlank(message = "O nome da banda é obrigatório")
    @Size(max = 100, message = "O nome da banda deve ter no máximo 100 caracteres")
    private String banda;

    /**
     * Ano de lançamento do álbum.
     * 
     * @NotNull: campo obrigatório
     * @Min: não pode ser negativo (valor mínimo 0)
     * @Max: não pode ser maior que 2025 (ano atual/limite)
     */
    @NotNull(message = "O ano é obrigatório")
    @Max(value = 2025, message = "o ano nao pode ser maior que o ano vigente")
    @Min(value = 0, message = "O ano não pode ser negativo")
    private Integer ano;

    /**
     * Gênero musical do álbum (ex: Rock, Metal, Pop, etc.).
     * 
     * @NotBlank: campo obrigatório
     */
    @NotBlank(message = "O gênero é obrigatório")
    private String genero;

    /**
     * Preço do álbum em reais.
     * 
     * BigDecimal é usado para valores monetários porque:
     * - Evita problemas de precisão com ponto flutuante (float/double)
     * - Garante cálculos financeiros precisos
     * 
     * @NotNull: campo obrigatório
     * @DecimalMin: valor mínimo 0.0 (exclusive, ou seja, > 0)
     * @DecimalMax: valor máximo 1000.0 (exclusive, ou seja, < 1000)
     * @Column: define precisão decimal no banco (10 dígitos totais, 2 casas decimais)
     *          Exemplo: 99999999.99 (máximo valor possível)
     */
    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser positivo")
    @DecimalMax(value = "1000", inclusive = false, message = "Verifique o preço está alto demais!!!")
    @Column(precision = 10, scale = 2)
    private BigDecimal preco;

    // ========== GETTERS E SETTERS ==========
    // 
    // Getters e Setters permitem acesso controlado aos campos privados.
    // São necessários para:
    // - Serialização JSON (conversão objeto → JSON para API)
    // - Acesso do JPA para ler/escrever dados do banco
    // - Encapsulamento (princípio OOP)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getBanda() {
        return banda;
    }

    public void setBanda(String banda) {
        this.banda = banda;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}

