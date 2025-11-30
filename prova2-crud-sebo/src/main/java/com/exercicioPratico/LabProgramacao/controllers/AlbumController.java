package com.exercicioPratico.LabProgramacao.controllers;

import com.exercicioPratico.LabProgramacao.dto.AlbumDTO;
import com.exercicioPratico.LabProgramacao.model.AlbumModel;
import com.exercicioPratico.LabProgramacao.service.AlbumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST responsável por expor os endpoints da API de álbuns.
 * 
 * Esta classe recebe requisições HTTP do frontend (mobile/web) e delega
 * a lógica de negócio para o AlbumService, convertendo os dados entre
 * AlbumModel (entidade JPA) e AlbumDTO (objeto de transferência).
 * 
 * Todos os endpoints estão mapeados sob o caminho base "/api/albuns"
 */
@RestController
@RequestMapping("/api/albuns")
public class AlbumController {

    // Injeção de dependência do serviço de álbuns via construtor
    // O Spring Boot automaticamente injeta uma instância de AlbumService
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    /**
     * Endpoint GET /api/albuns
     * Lista todos os álbuns ou filtra por banda (se o parâmetro for fornecido).
     * 
     * @param banda Parâmetro opcional de query string para filtrar por nome da banda
     * @return Lista de álbuns convertidos para DTO com status HTTP 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<AlbumDTO>> listarAlbuns(@RequestParam(required = false) String banda) {
        List<AlbumModel> albuns;
        
        // Verifica se foi fornecido um filtro de banda na query string
        // Exemplo: GET /api/albuns?banda=Metallica
        if (banda != null && !banda.isEmpty()) {
            // Busca álbuns que contenham o nome da banda (case-insensitive)
            albuns = albumService.buscarPorBanda(banda);
        } else {
            // Lista todos os álbuns ordenados por banda
            albuns = albumService.listar();
        }
        
        // Converte lista de AlbumModel (entidade JPA) para AlbumDTO (objeto de transferência)
        // Isso evita expor detalhes internos da entidade e melhora a performance
        List<AlbumDTO> albunsDTO = albuns.stream()
            .map(album -> new AlbumDTO(
                album.getId(),
                album.getTitulo(),
                album.getBanda(),
                album.getAno(),
                album.getGenero(),
                album.getPreco()
            ))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(albunsDTO);
    }

    /**
     * Endpoint GET /api/albuns/{id}
     * Busca um álbum específico pelo seu ID.
     * 
     * @param id ID do álbum extraído da URL (path variable)
     * @return Álbum convertido para DTO com status HTTP 200 (OK)
     * @throws RuntimeException se o álbum não for encontrado (tratado no Service)
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> buscarPorId(@PathVariable Long id) {
        // Busca o álbum no banco de dados através do Service
        AlbumModel album = albumService.buscarPorId(id);
        
        // Converte a entidade para DTO antes de retornar
        AlbumDTO albumDTO = new AlbumDTO(
            album.getId(),
            album.getTitulo(),
            album.getBanda(),
            album.getAno(),
            album.getGenero(),
            album.getPreco()
        );
        return ResponseEntity.ok(albumDTO);
    }

    /**
     * Endpoint POST /api/albuns
     * Cria um novo álbum no banco de dados.
     * 
     * O corpo da requisição (JSON) é automaticamente convertido para AlbumModel
     * pelo Spring Boot através da anotação @RequestBody.
     * 
     * @Valid ativa as validações do Bean Validation (@NotBlank, @Size, etc.)
     * Se a validação falhar, o ApiExceptionHandler captura e retorna erro 400.
     * 
     * @param album Objeto AlbumModel recebido no corpo da requisição HTTP
     * @return Álbum criado convertido para DTO com status HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<AlbumDTO> criarAlbum(@Valid @RequestBody AlbumModel album) {
        // O Service valida os dados e salva no banco
        // Se o ID estiver null, será criado um novo registro
        AlbumModel albumSalvo = albumService.salvar(album);
        
        // Converte para DTO e retorna com status 201 (Created)
        AlbumDTO albumDTO = new AlbumDTO(
            albumSalvo.getId(),
            albumSalvo.getTitulo(),
            albumSalvo.getBanda(),
            albumSalvo.getAno(),
            albumSalvo.getGenero(),
            albumSalvo.getPreco()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(albumDTO);
    }

    /**
     * Endpoint PUT /api/albuns/{id}
     * Atualiza um álbum existente no banco de dados.
     * 
     * @Valid ativa as validações do Bean Validation (@NotBlank, @Size, etc.)
     * Se a validação falhar, o ApiExceptionHandler captura e retorna erro 400.
     * 
     * @param id ID do álbum a ser atualizado (extraído da URL)
     * @param album Objeto AlbumModel com os novos dados (recebido no corpo da requisição)
     * @return Álbum atualizado convertido para DTO com status HTTP 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlbumDTO> atualizarAlbum(@PathVariable Long id, @Valid @RequestBody AlbumModel album) {
        // Define o ID no objeto para que o Service saiba que é uma atualização
        album.setId(id);
        
        // O Service verifica se o ID existe e atualiza o registro
        AlbumModel albumAtualizado = albumService.salvar(album);
        
        // Converte para DTO e retorna
        AlbumDTO albumDTO = new AlbumDTO(
            albumAtualizado.getId(),
            albumAtualizado.getTitulo(),
            albumAtualizado.getBanda(),
            albumAtualizado.getAno(),
            albumAtualizado.getGenero(),
            albumAtualizado.getPreco()
        );
        return ResponseEntity.ok(albumDTO);
    }

    /**
     * Endpoint DELETE /api/albuns/{id}
     * Remove um álbum do banco de dados.
     * 
     * @param id ID do álbum a ser excluído (extraído da URL)
     * @return Resposta vazia com status HTTP 204 (NO_CONTENT) indicando sucesso
     * @throws RuntimeException se o álbum não for encontrado (tratado no Service)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAlbum(@PathVariable Long id) {
        // O Service verifica se o álbum existe antes de excluir
        albumService.excluir(id);
        
        // Retorna status 204 (No Content) - operação bem-sucedida, sem conteúdo
        return ResponseEntity.noContent().build();
    }
}