package van.planifolia.service;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

/**
 * Url请求service的具体实现类，里面如果涉及dao操作则直接调用dao否则直接实现对应功能
 * @author Van.Planifolia
 */
@Beans
public class UrlRequestServiceImpl implements UrlRequestService{

    @Override
    public void getRandomMusic(GroupMsg groupMsg, Sender sender) {

    }

    @Override
    public void repeat(GroupMsg groupMsg, Sender sender) {
        sender.sendGroupMsg(groupMsg.getGroupInfo(),groupMsg.getMsgContent());
    }
}
