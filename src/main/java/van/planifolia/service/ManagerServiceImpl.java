package van.planifolia.service;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import van.planifolia.action.ManagerAction;

/**
 * 管理器的Service实现类,用于调用管理器的action
 * @author Van.Planifolia
 */
@Beans
public class ManagerServiceImpl implements ManagerService {
    //注入ManagerAction
    @Depend
    ManagerAction managerAction;

    /**
     * 关闭bot的service
     * @param sender 送信器
     */
    @Override
    public void offBot(Sender sender, GroupMsg groupMsg) {
        managerAction.offBot(sender,groupMsg);
    }
    /**
     * AT全体的service
     */
    @Override
    public void atAllPeople(Sender sender, GroupMsg groupMsg) {
        managerAction.atAllPeople(sender, groupMsg);
    }

}
