package van.planifolia.util;

import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.bot.BotManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量类，保存一些常用的常量
 */
public class Constant {
    //bot管理器的常量
    public static  BotManager botManager;
    //群组信息List的常量
    public static List<String> groupList=new ArrayList<>();
    //送信器的常量
    public static BotSender sender;
    //文件目录的路径
    public static String parentPath;
    //Bot开关机状态的变量
    public static boolean BotState=false;
    //色图的简易请求拦截锁,true代表解锁反之上锁
    public static boolean StLock=true;
}
