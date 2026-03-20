# Spring E-Commerce API

Uma API REST completa para e-commerce construída com Spring Boot, com suporte para autenticação via JWT, gestão de produtos, categorias, pedidos e pagamentos.

## 🛠️ Tecnologias Utilizadas

- **Java 25**: Linguagem principal.
- **Spring Boot 4.0.3**: Framework base para o back-end, incluindo Spring Web, Spring Data JPA, Spring Security e Validation.
- **MySQL**: Banco de dados relacional (Aiven Cloud configurado).
- **JWT (JSON Web Token)**: Segurança e autenticação com a biblioteca jjwt.
- **Maven**: Gerenciador de dependências e build.
- **Dotenv**: Gerenciamento facilitado de variáveis de ambiente no ambiente de desenvolvimento (`spring-dotenv`).

## ⚙️ Pré-requisitos

Para rodar este projeto localmente, certifique-se de ter:

- [Java JDK 25+](https://jdk.java.net/)
- [Maven](https://maven.apache.org/) (Opcional, o projeto usa o wrapper `./mvnw`)
- Opcional: Instância do MySQL rodando ou banco na nuvem.

## 🚀 Configuração Inicial e Como Executar Localmente

### 1. Clonando o Repositório

```bash
git clone <URL-DO-SEU-REPOSITORIO>
cd spring-ecommerce-api
```

### 2. Configurando Variáveis de Ambiente

Crie um novo arquivo chamado `.env` na raiz do projeto, baseando-se no arquivo `.env.example` existente, ou apenas adicione suas próprias credenciais configuradas:

```env
DB_URL=jdbc:mysql://SEU-HOST:PORT/DB?sslMode=REQUIRED
DB_USERNAME=SEU-USER
DB_PASSWORD=SUA-SENHA
SPRING_DATASOURCE_PASSWORD=SUA-SENHA
JWT_SECRET=UM-SECRET-SEGURO-AQUI-NO-MINIMO-256-BITS
JWT_EXPIRATION=3600000
SPRING_PROFILES_ACTIVE=dev
```

> **Atenção:** O arquivo `.env` já está no `.gitignore` para proteção das credenciais e chaves do banco e da sua cloud não subirem para o GitHub.

### 3. Compilando e Executando

Com o ambiente em ordem, limpe a compilação anterior, construa o projeto e inicie:

```bash
# Dar permissão de execução ao Wrapper (Linux/Mac)
chmod +x mvnw

# Para compilar garantindo que não há erros
./mvnw clean compile

# Rodando o projeto
./mvnw spring-boot:run
```

## ☁️ Deploy (Azure / Nuvem)

Ao realizar deploy para Azure App Services ou Azure Spring Apps e outras clouds:

1. **Não envie o arquivo `.env` para a nuvem**, ele existe somente para facilitar o desenvolvimento local.
2. Nas configurações do serviço de hospedagem (Application Settings/Environment variables), adicione as referidas variáveis de ambiente do seu banco de dados MySQL no Aiven e JWT (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`).
3. Você pode gerar o executável (build) para deploy executando:
   ```bash
   ./mvnw clean package -DskipTests
   ```
   Isso criará seu `.jar` executável na pasta `/target`.

## 📦 Estrutura Essencial do Projeto

- `controller/`: Camada com endpoints/APIs REST (Auth, Categoria, Produtos...).
- `service/`: Regras de negócio, tratamento e fluxo da API.
- `repository/`: Camada de comunicação com o JPA/Hibernate e abstrações de acesso ao MySQL.
- `entity/`: Classes de mapeamento Objeto-Relacional referenciando tabelas como `User`, `Product`, `Order`.
- `exception/`: Middleware para interceptação central global de eventos de erro (ResourceExceptionHandler).
- `dto/`: Transfer objects para receber e entregar payloads limpos pelas portas http do sistema.
