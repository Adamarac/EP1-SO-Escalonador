package Structures;

public class BCP {

    private int PC;
    private int X;
    private int Y;
    private State state;

    //Index 0 reservado para o nome do programa + 21 indexes de instrução
    private String[] reference = new String[22];

    public BCP(){
        this.PC=1;
    }

    public void setReference(int index, String instruction){
        reference[index] = instruction;
    }

    public void usePC(){
        System.out.println(this.reference[PC++]);
    }

}

enum State {
    EXECUTANDO,
    PRONTO,
    BLOQUEADO
}