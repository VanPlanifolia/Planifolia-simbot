package van.planifolia.service;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import van.planifolia.action.GroupToolAction;

@Beans
public class GroupToolServiceImpl implements GroupToolService{

    //引入action层
    @Depend
    GroupToolAction groupToolAction;


    @Override
    public void requestTimer(GroupMsg groupMsg, Sender sender) {
        groupToolAction.requestTimer(groupMsg,sender);
    }

    @Override
    public void timerList(GroupMsg groupMsg, Sender sender) {
        groupToolAction.timerList(groupMsg, sender);
    }

    @Override
    public void stopTimer(GroupMsg groupMsg, Sender sender) {
        groupToolAction.stopTimer(groupMsg, sender);
    }
}
