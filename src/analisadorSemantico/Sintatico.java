package analisadorSemantico;

import analisador.Lidos;
import java.util.HashMap;
import java.util.HashSet;

public class Sintatico {
    public static void iniciarAnalise(HashMap<Integer, Lidos> lidosMap) {
        HashSet<String> declaradas = new HashSet<>();
        HashSet<String> usadas = new HashSet<>();

        for (Lidos lido : lidosMap.values()) {
            // Verifica declarações de variáveis
            if (lido.getToken().equals("Token_palavraReservada") && lido.getLexema().matches("(int|float|string|double|char|boolean)")) {
                Lidos proxLido = lidosMap.get(lido.getLinha() + 1); // Assume que a próxima linha tem a variável
                if (proxLido != null && proxLido.getToken().equals("Token_identificador")) {
                    declaradas.add(proxLido.getLexema());
                }
            }
            // Verifica usos de variáveis
            if (lido.getToken().equals("Token_identificador")) {
                usadas.add(lido.getLexema());
            }
        }

        // Verifica se todas as variáveis usadas foram declaradas
        for (String usada : usadas) {
            if (!declaradas.contains(usada)) {
                System.out.println("Erro sintático: Variável '" + usada + "' usada mas não declarada.");
            }
        }

        // Verifica se todas as variáveis declaradas foram usadas
        for (String declarada : declaradas) {
            if (!usadas.contains(declarada)) {
                System.out.println("Aviso: Variável '" + declarada + "' declarada mas não usada.");
            }
        }
    }
}
