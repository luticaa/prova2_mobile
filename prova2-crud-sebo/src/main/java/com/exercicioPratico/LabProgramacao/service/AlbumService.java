package com.exercicioPratico.LabProgramacao.service;
import com.exercicioPratico.LabProgramacao.model.AlbumModel;
import com.exercicioPratico.LabProgramacao.repositories.AlbumRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.Year; //para captarmos ano atual
import java.util.List;
import java.util.Optional;


/**
 * CAMADA DE SERVIÇO (Service Layer)
 * 
 * Esta classe contém a lógica de negócio da aplicação, sendo responsável por:
 * - Validar dados antes de persistir no banco
 * - Aplicar regras de negócio (validações de ano, preço, etc.)
 * - Coordenar operações entre Controller e Repository
 * - Tratar exceções e garantir integridade dos dados
 * 
 * O Spring Boot gerencia automaticamente o ciclo de vida desta classe
 * através da anotação @Service (Injeção de Dependência).
 */
@Service
public class AlbumService {
    
    /**
     * Repositório JPA que abstrai o acesso ao banco de dados.
     * O JPA gera automaticamente as queries SQL necessárias.
     * 
     * Declarado como 'final' para garantir imutabilidade após construção.
     */
    private final AlbumRepository albumRepository;

    /**
     * Construtor com injeção de dependência.
     * O Spring Boot automaticamente fornece uma instância de AlbumRepository
     * quando esta classe é criada (padrão IoC - Inversion of Control).
     */
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    /**
     * Lista todos os álbuns cadastrados, ordenados alfabeticamente por banda.
     * 
     * @return Lista de álbuns ordenada por nome da banda (A-Z)
     */
    public List<AlbumModel> listar() {
        // Sort.by("banda").ascending() ordena os resultados por nome da banda em ordem crescente
        return albumRepository.findAll(Sort.by("banda").ascending());
    }

    /**
     * Busca álbuns cujo nome da banda contenha o texto fornecido.
     * A busca é case-insensitive (ignora maiúsculas/minúsculas).
     * 
     * Exemplo: buscarPorBanda("metal") retorna álbuns de "Metallica", "Iron Maiden", etc.
     * 
     * @param banda Texto a ser buscado no nome da banda
     * @return Lista de álbuns que correspondem à busca
     */
    public List<AlbumModel> buscarPorBanda(String banda) {
        // findByBandaContainingIgnoreCase é um método customizado do Repository
        // O Spring Data JPA gera automaticamente a query SQL baseada no nome do método
        return albumRepository.findByBandaContainingIgnoreCase(banda);
    }

    /**
     * Busca um álbum específico pelo seu ID.
     * 
     * @param id ID do álbum a ser buscado
     * @return AlbumModel encontrado
     * @throws RuntimeException se o álbum não for encontrado
     * 
     * Nota: Esta exceção pode ocorrer se:
     * - O usuário digitar manualmente um ID inválido na URL
     * - O registro foi deletado por outro processo
     * - O banco de dados foi corrompido externamente
     */
    public AlbumModel buscarPorId(Long id) {
        // findById retorna um Optional<AlbumModel>
        // Optional evita NullPointerException e permite tratamento elegante de valores ausentes
        return albumRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("album não encontrado com id: " + id));
    }

    /**
     * Salva um álbum no banco de dados (cria novo ou atualiza existente).
     * 
     * Esta função unifica criação e atualização:
     * - Se album.getId() == null → cria novo registro
     * - Se album.getId() != null → atualiza registro existente
     * 
     * Antes de salvar, realiza validações de regra de negócio:
     * - Título e banda não podem estar vazios
     * - Ano deve estar entre 1900 e o ano atual
     * - Preço deve estar entre 0 e 1000
     * 
     * @param album Objeto AlbumModel a ser salvo
     * @return AlbumModel salvo (com ID preenchido se for novo registro)
     * @throws IllegalArgumentException se alguma validação falhar
     */
    public AlbumModel salvar(AlbumModel album) {
        // Obtém o ano atual para validação
        int anoAtual = Year.now().getValue();

        // ========== VALIDAÇÕES DE REGRA DE NEGÓCIO ==========
        
        // Validação: Título não pode ser nulo ou vazio
        if (album.getTitulo() == null || album.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título do álbum não pode ser vazio.");
        }
        
        // Validação: Banda não pode ser nula ou vazia
        if (album.getBanda() == null || album.getBanda().isBlank()) {
            throw new IllegalArgumentException("O nome da banda não pode ser vazio.");
        }

        // Validação: Ano deve estar entre 1900 e o ano atual
        // Evita anos inválidos como 0, negativos ou futuros
        if (album.getAno() == null || album.getAno() < 1900 || album.getAno() > anoAtual) {
            throw new IllegalArgumentException("Ano de lançamento inválido.");
        }
        
        // Validação: Preço deve estar entre 0 e 1000
        // doubleValue() converte BigDecimal para double para comparação
        if (album.getPreco() == null || album.getPreco().doubleValue() < 0 || album.getPreco().doubleValue() > 1000) {
            throw new IllegalArgumentException("Preço invalido, verifique o valor.");
        }

        // ========== LÓGICA DE CRIAÇÃO/ATUALIZAÇÃO ==========
        
        // Se o ID estiver preenchido, é uma atualização
        if (album.getId() != null) {
            // Busca o registro existente no banco
            Optional<AlbumModel> existente = albumRepository.findById(album.getId());
            
            // Optional.isPresent() verifica se o registro foi encontrado
            // Isso evita NullPointerException caso o ID não exista
            if (existente.isPresent()) {
                // Atualiza os campos do registro existente
                // Mantém a referência do objeto gerenciado pelo JPA (melhor performance)
                AlbumModel atualizado = existente.get();
                atualizado.setTitulo(album.getTitulo());
                atualizado.setBanda(album.getBanda());
                atualizado.setAno(album.getAno());
                atualizado.setPreco(album.getPreco());
                atualizado.setGenero(album.getGenero());
                
                // Salva e retorna o registro atualizado
                return albumRepository.save(atualizado);
            }
        }
        
        // Se chegou aqui, é um novo registro (ID null ou não encontrado)
        // O JPA automaticamente gera um novo ID ao salvar
        return albumRepository.save(album);
    }

    /**
     * Exclui um álbum do banco de dados pelo seu ID.
     * 
     * @param id ID do álbum a ser excluído
     * @throws RuntimeException se o álbum não for encontrado
     */
    public void excluir(Long id) {
        // Verifica se o álbum existe antes de tentar excluir
        // Isso evita erros silenciosos e fornece mensagem clara ao usuário
        if (!albumRepository.existsById(id)) {
            throw new RuntimeException("album não encontrado com id: " + id);
        }
        
        // Se chegou aqui, o álbum existe e pode ser excluído com segurança
        albumRepository.deleteById(id);
    }
}
