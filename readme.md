# Sistema de Arquivos com Tabela Hash

### Opções
- [1 - Criar Tabela](#1---criar-tabela) <!---noqa-->
- [2 - Exibir Tabela](#2---exibir-tabela)
- [3 - Inserir registro](#3---inserir-registro)
- [4 - Consultar tabela](#4---consultar-tabela)
  - [4.1 - Consultar estrutura da tabela](#41---consultar-estrutura-da-tabela)
  - [4.2 - Exibir todos os registros da tabela](#42---exibir-todos-os-registros-da-tabela)
  - [4.3 - Selecionar registros](#43---selecionar-registros)
    - [4.3.1 - Consultar registro por chave](#431---consultar-registro-por-chave)
    - [4.3.2 - Consultar por outros atributos](#432---consultar-por-outros-atributos)
- [5 - Excluir registros](#5---excluir-registros)
  - [5.1 - Excluir tabela](#51---excluir-tabela)
  - [5.2 - Selecionar registros a serem excluídos](#52---selecionar-registros-a-serem-excluídos)
  - [5.3 - Excluir todos os registros](#53---excluir-todos-os-registros)
- [6 - Atualizar registros](#6---atualizar-registros)
- [7 - Sair](#7---sair)

## Definição das opções do menu:

#### 1 - Criar Tabela
Esta opção, quando selecionada, permite ao usuário criar uma tabela. São perguntados ao usuário o
nome da tabela, o número de atributos, o nome e o tipo de cada atributo e a chave da tabela. Os
tipos suportados pelo programa são: inteiro, double e string.

#### 2 - Exibir Tabela
Esta opção exibe o nome de todas as tabelas cadastradas no sistema

#### 3 - Inserir registro
Esta opção permite que o usuário insira registros em uma determinada tabela. São checados se os
tipos dos valores que o usuário fornece são compatíveis com os tipos dos atributos da tabela e se o
usuário está colocando caracteres especiais em um atributo de tipo string, pois, se isso acontecer, o
registro ficará com uma quantidade de bytes diferente da prevista na criação da tabela.

#### 4 - Consultar tabela
Esta opção permite que o usuário faça consultas a tabela.

#### 4.1 - Consultar estrutura da tabela
Esta opção retorna informações de uma tabela informada pelo usuário, tais como o nome de tabela,
os nomes dos atributos e os tipos dos atributos.

#### 4.2 - Exibir todos os registros da tabela
Esta opção retorna todos os registros cadastrados em uma tabela do sistema, informada pelo usuário.

#### 4.3 - Selecionar registros
Esta opção permite ao usuário selecionar um ou vários registros de uma tabela e exibi-los na saída

#### 4.3.1 - Consultar registro por chave 
Esta opção realiza uma consulta ao sistema por meio de uma chave, fornecida pelo usuário, onde,
ao final do processo, é exibido o registro que tem exatamente aquela chave.

#### 4.3.2 - Consultar por outros atributos
Esta opção realiza uma consulta ao sistema, onde são exibidos os registros que tem as
características que o usuário deseja. (Ex.: Exibir registros, cujo o atributo Salário é maior a 3000).

#### 5 - Excluir registros
Esta opção permite que o usuário elimine registros de uma determinada tabela ou a própria tabela. 

#### 5.1 - Excluir tabela
Esta opção exclui uma tabela, informada pelo usuário, do sistema

#### 5.2 - Selecionar registros a serem excluídos
Esta opção permite que o usuário selecione registros. Os critérios de exclusão são especificados
pelo usuário e funcionam de forma similar a seleção dos registros, ou seja, podem ser excluídos
registros pela chave (opção 5.2.1) ou por meio de outros atributos (opção 5.2.2).

#### 5.3 - Excluir todos os registros
Esta opção permite que o usuário exclua todos os registros de uma tabela, mantendo somente sua
estrutura no sistema, como se ela estivesse recém criada.

#### 6 - Atualizar registros
Esta opção permite que o usuário modifique os valores dos registros de uma tabela. Os critérios de
atualização são especificados pelo usuário e funcionam de forma similar a seleção dos registros, ou
seja, podem ser atualizados registros pela chave (opção 6.1) ou por meio de outros atributos (opção
6.2).

#### 7 - Sair
Esta opção encerra a execução do programa