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
     * ???
     */
    //WindowImage WI;
    /**
     * Точка удочки
     */
    Point fishActionCoord;
    /**
     * Кол-во пойманной рыбы
     */
    int fishAmount;
    /**
     * Чувствительность
     */
    double sens;

    /**
     *
     * @param sens чувствительность
     * @param fishActionCoord точка удочки
     * @param AOI зона интереса поплавка
     */
    public Bot(Double sens, Point fishActionCoord, Rectangle AOI) {
        this.AOI = AOI;
        this.fishActionCoord = fishActionCoord;
        fishAmount = 0;
        this.sens = sens;
        start();
    }

    @Override
    public void run() {
        super.run();
        Platform.runLater(() -> new BotStage(getName(), this));
    }

    /**
     * Метод определения через шаблон
     */
    private void templateMethod() {

    }

    /**
     * Запуск рыбалки
     * @throws IOException не найден рабочий файл
     * @throws AWTException ошибка с роботом
     * @throws InterruptedException прерван слип
     */
//    private void startMethod() throws IOException, AWTException, InterruptedException {
//        while (!isInterrupted()) {
//            System.out.println("NEW ITERATION");
//            long time = System.currentTimeMillis();
//            BotUtil.moveMouse(fishActionCoord.x, fishActionCoord.y);
//            BotUtil.clickMouse();
//            BotUtil.moveMouse(0, 0);
//            sleep(3000);
//            org.opencv.core.Point bobberPoint = new org.opencv.core.Point();
//            try {
//                bobberPoint = BotUtil.colorMethod(AOI);
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (bobberPoint.x == 0 && bobberPoint.y == 0) {
//                System.out.println("Поплавок не найден.");
//                continue;
//            }
//            Rectangle bobberAOI = new Rectangle((int) (AOI.x + bobberPoint.x), (int) (AOI.y + bobberPoint.y - 5), 20, 15);
//
//            // Скрин поплавка в матрицу
//            Mat bobber = BotUtil.Convertator.BufferedImage2Mat(BotUtil.grabScreen(bobberAOI));
//            // Поплавок в серый цвет
//            Imgproc.cvtColor(bobber, bobber, Imgproc.COLOR_BGR2GRAY);
////            double mean = Core.mean(bobber).val[0];
//            ArrayList<Double> norma = new ArrayList<>();
//            norma.add(0.0);
//            while (!isInterrupted()) {
//                BufferedImage bf_now = BotUtil.grabScreen(bobberAOI);
//                MI.setBobberField(SwingFXUtils.toFXImage(bf_now, null));
//                Mat bobber_now = BotUtil.Convertator.BufferedImage2Mat(bf_now);
//                Imgproc.cvtColor(bobber_now, bobber_now, Imgproc.COLOR_BGR2GRAY);
//                double mean_now = Core.mean(bobber_now).val[0];
//                // STATIC MEAN - NOW MEAN
//                double diff = Math.abs(mean_now - norma.get(norma.size() - 1));
//                norma.add(mean_now);
//                    if (diff > 1)
//                        System.out.println(diff);
//                if (diff > sens && diff < 20) {
//                    System.out.println(diff);
//                    BotUtil.moveMouse(bobberAOI.x, bobberAOI.y);
//                    BotUtil.clickMouse();
//                    fishAmount++;
//                    break;
//                }
//                if(System.currentTimeMillis() - time > 17300)
//                    break;
//            }
//            BotUtil.moveMouse(0, 0);
//            norma.clear();
//            sleep(4000);
//        }
//    }

    @Override
    public void interrupt() {
        super.interrupt();
        System.out.println(getName() + " stop.");
    }

}
