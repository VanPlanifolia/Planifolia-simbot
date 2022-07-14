package van.planifolia.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import van.planifolia.service.UrlRequestService;

/**
 * 群组监听的类，监听群组中的消息去调用对应的service相当于controller。
 * tips:需要注意的是simbot框架内置了一个简单的ioc容器，所有需要交给ioc容器管理的bean要使用@Beans注解标注，
 * 而获取这个bean则需要使用@Depend注解标注。
 * @author Van.Planifolia
 */
@Beans
public class GroupListener {
    //
    @Depend
    public UrlRequestService urlRequestService;
    /**
     * 复读的listener，做为示范
     * @param groupMsg 消息容器
     * @param sender 送信器
     */
    @OnGroup
    @Filter(value = "复读",matchType = MatchType.CONTAINS)
    public void repeat(GroupMsg groupMsg, Sender sender){
        urlRequestService.repeat(groupMsg, sender);
    }
}
