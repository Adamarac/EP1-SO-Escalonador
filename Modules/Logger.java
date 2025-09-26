package Modules;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    HashMap<String, Integer> interruptionscounter = new HashMap<>();
    HashMap<String, QuantumInstructions> quantumInstructions = new HashMap<>();

    private BufferedWriter bw; 

    public Logger(Escalonador escalonador) throws IOException {
        bw = new BufferedWriter(
            new FileWriter(
                "log" + String.format("%02d", escalonador.getQuantumValue()) + ".txt"
            )
        );
    }
    
    // classe para armazenar quantas instruções foram executadas por quantum por processo
    class QuantumInstructions {
        public ArrayList<Integer> instructionsList = new ArrayList<>();

        public void addInstructions(int instructions) {
            instructionsList.add(instructions);
        }

        // média de instruções por quantum
        public double average() {
            return instructionsList.stream().mapToInt(Integer::intValue).average().orElse(0);
        }
    }

    public void addInterrupt(String processId) {
        interruptionscounter.put(processId, interruptionscounter.getOrDefault(processId, 0) + 1);
    }

    public void addInstructionsUntilInterrupt(String processId, int instructions) {
        var q = quantumInstructions.getOrDefault(processId, new QuantumInstructions());
        q.addInstructions(instructions);
        quantumInstructions.put(processId, q);
    }

    public void printLog() {
        this.print("MÉDIA DE TROCAS: ");
        double media = (interruptionscounter.values().stream().mapToInt(Integer::intValue).average().orElse(0));
        this.print(media + "");
        StatisticsGenerator.setMediaTrocas(media);
        this.print("\nMÉDIA DE INSTRUÇÕES: ");

        // soma das médias de instruções por quantum de cada processo dividido pelo número de processos
        media = (quantumInstructions.values().stream().mapToDouble((a) -> {
            return a.average();
        }).average().orElse(0));

        StatisticsGenerator.setMediaInstrucoes(media);
        this.print(media + "");
    }

    public void logInterrupt(String processId, int instructions) {
        this.println("Interrompendo " + processId + " após " +  instructions + " instruções");
    }

    public void logEndProcess(String processId, int x, int y) {
        this.println(processId + " terminado. X=" + x + ". Y=" + y);
    }

    public void logLoadProcess(String processId) {
        this.println("Carregando " + processId);
    }

    public void logRunningProcess(String processId) {
        this.println("Executando " + processId);
    }

    public void logES(String pid) {
        this.println("E/S iniciada em " + pid);
    }

    private void println(String msg) {
        try {
            this.bw.write(msg + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() throws IOException {
        this.bw.flush();
    }

    private void print(String msg) {
        try {
            this.bw.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   

    public void close() throws IOException {
        this.bw.close();
    }
}
