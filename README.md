# Hackaton - API de Notifications

API de notificações construída com Quarkus para persistência de mensagens, entrega em tempo real via SSE e integração com Kafka (Azure Event Hubs compatível) e Redis.

## Visão Geral

- **Persistência**: Armazena mensagens em PostgreSQL usando JPA/Panache (`JpaMessagesEntity`).
- **Tempo real (SSE)**: Entrega de eventos aos clientes via Server-Sent Events com `EventSenderAdapter` e publicação/assinatura no Redis (`EventRedisAdapter`) no canal `notifications-channel`.
- **Integração Kafka**: Consome eventos de status de vídeo no canal reativo `video-status` e transforma em mensagens persistidas e notificadas aos clientes.
- **Documentação**: Swagger UI habilitado via SmallRye OpenAPI.
- **Observabilidade**: Health endpoints via SmallRye Health.

## Funcionalidades

- **Criar mensagem**: `POST /notifications/messages` para criar e publicar uma nova mensagem.
- **Stream de mensagens (SSE)**: `GET /notifications/sse/messages?clientId={id}` para assinar eventos em tempo real para um cliente.
- **Consumidor de status de vídeo (Kafka)**: Recebe `StatusVideoDTO` no canal `video-status`, persiste como `Messages` e notifica assinantes SSE.

## Stack Tecnológico

- **Java 21**
- **Quarkus 3.25.4** (`quarkus-arc`, `quarkus-rest-jackson`, `quarkus-hibernate-orm-panache`, `quarkus-kubernetes`, `quarkus-smallrye-openapi`, `quarkus-swagger-ui`, `quarkus-smallrye-health`, `quarkus-micrometer`, `quarkus-redis-client`, `quarkus-messaging-kafka`)
- **PostgreSQL** + **Flyway**
- **Redis** (Pub/Sub)
- **Kafka** (Azure Event Hubs compatível)
- **Lombok**, **ModelMapper**, **Gson**
- Testes: **JUnit 5**, **Mockito**, **Rest-Assured**, **JaCoCo**

## Arquitetura

![image](https://github.com/user-attachments/assets/c8996715-f174-4611-ab40-7c1d5ba35877)

- **Presentation** (`br/com/fiap/messages/presentation/...`)
    - REST: `MessagesRestControllerImpl` expõe `POST /notifications/messages`.
    - SSE: `MessagesSseControllerImpl` expõe `GET /notifications/sse/messages?clientId=...`.
    - Messaging: `VideoStatusConsumerImpl` consome do canal Kafka `video-status`.
- **Application** (`br/com/fiap/messages/application/...`)
    - Controllers: `MessagesControllerImpl`, `StatusVideoControllerImpl` orquestram casos de uso.
    - Use cases: `SaveMessagesUseCaseImpl`, `ConsultMessagesUseCaseImpl`, `EventSenderUseCaseImpl`.
    - Gateways/Mappers/Presenter: isolam domínio de infraestrutura.
- **Domain** (`br/com/fiap/messages/domain/...`)
    - Entidades de domínio: `Messages`, `Event`, `EventPayload`, `MessageType`, `StatusType`.
- **Infrastructure**
    - Banco: `JpaMessagesEntity`, `JpaMessageReadsEntity`, `JpaMessagesRepository` (Panache Repository).
    - Redis: `EventRedisAdapter` publica/assina canal `notifications-channel` e delega ao `EventSenderAdapter` para SSE.
    - SSE: `EventSenderAdapter` realiza broadcast para assinantes por `clientId`.

Raiz de contexto HTTP: `quarkus.http.root-path=/notifications` (ver `src/main/resources/application.properties`).

## Estrutura de Pastas (resumo)

```
src/main/java/br/com/fiap/messages/
├── application/
│   ├── controller/ (orquestração)
│   ├── gateway/ (portas de saída)
│   ├── mapper/ (DTO/domain)
│   ├── presenter/
│   └── usecase/
├── common/
│   ├── domain/entity/ (JPA)
│   └── dto/ (request/response)
├── domain/ (objetos e enums de domínio)
├── infrastructure/
│   ├── database/ (JPA + Panache)
│   ├── redis/ (Pub/Sub)
│   └── sse/ (Server-Sent Events)
└── presentation/ (REST, SSE, Kafka consumer)
```

## Endpoints

- **Swagger UI**: `GET /notifications/q/swagger-ui`
- **OpenAPI**: `GET /notifications/q/openapi`
- **Health**: `GET /notifications/q/health`

### Criar Mensagem

```
POST /notifications/messages
Content-Type: application/json

{
  "content": "string",
  "type": "S|E|...",  // ver enum MessageType
  "recipient": "cliente-123",
  "status": "CREATED|SENT|..." // ver enum StatusType
}
```

Resposta: `200 OK` com `MessagesResponseDTO`.

Exemplo curl:

```
curl -X POST http://localhost:8080/notifications/messages \
  -H "Content-Type: application/json" \
  -d '{
    "content":"Olá!",
    "type":"S",
    "recipient":"cliente-123",
    "status":"CREATED"
  }'
```

### Stream de Mensagens (SSE)

```
GET /notifications/sse/messages?clientId={id}
Accept: text/event-stream
```

Exemplo curl:

```
curl -N http://localhost:8080/notifications/sse/messages?clientId=cliente-123
```

Payload de evento (exemplo):

```
{
  "id": "uuid",
  "type": "S",
  "recipient": "cliente-123",
  "eventPayload": { ... }
}
```

## Configuração

Arquivo: `src/main/resources/application.properties` (ajuste para seu ambiente; recomenda-se usar variáveis de ambiente em produção):

- Datasource Postgres: `quarkus.datasource.*`
- Flyway: `quarkus.flyway.*`
- Redis: `quarkus.redis.*`
- HTTP root: `quarkus.http.root-path=/notifications`
- Kafka (Azure Event Hubs): `mp.messaging.incoming.video-status.*`

Variáveis úteis (exemplos):

```
export POSTGRES_USER=postgres
export POSTGRES_PASSWORD=postgres
export POSTGRES_DB=notifications
export REDIS_PASSWORD=redispass
```

## Execução Local

Pré-requisitos: Java 21, Maven 3.8+, Docker (opcional para Postgres/Redis).

1) Subir dependências via Docker Compose (opcional):

```
docker compose -f src/main/docker/docker-compose.yml up -d
```

2) Rodar a aplicação em modo dev:

```
mvn quarkus:dev
```

Aplicação: `http://localhost:8080/notifications`

## Build e Docker

1) Build do artefato:

```
mvn clean package -DskipTests
```

2) Build da imagem Docker (usa `Dockerfile` na raiz e artefatos de `target/quarkus-app/`):

```
docker build -t notifications-api:latest .
docker run --rm -p 8080:8080 \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://host.docker.internal:5432/notifications \
  -e QUARKUS_DATASOURCE_USERNAME=$POSTGRES_USER \
  -e QUARKUS_DATASOURCE_PASSWORD=$POSTGRES_PASSWORD \
  -e QUARKUS_REDIS_HOSTS=redis://host.docker.internal:6379 \
  -e QUARKUS_REDIS_PASSWORD=$REDIS_PASSWORD \
  notifications-api:latest
```

## Kubernetes

Manifests em `kubernetes/`:

- `Deployment.yaml`
- `Secret.yaml`
- `RoleMetrics.yaml`

Ajuste imagens/variáveis e aplique com `kubectl apply -f kubernetes/`.

## Cobertura Sonar
![sonar.png](assets/images/sonar.png)

## Arquitetura Infraestrutura

### Diagrama de Fluxo
![infra.jpg](assets/images/infra.jpg)
- Dentro do Resource Group techchallenge-rg, há um IP Público que acessa o APIM (Azure API Management)
- Quando acessado e havendo configuração de suas políticas realiza a chamada para a function.
- O Ingress Controller então roteia as requisições para os diferentes serviços internos a depender da URI chamada, utilizando a comunicação via Cluster IP.
- As aplicações java se comunicam com seus respectivos databases utilizando a comunicação via Cluster IP.
  Obs: Para saber mais sobre o recurso Standard_B2S: https://learn.microsoft.com/pt-br/azure/virtual-machines/sizes/general-purpose/bv1-series?tabs=sizebasic

### Diagrama de Componente
![pods.jpg](assets/images/pods.jpg)
O cluster k8s-fiap é configurado com dois namespaces principais, cada um com funções específicas:
- default: Namespace onde as aplicações principais são implantadas e gerenciadas, contendo os PODs:
    - java-app-*: microsserviço presente no cluster.
        - Ingress: Configurado para gerenciar o tráfego de entrada direcionado à aplicação Java.
        - Cluster IP: Endereço IP interno para comunicação dentro do cluster.
        - Deployment: Gerencia a implantação e a escalabilidade da aplicação Java.
        - Secret: Armazena dados sensíveis, como chaves de API ou credenciais usadas pela aplicação.
        - Horizontal Pod Autoscaler (HPA): Configurado para escalar automaticamente o número de réplicas do pod com base na utilização de CPU.
        - Configuração do HPA:
            - Mínimo de 1 e máximo de 3 réplicas.
            - Escala a partir da métrica de uso de CPU atingir 70%.
        - Role HPA: Define as permissões necessárias para que o HPA acesse métricas do cluster (como CPU e memória) para tomar decisões de escalabilidade.
- ingress-basic: é responsável por gerenciar o tráfego externo e rotear as requisições para os serviços no namespace default.
    - ingress-nginx-controller: Executa o controlador NGINX Ingress, que atua como ponto de entrada para requisições externas e roteia o tráfego para os serviços apropriados no namespace default.
        - Ingress: Define as regras de roteamento para requisições externas (por exemplo, rotear requisições para o serviço do java-app).
        - Service: Expõe o controlador NGINX internamente no cluster.
        - Endpoint: Mapeia os endpoints para os serviços internos.
        - Deployment: Gerencia a implantação do controlador NGINX.
        - ConfigMap: Armazena configurações do NGINX, como limites de requisições, timeouts e outras opções de personalização.
        - Secret: Armazena informações sensíveis, como certificados TLS para habilitar HTTPS.    
          *Os arquivos de configuração do Kubernetes (em formato .yml) estão organizados no diretório kubernetes/, que contém os recursos descritos no diagrama.

## Segurança

- Não comite credenciais reais. Use variáveis de ambiente e Secrets (Docker/Kubernetes).
- Configure TLS e autenticação em Kafka/Azure Event Hubs conforme seu provedor.

## Contribuição

1. Fork do repositório
2. Branch de feature
3. Testes: `mvn test`
4. Pull Request