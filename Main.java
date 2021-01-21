package analyzer;

import java.io.*;

public class Main {

    public static void main (String[] args) {

        if (args.length < 4) {
            System.out.println("Wrong cmdline arguments.");
            return;
        }

        logic(args[0], read(args[1]), args[3], args[2]);
    }

    private static void logic(String algorithm, String file, String fileType, String pattern) {

        ContextStrategyAlgorithms strategy = new ContextStrategyAlgorithms();

        switch (algorithm) {

            case "--naive":
                long startTime0 = System.nanoTime();
                strategy.setAlgorithm(new NativeRegexAlgorithm());
                boolean result0 = strategy.analyze(file, pattern);
                long elapsedNanos0 = System.nanoTime() - startTime0;
                if (result0) {
                    System.out.println(fileType);
                } else {
                    System.out.println("Unknown file type");
                }
                System.out.println("It took" + elapsedNanos0 + "seconds");
            break;

            case "--KMP":
                long startTime1 = System.nanoTime();
                strategy.setAlgorithm(new KMPAlgorithm());
                boolean result1 = strategy.analyze(file, pattern);
                long elapsedNanos1 = System.nanoTime() - startTime1;
                if (result1) {
                    System.out.println(fileType);
                } else {
                    System.out.println("Unknown file type");
                }
                System.out.println("It took" + elapsedNanos1 + "seconds");
            break;

            default:
                System.out.println("Wrong cmdline arguments.");
            break;
        }

    }

    private static String read(String inputPathToFile) {
        try (InputStream inputStream = new FileInputStream(inputPathToFile)) {
            byte[] byteArray = inputStream.readAllBytes();
            return new String(byteArray);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}