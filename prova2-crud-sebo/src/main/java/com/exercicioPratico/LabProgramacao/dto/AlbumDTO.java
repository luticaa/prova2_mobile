package com.exercicioPratico.LabProgramacao.dto;

import java.math.BigDecimal;

public class AlbumDTO {
    private Long id;
    private String titulo;
    private String banda;
    private Integer ano;
    private String genero;
    private BigDecimal preco;
    
    // Construtores, getters e setters
    public AlbumDTO() {}
    
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