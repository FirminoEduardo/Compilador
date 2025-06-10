package symboltable;

public class Symbol {
    private final String name;
    private final String type;
    private final String category; // variable, function, parameter

    public Symbol(String name, String type, String category) {
        this.name = name;
        this.type = type;
        this.category = category;
    }

    public String getName() { return name;}
    public String getType() { return type;}
    public String getCategory() { return category;}

    @Override
    public String toString() {
        return String.format("Symbol(name=%s, type=%s, category=%s)", name, type, category);
    }
}
