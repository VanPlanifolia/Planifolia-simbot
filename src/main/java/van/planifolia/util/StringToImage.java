package van.planifolia.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Van.Planifolia
 */
public class StringToImage {
    /**
     * 字符串转图片的工具类
     * @param sourceString 源字符串
     * @return 返回BufferImage对象
     */
    public static BufferedImage stringToImg(String[] sourceString){
        //设置画布大小
        int canvasWidth=400;
        int canvasHeight=550;
        BufferedImage image=new BufferedImage(canvasWidth,canvasHeight,BufferedImage.TYPE_INT_BGR);
        // 获取图形上下文对象
        Graphics graphics = image.getGraphics();
        // 填充
        graphics.fillRect(0, 0, canvasWidth, canvasHeight);
        // 设定字体大小及样式
        graphics.setFont(new Font("微软雅黑", Font.PLAIN,20));
        // 字体颜色
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < sourceString.length; i++) {
            // 描绘字符串
            graphics.drawString(sourceString[i], 50,  20+ (i + 1) * 20);
        }
        graphics.dispose();
        return image;
    }

}
