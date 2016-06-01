/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquina_universal;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class Maquina_Universal {

    private Vector<String[]> transitions = null;

    public Maquina_Universal() {
        this.transitions = new Vector<String[]>();
    }

    public void addTransition(String newTransition) {
        String[] transition = newTransition.split(",");
        this.transitions.add(transition);
    }

    public void process(String[] input) {
        boolean result = false;
        String log = "";
        String current_state = "1";
        int index = 1;
        String[] tape = new String[input.length + 2];
        tape[0] = tape[tape.length - 1] = "1";
        for (int i = 1; i < (tape.length - 1); i++) {
            tape[i] = input[i - 1] + "";
        }
        while (!current_state.equals("11") || !tape[index].equals("1")) {
            log += ("State: " + current_state + "\tSymbol: " + tape[index] + "\tIndex: " + index + "\n");
            int i;
            for (i = 0; i < this.transitions.size(); i++) {
                if (this.transitions.get(i)[0].equals(current_state)) {
                    if (this.transitions.get(i)[1].equals(tape[index])) {
                        current_state = this.transitions.get(i)[2];
                        tape[index] = this.transitions.get(i)[3];
                        if (this.transitions.get(i)[4].equals("1")) {
                            index += 1;
                        } else {
                            if (this.transitions.get(i)[4].equals("11")) {
                                index -= 1;
                            }
                        }
                        break;
                    }
                }
            }
            if (i == this.transitions.size()) {
                break;
            }
        }
        log += ("State: " + current_state + "\tSymbol: " + tape[index] + "\tIndex: " + index + "\n");
        if (current_state.equals("11") && tape[index].equals("1")) {
            result = true;
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        try {
            File file = new File("Resultados.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter fileWriter = new BufferedWriter(fw);
            bw.write(log);
            if (result) {
                bw.write("\nComputo terminado");
                fileWriter.write("\nComputo terminado");
            } else {
                bw.write("\nComputo rechazado");
                fileWriter.write("\nComputo rechazado");
            }
            bw.flush();
            fileWriter.newLine();
            fileWriter.flush();
            bw.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String file_transitions = "";
        String w = "";
        String universal_machine_binary = "";
        String universal_machine = "";
        StringBuilder sb = new StringBuilder();
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        try {
            universal_machine_binary = console.readLine();
            for (int i = 1; i < universal_machine_binary.length(); i++) {
                char c = universal_machine_binary.charAt(i);
                if (c == '0') {
                    sb.append(",");
                } else {
                    sb.append("1");
                }
                universal_machine = sb.toString();
            }
            String[] universal_machine_splitted = universal_machine.split(",,,");
            file_transitions = universal_machine_splitted[0];
            w = universal_machine_splitted[1];
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Maquina_Universal M = new Maquina_Universal();
        String lines[] = file_transitions.split(",,");
        for (int i = 0; i < lines.length; i++) {
            M.addTransition(lines[i]);
        }
        String symbols[] = w.split(",");
        M.process(symbols);
    }
}
