import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class QuickSortAction extends RecursiveAction {
    private static final int THRESHOLD = 1000;

    private int[] data;
    private int left;
    private int right;

    public QuickSortAction(int[] data) {
        this.data = data;
        left = 0;
        right = data.length - 1;
    }

    public QuickSortAction(int[] data, int left, int right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (left < right) {
            if (right - left < THRESHOLD) {
                Arrays.sort(data, left, right + 1);
            } else {
                int pivot = partition(data, left, right);
                var quickSort1 = new QuickSortAction(data, left, pivot - 1);
                var quickSort2 = new QuickSortAction(data, pivot + 1, right);
                quickSort1.fork();
                quickSort2.compute();
                quickSort1.join();
            }
        }
    }

    private int partition(int[] array, int low, int high) {
        int i = low - 1;
        int x = array[high];
        for (int j = low; j < high; j++) {
            if (array[j] < x) {
                i++;
                swap(array, i, j);
            }
        }
        i++;
        swap(array, i, high);
        return i;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
