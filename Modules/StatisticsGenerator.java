package Modules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import Structures.BCP;

public class StatisticsGenerator {

    private static int instrAcc = 0; // acumula a qtd de instruções rodadas do 0 ao fim de todos os programas -> simplificado pois todos os processos iniciam em 0.
    private static int somaTurnaroundTimes = 0;
    private static int somaWaitingTimes = 0;
    private static int totalTrocas = 0;
    private static double mediaTrocas;
    private static double mediaIntrucoes;
    private static int numProcessos = 0;

    public static float calcularMeanTurnaroundTime(){
        return (float) somaTurnaroundTimes / numProcessos;
    }

    public static float calcularMeanWaitingTime(){
        return (float) somaWaitingTimes / numProcessos;
    }

    public static void acumularTurnaroundTime(BCP bcp){
        somaTurnaroundTimes += bcp.getTurnaroundTime();
    }

    public static void acumularWaitingTime(BCP bcp){
        int turnaround = bcp.getTurnaroundTime();
        somaWaitingTimes += turnaround - bcp.getTempoEmCPU();
    }

    public static void setNumProcessos(int n){
        numProcessos = n;
    }

    public static int getInstrAcc(){
        return instrAcc;
    }

    public static void incrementInstrAcc(){
        instrAcc++;
    }

    public static void setTotalTrocas(int trocas){
        totalTrocas = trocas;
    }

    public static int getTotalTrocas(){
        return totalTrocas;
    }

    public void gerarEstatisticas(Logger logger, int quantum) throws IOException{
        totalTrocas = logger.interruptionscounter.values().stream().mapToInt(Integer::intValue).sum();
        mediaTrocas = (logger.interruptionscounter.values().stream().mapToInt(Integer::intValue).average().orElse(0));
        mediaIntrucoes = (logger.quantumInstructions.values().stream().mapToDouble((a) -> {
            return a.average();
        }).average().orElse(0));

        float MTT = calcularMeanTurnaroundTime();
        float MWT = calcularMeanWaitingTime();
        System.out.println("Mean Turnaround Time: " + MTT + " instrucoes");
        System.out.println("Mean Waiting Time: " + MWT + " instrucoes");
        System.out.println("Total de trocas: " + totalTrocas);
        System.out.println("Media de trocas por processo: " + mediaTrocas);
        System.out.println("Media de instrucoes por quantum: " + mediaIntrucoes);
        
        printTxt(quantum, MTT, MWT);
        printCsv(quantum, MTT, MWT);
    }

    private void printTxt(int quantum, float MTT, float MWT) throws IOException{
        File arquivo1 = new File("statistics.txt");
        boolean vazio1 = !arquivo1.exists() || arquivo1.length() == 0;
        BufferedWriter bw1 = new BufferedWriter(new FileWriter(arquivo1, true));

        if (vazio1) {
            bw1.write(
                "Quantum\t" +
                "Mean Turnaround Time\t" + 
                "Mean Waiting Time\t" + 
                "No total de trocas\t" +
                "Media trocas por processo\t" +
                "Media instr por quantum\t"
            ); 
            bw1.newLine();
        }

        bw1.write(
            quantum + "\t" +  
            String.format(Locale.US, "%.2f", MTT) + "\t" + 
            String.format(Locale.US, "%.2f", MWT) + "\t" +
            totalTrocas + "\t" +
            String.format(Locale.US, "%.2f", mediaTrocas) + "\t" +
            String.format(Locale.US, "%.2f", mediaIntrucoes) + "\t"
        );
        bw1.newLine();
        bw1.close();
    }

    private void printCsv(int quantum, float MTT, float MWT) throws IOException{
        File arquivo2 = new File("statistics.csv");
        boolean vazio2 = !arquivo2.exists() || arquivo2.length() == 0;
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(arquivo2, true));

        if (vazio2) {
            bw2.write(
                "Quantum," +
                "Mean Turnaround Time," + 
                "Mean Waiting Time," + 
                "No total de trocas," +
                "Media trocas por processo," +
                "Media instr por quantum,"
            ); 
            bw2.newLine();
        }

        bw2.write(
            quantum + "," +  
            String.format(Locale.US, "%.2f", MTT) + "," + 
            String.format(Locale.US, "%.2f", MWT) + "," +
            totalTrocas + "," +
            String.format(Locale.US, "%.2f", mediaTrocas) + "," +
            String.format(Locale.US, "%.2f", mediaIntrucoes) + ","
        );
        bw2.newLine();
        bw2.close();
    }
}
