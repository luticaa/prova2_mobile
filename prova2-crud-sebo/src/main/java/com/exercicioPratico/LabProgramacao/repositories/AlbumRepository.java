package com.exercicioPratico.LabProgramacao.repositories;

import com.exercicioPratico.LabProgramacao.model.AlbumModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * CAMADA DE ACESSO AOS DADOS (Data Access Layer / Repository)
 * 
 * Esta interface abstrai o acesso ao banco de dados MySQL.
 * O Spring Data JPA implementa automaticamente todos os métodos necessários.
 * 
 * Ao estender JpaRepository<AlbumModel, Long>:
 * - AlbumModel: tipo da entidade gerenciada
 * - Long: tipo da chave primária (ID)
 * 
 * Métodos automáticos fornecidos pelo JPA (sem necessidade de implementação):
 * - save(AlbumModel) → Salva ou atualiza um álbum
 * - findById(Long) → Busca por ID
 * - findAll() → Lista todos os álbuns
 * - deleteById(Long) → Exclui por ID
 * - existsById(Long) → Verifica se existe por ID
 * - count() → Conta total de registros
 * - etc.
 */
public interface AlbumRepository extends JpaRepository<AlbumModel, Long> {
    
    /**
     * Método customizado para buscar álbuns por nome da banda.
     * 
     * O Spring Data JPA gera automaticamente a query SQL baseado no nome do método:
     * - "findBy" → indica busca
     * - "Banda" → campo da entidade a ser buscado
     * - "Containing" → equivalente a SQL LIKE '%texto%'
     * - "IgnoreCase" → ignora maiúsculas/minúsculas (case-insensitive)
     * 
     * Query SQL gerada automaticamente:
     * SELECT * FROM albuns WHERE LOWER(banda) LIKE LOWER('%?%')
     * 
     * Exemplo de uso:
     * - findByBandaContainingIgnoreCase("metal") retorna:
     *   - "Metallica"
     *   - "Iron Maiden"
     *   - "Metal Church"
     *   - etc.
     * 
     * @param banda Texto a ser buscado no nome da banda
     * @return Lista de álbuns cujo nome da banda contém o texto fornecido
     */
    List<AlbumModel> findByBandaContainingIgnoreCase(String banda);
}
