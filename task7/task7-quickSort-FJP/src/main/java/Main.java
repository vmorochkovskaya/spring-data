import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        int[] data = new int[1000000];
        Random rand = new Random();
        IntStream.range(0, data.length).forEach(i-> data[i] = rand.nextInt((1000000) + 1));

        QuickSortAction quickSort = new QuickSortAction(data);
        ForkJoinPool pool = new ForkJoinPool();
        long startTime = System.nanoTime();
        pool.invoke(quickSort);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        pool.shutdown();
        System.out.println("sorted data = " + Arrays.toString(data));
        System.out.println("total time = " + totalTime);
    }
}
