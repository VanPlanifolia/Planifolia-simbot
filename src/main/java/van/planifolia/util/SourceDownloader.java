package van.planifolia.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

/**
 * 资源下载的工具类，如果有资源url的前提下，可以通过本下载器来下载源文件并且返回文件的具体位置。传入参数在方法注释上可以看到
 */
public class SourceDownloader {
    public static String downloadImg(String imgurl) {
        String filePath = "";
        try {
            URL url = new URL(imgurl);
            //打开链接
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/4.76");
            url.openConnection().setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //设置请求超时 5sg3
            con.setConnectTimeout(10 * 1000);
            BufferedInputStream in = new BufferedInputStream(con.getInputStream());
            //文件名字
            Random R = new Random();
            int random = R.nextInt(1000);
            filePath = Constant.parentPath+"\\"+ random + ".png";
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] buffer = new byte[1024 * 8];
            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            //关闭流
            out.close();
            in.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * 图片下载器 工具类里面的下载方法，需要传入下载链接与文件名
     * @param imgurl 图片的下载链接
     * @param filename 文件名字
     * @return 下载下来的文件路径
     * @author Planifolia.Van
     * */
    public static String downloadImg(String imgurl,String filename) {
        String filePath = "";
        try {
            URL url = new URL(imgurl);
            //打开链接
            URLConnection con = url.openConnection();
            //设置请求超时 5sg3
            con.setConnectTimeout(10 * 1000);
            BufferedInputStream in = new BufferedInputStream(con.getInputStream());
            //文件名字
            filePath = Constant.parentPath+"\\"+  filename + ".png";
            //使用任意一个流打开目标文件
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
            //文件写入三部曲，创建缓存区，while循环写入，关闭流
            byte[] buffer = new byte[1024 * 8];
            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            //关闭流
            out.close();
            in.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果上面出现异常了我们不妨直接返回一个空的文件地址
        return filePath;
    }

    /**
     * 音乐下载器 工具类里面的音乐资源下载方法，需要传入音乐资源的url，音乐名称，音乐作者
     * @param musUrl 资源的url
     * @param musName 音乐的名字
     * @param musArt 音乐的作者
     * @author Planifolia.Van
     * @return 文件的路径
     */
    public static String dowLoadMusic(String musUrl,String musName,String musArt )  {
        String filePath="";
        try {
        //获取url
        URL url=new URL(musUrl);
        //建立链接
        URLConnection con=url.openConnection();
        //设置请求超时 5sg3
        con.setConnectTimeout(5*1000);
        //缓冲流
        BufferedInputStream in = new BufferedInputStream(con.getInputStream());
        //文件名字
        filePath = Constant.parentPath+musName+"-"+musArt + ".mp3";
        //字节缓冲流下写出文件
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] buffer = new byte[1024*8];
        int count;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        //关闭流
        out.close();
        in.close();
        }catch (Exception e){
            System.out.println(e);
        }
        return filePath;
    }
}
