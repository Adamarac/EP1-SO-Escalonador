import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

import Structures.*;

public class Escalonador {

    private processTable processTable = new processTable();
    private readyList readyList = new readyList();
    private blockedList blockedList = new blockedList();

    public static void main(String[] args) {
        try {

            File programsDirectory = new File("programas");
            String quantumFile = "quantum.txt";

            if(programsDirectory.exists() && programsDirectory.isDirectory()){
                File[] programs = programsDirectory.listFiles();

                if (programs != null) {
                    for(File program : programs){
                        if(program.isFile() && !program.getName().equals(quantumFile)){
                            loadProgram(program);
                        }
                    }
                }
            }

            File quantum = new File(programsDirectory, quantumFile);
            int quantumValue = readQuantum(quantum);

        } catch (Exception e) {
            System.out.println("Arquivo não encontrado!");
            e.printStackTrace();
        }
    }

    private static void loadProgram(File file){
        try (Scanner reader = new Scanner(file)) {

            BCP program = new BCP();

            int referenceIndex = 0;
            while (reader.hasNextLine()) {
                program.setReference(referenceIndex,reader.nextLine());
                referenceIndex++;
            }

        } catch (FileNotFoundException e) {
            System.err.println("Erro ao ler o arquivo: " + file.getName());
            e.printStackTrace();
        }
    }

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
