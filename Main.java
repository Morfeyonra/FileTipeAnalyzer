package analyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main (String[] args) throws ExecutionException, InterruptedException {

        if (args.length < 2) {
            System.out.println("Wrong cmdline arguments.");
            return;
        }
        if (args.length == 3) {
            logic(args[0], args[1], args[2]);
        }
        logic(args[0], args[1], "--KMP");
    }


    private static void logic(String fileDir, String patternDBDir, String algorithm)
            throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(10);

        ContextStrategyAlgorithms strategy = new ContextStrategyAlgorithms();

        File parentDir = new File(fileDir);
        File[] arrayOfFiles = parentDir.listFiles();
        if (arrayOfFiles == null) {
            System.out.println("No files found.");
            return;
        }

        List <Pattern> patterns = analiseDB(patternDBDir);
        if (patterns == null) {
            return;
        }

        final List<Future<?>> futures = new ArrayList<>();
        if (algorithm == null) {algorithm = "";}
        switch (algorithm) {
            case "--RK":
                for (File fileToAnalyse : arrayOfFiles) {
                    futures.add(executor.submit(() -> {
                        String file = read(fileToAnalyse.getPath());
                        strategy.setAlgorithm(new RabinKarpAlgorithm());
                        boolean result = false;
                        long startTime = System.nanoTime();
                        for (Pattern i : patterns) {
                            result = strategy.analyze(file, i.getPattern());
                            if (result) {
                                long elapsedNanos = System.nanoTime() - startTime;
                                System.out.println(fileToAnalyse.getName() + ": " + i.getFileName() + "\n" +
                                        "It took " + (double) elapsedNanos / 1_000_000_000L + " seconds");
                                break;
                            }
                        }
                        if (!result) {
                            long elapsedNanos = System.nanoTime() - startTime;
                            System.out.println(fileToAnalyse.getName() + ": " + "Unknown file type"+ "\n" +
                                    "It took " + (double) elapsedNanos / 1_000_000_000L + " seconds");
                        }
                    }));
                }
                break;

            case "--KMP":
                for (File fileToAnalyse : arrayOfFiles) {
                    futures.add(executor.submit(() -> {
                        String file = read(fileToAnalyse.getPath());
                        strategy.setAlgorithm(new KMPAlgorithm());
                        boolean result = false;
                        long startTime = System.nanoTime();
                        for (Pattern i : patterns) {
                            result = strategy.analyze(file, i.getPattern());
                            if (result) {
                                long elapsedNanos = System.nanoTime() - startTime;
                                System.out.println(fileToAnalyse.getName() + ": " + i.getFileName() + "\n" +
                                        "It took " + (double) elapsedNanos / 1_000_000_000L + " seconds");
                                break;
                            }
                        }
                        if (!result) {
                            long elapsedNanos = System.nanoTime() - startTime;
                            System.out.println(fileToAnalyse.getName() + ": " + "Unknown file type"+ "\n" +
                                    "It took " + (double) elapsedNanos / 1_000_000_000L + " seconds");
                        }
                    }));
                }
                break;

            default:
                for (File fileToAnalyse : arrayOfFiles) {
                    futures.add(executor.submit(() -> {
                        String file = read(fileToAnalyse.getPath());
                        strategy.setAlgorithm(new NativeRegexAlgorithm());
                        boolean result = false;
                        long startTime = System.nanoTime();
                        for (Pattern i : patterns) {
                            result = strategy.analyze(file, i.getPattern());
                            if (result) {
                                long elapsedNanos = System.nanoTime() - startTime;
                                System.out.println(fileToAnalyse.getName() + ": " + i.getFileName() + "\n" +
                                        "It took " + (double) elapsedNanos / 1_000_000_000L + " seconds");
                                break;
                            }
                        }
                        if (!result) {
                            long elapsedNanos = System.nanoTime() - startTime;
                            System.out.println(fileToAnalyse.getName() + ": " + "Unknown file type"+ "\n" +
                                    "It took " + (double) elapsedNanos / 1_000_000_000L + " seconds");
                        }
                    }));
                }
                break;
        }

        executor.shutdown();

        for (Future<?> future : futures) {
            future.get();
        }
    }


    private static ArrayList<Pattern> analiseDB(String patternDBDir) {
        File patternDB = new File(patternDBDir);
        String patternDBAsString = read(patternDB.getPath());
        if (patternDBAsString == null) {
            System.out.println("No patterns DB found.");
            return null;
        }
        String[] patternDBAsArray = patternDBAsString.split("[\";\\n]+");
        ArrayList<Pattern> patterns = new ArrayList<>();
        for (int i = patternDBAsArray.length - 3; i >= 0; i -= 3) {
            Pattern pattern = new Pattern(
                    Integer.parseInt(patternDBAsArray[i]),
                    patternDBAsArray[i + 1],
                    patternDBAsArray[i + 2]);
            patterns.add(pattern);
        }
        return patterns;
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