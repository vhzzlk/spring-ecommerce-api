# Spring E-Commerce API

Uma API REST completa para e-commerce construída com Spring Boot, com suporte para autenticação via JWT, gestão de produtos, categorias, pedidos e pagamentos.

## 🛠️ Tecnologias Utilizadas

- **Java 21**: Linguagem principal.
- **Spring Boot 4.0.3**: Framework base para o back-end.
- **Spring Security & JWT**: Autenticação e autorização segura.
- **Spring Data JPA**: Abstração de acesso ao banco de dados.
- **MySQL**: Banco de dados relacional.
- **SpringDoc OpenAPI (Swagger)**: Documentação automatizada da API.
- **Docker & Docker Compose**: Containerização e orquestração.
- **Maven**: Gerenciador de dependências.

## 📖 Documentação da API (Swagger)

Com a aplicação rodando, você pode acessar a documentação interativa do Swagger UI em:
`http://localhost:8080/swagger-ui/index.html`

## 🚀 Como Executar

### Via Docker (Recomendado)

Certifique-se de ter o Docker e o Docker Compose instalados.

```bash
docker-compose up --build
```

Isso iniciará a API e o banco de dados MySQL automaticamente.

### Localmente

1. **Configurar o Banco de Dados:** Certifique-se de ter um MySQL rodando.
2. **Configurar Variáveis de Ambiente:** Crie um arquivo `.env` na raiz (veja `.env.example`).
3. **Executar:**
```bash
./mvnw spring-boot:run
```

## 📦 Estrutura do Projeto

- `controller/`: Endpoints da API.
- `service/`: Lógica de negócio.
- `repository/`: Acesso ao banco de dados.
- `entity/`: Modelos de dados.
- `dto/`: Objetos de transferência de dados.
- `config/`: Configurações de segurança e Swagger.

## 🛡️ Segurança

A API utiliza JWT para autenticação. Para acessar endpoints protegidos (como criação de produtos), é necessário:
1. Registrar um usuário.
2. Fazer login para obter o token JWT.
3. Incluir o token no header `Authorization: Bearer <token>`.

---
Desenvolvido por [Victor Hugo](https://github.com/vhzzlk)
