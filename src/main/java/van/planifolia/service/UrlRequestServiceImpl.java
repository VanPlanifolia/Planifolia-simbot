package van.planifolia.service;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import van.planifolia.action.UrlRequestAction;

/**
 * Url请求service的具体实现类,这个模块只负责调用action与dao来完成请求。
 * @author Van.Planifolia
 */
@Beans
public class UrlRequestServiceImpl implements UrlRequestService{
    //注入action的依赖
    @Depend
    UrlRequestAction urlRequestAction;

    /**
     * 随机音乐
     */
    @Override
    public void getRandomMusic(GroupMsg groupMsg, Sender sender) {
        urlRequestAction.getRandomMusic(groupMsg, sender);
    }

    @Override
    public void repeat(GroupMsg groupMsg, Sender sender) {
        sender.sendGroupMsg(groupMsg.getGroupInfo(),groupMsg.getMsgContent());
    }
}
