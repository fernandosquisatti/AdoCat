-- AdoCat: libera o mesmo e-mail em mais de uma categoria de inscrição.
-- Necessário SOMENTE se o banco já existia antes desta versão
-- (o Hibernate com ddl-auto=update não remove índices antigos).
-- Faça backup antes.

-- 1) Descubra o nome do índice único da coluna email (algo como "UK..."):
SHOW INDEX FROM tb_usuarios WHERE Column_name = 'email' AND Non_unique = 0;

-- 2) Troque NOME_DO_INDICE pelo valor da coluna Key_name do passo 1 e execute:
-- ALTER TABLE tb_usuarios DROP INDEX NOME_DO_INDICE;
