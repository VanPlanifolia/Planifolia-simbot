package van.planifolia.action;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;

/**
 * Url请求的action接口，里面定义了所有的url的请求的action方法
 */

public interface UrlRequestAction {

    void getRandomMusic(GroupMsg groupMsg, Sender sender);
}
