package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class FileStatisticTask extends RecursiveTask<FolderStatistic> {
    private File file;
    private static AtomicInteger foldersCount = new AtomicInteger();
    private static volatile boolean isBusy;
    private static volatile boolean isProgressBarStarted;

    public FileStatisticTask(File file) {
        this.file = file;
    }

    @Override
    protected FolderStatistic compute() {
        isBusy = true;
        initiateProgressBar();
        if (file.isFile()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return FolderStatistic.builder()
                    .filesCount(1)
                    .allFilesSize(file.length())
                    .build();
        } else {
            foldersCount.incrementAndGet();
        }

        var tasks = new ArrayList<FileStatisticTask>();
        var children = file.listFiles();
        if (children != null) {
            Stream.of(children).forEach(child -> {
                FileStatisticTask task = new FileStatisticTask(child);
                task.fork();
                tasks.add(task);
            });
        }

        var folderStatistic = FolderStatistic.builder().build();
        tasks.forEach(task -> {
            FolderStatistic folderStatistic1 = task.join();
            folderStatistic.setFilesCount(folderStatistic.getFilesCount() + folderStatistic1.getFilesCount());
            folderStatistic.setAllFilesSize(folderStatistic.getAllFilesSize() + folderStatistic1.getAllFilesSize());
        });
        isBusy = !this.isDone();
        folderStatistic.setFoldersCount(foldersCount.get() - 1);

        return folderStatistic;
    }

    public static boolean isBisy() {
        return isBusy;
    }

    private void initiateProgressBar() {
        if (!isProgressBarStarted) {
            isProgressBarStarted = true;
            new Thread(new AnimationThread()).start();
            new Thread(new ScannerThread()).start();
        }
    }

}

