package Structures;
import java.util.Queue; 
import java.util.LinkedList; 

public class ReadyList {
    private Queue<BCP> list = new LinkedList<>();

    // Adiciona um processo à lista de prontos
    public void addProcess(BCP bcp) {
        list.add(bcp);
        bcp.setState(BCP.State.PRONTO);
    }

    // Remove um processo da lista de prontos
    public void removeProcess(BCP bcp) {
        list.remove(bcp);
    }

    public void getList() {
        System.out.println(list);
    }

    public BCP getNextProcess() {
        return list.poll();
    }
}
