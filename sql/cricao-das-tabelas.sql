CREATE TABLE usuario (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  senha VARCHAR(100) NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  tipo_usuario VARCHAR(15) NOT NULL
);

CREATE TABLE servico (
  id INT PRIMARY KEY AUTO_INCREMENT,
  titulo VARCHAR(100) NOT NULL,
  descricao VARCHAR(500) NOT NULL,
  valor DECIMAL(10, 2) NOT NULL,
  contratante_id INT NOT NULL,
  prestador_id INT,
  concluido BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY (contratante_id) REFERENCES usuario(id),
  FOREIGN KEY (prestador_id) REFERENCES usuario(id)
);