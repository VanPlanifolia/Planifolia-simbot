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
}
