# SkyTrip — Java + HTML/CSS/JS

Projeto responsivo para organizar viagens, com API Java/Spring Boot, banco H2 local e cadastro/login.

## Requisitos
- Java JDK 17 ou superior
- Maven 3.8+ (ou use a extensão Spring Boot no VS Code/IntelliJ)

## Executar no computador (localhost)
1. Extraia o ZIP.
2. Abra o terminal dentro da pasta `SkyTrip`.
3. Execute `mvn spring-boot:run`.
4. No mesmo computador, acesse http://localhost:8080
5. Para testar em outro aparelho na mesma rede Wi-Fi, descubra o IP local do computador e abra `http://IP-DO-COMPUTADOR:8080` no celular. Os dois aparelhos precisam estar na mesma rede e o firewall pode precisar liberar a porta 8080.

## Recursos
- Cadastro e login; senha armazenada com hash BCrypt.
- Várias viagens por conta.
- Destino, datas, número de viajantes.
- Adicionar/remover atividades do roteiro.
- Adicionar/remover gastos, total calculado automaticamente.
- Excluir viagem e seus itens.
- Banco H2 em arquivo na pasta `data/` para manter dados entre reinicializações locais.

## Publicar em hospedagem pública
Este projeto pode ser publicado em uma hospedagem que execute Java 17 e aceite aplicação Spring Boot, como Render (Web Service) ou Railway.
- Build command: `mvn clean package`
- Start command: `java -jar target/skytrip-1.0.0.jar`
- O serviço precisa permitir definir a variável `PORT` (já suportada).
- Para dados persistentes na nuvem, configure um banco PostgreSQL gerenciado e adapte `pom.xml`/`application.properties`, ou use um disco persistente da hospedagem. O disco local de hospedagem gratuita pode ser apagado ao reiniciar/deploy e não deve ser tratado como backup.
- Use HTTPS e um banco persistente em produção. O H2 incluído é para desenvolvimento/teste local; não é recomendado para vários usuários em produção.

## Estrutura
- `src/main/java/com/skytrip/` — entidades, repositórios, API e aplicação Java.
- `src/main/resources/static/` — interface HTML, CSS e JavaScript.
- `src/main/resources/application.properties` — configuração local/servidor.

## Rotas principais
- `POST /api/cadastro`, `POST /api/login`, `POST /api/logout`, `GET /api/me`
- `GET /api/viagens`, `POST /api/viagens`, `DELETE /api/viagens/{id}`
- `POST /api/viagens/{id}/atividades`, `DELETE /api/atividades/{id}`
- `POST /api/viagens/{id}/gastos`, `DELETE /api/gastos/{id}`

A interface e API são servidas pelo mesmo servidor, então não precisa configurar CORS.
