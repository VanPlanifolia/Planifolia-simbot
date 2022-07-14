package van.planifolia.service;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

/**
 * Url请求的Service层，所有Listener中的Url请求事件在这里处理
 * @author Van.Planifolia
 */
public interface UrlRequestService {
    /**
     * 获取随机音乐
     * @param groupMsg
     * @param sender
     */
    void getRandomMusic(GroupMsg groupMsg, Sender sender);
    void repeat(GroupMsg groupMsg,Sender sender);
}
