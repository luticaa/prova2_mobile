package com.exercicioPratico.LabProgramacao.service;
import com.exercicioPratico.LabProgramacao.model.AlbumModel;
import com.exercicioPratico.LabProgramacao.repositories.AlbumRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.Year; //para captarmos ano atual
import java.util.List;
import java.util.Optional;


/*CAMADA DE SERVIÇO OU NEGÓCIO - LOGICA DE NEGOCIO*/

@Service//anotacao para classe pertencer a camada de servicos
public class AlbumService {//declaracao da classe AlbumService
    //nosso repositorio JPA realiza todas as querys por baixo dos panos facilitando nossa vida
    private final AlbumRepository albumRepository; //cria o repositorio, apos construcao imutavel

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;//construtor para repositorio, IoC/ID
    }

    public List<AlbumModel> listar() {
        return albumRepository.findAll(Sort.by("banda").ascending());
        //lista as bandas existentes, sort ascendente no argumento.
    }

    public List<AlbumModel> buscarPorBanda(String banda) {
        return albumRepository.findByBandaContainingIgnoreCase(banda);
        //busca banda por nome ja trata maiusculas e minusculas
    }

    public AlbumModel buscarPorId(Long id) {
        return albumRepository.findById(id)
                /*caso nao ache, gera excecao, aqui em funcionamento normal nunca sera lancada essa excecao pois chamamos
                do botao acoes do proprio album, mas por segunca ainda sim se faz necessario pois um usuario pode digitar
                manualmente um id na barra de busca ou ainda o banco de dados ter sido corrompido externamente*/
            .orElseThrow(() -> new RuntimeException("album não encontrado com id: " + id));
    }

    public AlbumModel salvar(AlbumModel album) {
        int anoAtual = Year.now().getValue();
        //variavel ano vigente

        // Funcao salvar, realiza a atualizacao do album Validações manuais de regra de negócio
        if (album.getTitulo() == null || album.getTitulo().isBlank()) {
            //checa validade do titulo caso contrario recusa
            throw new IllegalArgumentException("O título do álbum não pode ser vazio.");
        }
        if (album.getBanda() == null || album.getBanda().isBlank()) {
            //checa validade do do nome da banda caso contrario recusa
            throw new IllegalArgumentException("O nome da banda não pode ser vazio.");
        }

        if (album.getAno() == null || album.getAno() < 1900 || album.getAno() > anoAtual ) {
            //checa validade do ano de lançamento caso contrario recusa
            throw new IllegalArgumentException("Ano de lançamento inválido.");
        }
        if (album.getPreco() == null || album.getPreco().doubleValue() < 0 || album.getPreco().doubleValue() > 1000 ) {
            //checa validade do preco caso contrario recusa
            throw new IllegalArgumentException("Preço invalido, verifique o valor.");
        }
        // Se o ID vier preenchido, é atualização; senão, é novo registro
        if (album.getId() != null) {//se existe um id valido
            Optional<AlbumModel> existente = albumRepository.findById(album.getId());
            /*OPTIONAL >>> EXPLICITA para o JPA a validade do id ou nao pois mesmo nao sendo nulo, pode nao ser aceitavel
            evita NullPointerException caso nao exista um registro com o id fornecido*/
            if (existente.isPresent()) {
                // Atualizar dados do existente caso esteja consistente (passou pelo check acima)
                AlbumModel atualizado = existente.get();
                atualizado.setTitulo(album.getTitulo());
                atualizado.setBanda(album.getBanda());
                atualizado.setAno(album.getAno());      //ATUALIZACOES DO OBJETO CORRENTE (EXISTENTE)
                atualizado.setPreco(album.getPreco());
                atualizado.setGenero(album.getGenero());
                return albumRepository.save(atualizado); //RETORNA ATUALIZADO
            }
        }
        return albumRepository.save(album);
        /*funcao salvar trabalha sob duas condicoes salvando uma edicao ou salvando um novo, por isso contempla ambos
        este return é referente ao salvamento de um novo album (caso nao entre no if)*/
    }

    public void excluir(Long id) {
        if (!albumRepository.existsById(id)) {
            throw new RuntimeException("album não encontrado com id: " + id);
        }//funcao excluir tranquila, chama do repositório direto se nao existe, exceção caso exista deleta.
        albumRepository.deleteById(id);
    }
}
