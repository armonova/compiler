package symbols;

import semantic.Type;

public class Id {

    private final int level;
    private Type type;

    public Id(int level) {
        this.level = level;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return level
                + "#" + hashCode();
    }

    public boolean typeIsNull() {
        return type == null;
    }
}
