package analyzer;

public class NativeRegexAlgorithm implements StrategyAlgorithms {
    @Override
    public boolean analyze(String file, String pattern) {
        String regex = ".*?" + pattern + ".*?";
        return file.matches(regex);
    }
}
