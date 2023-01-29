package org.example;

import java.io.File;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        try {
            FolderStatistic folderStatistic = pool.invoke(new FileStatisticTask(
                    new File(System.getProperty("folder"))));
            System.out.println("Folder statistic: " + folderStatistic);
        } finally {
            pool.shutdown();
        }
    }
}
