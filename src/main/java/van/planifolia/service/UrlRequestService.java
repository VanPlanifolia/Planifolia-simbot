package van.planifolia.service;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;

/**
 * Url请求的Service层，所有Listener中的Url请求事件在这里处理
 * @author Van.Planifolia
 */
public interface UrlRequestService {
    /**
     * 获取随机音乐
     * @param groupMsg 群组消息
     * @param sender 送信器
     */
    void getRandomMusic(GroupMsg groupMsg, Sender sender);

    /**
     * 复读
     * @param groupMsg
     * @param sender
     */
    void repeat(GroupMsg groupMsg,Sender sender);

    /**
     * 随机st
     * @param groupMsg
     * @param sender
     */
    void getRandomSt(GroupMsg groupMsg, Sender sender, Setter setter);

    /**
     * 随机二次元图片
     * @param groupMsg
     * @param sender
     */
    void getRandomImageECY(GroupMsg groupMsg,Sender sender);

    /**
     * 随机Men图片
     * @param groupMsg
     * @param sender
     */
    void getRandomImageMen(GroupMsg groupMsg,Sender sender);

    /**
     * 随机骚话
     * @param groupMsg
     * @param sender
     */
    void getRandomHitokoto(GroupMsg groupMsg, Sender sender);

    /**
     * 历史上的今天
     */
    void getTodayForHis(GroupMsg groupMsg,Sender sender);

    /**
     * 获取随机头像
     */
    void getRandomImageTx(GroupMsg groupMsg,Sender sender);

    /**
     * 每日运势方法
     */
    void getDailyFortune(Sender sender,GroupMsg groupMsg);

    /**
     * 点歌
     */
    void song(Sender sender,GroupMsg groupMsg);

    /**
     * 发送点歌语音
     */
    void sendMusic(Sender sender,GroupMsg groupMsg);

    /**
     * 万能搜索
     */
    void searchSourceForImage(GroupMsg groupMsg,Sender sender);
    /**
     * 番剧搜索
     */
    void serachCartoonForName(GroupMsg groupMsg,Sender sender);
    /**
     * 番剧发送
     */
    void sendCartoon(GroupMsg groupMsg,Sender sender);
    /**
     * 可达鸭图片
     */
    void makeKeDaYa(GroupMsg groupMsg,Sender sender);
}
