# SkyTrip — publicação do site

Este projeto tem interface adaptável para celular e computador, API Java/Spring Boot, cadastro/login, viagens, atividades e despesas. O modo local usa H2; o perfil `production` usa PostgreSQL pela variável `DATABASE_URL`.

## Publicar com o Render Blueprint

1. Envie esta pasta para um repositório GitHub.
2. Entre em https://render.com/ e conecte sua conta GitHub.
3. No painel do Render, escolha **New + → Blueprint** e selecione o repositório `skytrip`.
4. O Render lê `render.yaml` e cria o serviço web e o banco PostgreSQL.
5. Aguarde o deploy terminar e abra a URL `https://...onrender.com` que aparecer no serviço.
6. Teste cadastro, login, criação de viagem, atividade, gasto e exclusão.

## Importante

- Não compartilhe a URL privada do banco nem senhas.
- O plano gratuito e as regras de retenção/disponibilidade podem mudar; confirme as condições atuais do Render antes de usar dados importantes.
- A publicação ainda precisa ser executada na conta de hospedagem e testada após o deploy. Este ZIP não é, por si só, um site já publicado.
- Antes de uso público real, recomenda-se revisar a segurança, privacidade, recuperação de senha e política de dados.

## Rodar localmente

Requer Java 17 e Maven. Na pasta do projeto, execute `mvn spring-boot:run` e abra `http://localhost:8080`.
