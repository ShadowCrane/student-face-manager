package cn.shadowtranx.sfm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
// 写日记的 拉去文革批斗了


public class Main {

    public static final String NAME = "Student-Face-Manager";
    public static final String VERSION = "v1.0";
    public static final String OWNER = "ShadowTranX-Team";

    public static Logger logger = LogManager.getLogger("SFM");

    public static void main(String[] args) {
        logger.info(NAME + " " + VERSION + "Loading. By " + OWNER);
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
        JFrame sfmWindow = new JFrame("网课学生管理大师");

        URL url = Main.class.getClassLoader().getResource("image/icon.png");
        if (url != null)
            sfmWindow.setIconImage(new ImageIcon(url.getPath()).getImage()); // YOUR Stupid Code had FIXED!!

        sfmWindow.setVisible(true);
        sfmWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Set window size
        sfmWindow.setSize((int) width,(int) height);
        logger.info("Window has been creating.");

        // OpenCV







    }
}