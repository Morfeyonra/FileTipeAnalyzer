package analyzer;

public class Pattern {

    private final int priority;
    private final String pattern;
    private final String fileName;

    public int getPriority() {
        return priority;
    }

    public String getPattern() {
        return pattern;
    }

    public String getFileName() {
        return fileName;
    }

    public Pattern(int priority, String pattern, String fileName) {
        this.priority = priority;
        this.pattern = pattern;
        this.fileName = fileName;
    }
}
