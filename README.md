# Simulador de Elevadores

Este projeto é uma simulação de um sistema de elevadores com múltiplos andares e passageiros, onde os usuários podem adicionar passageiros, solicitar um elevador e ver o status de movimento dos elevadores em tempo real. O sistema permite o gerenciamento de múltiplos elevadores, escolha do melhor elevador para um passageiro com base na distância e movimentação dos elevadores de forma interativa.

## Objetivo

O objetivo principal deste projeto é simular o comportamento de elevadores em um prédio. O sistema deve ser capaz de:
- Adicionar passageiros com informações de origem e destino.
- Simular a movimentação dos elevadores de acordo com a solicitação dos passageiros.
- Exibir a movimentação dos elevadores em tempo real.
- Permitir que o usuário visualize os passageiros no elevador e sua movimentação.

## Tecnologias Utilizadas

- **Java 17**: Linguagem de programação principal para a lógica do sistema.
- **JavaFX**: Utilizado para a criação da interface gráfica, com visualização do status dos elevadores e interação com o usuário.
- **Threads**: Para simulação de movimento do elevador de forma assíncrona.
- **Locks**: Para controle de concorrência no acesso a dados compartilhados entre os elevadores e os passageiros.
- **Coleções Java (Queue, LinkedList)**: Para gerenciar listas de passageiros no elevador e nas áreas de espera.

## Funcionalidades

- **Simulação de Elevadores**: O sistema simula a movimentação de elevadores entre andares.
- **Solicitação de Elevador**: Passageiros podem solicitar elevadores e indicar o andar de origem e destino.
- **Visualização em Tempo Real**: A interface gráfica exibe o status dos elevadores e a lista de passageiros.
- **Seleção Automática do Elevador**: O sistema escolhe o elevador mais próximo do passageiro com base na sua localização.
## Estrutura do Projeto
- Classe Elevador: Responsável pela lógica de movimentação do elevador, controle de passageiros e destino.
- Classe Passageiro: Representa um passageiro com nome, andar de origem e destino.
- Classe ElevadorApp: Responsável pela criação da interface gráfica usando JavaFX, gerenciamento de elevadores e passageiros.
- Classe JavaFXLauncher: Classe com o método main, responsável por iniciar o projeto.

## Como Rodar o Projeto

1. **Requisitos**:
    - JDK 17
    - IDE como IntelliJ IDEA, Eclipse ou NetBeans (ou terminal).

2. **Passos para Executar**:
    - Tenha o Java 17 instalado em sua máquina.
    - Clone este repositório para sua máquina local.
    - Abra o projeto na sua IDE preferida.
    - Compile e execute o projeto. A interface gráfica será exibida.
    - Ao iniciar o simulador, você pode definir o número de elevadores e adicionar passageiros para simular o funcionamento.

3. **Funcionalidades na Interface**:
    - **Adicionar Passageiro**: Preencha o nome, andar de origem e destino do passageiro.
    - **Status dos Elevadores**: A cada movimento, o status de cada elevador é atualizado, mostrando seu andar atual.

## Exemplo de Uso

1. Inicie o simulador e escolha o número de elevadores que deseja simular.
2. Adicione passageiros, informando o nome e os andares de origem e destino.
3. O sistema calculará automaticamente qual elevador é mais próximo de cada passageiro e os moverá para o destino.
4. O status de cada elevador será exibido na interface, permitindo o acompanhamento da movimentação.
5. Também é permitido que o passageiro desça do elevador a qualquer momento da simulação.
