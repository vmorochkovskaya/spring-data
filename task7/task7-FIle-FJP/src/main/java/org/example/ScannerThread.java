package org.example;

import java.io.IOException;

public class ScannerThread implements Runnable {
    @Override
    public void run() {
        if (FileStatisticTask.isBisy()) {
            try {
                System.in.read();
                System.exit(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
