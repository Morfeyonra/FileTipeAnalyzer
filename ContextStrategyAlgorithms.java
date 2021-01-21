package analyzer;

public class ContextStrategyAlgorithms {

    StrategyAlgorithms algorithm;

    public void setAlgorithm(StrategyAlgorithms algorithm) {
        this.algorithm = algorithm;
    }

    public boolean analyze(String file, String pattern) {
        return this.algorithm.analyze(file, pattern);
    }

}
