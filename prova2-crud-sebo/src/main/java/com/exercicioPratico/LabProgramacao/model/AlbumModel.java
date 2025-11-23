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

/*CAMADA DE DOMINIO, REPRESENTACAO DO OBJETO*/

@Entity//Anotacao informa ao JPA que a classe representa um banco de dados
@Table(name = "albuns") //nome da tabela definido
public class AlbumModel { //declaracao da classe

    @Id //chave primaria incremental
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título não pode estar em branco")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
    private String titulo; //titulo do album anotacoes de validacao nao nulo e tamanho maximo da string

    @NotBlank(message = "O nome da banda é obrigatório")
    @Size(max = 100, message = "O nome da banda deve ter no máximo 100 caracteres")
    private String banda;//banda, nao pode ser vazio e tamanho maximo 100 caracteres

    @NotNull(message = "O ano é obrigatório")
    @Max(value = 2025, message = "o ano nao pode ser maior que o ano vigente")
    @Min(value = 0, message = "O ano não pode ser negativo")
    private Integer ano; //ano de lancamento do album, nao pode ser nulo limites minimo e maximo

    @NotBlank(message = "O gênero é obrigatório")
    private String genero;//genero do album deve ser obrigatorio

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser positivo")
    @DecimalMax(value = "1000", inclusive = false, message = "Verifique o preço está alto demais!!!")
    @Column(precision = 10, scale = 2)
    private BigDecimal preco; //decimal do preco nao pode ser nulo nem negativo, nem alto demais >1000

    // Getters e Setters para acesso pelas classes externas aos metodos internas, boilerplate code.

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

