# 🎵 LUTICA'S SEBO - Sistema CRUD de Discos Musicais

## 📋 Descrição do Projeto

Sistema web desenvolvido em **Spring Boot** para gerenciamento de álbuns musicais, implementando **CRUD completo** com **autenticação de usuários** e **persistência de filtros** via sessão HTTP.

Sistema proposto como entrega para a disciplina de **Laboratório de Programação**.

##  Rotas

- `/albuns/galeria` → Lista de álbuns
- `/albuns/novo` → Criar novo álbum
- `/albuns/editar/{id}` → Editar álbum
- `/albuns/excluir/{id}` → Excluir álbum
- `/albuns/galeria/filtro` → Aplicar filtro
- `/albuns/limpar-filtro` → Limpar filtro
- `/logout` → Fazer logout

## Interface do Usuário

- **Design responsivo** com Bootstrap 5
- **Mensagens de feedback** para ações do usuário
- **Modais** para confirmação de ações

##  Modelo de Dados

### Usuario
- ID, Nome, Email (único), Senha

### AlbumModel
- ID, Título, Banda, Ano, Gênero, Preço

**Lucas Silva**  
*Disciplina: Laboratório de Programação*  



