package com.exercicioPratico.LabProgramacao.controllers;

import com.exercicioPratico.LabProgramacao.dto.AlbumDTO;
import com.exercicioPratico.LabProgramacao.model.AlbumModel;
import com.exercicioPratico.LabProgramacao.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/albuns")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public ResponseEntity<List<AlbumDTO>> listarAlbuns(@RequestParam(required = false) String banda) {
        List<AlbumModel> albuns;
        if (banda != null && !banda.isEmpty()) {
            albuns = albumService.buscarPorBanda(banda);
        } else {
            albuns = albumService.listar();
        }
        
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

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> buscarPorId(@PathVariable Long id) {
        AlbumModel album = albumService.buscarPorId(id);
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

    @PostMapping
    public ResponseEntity<AlbumDTO> criarAlbum(@RequestBody AlbumModel album) {
        AlbumModel albumSalvo = albumService.salvar(album);
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

    @PutMapping("/{id}")
    public ResponseEntity<AlbumDTO> atualizarAlbum(@PathVariable Long id, @RequestBody AlbumModel album) {
        album.setId(id);
        AlbumModel albumAtualizado = albumService.salvar(album);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAlbum(@PathVariable Long id) {
        albumService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}