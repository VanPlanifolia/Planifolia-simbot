package van.planifolia.action;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

/**
 * Action类的具体实现类，里面用于处理Listener中的请求并且发送消息.
 * @author Van.Planifolia
 */
@Beans
public class UrlRequestActionImpl implements UrlRequestAction{
    /**
     * 随机音乐
     * @param groupMsg 群组消息
     * @param sender 送信器
     */
    @Override
    public void getRandomMusic(GroupMsg groupMsg, Sender sender) {

    }
}
