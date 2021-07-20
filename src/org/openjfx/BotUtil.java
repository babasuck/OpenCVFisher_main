package org.openjfx;

import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class BotUtil {

    public static BufferedImage grabScreen(Rectangle rectangle) {
        try {
            return new Robot().createScreenCapture(rectangle);
        } catch (SecurityException | AWTException ignored) {
        }
        return null;
    }

    public static String generateString(int length) {
        Random rng = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = (char) (rng.nextInt(57) + 65);
        }
        return new String(text);
    }

    public static void moveMouse(int x, int y) throws AWTException {
        new Robot().mouseMove(x, y);
    }

    public static void clickMouse() throws AWTException {
        Robot rob = new Robot();
        rob.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        rob.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public static Point colorMethod(Rectangle rect) throws IOException, InterruptedException {
        BufferedImage bf1 = grabScreen(rect);
        Mat AOI = BotUtil.Convertator.BufferedImage2Mat(bf1);
        Imgproc.cvtColor(AOI, AOI, Imgproc.COLOR_BGR2HSV);
        Mat hue = new Mat();
        Core.extractChannel(AOI, hue, 0);
        Core.inRange(AOI, new Scalar(0,70,0), new Scalar(0,255,255), hue);
        Mat res = new Mat();
        Core.copyTo(AOI, res, hue);
        Imgproc.cvtColor(res, res, Imgproc.COLOR_BGR2GRAY);
        Mat index = new Mat();
        Core.findNonZero(res, index);
//        Rect bobberRect = new Rect(new org.opencv.core.Point(index.get(0, 0)), new Point(index.get(index.rows() - 1, 0)));
        return new Point(index.get(index.rows() - 1, 0));
//        System.out.println("x - " + bobberRect.x + " y - " + bobberRect.y + " w: " + bobberRect.width + " h: " + bobberRect.height);
//        Imgproc.rectangle(res, bobberRect, new Scalar(255, 255, 255));
//        BufferedImage bf = BotUtil.Convertator.MatToBufferedImage(res);
//        return bf;
//        return bobberRect;
    }

    static class Convertator {
        public static BufferedImage MatToBufferedImage(Mat m) {
            if (m == null || m.empty()) return null;
            if (m.depth() == CvType.CV_8U) {
            } else if (m.depth() == CvType.CV_16U) { // CV_16U => CV_8U
                Mat m_16 = new Mat();
                m.convertTo(m_16, CvType.CV_8U, 255.0 / 65535);
                m = m_16;
            } else if (m.depth() == CvType.CV_32F) { // CV_32F => CV_8U
                Mat m_32 = new Mat();
                m.convertTo(m_32, CvType.CV_8U, 255);
                m = m_32;
            } else return null;
            int type = 0;
            if (m.channels() == 1) type = BufferedImage.TYPE_BYTE_GRAY;
            else if (m.channels() == 3) type = BufferedImage.TYPE_3BYTE_BGR;
            else if (m.channels() == 4) type = BufferedImage.TYPE_4BYTE_ABGR;
            else return null;

            byte[] buf = new byte[m.channels() * m.cols() * m.rows()];
            m.get(0, 0, buf);
            byte tmp = 0;
            if (m.channels() == 4) { // BGRA => ABGR
                for (int i = 0; i < buf.length; i += 4) {
                    tmp = buf[i + 3];
                    buf[i + 3] = buf[i + 2];
                    buf[i + 2] = buf[i + 1];
                    buf[i + 1] = buf[i];
                    buf[i] = tmp;
                }
            }

            BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(buf, 0, data, 0, buf.length);
            return image;
        }

        public static Mat BufferedImage2Mat(BufferedImage image) throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
        }

        public static Image MatToImage(Mat m) {
            BufferedImage bf = MatToBufferedImage(m);
            ImageIcon temp = new ImageIcon(bf);
            return temp.getImage();
        }

        public static Image BfToImage(BufferedImage m) {
            ImageIcon temp = new ImageIcon(m);
            return temp.getImage();
        }
    }
}
