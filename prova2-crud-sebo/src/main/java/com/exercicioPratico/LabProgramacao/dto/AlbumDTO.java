package com.exercicioPratico.LabProgramacao.dto;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) - Objeto de Transferência de Dados
 * 
 * Esta classe é usada para transferir dados entre o backend e o frontend.
 * 
 * Por que usar DTO ao invés de retornar AlbumModel diretamente?
 * 
 * 1. Segurança:
 *    - Evita expor detalhes internos da entidade JPA
 *    - Permite controlar quais campos são enviados ao cliente
 * 
 * 2. Performance:
 *    - DTOs são mais leves (sem anotações JPA, sem lazy loading)
 *    - Reduz overhead de serialização JSON
 * 
 * 3. Desacoplamento:
 *    - Frontend não depende da estrutura interna do banco
 *    - Facilita mudanças na entidade sem quebrar a API
 * 
 * 4. Versionamento:
 *    - Permite diferentes versões da API com diferentes DTOs
 * 
 * Fluxo: AlbumModel (entidade) → AlbumDTO (transferência) → JSON → Frontend
 */
public class AlbumDTO {
    private Long id;
    private String titulo;
    private String banda;
    private Integer ano;
    private String genero;
    private BigDecimal preco;
    
    /**
     * Construtor padrão (sem argumentos).
     * Necessário para frameworks de serialização JSON (Jackson, Gson, etc.)
     * que precisam instanciar objetos vazios e preencher via setters.
     */
    public AlbumDTO() {}
    
    /**
     * Construtor completo com todos os campos.
     * Útil para criar DTOs a partir de AlbumModel no Controller.
     * 
     * @param id ID único do álbum
     * @param titulo Título do álbum
     * @param banda Nome da banda
     * @param ano Ano de lançamento
     * @param genero Gênero musical
     * @param preco Preço em reais
     */
    public AlbumDTO(Long id, String titulo, String banda, Integer ano, String genero, BigDecimal preco) {
        this.id = id;
        this.titulo = titulo;
        this.banda = banda;
        this.ano = ano;
        this.genero = genero;
        this.preco = preco;
    }
    
    // Getters e Setters
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