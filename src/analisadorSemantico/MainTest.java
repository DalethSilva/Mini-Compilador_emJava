package analisadorSemantico;

import analisador.AnalisadorLexico;
import java.nio.file.Paths;

public class MainTest {
    public static void main(String[] args) {
        
        AnalisadorLexico lexico = new AnalisadorLexico();
        lexico.lerSource_code("src/fonte/source_code.txt");  

        AnalisadorSemantico semantico = new AnalisadorSemantico(lexico.getLidosMap());
        semantico.verificarDeclaracaoVariaveis();
        semantico.verificarCompatibilidadeTipos();
        semantico.verificarEstruturasControle();
        
        
        // Caminho do arquivo de código-fonte
        String filePath = Paths.get("src", "fonte", "source_code.txt").toString(); 

        // Passo 1: Análise Léxica
        AnalisadorLexico analisador = new AnalisadorLexico();
        analisador.lerSource_code(filePath); // Isso deve preencher lidosMap com tokens

        // Passo 2: Análise Sintática
        Sintatico.iniciarAnalise(analisador.getLidosMap());

        // Passo 3: Análise Semântica
        AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico(analisador.getLidosMap());
        analisadorSemantico.verificarDeclaracaoVariaveis();
    }
}
