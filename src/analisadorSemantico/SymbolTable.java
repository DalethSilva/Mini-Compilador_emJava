package analisadorSemantico;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, String> table;

    public SymbolTable() {
        table = new HashMap<>();
    }

    public void add(String identifier, String type) {
        table.put(identifier, type);
    }

    public boolean isDeclared(String identifier) {
        return table.containsKey(identifier);
    }

    public String getType(String identifier) {
        return table.get(identifier);
    }

    public String get(String identifier) {
        return table.get(identifier);
    }

 

}
