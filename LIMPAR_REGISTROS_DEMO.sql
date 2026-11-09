-- Execute somente depois de fazer backup do banco adocat.
-- Remove exclusivamente registros inseridos pelo carregador demonstrativo antigo,
-- identificados pelo prefixo literal "DEMO •". Não remove cadastros reais.
DELETE FROM tb_pagamentos WHERE nome_pagador LIKE 'DEMO •%';
DELETE FROM tb_assinaturas WHERE nome_apoiador LIKE 'DEMO •%';
DELETE FROM tb_doacoes WHERE nome_doador LIKE 'DEMO •%';
DELETE FROM tb_usuarios WHERE nome LIKE 'DEMO •%';
DELETE FROM gatos WHERE nome LIKE 'DEMO •%';
