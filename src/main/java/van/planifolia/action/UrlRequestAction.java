package van.planifolia.action;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;

/**
 * Url请求的action接口，里面定义了所有的url的请求的action方法
 */

public interface UrlRequestAction {
    /**
     * 随机音乐
     * @param groupMsg 群组消息
     * @param sender 送信器
     */
    void getRandomMusic(GroupMsg groupMsg, Sender sender);
    /**
     * 随机st
     * @param groupMsg 群组消息
     * @param sender 送信器
     */
    void getRandomSt(GroupMsg groupMsg, Sender sender, Setter setter);

    /**
     * 随机二次元图片
     */
    void getRandomImageECY(GroupMsg groupMsg, Sender sender);

    /**
     * 随机Men图片
     */
    void getRandomImageMen(GroupMsg groupMsg,Sender sender);

    /**
     * 随机骚话
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
     * 每日占卜
     */
    void getDailyFortune(GroupMsg groupMsg,Sender sender);

    /**
     * 点歌
     */
    void song(GroupMsg groupMsg,Sender sender);

    /**
     * 发送语音
     */
    void sendMusic(GroupMsg groupMsg,Sender sender);
    /**
     * 万能搜索
     */
    void searchSourceForImage(GroupMsg groupMsg,Sender sender);
}
