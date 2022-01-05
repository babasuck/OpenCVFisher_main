package org.openjfx;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Bot extends Thread {
    /**
     * Зона интереса для поплавка
     */
    Rectangle AOI;
    /**
     * Точка удочки
     */
    Point fishActionCoord;
    Point destroyFishPosition;
    /**
     * Чувствительность
     */
    double sens;
    /**
     * BotStage
     */
    BotStage bs;

    /**
     * @param sens            чувствительность
     * @param fishActionCoord точка удочки
     * @param AOI             зона интереса поплавка
     */
    public Bot(Double sens, Point fishActionCoord, Rectangle AOI, Point destroyFishPosition, BotStage bs) {
        this.bs = bs;
        this.AOI = AOI;
        this.fishActionCoord = fishActionCoord;
        this.sens = sens;
        this.destroyFishPosition = destroyFishPosition;
        start();
    }

    @Override
    public void run() {
        super.run();
        System.out.println(bs);
        System.out.println(bs.getBotStageController());
        try {
            startFishing();
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод определения через шаблон
     */
    private void templateMethod() {

    }

    /**
     * Запуск рыбалки
     *
     * @throws AWTException         ошибка с роботом
     * @throws InterruptedException прерван слип
     */
    private void startFishing() throws AWTException, InterruptedException {
        sleep(2000);
        int fails = 0;
        int fish = 0;
        int timeout = 0;
        BotStageController bsc = bs.getBotStageController();
        while (!isInterrupted()) {
            if((fish % 20) == 0) {
                BotUtil.moveMouse(destroyFishPosition.x, destroyFishPosition.y);
                BotUtil.clickMouse();
                sleep(700);
            }
            int finalFails = fails;
            int finalFish = fish;
            int finalTimeout = timeout;
            Platform.runLater(() -> {
                bsc.setFailsNumField(finalFails);
                bsc.setFishNumField(finalFish);
                bsc.setTimeoutLabel(finalTimeout);
            });
            System.out.println("NEW ITERATION");
            BotUtil.moveMouse(fishActionCoord.x, fishActionCoord.y);
            BotUtil.clickMouse();
            sleep(3500);
            org.opencv.core.Point bobberPoint = new org.opencv.core.Point();
            try {
                bobberPoint = BotUtil.colorMethod(AOI);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            if (bobberPoint.x == 0 && bobberPoint.y == 0) {
                System.out.println("Поплавок не найден.");
                fails++;
                continue;
            }
            // Зона поплавка
            Rectangle bobberAOI = new Rectangle((int) (AOI.x + bobberPoint.x), (int) (AOI.y + bobberPoint.y - 5), 20, 15);
            // Скрин поплавка
            BufferedImage bf_now = BotUtil.grabScreen(bobberAOI);
            bsc.setBobberImage(SwingFXUtils.toFXImage(bf_now, null));
            // Скрин поплавка в матрицу
            Mat bobber = BotUtil.Convertator.BufferedImage2Mat(bf_now);
            if (bobber == null)
                break;
            // Поплавок в серый цвет
            Imgproc.cvtColor(bobber, bobber, Imgproc.COLOR_BGR2GRAY);
            double mean = Core.mean(bobber).val[0];
            ArrayList<Double> norma = new ArrayList<>();
            norma.add(mean);
            long time = System.currentTimeMillis();
            while (!isInterrupted()) {
                Platform.runLater(() -> bsc.setTimeLabel((int) (23 - (System.currentTimeMillis() - time) / 1000)));
                bf_now = BotUtil.grabScreen(bobberAOI);
                if (bf_now == null) break;
                Mat bobber_now = BotUtil.Convertator.BufferedImage2Mat(bf_now);
                if (bobber_now == null) break;
                Imgproc.cvtColor(bobber_now, bobber_now, Imgproc.COLOR_BGR2GRAY);
                //double mean_now = Core.mean(bobber_now).val[0];
                // STATIC MEAN - NOW MEAN
                mean = Core.mean(bobber_now).val[0];
                double diff = Math.abs(mean - norma.get(norma.size() - 1));
                norma.add(mean);
                if (diff > 1) {
                   //System.out.println(diff);
                    Platform.runLater(() -> bsc.setDifNum(diff));
                }
                if (diff > sens && diff < 20) {
                    BotUtil.moveMouse(bobberAOI.x, bobberAOI.y);
                    BotUtil.clickMouseRight();
                    fish++;
                    break;
                }
                if (System.currentTimeMillis() - time > 25000) {
                    timeout++;
                    break;
                }
            }
            BotUtil.moveMouse(0, 0);
            norma.clear();
            sleep(3000);
            Platform.runLater(() -> bsc.setDifNum(0));
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        System.out.println(getName() + " stop.");
    }

}
