package van.planifolia.action;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import van.planifolia.util.TimerPlus;
import java.text.SimpleDateFormat;
import java.util.*;

@Beans
public class GroupToolActionImpl implements GroupToolAction{
    int timeLength;
    //    TimerPlus t[]=new TimerPlus[5];
    Map<String, TimerPlus> timeMap=new HashMap<>();
    int VacancyNumber=5;
    String TimerRemark=null;

    @Depend
    private MessageContentBuilderFactory builderFactory;

    @Override
    public void requestTimer(GroupMsg groupMsg, Sender sender) {
        if(VacancyNumber<=0){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"当前计时器队列已满，请稍后尝试！🥺");
            return;
        }
        //拿到消息字符串
        String MsgText=groupMsg.getText();
        //切割字符串
        String [] splits=MsgText.split(" ");
        try {
            //判断一下指令的第二个参数是否能够转化成int类型的
            timeLength = Integer.parseInt(splits[1]);
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"指令格式不对哦！使用格式/Timer 参数1 参数2，参数1：计时时长，参数2：任务名字");
        }
        try{
            //保存计时任务的名字，没有的话就抛一下异常
            TimerRemark=splits[2];
        }catch (Exception e){
            TimerRemark=null;
        } if(TimerRemark!=null){
            //获取日期
            Date dd=new Date();
            SimpleDateFormat sim=new SimpleDateFormat("HH:mm:ss");
            String time=sim.format(dd);
            MessageContentBuilder builder1 = builderFactory.getMessageContentBuilder();//消息构建器
            TimerPlus timerPlus=new TimerPlus(new Date());
            timerPlus.setName(TimerRemark);
            //为了方式timeMap中的任务被覆盖，我们就进行一次判断如果此时申请的任务key已经在map中存在我们就直接结束这个任务
            if (timeMap.containsKey(TimerRemark)){
                sender.sendGroupMsg(groupMsg.getGroupInfo(),"当前队列里面已经存在任务名为 '"+TimerRemark+"' 的Timer,请更换任务名或者不填写使用默认值！");
                return;
            }
            //否则就往map中压入这个timePlus
            timeMap.put(timerPlus.getName(),timerPlus);
            MessageContent msg1 = builder1.at(groupMsg.getAccountInfo().getAccountCode()).text("当前时间["+time+"]任务:"+TimerRemark+" 开始计时，" + timeLength + "分钟！").build();
            sender.sendGroupMsg(groupMsg.getGroupInfo(),msg1);
            //创建成功后计时器数量减一
            VacancyNumber--;
            //匿名内部类的方式去创建一个TimerTask
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    MessageContentBuilder builder2 = builderFactory.getMessageContentBuilder();//消息构建器
                    MessageContent msg1 = builder2.at(groupMsg.getAccountInfo().getAccountCode()).text("时间到了！").build();
                    sender.sendGroupMsg(groupMsg.getGroupInfo(),msg1);
                    //在任务结束后将对象置空
                    timeMap.remove(TimerRemark);
                    //空位加一
                    VacancyNumber++;
                }
            };
            //执行schedule后置空TimerRemark
            timeMap.get(TimerRemark).schedule(task,timeLength* 60000L);
            TimerRemark=null;
        }else {
            //获取日期
            Date dd=new Date();
            SimpleDateFormat sim=new SimpleDateFormat("HH:mm:ss");
            String time=sim.format(dd);
            MessageContentBuilder builder1 = builderFactory.getMessageContentBuilder();//消息构建器
            //创建一个tp对象保存到map中
            TimerPlus timerPlus=new TimerPlus(new Date());
            TimerRemark= String.valueOf(timerPlus.hashCode());
            //如果我们没有为任务命名就默认使用timerPlus的哈希值作为name
            timeMap.put(TimerRemark,timerPlus);
            //发送任务创建成功消息
            MessageContent msg1 = builder1.at(groupMsg.getAccountInfo().getAccountCode()).text("当前时间["+time+"]任务:"+timerPlus.hashCode()+" 开始计时，" + timeLength + "分钟！").build();
            sender.sendGroupMsg(groupMsg.getGroupInfo(),msg1);
            //创建成功后计时器数量减一
            VacancyNumber--;
            //创建TimerTask任务
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    MessageContentBuilder builder2 = builderFactory.getMessageContentBuilder();//消息构建器
                    MessageContent msg1 = builder2.at(groupMsg.getAccountInfo().getAccountCode()).text("时间到了！").build();
                    sender.sendGroupMsg(groupMsg.getGroupInfo(),msg1);
                    //在任务结束后将这个任务从map中移除
                    timeMap.remove(String.valueOf(timerPlus.hashCode()));
                    //空位加一
                    VacancyNumber++;
                }
            };
            //执行schedule后置空TimerRemark
            timeMap.get(TimerRemark).schedule(task,timeLength* 60000L);
        }
    }

    @Override
    public void timerList(GroupMsg groupMsg, Sender sender) {
        //使用set拿到所有的计时器key
        Set<String> set=timeMap.keySet();
        //判断是否为空
        if(set.isEmpty()){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"当前没有正在进行的⏲计时任务！");
        }
        //使用StringBuilder能够很好的节省资源，如果使用String常量的话每一次改变都会创建一个新的String对象
        StringBuilder stringBuilder=new StringBuilder();
        //遍历Set里面的值，追加到字符串中
        for (Object o : set) {
            stringBuilder.append("任务：").append(o).append('\n');
        }
        sender.sendGroupMsg(groupMsg.getGroupInfo(),stringBuilder.toString());
    }

    @Override
    public void stopTimer(GroupMsg groupMsg, Sender sender) {
        String name = null;
        if(VacancyNumber>=5){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"当前没有人计时！😋");
            return;
        }
        //拿到消息字符串
        String MsgText=groupMsg.getText();
        //切割字符串
        String [] splits=MsgText.split(" ");
        //拿到对应的任务name值
        try {
            name = splits[1];
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"指令格式不对哦！使用格式/stop 参数1，参数1：任务名称");
        }
        TimerPlus timerPlus=timeMap.get(name);
        if (timerPlus!=null){
            timerPlus.cancel();
            MessageContentBuilder builder3 = builderFactory.getMessageContentBuilder();//消息构建器
            MessageContent msg1 = builder3.at(groupMsg.getAccountInfo().getAccountCode()).text("任务:"+ timeMap.get(name).hashCode()+"已终止！").build();
            sender.sendGroupMsg(groupMsg.getGroupInfo(),msg1);
            //当然终止完任务不要忘记从map中移除这一个对象
            timeMap.remove(name);
            VacancyNumber++;
        }else {
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"任务"+name+"不存在");
        }
    }
}
