package van.planifolia.action;

import catcode.CatCodeUtil;
import com.alibaba.fastjson.JSONObject;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import van.planifolia.util.ApiEnum;
import van.planifolia.util.HttpClient;
import van.planifolia.util.SourceDownloader;
import van.planifolia.util.VoiceSender;
import java.io.File;

/**
 * Action类的具体实现类，里面用于处理Listener中的请求并且发送消息.
 * @author Van.Planifolia
 */
@Beans
public class UrlRequestActionImpl implements UrlRequestAction{
    @Depend
    //自动注入消息构建器工厂与猫猫码工厂
    private MessageContentBuilderFactory messageContentBuilderFactory;
    CatCodeUtil catCodeUtil = CatCodeUtil.getInstance();//使用猫猫码
    /**
     * 随机音乐
     * @param groupMsg 群组消息
     * @param sender 送信器
     */
    @Override
    public void getRandomMusic(GroupMsg groupMsg, Sender sender) {
        MessageContentBuilder builder=messageContentBuilderFactory.getMessageContentBuilder();
        //使用HttpClient里面的方法获取到api网站里面的内容
        System.out.println(ApiEnum.RandomMusic.getValue());
        String result = HttpClient.doGetMessage(ApiEnum.RandomMusic.getValue());
        //拿到杰森对象
        JSONObject object = JSONObject.parseObject(result);
        //拿到杰森对象中的data对象
        JSONObject objectData=object.getJSONObject("data");
        //拿到杰森data对象里面的一系列参数
        String nameValue = objectData.getString("name");
        String musicValue=objectData.getString("url");
        String picValue=objectData.getString("picurl");
        String artValue=objectData.getString("artistsname");
        //下载图片
        String dwImage = SourceDownloader.downloadImg(picValue);
        File file=new File(dwImage);
        //猫猫码构建图片消息
        String image=catCodeUtil.toCat("image",true,"file="+file.getAbsolutePath());
        MessageContent mct1=builder.at(groupMsg.getAccountInfo().getAccountCode()).build();
        sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),mct1);
        sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),image+" "+nameValue+"--"+artValue+"URL:"+musicValue);
        file.delete();
        //调用工具类里面的发送语音方法
        VoiceSender.getMusicSources(groupMsg,sender,nameValue,musicValue,artValue);
    }
}
