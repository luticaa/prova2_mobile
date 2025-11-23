package com.exercicioPratico.LabProgramacao;
//aplicacao principal
import com.exercicioPratico.LabProgramacao.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//anotacao pra informar que é spring
@SpringBootApplication
public class LabProgramacaoApplication implements CommandLineRunner {
	
	@Autowired
	private UsuarioService usuarioService;
	
	//funcao principal que roda a aplicacao
	public static void main(String[] args) {
		SpringApplication.run(LabProgramacaoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Criar usuário padrão para teste
		usuarioService.criarUsuarioPadrao();
	}
}
