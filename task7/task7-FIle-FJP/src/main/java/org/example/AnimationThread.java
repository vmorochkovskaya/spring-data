package org.example;

public class AnimationThread implements Runnable {
    private byte anim = 0;
    private String lastLine = "";

    @Override
    public void run() {
        int line = 0;
        while (FileStatisticTask.isBisy()) {
            switch (anim) {
                case 1:
                    print("[ \\ ] " + line);
                    break;
                case 2:
                    print("[ | ] " + line);
                    break;
                case 3:
                    print("[ / ] " + line);
                    break;
                default:
                    anim = 0;
                    print("[ - ] " + line);
            }
            anim++;
            line++;
        }

    }

    private void print(String line) {
        if (lastLine.length() > line.length()) {
            String temp = "";
            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }
            if (temp.length() > 1)
                System.out.print("\r" + temp);
        }
        System.out.print("\r" + line);
        lastLine = line;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
