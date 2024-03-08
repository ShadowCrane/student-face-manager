package cn.shadowtranx.sfm;

import javax.swing.*;
import java.awt.*;
import java.net.URL;


public class Main {

    public static void main(String[] args) {
        log.msg("虽然不知道这个log有啥用，但是还是想写[吐舌]");
        log.msg("等一等啊，爪哇在努力创建窗口，闲的话就闲吧：>");
        //乐了，写了个log，接下来写这
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        //虽然忘记这个toolkit干啥用的，但之前我写过，但应该是要的:)
        int width = screenSize.width;
        
        int height= screenSize.height;

        JFrame sfma = new JFrame("\u7f51\u8bfe\u5b66\u751f\u7ba1\u7406\u5927\u5e08");//妈了个巴子只能依靠这个了，shadow别乱搞
        sfma.setVisible(true);
        sfma.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        String swidth = String.valueOf(width);
        String sheight = String.valueOf(height);

        Double dwidth = Double.valueOf(width);
        Double dheight = Double.valueOf(height);

        dwidth = dwidth * 0.33333;
        dheight = dheight * 0.37037;
        width = (int) Math.round(dwidth);
        height = (int) Math.round(dheight);
        log.msg(swidth);
        log.msg(sheight);
        sfma.setSize(width,height);
        System.out.println(width);
        System.out.println(height);
        log.msg("好了，创建完成了 ");//
        log.msg("这里的编码本来有问题的，然后我发现在字符串后面加个空格就行了 ");//
        log.msg("接下来，是幻想时刻（doge");//
        //神知道我自己创建个xml文档（（//
        //为了适配opencv//
//
//
        URL url = ClassLoader.getSystemResource("student-face-manager/src/resources/opencv-480.jar");//这里跑不//
        System.load(url.getPath());//



    }
}