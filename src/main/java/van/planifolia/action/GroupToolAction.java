package van.planifolia.action;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

/**
 * 群组工具的Action层接口
 * @author Van.Planifolia
 */
public interface GroupToolAction {

    /**
     * 开启请求式计时器
     * @param groupMsg
     * @param sender
     */
   void requestTimer(GroupMsg groupMsg, Sender sender);

    /**
     * 查看计时器列表
     * @param groupMsg
     * @param sender
     */
   void timerList(GroupMsg groupMsg,Sender sender);

    /**
     *
     */
    void stopTimer(GroupMsg groupMsg, Sender sender);
}
