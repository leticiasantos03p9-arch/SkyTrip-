# SkyTrip — projeto inicial

Aplicativo web responsivo com frontend HTML/CSS/JavaScript, backend Java Spring Boot e banco H2 persistente.

## Requisitos
- Java JDK 17 ou superior
- Maven 3.8+ (ou Maven Wrapper adicionado pela sua IDE)

## Executar no computador
1. Extraia o ZIP.
2. Abra o terminal dentro da pasta `SkyTrip`.
3. Execute: `mvn spring-boot:run`
4. No navegador, abra: `http://localhost:8080`

O frontend e a API são servidos pela mesma aplicação. A API usa `/api/trips`.
Os dados ficam em `./data/skytripdb` e permanecem após reiniciar a aplicação.

## Usar no celular na mesma rede Wi-Fi
1. Inicie o servidor no computador.
2. Descubra o IP local do computador (ex.: `192.168.1.20`).
3. No celular conectado ao mesmo Wi-Fi, abra `http://IP-DO-COMPUTADOR:8080`.
4. Se não abrir, verifique o firewall do computador e permita a porta 8080.

`localhost` no celular aponta para o próprio celular, não para o computador.

## Publicar em hospedagem
Para abrir fora da sua rede, publique o projeto em uma hospedagem que aceite aplicações Java/Spring Boot e configure armazenamento persistente para o banco. O banco H2 em arquivo é bom para desenvolvimento; para uma publicação com vários usuários, troque por PostgreSQL e configure autenticação/contas. Não exponha o console H2 (`/h2-console`) em produção.

## Observações
- Esta é uma base funcional, não um APK Android.
- As viagens são compartilhadas por quem acessa a mesma instância; autenticação individual ainda não foi implementada.
- Para produção, adicione login, autorização por usuário, validação reforçada e banco gerenciado.
