package cn.shadowtranx.sfm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import org.bytedeco.opencv.opencv_core.Point;
import org.opencv.videoio.Videoio;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static org.bytedeco.opencv.global.opencv_imgproc.*;


public class Main {

    public static final String NAME = "Student-Face-Manager";
    public static final String VERSION = "v1.0";
    public static final String OWNER = "ShadowTranX-Team";

    public static boolean looking = false;

    public static Logger logger = LogManager.getLogger("SFM");

    public static void main(String[] args) {
        logger.info(NAME + " " + VERSION + " 正在加载，由 " + OWNER + " 驱动. ");
        // 图标
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        // 获取显示器分辨率
        double width = screenSize.width;
        double height = screenSize.height;

        // 计算窗口大小
        width = width * 0.33333;
        height = height * 0.37037;
        width = Math.round(width);
        height = Math.round(height);

        // 创建窗口
        JFrame sfmWindow = new JFrame(NAME + " " + VERSION);
        JLabel label = new JLabel();
        sfmWindow.add(label);

        URL url = Main.class.getClassLoader().getResource("image/icon.png");
        if (url != null)
            sfmWindow.setIconImage(new ImageIcon(url.getPath()).getImage()); // YOUR Stupid Code had FIXED!! @TranXStar

        sfmWindow.setVisible(true);
        sfmWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 设置窗口大小
        sfmWindow.setSize((int) width, (int) height);

        sfmWindow.setLocationRelativeTo(null);

        // OpenCV 部分
        Loader.load(opencv_core.class);
        Loader.load(opencv_imgproc.class);
        Loader.load(opencv_highgui.class);

        // 从JAR获取文件的输入流
        InputStream inputStreamFace = Main.class.getResourceAsStream("/haarcascade_frontalface_default.xml");
        InputStream inputStreamEye = Main.class.getResourceAsStream("/haarcascade_eye.xml");

        // 创建对象
        Path faceModelFilePath = null;
        Path eyeModelFilePath = null;
        try {
            faceModelFilePath = Files.createTempFile("haarcascade_frontalface_default", ".xml");
            eyeModelFilePath = Files.createTempFile("haarcascade_eye", ".xml");
            // 将模型复制到系统临时文件夹
            if (inputStreamEye != null & inputStreamFace != null) {
                Files.copy(inputStreamFace, faceModelFilePath, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(inputStreamEye, eyeModelFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        // 加载模型
        CascadeClassifier faceDetector = new CascadeClassifier();
        CascadeClassifier eyeDetector = new CascadeClassifier();
        if (faceModelFilePath != null & eyeModelFilePath != null) {
            faceDetector.load(faceModelFilePath.toString());
            eyeDetector.load(eyeModelFilePath.toString());
        }


        VideoCapture capture = new VideoCapture(0);

        if (!capture.isOpened()) {
            logger.error("摄像头无法打开. ");
            return;
        }

        // 重新设置窗口大小 设置成摄像头分辨率
        sfmWindow.setSize((int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
        sfmWindow.setLocationRelativeTo(null);

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                }
                tips();
            }
        }).start();

        Mat frameMat = new Mat();
        while (true) {
            capture.read(frameMat);

            RectVector faces = new RectVector();
            faceDetector.detectMultiScale(frameMat, faces);

            for (int i = 0; i < faces.size(); i++) {
                looking = true;

                Rect face = faces.get(i);
                Mat faceROI = new Mat(frameMat, face);

                RectVector eyes = new RectVector();
                eyeDetector.detectMultiScale(faceROI, eyes);

                if (eyes.size() >= 2) {
                    Point eye1Center = new Point();
                    Point eye2Center = new Point();

                    for (int j = 0; j < 2; j++) {
                        Rect eye = eyes.get(j);
                        Point eyeCenter = new Point(face.x() + eye.x() + eye.width() / 2, face.y() + eye.y() + eye.height() / 2);
                        if (j == 0) {
                            eye1Center = eyeCenter;
                        } else {
                            eye2Center = eyeCenter;
                        }
                        rectangle(faceROI, eye, new Scalar(255, 0, 0, 0));
                    }

                    double angle = Math.toDegrees(Math.atan2(eye2Center.y() - eye1Center.y(), eye2Center.x() - eye1Center.x()));
                    putText(frameMat, "Angle: " + angle, new Point(face.x(), face.y() - 10),
                            FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0, 0));

                    putText(frameMat, "Looking", new Point(face.x(), face.y() - 30),
                            FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0, 0));


                } else looking = false;
            }

            BufferedImage image = matToBufferedImage(frameMat);
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(image));
            label.setIcon(icon);
        }
    }

    private static BufferedImage matToBufferedImage(Mat matrix) {
        int cols = matrix.cols();
        int rows = matrix.rows();
        int elemSize = (int) matrix.elemSize();
        byte[] data = new byte[cols * rows * elemSize];
        int type;
        matrix.data().get(data);
        switch (matrix.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                // bgr 2 rgb
                byte b;
                for (int i = 0; i < data.length; i = i + 3) {
                    b = data[i];
                    data[i] = data[i + 2];
                    data[i + 2] = b;
                }
                break;
            default:
                return null;
        }

        BufferedImage image2 = new BufferedImage(cols, rows, type);
        image2.getRaster().setDataElements(0, 0, cols, rows, data);
        return image2;
    }

    private static void tips() {
        TrayIcon trayIcon = null;

        URL url = Main.class.getClassLoader().getResource("image/icon.png");
        if (url != null)
            trayIcon = new TrayIcon(new ImageIcon(url.getPath()).getImage(), NAME);

        SystemTray tray = SystemTray.getSystemTray();
        try {
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("System tray icon demo");
            tray.add(trayIcon);
        } catch (AWTException e) {
        }

        if (url != null)
            trayIcon.displayMessage(NAME, looking ? "学生正常上课" : "学生视线偏移", TrayIcon.MessageType.INFO);
    }
}