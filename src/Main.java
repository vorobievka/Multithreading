import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        final ExecutorService threadPool = Executors.newFixedThreadPool(8);
        List<Future> future = new ArrayList<Future>();

        long startTs = System.currentTimeMillis(); // start time
        for (String text : texts) {
            Callable task = () -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }

                  return maxSize;
            };
              final Future<String> job = threadPool.submit(task);
              future.add(job);

        }

         List<Integer> results = new ArrayList<Integer>();

         for (Future futuring : future) {
             results.add((Integer) futuring.get());
         }
        Collections.sort(results);
        System.out.println("max = " + results.get(results.size()-1));
        long endTs = System.currentTimeMillis(); // end time
        threadPool.shutdown();

        System.out.println("Time's: " + (endTs - startTs) + "ms");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
