package lexer;

public class Id {

    private final int level;

    public Id(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return level
                + "#" + hashCode();
    }

}
