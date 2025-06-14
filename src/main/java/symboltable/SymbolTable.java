package symboltable;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    public enum SymbolType {
        VARIABLE,
        FUNCTION,
        PARAMETER
    }

    public static class Symbol {
        private final String name;
        private final String dataType;
        private final SymbolType kind;

        public Symbol(String name, String dataType, SymbolType kind) {
            this.name = name;
            this.dataType = dataType;
            this.kind = kind;
        }

        public String getName() {
            return name;
        }

        public String getDataType() {
            return dataType;
        }

        public SymbolType getKind() {
            return kind;
        }
    }

    private final Map<String, Symbol> symbols = new HashMap<>();

    public void declare(String name, String dataType, SymbolType kind) {
        if (symbols.containsKey(name)) {
            throw new RuntimeException("Identificador já declarado: " + name);
        }
        symbols.put(name, new Symbol(name, dataType, kind));
    }

    public Symbol lookup(String name) {
        if (!symbols.containsKey(name)) {
            throw new RuntimeException("Identificador não declarado: " + name);
        }
        return symbols.get(name);
    }

    public boolean exists(String name) {
        return symbols.containsKey(name);
    }
    public void printTable() {
        System.out.println("\nTabela de Símbolos:");
        for (Symbol sym : getAllSymbols()) {
            System.out.printf("%s : %s (%s)%n", sym.getName(), sym.getDataType(), sym.getKind());
        }
    }

    public void printTable(PrintWriter out) {
        out.println("Tabela de Símbolos:");
        for (Symbol sym : getAllSymbols()) {
            out.printf("%s : %s (%s)%n", sym.getName(), sym.getDataType(), sym.getKind());
        }
    }

    public Collection<Symbol> getAllSymbols() {
        return symbols.values();
    }
}
