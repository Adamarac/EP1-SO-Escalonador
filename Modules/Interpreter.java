package Modules;

import Structures.BCP;
import Instructions.*;
import Instructions.Exceptions.SaidaException;

/*
    Se fizer E/S durante uma quantum, bloqueado -> lista de bloqueados
    Na lista de bloqueados, quantos quantums até voltar a rodar

    Cada processo fica bloqueado até que dois processos sejam executados

    Tempo de espera do processo bloqueado for zero, volta para a fila de prontos no final
    Processo bloqueado por E/S quando volta -> instrução + 1
*/

public class Interpreter {
    private Escalonador escalonador;

    public Interpreter(Escalonador escalonador) {
        this.escalonador = escalonador;
    }

    public Logger getLogger() {
        return this.escalonador.getLogger();
    }

    private int PC = 0;

    private int X = 0, Y = 0;

    private BCP runningProcess;

    public void setupContext(BCP bcp) {
        bcp.setState(BCP.State.EXECUTANDO);

        this.getLogger().logRunningProcess(bcp.getPID());

        this.saveContext();
        this.restoreContext(bcp);
    }

    public BCP getRunningProcess() {
        return this.runningProcess;
    }

    // Executa uma instrução
    public void run() throws Exception {
        if (this.runningProcess == null) {
            return;
        }

        String currentInstruction = this.runningProcess.getReference(this.PC);
        Instruction instruction = this.formatInstruction(currentInstruction);

        instruction.run(this);

        this.incrementPC();
    }

    public int getPC() {
        return this.PC;
    }

    public void incrementPC() {
        this.PC++;
    }

    public int getX() {
        return this.X;
    }

    public int getY() {
        return this.Y;
    }

    public void setX(int value) {
        this.X = value;
    }

    public void setY(int value) {
        this.Y = value;
    }

    private Instruction formatInstruction(String instruction) {
        String[] setSplit = instruction.split("=");
        if (setSplit.length == 2) {
            // É uma instrução SET
            return new SET(setSplit[0], Integer.parseInt(setSplit[1]));
        } else {
            switch (setSplit[0]) {
                case "SAIDA":
                    return new SAIDA();
                case "E/S":
                    return new ES();
                case "COM":
                    return new COM();
                default:
                    return new Instruction();
            }
        }
    }

    public void clearContext() {
        this.saveContext();
        
        this.runningProcess = null;
        this.PC = 0;
        this.X = 0;
        this.Y = 0; 
    }

    // Salva o contexto de um processo
    private void saveContext() {
        if (this.runningProcess != null) {
            this.runningProcess.setPC(this.PC);
            this.runningProcess.setX(this.X);
            this.runningProcess.setY(this.Y);
        }
    }

    // Restaura o contexto de um processo
    private void restoreContext(BCP bcp) {
        this.runningProcess = bcp;
        this.PC = bcp.getPC();
        this.X = bcp.getX();
        this.Y = bcp.getY();
    }
}