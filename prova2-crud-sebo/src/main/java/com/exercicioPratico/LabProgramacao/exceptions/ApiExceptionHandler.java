package com.exercicioPratico.LabProgramacao.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe centralizada para tratamento de exceções da API REST
 * 
 * @RestControllerAdvice intercepta exceções lançadas por qualquer @RestController
 * e permite tratar todas de forma centralizada, retornando respostas padronizadas.
 * 
 * Benefícios:
 * - Respostas de erro consistentes em toda a API
 * - Código mais limpo (sem try-catch em cada controller)
 * - Fácil manutenção (tratamento em um único lugar)
 * - Melhor experiência para o frontend (formato JSON padronizado)
 * 
 * Como funciona:
 * 1. Qualquer exceção lançada em um @RestController é capturada aqui
 * 2. O método @ExceptionHandler correspondente trata a exceção
 * 3. Retorna uma resposta HTTP padronizada com status e mensagem apropriados
 */
@RestControllerAdvice
public class ApiExceptionHandler {
    
    /**
     * Trata exceções de validação do Bean Validation (@Valid)
     * 
     * Quando um @RequestBody com @Valid falha na validação,
     * o Spring lança MethodArgumentNotValidException.
     * 
     * Exemplo: se AlbumModel tem @NotBlank e recebe string vazia,
     * esta exceção é lançada automaticamente.
     * 
     * @param ex Exceção de validação lançada pelo Spring
     * @return Resposta HTTP 400 (Bad Request) com detalhes dos erros de validação
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Validação");
        
        // Mapeia cada campo com erro para sua mensagem de validação
        // Exemplo: { "titulo": "O título não pode estar em branco", "preco": "O preço é obrigatório" }
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        body.put("errors", errors);
        
        return ResponseEntity.badRequest().body(body);
    }
    
    /**
     * Trata exceções de argumento inválido (validações de regra de negócio)
     * 
     * Quando o Service lança IllegalArgumentException para validações de regra de negócio.
     * Exemplo: "O título do álbum não pode ser vazio" (validação manual no Service)
     * 
     * @param ex Exceção de argumento inválido
     * @return Resposta HTTP 400 (Bad Request) com mensagem de erro
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de Validação");
        body.put("message", ex.getMessage());
        
        return ResponseEntity.badRequest().body(body);
    }
    
    /**
     * Trata exceções de violação de integridade de dados no banco
     * 
     * DataIntegrityViolationException é lançada pelo Spring Data JPA quando há:
     * - Violação de constraint única (ex: tentar inserir registro duplicado)
     * - Violação de foreign key (ex: referenciar registro que não existe)
     * - Violação de NOT NULL (ex: tentar inserir null em campo obrigatório)
     * - Violação de CHECK constraint
     * 
     * Exemplo: tentar criar álbum com ID duplicado ou violar constraint do banco
     * 
     * @param ex Exceção de violação de integridade
     * @return Resposta HTTP 409 (Conflict) ou 400 (Bad Request) com mensagem de erro
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Violação de Integridade de Dados");
        
        // Extrai mensagem mais amigável da exceção
        String mensagem = ex.getMessage();
        if (mensagem != null) {
            // Tenta identificar o tipo de violação pela mensagem
            if (mensagem.contains("Duplicate entry") || mensagem.contains("duplicate key")) {
                body.put("message", "Já existe um registro com estes dados. Verifique se não está tentando criar um registro duplicado.");
            } else if (mensagem.contains("foreign key") || mensagem.contains("FOREIGN KEY")) {
                body.put("message", "Não é possível realizar esta operação. Existem referências a este registro em outras tabelas.");
            } else if (mensagem.contains("cannot be null") || mensagem.contains("NOT NULL")) {
                body.put("message", "Campos obrigatórios não podem ser nulos.");
            } else {
                // Mensagem genérica se não conseguir identificar o tipo específico
                body.put("message", "Violação de integridade de dados. Verifique os dados enviados.");
            }
        } else {
            body.put("message", "Violação de integridade de dados no banco.");
        }
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
    
    /**
     * Trata exceções quando recurso não é encontrado
     * 
     * Quando o Service lança RuntimeException para recursos não encontrados.
     * Exemplo: "album não encontrado com id: 999"
     * 
     * @param ex Exceção de recurso não encontrado
     * @return Resposta HTTP 404 (Not Found) com mensagem de erro
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        // Verifica se é erro de "não encontrado" pela mensagem
        if (ex.getMessage() != null && ex.getMessage().contains("não encontrado")) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", "Recurso Não Encontrado");
            body.put("message", ex.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }
        
        // Outras RuntimeException → erro genérico 500
        // Em produção, não exponha detalhes da exceção por segurança
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Erro Interno do Servidor");
        body.put("message", "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}

