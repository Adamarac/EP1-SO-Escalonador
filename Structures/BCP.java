package Structures;

// Bloco de Controle de Processo -> contem toda informação para que um processo interrompido possa voltar
public class BCP {
    private String fileName; // nome do arquivo do programa

    private int PC; // Contador de Programa


    private State state = State.PRONTO;    // estado do processo

    // Registradores
    private int X = 0;
    private int Y = 0;

    // Memória de instruções
    private String[] reference = new String[22];   // nome do programa (indice 0) + 21 indexes de instrução

    public BCP(String fileName) {
        this.PC = 1; // começa na instrução 1, pois a 0 é o nome do programa
        this.fileName = fileName;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public String getPID() {
        return this.reference[0];
    }

    public void setReference(int index, String instruction){
        reference[index] = instruction;
    }

    public String getReference(int index) {
        return reference[index];
    }

    public String getFileName() {
        return fileName;
    }

    public State getState() {
        return state;
    }

    public String toString() {
        return this.getPID();
    }

    public void setX(int x) {
        X = x;
    }

    public int getX() {
        return X;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getY() {
        return Y;
    }

    public enum State {
        EXECUTANDO,
        PRONTO,
        BLOQUEADO
    }
}

