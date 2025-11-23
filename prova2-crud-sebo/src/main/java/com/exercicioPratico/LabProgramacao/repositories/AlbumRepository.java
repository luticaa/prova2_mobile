package com.exercicioPratico.LabProgramacao.repositories;

import com.exercicioPratico.LabProgramacao.model.AlbumModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/*CAMADA DE ACESSO AOS DADOS (DAL), CAMADA DE PERSISTENCIA*/


public interface AlbumRepository extends JpaRepository<AlbumModel, Long> {
    /* Método para buscar álbuns pelo artista, ignorando maiúsculas/minúsculas, demorei a entender que o JPA capta
    automaticamente pela assinatura da funcao oque fazer isso aqui é apelação máxima, fantástico, todas as demais sao
     padrao por isso nao precisaram ser redefinidas, ISSO TAMBEM EH UM METODO ABSTRATO (EXTENDS DETERMINA ISSO)!!!!!*/
    List<AlbumModel> findByBandaContainingIgnoreCase(String banda);
}
