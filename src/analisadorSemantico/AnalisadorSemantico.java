package analisadorSemantico;

import analisador.Lidos;
import java.util.HashMap;
import java.util.HashSet;

public class AnalisadorSemantico {

    private HashMap<Integer, Lidos> lidosMap;
    private SymbolTable symbolTable;

    public AnalisadorSemantico(HashMap<Integer, Lidos> lidosMap) {
        this.lidosMap = lidosMap;
        this.symbolTable = new SymbolTable();
    }

    public void verificarDeclaracaoVariaveis() {
        HashSet<String> declaradas = new HashSet<>();
        HashSet<String> usadas = new HashSet<>();

        // Verifica as declarações de variáveis
        for (int i = 1; i <= lidosMap.size(); i++) {
            Lidos lido = lidosMap.get(i);
            if (lido != null && lido.getToken().equals("Token_palavraReservada")) {
                Lidos proxLido = lidosMap.get(i + 1); // Próximo token deve ser o identificador da variável
                if (proxLido != null && proxLido.getToken().equals("Token_identificador")) {
                    if (symbolTable.isDeclared(proxLido.getLexema())) {
                        System.out.println("Erro semântico: Variável '" + proxLido.getLexema() + "' já foi declarada na linha " + proxLido.getLineNumber());
                    } else {
                        symbolTable.add(proxLido.getLexema(), lido.getLexema());
                        declaradas.add(proxLido.getLexema());
                    }
                }
            }
        }

        // Verifica o uso de variáveis
        for (int i = 1; i <= lidosMap.size(); i++) {
            Lidos lido = lidosMap.get(i);
            if (lido != null && lido.getToken().equals("Token_identificador")) {
                if (!symbolTable.isDeclared(lido.getLexema())) {
                    System.out.println("Erro sintático: Variável '" + lido.getLexema() + "' usada mas não declarada.");
                } else {
                    usadas.add(lido.getLexema());
                }
            }
        }
    }

    public void verificarCompatibilidadeTipos() {
        System.out.println("Iniciando verificação de compatibilidade de tipos...");
        for (int i = 1; i <= lidosMap.size(); i++) {
            Lidos lido = lidosMap.get(i);
            if (lido != null && lido.getToken().equals("Token_identificador")) {
                Lidos proxLido = lidosMap.get(i + 1); // Próximo token deve ser um operador
                Lidos valorLido = lidosMap.get(i + 2); // Valor atribuído à variável

                if (proxLido != null && proxLido.getToken().equals("Token_operador")) {
                    String tipoVariavel = symbolTable.get(lido.getLexema());

                    if (tipoVariavel == null) {
                        System.out.println("Erro semântico: Variável '" + lido.getLexema() + "' usada mas não declarada.");
                        continue;
                    }

                    String tipoValor = getTipoValor(valorLido.getLexema());

                    if (!tipoVariavel.equals(tipoValor)) {
                        System.out.println("Erro de compatibilidade de tipos: Variável '" + lido.getLexema() + "' do tipo '" + tipoVariavel + "' recebendo valor do tipo '" + tipoValor + "' na linha " + lido.getLineNumber());
                    }
                }
            }
        }
        System.out.println("Verificação de compatibilidade de tipos concluída.");
    }

    private String getTipoValor(String valor) {
        try {
            Integer.parseInt(valor);
            return "int";
        } catch (NumberFormatException e) {
            try {
                Float.parseFloat(valor);
                return "float";
            } catch (NumberFormatException ex) {
                if (valor.startsWith("\"") && valor.endsWith("\"")) {
                    return "string";
                }
                return "desconhecido";
            }
        }
    }

    public void verificarEstruturasControle() {
        for (int i = 1; i <= lidosMap.size(); i++) {
            Lidos lido = lidosMap.get(i);
            if (lido != null) {
                switch (lido.getLexema()) {
                    case "if":
                        verificarIf(i);
                        break;
                    case "while":
                        verificarWhile(i);
                        break;
                    case "for":
                        verificarFor(i);
                        break;
                    case "do":
                        verificarDoWhile(i);
                        break;
                    case "foreach":
                        verificarForeach(i);
                        break;
                }
            }
        }
    }

    private void verificarIf(int i) {
        Lidos lido = lidosMap.get(i + 1); // Deve ser '('
        if (lido == null || !lido.getLexema().equals("(")) {
            System.out.println("Erro sintático: Estrutura 'if' não está corretamente formada na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        // Verificar fechamento de ')'
        lido = buscarTokenFechamento(i + 1, "(", ")");
        if (lido == null) {
            System.out.println("Erro sintático: Estrutura 'if' não está corretamente fechada na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        // Verificar presença de '{' e '}'
        if (!verificarBlocoCodigo(lido.getLineNumber() + 1)) {
            System.out.println("Erro sintático: Bloco de código do 'if' não está corretamente formado na linha " + lido.getLineNumber());
        }
    }

    private void verificarWhile(int i) {
        Lidos lido = lidosMap.get(i + 1); // Deve ser '('
        if (lido == null || !lido.getLexema().equals("(")) {
            System.out.println("Erro sintático: Estrutura 'while' não está corretamente formada na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        // Verificar fechamento de ')'
        lido = buscarTokenFechamento(i + 1, "(", ")");
        if (lido == null) {
            System.out.println("Erro sintático: Estrutura 'while' não está corretamente fechada na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        // Verificar presença de '{' e '}'
        if (!verificarBlocoCodigo(lido.getLineNumber() + 1)) {
            System.out.println("Erro sintático: Bloco de código do 'while' não está corretamente formado na linha " + lido.getLineNumber());
        }
    }

    private void verificarFor(int i) {
        Lidos lido = lidosMap.get(i + 1); // Deve ser '('
        if (lido == null || !lido.getLexema().equals("(")) {
            System.out.println("Erro sintático: Estrutura 'for' não está corretamente formada na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        // Verificar fechamento de ')'
        lido = buscarTokenFechamento(i + 1, "(", ")");
        if (lido == null) {
            System.out.println("Erro sintático: Estrutura 'for' não está corretamente fechada na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        // Verificar presença de '{' e '}'
        if (!verificarBlocoCodigo(lido.getLineNumber() + 1)) {
            System.out.println("Erro sintático: Bloco de código do 'for' não está corretamente formado na linha " + lido.getLineNumber());
        }
    }

    private void verificarDoWhile(int i) {
        Lidos lido = lidosMap.get(i + 1); // Deve ser '{'
        if (lido == null || !lido.getLexema().equals("{")) {
            System.out.println("Erro sintático: Estrutura 'do-while' não está corretamente formada na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        // Verificar fechamento de '}'
        lido = buscarTokenFechamento(i + 1, "{", "}");
        if (lido == null) {
            System.out.println("Erro sintático: Estrutura 'do-while' não está corretamente fechada na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        // Verificar presença de 'while'
        lido = lidosMap.get(lido.getLineNumber() + 1);
        if (lido == null || !lido.getLexema().equals("while")) {
            System.out.println("Erro sintático: Estrutura 'do-while' não possui 'while' correspondente na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        // Verificar presença de '(' e ')'
        lido = lidosMap.get(lido.getLineNumber() + 1); // Deve ser '('
        if (lido == null || !lido.getLexema().equals("(")) {
            System.out.println("Erro sintático: Estrutura 'do-while' não possui parênteses de condição na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        lido = buscarTokenFechamento(lido.getLineNumber(), "(", ")");
        if (lido == null) {
            System.out.println("Erro sintático: Estrutura 'do-while' não possui parênteses de condição fechados na linha " + lidosMap.get(i).getLineNumber());
        }
    }

    private void verificarForeach(int i) {
        Lidos lido = lidosMap.get(i + 1); // Deve ser '('
        if (lido == null || !lido.getLexema().equals("(")) {
            System.out.println("Erro sintático: Estrutura 'foreach' não está corretamente formada na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        // Verificar fechamento de ')'
        lido = buscarTokenFechamento(i + 1, "(", ")");
        if (lido == null) {
            System.out.println("Erro sintático: Estrutura 'foreach' não está corretamente fechada na linha " + lidosMap.get(i).getLineNumber());
            return;
        }
        // Verificar presença de '{' e '}'
        if (!verificarBlocoCodigo(lido.getLineNumber() + 1)) {
            System.out.println("Erro sintático: Bloco de código do 'foreach' não está corretamente formado na linha " + lido.getLineNumber());
        }
    }

    private Lidos buscarTokenFechamento(int inicio, String abertura, String fechamento) {
        int contador = 0;
        for (int i = inicio; i <= lidosMap.size(); i++) {
            Lidos lido = lidosMap.get(i);
            if (lido == null) {
                continue;
            }
            if (lido.getLexema().equals(abertura)) {
                contador++;
            }
            if (lido.getLexema().equals(fechamento)) {
                contador--;
            }
            if (contador == 0) {
                return lido;
            }
        }
        return null;
    }

    private boolean verificarBlocoCodigo(int inicio) {
        Lidos lido = lidosMap.get(inicio);
        if (lido == null || !lido.getLexema().equals("{")) {
            return false;
        }
        lido = buscarTokenFechamento(inicio, "{", "}");
        return lido != null;
    }

    private String inferirTipo(String valor) {
        if (valor.matches("-?\\d+")) {
            return "int";
        } else if (valor.matches("-?\\d*\\.\\d+")) {
            return "float";
        } else {
            return "string";
        }
    }
}
