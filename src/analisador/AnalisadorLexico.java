package analisador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class AnalisadorLexico {
    private HashMap<Integer, Lidos> lidosMap;

    public AnalisadorLexico() {
        this.lidosMap = new HashMap<>();
    }

    public void lerSource_code(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 2 && parts[1].equals(";")) {
                    // Declaração de variável
                    String token = "Token_palavraReservada";
                    String lexema = parts[0];
                    lidosMap.put(lineNumber, new Lidos(token, lexema, lineNumber));
                    Lidos proxLido = new Lidos("Token_identificador", parts[1].substring(0, parts[1].length() - 1), lineNumber);
                    lidosMap.put(lineNumber + 1, proxLido);
                } else if (parts.length == 3 && parts[1].equals("=") && parts[2].endsWith(";")) {
                    // Atribuição de variável
                    lidosMap.put(lineNumber, new Lidos("Token_identificador", parts[0], lineNumber));
                    lidosMap.put(lineNumber + 1, new Lidos("Token_operador", "=", lineNumber));
                    lidosMap.put(lineNumber + 2, new Lidos("Token_valor", parts[2].substring(0, parts[2].length() - 1), lineNumber));
                } else {
                    // Verificação de estruturas de controle
                    for (String part : parts) {
                        if (part.equals("if") || part.equals("while") || part.equals("for") || part.equals("do") || part.equals("foreach")) {
                            lidosMap.put(lineNumber, new Lidos("Token_estruturaControle", part, lineNumber));
                        }
                    }
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer, Lidos> getLidosMap() {
        return lidosMap;
    }
}
