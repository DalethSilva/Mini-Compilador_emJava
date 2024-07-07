package analisador;

public class Lidos {
    private String token;
    private String lexema;
    private int linha;
    private int lineNumber;

    public Lidos(String token, String lexema, int lineNumber) {
        this.token = token;
        this.lexema = lexema;
        this.lineNumber = lineNumber;
    }

    public String getToken() {
        return token;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getLinha() {
        return linha;
    }

    
    public void setToken(String token) {
        this.token = token;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }
}
