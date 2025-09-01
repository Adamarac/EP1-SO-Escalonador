package Modules;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.HashMap;

import Structures.ProcessTable;
import Structures.ReadyList;
import Structures.BlockedList;
import Structures.BCP;
import java.util.Collections;
import Instructions.Exceptions.*;

import Modules.Logger;

public class Escalonador {

    // Estruturas de dados do escalonador
    private ProcessTable processTable = new ProcessTable();
    private ReadyList readyList = new ReadyList();
    private BlockedList blockedList = new BlockedList();
    private Logger logger;

    private Interpreter interpreter;

    private int quantumValue;

    public Escalonador(int quantumValue) {
        this.quantumValue = quantumValue;
        try {
            this.logger = new Logger(this);
            this.interpreter = new Interpreter(this);
        } catch (Exception e) {
            System.out.println("Erro ao criar o logger");
            e.printStackTrace();
        }
    }

    public Logger getLogger() {
        return this.logger;
    }

    public int getQuantumValue() {
        return this.quantumValue;
    }

    public static void main(String[] args) {
        try {

            File programsDirectory = new File("programas");
            String quantumFile = "quantum.txt"; // quantum = qntd de instruções max por processo

            HashMap<String, BCP> programs = loadPrograms(programsDirectory, quantumFile);

            File quantum = new File(programsDirectory, quantumFile);
            int quantumValue = readQuantum(quantum); // n_com no enunciado

            Escalonador escalonador = new Escalonador(quantumValue);

            // Adicionar programas na tabela de processos
            escalonador.setupProcessTable(programs);

            // Adicionar na fila de processos prontos em ordem alfabética
            escalonador.transferStartProcessesToReadyList();

            // Inicia o escalonador
            escalonador.start();

            // Imprime o log
            escalonador.logger.printLog();

            escalonador.logger.flush();

        } catch (Exception e) {
            System.out.println("Arquivo não encontrado!");
            e.printStackTrace();
        }
    }

    private void processBlockedList() {
        // avança o relógio da lista de bloqueados e obtém os processos que devem ser
        // movidos para a fila de prontos
        var toReady = blockedList.clock();

        // move os processos para a fila de prontos
        for (BCP bcp : toReady) {
            this.readyList.addProcess(bcp);
        }
    }

    private void start() {
        while (!processTable.isEmpty()) {
            // processa a lista de bloqueados
            this.processBlockedList();

            BCP nextProcess = this.readyList.getNextProcess();

            // se não tiver processo pronto, volta pro início do loop
            if (nextProcess == null) {
                continue;
            }

            // assume que deve voltar pra fila de prontos
            this.scheduleNextProcess = true;

            // prepara o interpretador com o contexto do processo
            this.interpreter.setupContext(nextProcess);

            // cada i é uma instrução rodada
            int i = 0;

            while (i < this.quantumValue) {
                try {
                    this.interpreter.run();
                }
                // interrupções
                catch (EntradaSaidaException entradaSaidaException) {
                    this.interpreter.clearContext();

                    // Instruções no último quantum
                    this.logger.logInterrupt(nextProcess.getPID(), i + 1);

                    this.logger.addInterrupt(nextProcess.getPID());

                    this.logger.addInstructionsUntilInterrupt(nextProcess.getPID(), i + 1);

                    this.scheduleNextProcess = false;
                    this.blockedList.addProcess(nextProcess);
                    break;
                } catch (SaidaException saidaException) {
                    this.interpreter.clearContext();

                    this.logger.logInterrupt(nextProcess.getPID(), i + 1);

                    this.logger.logEndProcess(nextProcess.getPID(), nextProcess.getX(), nextProcess.getY());

                    this.logger.addInterrupt(nextProcess.getPID());

                    this.logger.addInstructionsUntilInterrupt(nextProcess.getPID(), i + 1);

                    this.scheduleNextProcess = false;
                    this.processTable.removeProcess(nextProcess);
                    break;
                } catch (Exception exception) {
                    break;
                }

                i++;

                // se terminou o processo (rodou todas as instruções do quantum)
                if (i == this.quantumValue) {
                    this.logger.logInterrupt(nextProcess.getPID(), i);
                    this.logger.addInterrupt(nextProcess.getPID());
                    this.logger.addInstructionsUntilInterrupt(nextProcess.getPID(), this.quantumValue);
                }
            }

            // registra quantas instruções foram executadas nesse quantum

            // se deve voltar pra fila de prontos
            if (this.scheduleNextProcess)
                this.readyList.addProcess(nextProcess);
        }
    }

    // Carrega os programas do diretório, exceto o arquivo quantum.txt
    private static HashMap<String, BCP> loadPrograms(File programsDirectory, String quantumFile) {
        HashMap<String, BCP> programsList = new HashMap<>();
        if (programsDirectory.exists() && programsDirectory.isDirectory()) {
            File[] programs = programsDirectory.listFiles();

            // verifica se a lista de arquivos não é nula
            if (programs != null) {
                for (File program : programs) {
                    if (program.isFile() && !program.getName().equals(quantumFile)) {
                        BCP bcp = loadProgram(program);
                        if (bcp != null) {
                            programsList.put(program.getName(), bcp);
                        }
                    }
                }
            }
        }
        return programsList;
    }

    // Adiciona os processos na tabela de processos
    private void setupProcessTable(HashMap<String, BCP> programs) {
        for (var entry : programs.values()) {
            BCP bcp = entry;
            this.processTable.addProcess(bcp);
        }
    }

    // Adiciona os processos em ordem alfabética na ready list
    private void transferStartProcessesToReadyList() {
        var orderedProcesses = this.processTable.getProcessList();
        Collections.sort(orderedProcesses, (a, b) -> a.getFileName().compareTo(b.getFileName()));

        for (var entry : orderedProcesses) {
            BCP bcp = entry;
            logger.logLoadProcess(bcp.getPID());
            this.readyList.addProcess(bcp);
        }
    }

    private boolean scheduleNextProcess = true;

    // Cria o BCP e separa as instruç~ioes
    private static BCP loadProgram(File file) {
        // Lê o arquivo e cria o BCP
        try (Scanner reader = new Scanner(file)) {
            // cria o BCP
            BCP program = new BCP(file.getName());

            // lê as instruções e adiciona no BCP
            int referenceIndex = 0;
            while (reader.hasNextLine()) {
                program.setReference(referenceIndex, reader.nextLine());
                referenceIndex++;
            }

            return program;
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao ler o arquivo: " + file.getName());
            e.printStackTrace();
            return null;
        }
    }

    // Lê o arquivo com o quantum
    private static int readQuantum(File quantumFile) {
        try (Scanner reader = new Scanner(quantumFile)) {
            if (reader.hasNextInt()) {
                return reader.nextInt();
            } else {
                throw new IllegalArgumentException("quantum.txt não contém um número válido!");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Arquivo quantum.txt não encontrado!", e);
        }
    }
}
