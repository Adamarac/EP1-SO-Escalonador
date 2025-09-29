package Modules;

import Structures.BCP;

public class StatisticsGenerator {
    private static int instrAcc = 0; // acumula a qtd de instruções rodadas do 0 ao fim de todos os programas -> simplificado pois todos os processos iniciam em 0.
    private static int somaTurnaroundTimes = 0;
    private static int somaWaitingTimes = 0;
    private static double mediaTrocas;
    private static double mediaIntrucoes;
    public static int numProcessos = 0;

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

    public static void setMediaTrocas(double m){
        mediaTrocas = m;
    }

    public static void setMediaInstrucoes(double m){
        mediaIntrucoes = m;
    }

    public static double getMediaTrocas(){
        return mediaTrocas;
    }

    public static double getMediaInstrucoes(){
        return mediaIntrucoes;
    }

    public static int getInstrAcc(){
        return instrAcc;
    }

    public static void incrementInstrAcc(){
        instrAcc++;
    }
}
