package cn.shadowtranx.sfm;

import javax.swing.*;
import java.awt.*;
import org.opencv.core.*;
//���ڳɹ��ˣ�crane��ƨ�²�����414
//���ˣ���bug���ް�


public class Main {

    public static void main(String[] args) {
        log.msg("��Ȼ��֪�����log��ɶ�ã����ǻ�����д[����]");
        log.msg("��һ�Ȱ���צ����Ŭ���������ڣ��еĻ����аɣ�>");
        //���ˣ�д�˸�log��������д��
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        //��Ȼ�������toolkit��ɶ�õģ���֮ǰ��д������Ӧ����Ҫ��:)
        //�������ˣ����ͼ����
        int width = screenSize.width;
        
        int height= screenSize.height;

        JFrame sfma = new JFrame("\u7f51\u8bfe\u5b66\u751f\u7ba1\u7406\u5927\u5e08");//���˸�����ֻ�����������

        sfma.setVisible(true);
        sfma.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        sfma.setIconImage(new ImageIcon("C:/Users/apple/Desktop/Cprogramme/project/student-face-manager/src/main/resources/image/icon.png").getImage());
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
        log.msg("���ˣ���������� ");//
        log.msg("����ı��뱾��������ģ�Ȼ���ҷ������ַ�������Ӹ��ո������ ");//
        log.msg("���������ǻ���ʱ�̣�doge");//
        //��֪�����Լ�������xml�ĵ�����//
        //Ϊ������opencv//







    }
}