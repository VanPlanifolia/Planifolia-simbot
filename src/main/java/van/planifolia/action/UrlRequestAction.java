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
     * @param groupMsg
     * @param sender
     */
    void getRandomImageECY(GroupMsg groupMsg, Sender sender);

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
     * @param groupMsg
     * @param sender
     */
    void getTodayForHis(GroupMsg groupMsg,Sender sender);

    /**
     * 获取随机头像
     * @param groupMsg
     * @param sender
     */
    void getRandomImageTx(GroupMsg groupMsg,Sender sender);

    /**
     * 每日占卜
     * @param groupMsg
     * @param sender
     */
    void getDailyFortune(GroupMsg groupMsg,Sender sender);
}
