package Modules;

import Structures.BCP;

public class StatisticsGenerator {
    private static long somaTurnaroundTimes = 0;
    private static long somaWaitingTimes = 0;
    private static double mediaTrocas;
    private static double mediaIntrucoes;
    public static int numProcessos = 0;

    public static float calcularMeanTurnaroundTime(){
        return (float) somaTurnaroundTimes / numProcessos;
    }

    public static float calcularMeanWaitingTime(){
        return (float) somaWaitingTimes / numProcessos;
    }

    public static void calcularTurnAroundTime(BCP bcp){
        long turnaround = bcp.getCompletionTime() - bcp.getArrivalTime();
        somaTurnaroundTimes += turnaround;
    }

    public static void calcularWaitingTime(BCP bcp){
        long turnaround = bcp.getCompletionTime() - bcp.getArrivalTime();
        somaWaitingTimes += turnaround - bcp.getCPUBurstTime();
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
}
