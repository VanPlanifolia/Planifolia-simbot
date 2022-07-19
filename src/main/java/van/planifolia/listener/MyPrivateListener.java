package van.planifolia.listener;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import van.planifolia.util.Constant;

/**
 * 私聊监听器，相当于bot的后台管理系统
 */
@Beans
public class MyPrivateListener {

    @OnPrivate
    @Filter(value = "/群组列表", trim = true, matchType = MatchType.CONTAINS)
    @Filter(value = "/glist", trim = true, matchType = MatchType.CONTAINS)
    public void getGroupList(PrivateMsg privateMsg, Sender sender){
        StringBuilder builder=new StringBuilder();
        for (String s : Constant.groupList) {
            builder.append(s).append("\n");
        }
        sender.sendPrivateMsg(privateMsg.getAccountInfo(), builder.toString());
        builder.delete(0,builder.length());
    }
    @OnPrivate
    @Filter(value = "/test", trim = true, matchType = MatchType.STARTS_WITH)
    public void replyPrivateMsg1(PrivateMsg privateMsg, Sender sender){
        // 获取消息正文。
        MessageContent msgContent = privateMsg.getMsgContent();
        sender.sendPrivateMsg(privateMsg, msgContent);
        CatCodeUtil catCodeUtil = CatCodeUtil.getInstance();
        String cat3 = catCodeUtil.getStringTemplate().face(9);
        sender.sendPrivateMsg(privateMsg, "表情：" + cat3);
    }
}
