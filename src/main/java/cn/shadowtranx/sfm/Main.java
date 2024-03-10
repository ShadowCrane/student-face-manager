package cn.shadowtranx.sfm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import org.bytedeco.opencv.opencv_core.Point;

import java.awt.image.BufferedImage;
import java.net.URL;
import static org.bytedeco.opencv.global.opencv_imgproc.*;


public class Main {

    public static final String NAME = "Student-Face-Manager";
    public static final String VERSION = "v1.0";
    public static final String OWNER = "ShadowTranX-Team";

    public static Logger logger = LogManager.getLogger("SFM");

    public static void main(String[] args) {
        logger.info(NAME + " " + VERSION + " 正在加载，由 " + OWNER + " 驱动。");
        // Toolkit
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        // Get screen size
        double width = screenSize.width;
        double height = screenSize.height;

        // Computing window size
        width = width * 0.33333;
        height = height * 0.37037;
        width = Math.round(width);
        height = Math.round(height);

        // Create window
        JFrame sfmWindow = new JFrame("网课管理大师");
        JLabel label = new JLabel();
        sfmWindow.add(label);

        URL url = Main.class.getClassLoader().getResource("image/icon.png");
        if (url != null)
            sfmWindow.setIconImage(new ImageIcon(url.getPath()).getImage()); // YOUR Stupid Code had FIXED!!

        sfmWindow.setVisible(true);
        sfmWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Set window size
        sfmWindow.setSize((int) width,(int) height);
        logger.info("窗口已创建。");


        // OpenCV
        Loader.load(org.bytedeco.opencv.global.opencv_core.class);
        Loader.load(org.bytedeco.opencv.global.opencv_imgproc.class);
        Loader.load(org.bytedeco.opencv.global.opencv_highgui.class);

        // 加载人脸检测器和姿态估计器
        CascadeClassifier faceDetector = new CascadeClassifier();
        CascadeClassifier eyeDetector = new CascadeClassifier();
        faceDetector.load(Main.class.getResource("/haarcascade_frontalface_default.xml").getPath().startsWith("/") ?
                Main.class.getResource("/haarcascade_frontalface_default.xml").getPath().substring(1) :
                Main.class.getResource("/haarcascade_frontalface_default.xml").getPath());
        eyeDetector.load(Main.class.getResource("/haarcascade_eye.xml").getPath().startsWith("/") ?
                Main.class.getResource("/haarcascade_eye.xml").getPath().substring(1) :
                Main.class.getResource("/haarcascade_eye.xml").getPath());



        VideoCapture capture = new VideoCapture(0);

        if (!capture.isOpened()) {
           logger.error("摄像头无法打开。");
            return;
        }

        Mat frameMat = new Mat();
        while (true) {
            capture.read(frameMat);

            RectVector faces = new RectVector();
            faceDetector.detectMultiScale(frameMat, faces);

            for (int i = 0; i < faces.size(); i++) {
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


                }
            }

            BufferedImage image = matToBufferedImage(frameMat);
            ImageIcon icon = new ImageIcon(image);
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
                // bgr to rgb
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
}