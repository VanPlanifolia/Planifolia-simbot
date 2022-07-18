package van.planifolia.action;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import van.planifolia.util.Constant;

/**
 * 管理器接口的实现类用于处理具体的管理请求
 */
@Beans
public class ManagerActionImpl implements ManagerAction {
    @Depend
    //自动注入消息构建器工厂与猫猫码工厂
    private MessageContentBuilderFactory messageContentBuilderFactory;

    @Override
    public void offBot(Sender sender, GroupMsg groupMsg) {
        sender.sendGroupMsg(groupMsg.getGroupInfo(),"正在关机....");
        Constant.BotState=false;
    }

    @Override
    public void atAllPeople(Sender sender, GroupMsg groupMsg) {
        String messageContext;
        MessageContentBuilder builder1 = messageContentBuilderFactory.getMessageContentBuilder();//消息构建器调用at()
        String[] messageSplit = groupMsg.getText().split(" ");//拿到消息text并且split切片
        try {
            messageContext=messageSplit[1];
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),"指令格式不对哦\uD83D\uDE44，使用方法 指令+空格+需要@全体的消息正文");
            return;
        }
        try {
            MessageContent msg = builder1.atAll().text(" "+messageContext+"。队长：").at(groupMsg.getAccountInfo().getAccountCode()).build();
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), msg);
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),"很抱歉今天的@全体次数用完了");
            return;
        }
    }
}
