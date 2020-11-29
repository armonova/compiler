package symbols;

import semantic.IdClass;
import semantic.Type;

public class Id {

    private final int level;
    private Type type;
    private IdClass idClass;

    public Id(int level) {
        this.level = level;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public IdClass getIdClass() {
        return idClass;
    }

    public void setIdClass(IdClass idClass) {
        this.idClass = idClass;
    }

    @Override
    public String toString() {
        return level
                + "#" + hashCode();
    }

    public boolean declared() {
        return getType() != null || getIdClass() != null;
    }

}
