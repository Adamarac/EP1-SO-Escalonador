# EP1 - Simulador de Escalonador de Processos  

## O que é o projeto  
Esse projeto é um **simulador de escalonamento de processos** feito para a disciplina de Sistemas Operacionais.  
Ele mostra como funciona o algoritmo **Round Robin**, com quantum configurável, simulando a execução de processos virtuais e suas mudanças de estado.  

É basicamente uma forma de enxergar, na prática, como o sistema operacional decide **quem usa a CPU, por quanto tempo e o que acontece quando um processo precisa de E/S ou termina**.  

---

## Estrutura do projeto  

Instructions/
Modules/
Structures/
programas/


**Escalonador** → controla quem roda, quando e por quanto tempo  
**Interpretador** → executa as instruções (SET, COM, E/S, SAIDA)  
**BCP** → guarda o contexto de cada processo (PC, registradores, estado etc.)  
**Logger** → registra tudo para análise depois  

---

## Como rodar  


### Compilar  
```bash
javac -cp . Modules/*.java Structures/*.java Instructions/*.java Instructions/Exceptions/*.java
```

### Executar  
```bash
java Modules.Escalonador
```

### Configurar quantum  
Edite o arquivo `programas/quantum.txt` e coloque o valor que quiser, por exemplo:  
```
3
```
Nesse caso, cada processo executa no máximo 3 instruções por vez.  

---

## Como escrever programas de teste  

Na pasta `programas/` já tem vários exemplos (`01.txt` a `11.txt`). O formato é:  

```
NOME-DO-PROCESSO
X=10
COM
COM
E/S
Y=5
COM
SAIDA
```

### Instruções disponíveis  
- `X=valor` ou `Y=valor` → atribui valor em registradores  
- `COM` → instrução computacional (só gasta tempo)  
- `E/S` → entrada/saída (bloqueia o processo por 2 quantums)  
- `SAIDA` → finaliza o processo  

---

## Como funciona

1. Os processos entram na fila de prontos em ordem alfabética  
2. Cada um executa até acabar o quantum ou sofrer uma interrupção  
3. Interrupções possíveis:  
   - **E/S** → vai pra lista de bloqueados por 2 quantums  
   - **Fim do quantum** → volta pro final da fila de prontos  
   - **SAIDA** → finaliza  
4. Quando acaba o bloqueio, o processo volta pra fila de prontos  

---

## Saída (logs)  

O sistema gera um arquivo de log (`log0X.txt`, onde X é o quantum usado).  

Exemplo:  
```
Carregando TESTE-1
Executando TESTE-1
E/S iniciada em TESTE-1
Interrompendo TESTE-1 após 2 instruções
Executando TESTE-2
...
MÉDIA DE TROCAS: 4.5
MÉDIA DE INSTRUÇÕES: 2.8 por quantum
TESTE-1 terminado. X=2. Y=10
``` 

---

## 👥 Sobre  

Projeto desenvolvido por Alan Moura, Arthur Hernandes, Felipe Ferreira, Gabriel Luis e Isabella Morija como parte da disciplina de **Sistemas Operacionais (EACH-USP, 4º semestre)** para entender, de forma prática, como funciona o módulo de escalonamento de um SO.  
