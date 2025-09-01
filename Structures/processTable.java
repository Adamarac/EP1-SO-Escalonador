package Structures;

import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

public class ProcessTable {

    private HashMap<String, BCP> processList;

    public ProcessTable() {
        processList = new HashMap<>();
    }

    public void addProcess(BCP bcp) {
        processList.put(bcp.getPID(), bcp);
       // System.out.println("Processo " + bcp.getPID() + " adicionado à tabela de processos.");
    }

    public ArrayList<BCP> getProcessList() {
        return new ArrayList<BCP>(processList.values());
    }

    public void removeProcess(BCP bcp) {
        processList.remove(bcp.getPID());
         //System.out.println("Processo " + bcp.getPID() + " removido da tabela de processos.");
    }

    public boolean isEmpty() {
        return processList.isEmpty();
    }
}
