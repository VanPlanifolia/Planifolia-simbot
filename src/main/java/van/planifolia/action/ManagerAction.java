package van.planifolia.action;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

/**
 * 管理器的接口用于定义管理器的方法
 * @author Van.Planifolia
 */
public interface ManagerAction {
    /**
     * 关闭bot的action接口
     * @param sender
     */
    void offBot(Sender sender, GroupMsg groupMsg);

    void atAllPeople(Sender sender,GroupMsg groupMsg);
}
