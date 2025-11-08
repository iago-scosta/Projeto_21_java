
INSERT INTO Departamento (nomeDep, depEspecialidade)
VALUES ('Cardiologia', 'Coração'),
       ('Pediatria', 'Crianças');

INSERT INTO Funcionario (firstName, lastName, funcionarioCPF)
VALUES ('João', 'Silva', '11111111111'),
       ('Maria', 'Souza', '22222222222'),
       ('Paulo', 'Oliveira', '33333333333');

INSERT INTO Medico (medico_CRM, medico_especialidade, fk_idFuncionarioMedico, fk_idDepartamento)
VALUES (123456, 'Cardiologista', 1, 1);

INSERT INTO Enfermeiro (numeroCoren, fk_idFuncionarioEnfermeiro)
VALUES (987654, 2);

INSERT INTO Paciente (nomePaciente, pacienteCPF, dataNascimento)
VALUES ('Carlos Santos', '55555555555', '1990-05-10'),
       ('Ana Paula', '66666666666', '2001-01-20');

INSERT INTO Sala (numeroSala, fk_idDepartamento)
VALUES (101, 1),
       (202, 2);

INSERT INTO Consulta (dataConsulta, fk_idPaciente, fk_idMedico, descricao)
VALUES ('2025-10-20', 1, 1, 'Consulta de rotina');

INSERT INTO Internacao (dataEntrada, dataSaida, fk_idPaciente, fk_idMedico, fk_idSala)
VALUES ('2025-10-18', NULL, 2, 1, 101);

INSERT INTO Atendimento (dataAtendimento, tipo, fk_idPaciente, fk_idEnfermeiro)
VALUES ('2025-10-21 10:30:00', 'Curativo', 1, 2);
