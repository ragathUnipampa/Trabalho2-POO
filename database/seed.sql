-- Dados ficticios para testes. Todas as senhas sao exclusivamente exemplos.

insert into public.cliente
  (nome, telefone, cnh, categoria_cnh, cep, senha_cliente)
select 'Cliente Exemplo', '(00) 00000-0000', '00000000000', 'B', '00000-000', 'teste123'
where not exists (
  select 1 from public.cliente where cnh = '00000000000'
);

insert into public.gerente
  (nome, email, senha_gerente)
select 'Gerente Exemplo', 'gerente.exemplo@teste.local', 'teste123'
where not exists (
  select 1 from public.gerente where email = 'gerente.exemplo@teste.local'
);

insert into public.veiculo
  (placa, ano, modelo, marca, "statusVeiculo", tipo_veiculo, valor_diario)
select 'TEST-0001', 2024, 'Modelo Exemplo', 'Marca Exemplo',
       'disponivel', 'carro', 150.00
where not exists (
  select 1 from public.veiculo where placa = 'TEST-0001'
);

insert into public.locacao
  (data_inicio, data_fim, dias_alugados, valor_total,
   forma_pagamento, veiculo_placa, cliente_id)
select date '2026-01-10', date '2026-01-12', 2, 300.00,
       'DINHEIRO', 'TEST-0001', cliente.id
from public.cliente cliente
where cliente.cnh = '00000000000'
  and exists (
    select 1 from public.veiculo where placa = 'TEST-0001'
  )
  and not exists (
    select 1
    from public.locacao
    where veiculo_placa = 'TEST-0001'
      and cliente_id = cliente.id
      and data_inicio = date '2026-01-10'
  );

update public.veiculo
set "statusVeiculo" = 'indisponivel'
where placa = 'TEST-0001'
  and exists (
    select 1
    from public.locacao
    where veiculo_placa = 'TEST-0001'
  );
