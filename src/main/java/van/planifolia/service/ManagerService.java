package van.planifolia.service;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

/**
 * 管理器的Service接口,用于规范需要实现的服务方法
 * @author Van.Planifolia
 */
public interface ManagerService {
    /**
     * 关闭bot的service方法
     *
     * @param sender 送信器
     */
    void offBot(Sender sender, GroupMsg groupMsg);

    /**
     * At全体的service方法
     */
    void atAllPeople(Sender sender, GroupMsg groupMsg);

}
