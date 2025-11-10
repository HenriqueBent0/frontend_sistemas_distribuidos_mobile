# Sistema de Controle de Estoque Distribuído

Este projeto tem como objetivo projetar e desenvolver um sistema de controle de estoque utilizando arquitetura distribuída em duas camadas — uma camada back-end (serviço) e uma camada front-end (cliente).
O sistema tem como objetivo auxiliar empresas comerciais no gerenciamento eficiente de seus produtos, controlando entradas e saídas de estoque, reajustes de preços, classificação por categorias, e emissão de relatórios automáticos sobre o inventário.

## Link para o parte Back-end

https://github.com/HenriqueBent0/backend_sistemas_distribuidos_mobile

## Requisitos Funcionais

**[RF001] Cadastro de Produtos:**
Permite incluir, alterar, consultar e excluir produtos do estoque, com informações detalhadas como nome, preço, unidade, quantidades mínima/máxima e categoria.

**[RF002] Cadastro de Categorias:**
Cada produto pertence a uma categoria com atributos de nome, tamanho (Pequeno, Médio, Grande) e tipo de embalagem (Lata, Vidro, Plástico).

**[RF003] Movimentações de Estoque:**
Registra entradas e saídas de produtos, atualizando automaticamente as quantidades disponíveis e emitindo alertas quando o estoque ultrapassa limites mínimos ou máximos.

**[RF004] Reajuste de Preços:**
Permite reajustar os preços de todos os produtos de uma só vez, aplicando um percentual definido pelo usuário.

**[RF005] Relatórios:**

 - **[RN1] Lista de Preços:** relação completa dos produtos com nome, categoria, unidade e preço.

 - **[RN2] Balanço Físico/Financeiro:** quantidade total em estoque e valor financeiro do inventário.

 - **[RN3] Produtos Abaixo do Mínimo:** relatório de produtos com estoque crítico.

 - **[RN4] Quantidade por Categoria:** resumo de produtos distintos por categoria.

 - **[RN5] Produtos com Maior Movimentação:** identifica o produto com mais entradas e saídas no período.

## Requisitos Não Funcionais

**[RNF001] Acesso:** O Sistema deve estar disponível para acesso do administrador sempre que necessário.

**[RNF002] Desempenho:** O Sistema deve ser otimizado para gerar respostas rápidas, minimizando o tempo de carregamento do software.

**[RNF003] Confiabilidade:** O Sistema deve ser capaz de lidar com falhas no software e no hardware de forma resiliente de forma que sempre garanta a disponibilidade do programa. 

**[RNF004] Dados:** O Sistema deve ser capaz de armazenar todos os dados cadastrados pelo administrador.

**[RNF005] Segurança:** O Sistema deve pedir uma senha para que o programa seja acessível. E deve ser capaz de identificar e validar a senha caso esteja correta.


## Arquitetura do Sistema

O sistema é dividido em duas camadas distribuídas:

- Back-end (Servidor):
Responsável pelo processamento da lógica de negócios, manipulação do banco de dados e fornecimento dos serviços.

- Front-end (Cliente):
Responsável por enviar requisições ao servidor, exibir resultados e permitir a interação com o usuário.

## Alunos

- Henrique Bento (HenriqueBent0) - 1072417437
- Jonathan Oliveira Lynch (JonnyLynch8) - 10724265639
- Ian Akira Fujimori Gaspar (IanAkira) - 1072417788
- Pedro Henrique Perez Kruk (pedrokrukz) - 10724112624
