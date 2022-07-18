package van.planifolia.action;

import catcode.CatCodeUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.common.utils.Carrier;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.assists.Flag;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;
import love.forte.simbot.filter.MatchType;
import van.planifolia.util.*;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

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

    /**
     * 随机St
     * @param groupMsg 群组消息
     * @param sender 送信器
     */
    @Override
    public void getRandomSt(GroupMsg groupMsg, Sender sender, Setter setter) {
        //如果色图锁锁上了我们就直接让他结束这个方法，以便节省资源
        if(!Constant.StLock){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"太快啦！10s才能请求一次!");
            return;
        }
        //两个计时器的对象实例，一个是撤回一个是解锁
        TimerPlus ChTimer=new TimerPlus(new Date());
        TimerPlus lockTimer=new TimerPlus(new Date());
        //上锁，10s才让访问一次
        Constant.StLock=false;
        //超时关锁的定时任务
        TimerTask stLockTask=new TimerTask() {
            @Override
            public void run() {
                Constant.StLock=true;
            }
        };
        lockTimer.schedule(stLockTask,10*1000);
        //下面开始进行url请求
        //先拿到消息正文中的tag
        String[] msgSplit= groupMsg.getText().split(" ");
        String tag;
        //拼接url进行请求,为什么要捕获一下异常呢？因为我们很可能没有携带tag那么我们直接捕获一个异常并且将tag字符串
        //置""然后在进行拼接
        try{
            tag=msgSplit[2];
        }catch (Exception e){
            tag="";
        }
        sender.sendGroupMsg(groupMsg.getGroupInfo(),"正在获取....");
        //变量作用域提升，一个是获得到的json对象，一个是网页返回的result
        JSONObject object = null;
        String result;
        //可能我们的HttpClient方法会失败做一个异常处理
        try{
            result = HttpClient.doGetMessage(ApiEnum.RandomSt.getValue());
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"上游API错误，获取失败");
            return;
        }
        //可能我们解析result为jsonObject
        try {
            object= JSONObject.parseObject(result);
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"解析网页返回值失败,可能是网络波动！");
        }
        //开始获取网页返回的json数据中的信息
        //这个url就是图片的源地址
        String originalUrl;
        try {
            //拿到杰森文件data里面的URL
            //拿到数组data
            JSONArray array = object.getJSONArray("data");
            //拿到data数组下标为0的对象
            JSONObject object1 = array.getJSONObject(0);
            //拿到该对象的url对象
            JSONObject object2 = object1.getJSONObject("urls");
            originalUrl= object2.getString("original");
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"JSON处理失败，可能是tag标签有误！");
            return;
        }
        MessageContent mct1 = null;
        //创建消息类型的flag方便撤回消息
        Carrier<? extends Flag<GroupMsg.FlagContent>> flag1 = null;
        try {
            //替换图片链接方便下载
            originalUrl=originalUrl.replace(".cat",".re");
            //下载图片
            String dwImage = SourceDownloader.downloadImg(originalUrl);
            File file=new File(dwImage);
            //猫猫码构建图片消息
            String image=catCodeUtil.toCat("image",true,"file="+file.getAbsolutePath());
            flag1=sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),"消息将在30s内撤回！"+image+"url:"+originalUrl);
            //发送完毕删除图片
            file.delete();
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"图片上传失败！可能是网络波动或者图片违规！");
            return;
        }
        //色图超时撤回，30s，虽然不知道为啥需要重新赋值一下但是idea推荐这样写
        Carrier<? extends Flag<GroupMsg.FlagContent>> finalFlag = flag1;
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                setter.setMsgRecall((MessageGet.MessageFlag<? extends MessageGet.MessageFlagContent>) finalFlag.get());
            }
        };
        ChTimer.schedule(timerTask,30*1000);
    }

    /**
     * 随机二次元图片
     * @param groupMsg
     * @param sender
     */
    @Override
    public void getRandomImageECY(GroupMsg groupMsg, Sender sender){
        try {
            MessageContentBuilder builder = messageContentBuilderFactory.getMessageContentBuilder();//消息构建器
            //使用HttpClient里面的方法获取到api网站里面的内容
            String result = HttpClient.doGetMessage(ApiEnum.RandomEcy.getValue());
            JSONObject object = JSONObject.parseObject(result);
            //拿到杰森文件imgurl后面的东西
            String imgurl = object.getString("imgurl");
            //下载图片
            String dwImage = SourceDownloader.downloadImg(imgurl);
            File file=new File(dwImage);
            //猫猫码构建图片消息
            String image=catCodeUtil.toCat("image",true,"file="+file.getAbsolutePath());
            MessageContent mct1=builder.at(groupMsg.getAccountInfo().getAccountCode()).build();
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),mct1);
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),image);
            file.delete();
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),"网络波动，获取壁纸失败！");
        }
    }

    /**
     * 随机Men图片
     * @param groupMsg
     * @param sender
     */
    @Override
    public void getRandomImageMen(GroupMsg groupMsg, Sender sender) {
        try {
            MessageContentBuilder builder = messageContentBuilderFactory.getMessageContentBuilder();//消息构建器
            //使用HttpClient里面的方法获取到api网站里面的内容
            //使用url对链接进行一次request
            URL url = new URL(ApiEnum.RandomMen.getValue());
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.getResponseCode();
            //请求后再次获取到跳转到的真实url
            String realUrl=conn.getURL().toString();
            conn.disconnect();
            //下载图片
            String dwImage = SourceDownloader.downloadImg(realUrl);
            File file=new File(dwImage);
            //猫猫码构建图片消息
            String image=catCodeUtil.toCat("image",true,"file="+file.getAbsolutePath());
            MessageContent mct1=builder.at(groupMsg.getAccountInfo().getAccountCode()).build();
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),mct1);
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),image);
            file.delete();
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),"因为网络问题Menhera并不想搭理你！");
        }
    }

    /**
     * 随机一言
     * @param groupMsg
     * @param sender
     */
    @Override
    public void getRandomHitokoto(GroupMsg groupMsg, Sender sender) {
        if (groupMsg.getText().equals("")||groupMsg.getText().equals(" ")) {
            try {
                //使用HttpClient里面的方法获取到api网站里面的内容
                String result = HttpClient.doGetMessage(ApiEnum.RandomHitokoto.getValue());
                JSONObject object = JSONObject.parseObject(result);
                //拿到杰森文件imgurl后面的东西
                String hitokoto = object.getString("hitokoto");
                String from = object.getString("from");
                sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "\u3000"+hitokoto +"\n"+ "FROM:" + from);
            } catch (Exception e) {
                sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "网络波动，获取words失败！");
            }
        }
    }

    /**
     * 历史上的今天
     * @param groupMsg
     * @param sender
     */
    @Override
    public void getTodayForHis(GroupMsg groupMsg, Sender sender) {
        //消息构建器
        MessageContentBuilder MsgBuilder = messageContentBuilderFactory.getMessageContentBuilder();//消息构建器
        String result = null;
        //获取当天的日期
        Date date = new Date();
        //日期标准化1得到格式 月日
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMdd");
        //日期标准化2得到格式 月-日
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM-dd");
        String nowDataNub = simpleDateFormat1.format(date);
        String nowData=simpleDateFormat2.format(date);
        MsgBuilder.text(nowData+"\n").build();
        //对API地址进行拼接
        String ApiUrl = ApiEnum.TodayInHis.getValue() + nowDataNub + ".json";
        try {
            //使用工具类获取历史上的今天json值
            result = HttpClient.doGetMessage(ApiUrl);
        } catch (Exception e) {
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "获取失败，可能上游API错误！");
        }
        //或缺杰森数组
        JSONArray jsonArray = JSONArray.parseArray(result);
        //便利杰森数组取到所有的 title 值，并且拼接消息正文
        for (int i = 0; i < jsonArray.size(); i++) {
            MsgBuilder.text(jsonArray.getJSONObject(i).getString("title") + "\n").build();
        }
        MessageContent messageContent=MsgBuilder.build();
        sender.sendGroupMsg(groupMsg.getGroupInfo(), messageContent);

    }

    /**
     * 获取随机头像
     * @param groupMsg
     * @param sender
     */
    @Override
    public void getRandomImageTx(GroupMsg groupMsg, Sender sender) {
        try {
            MessageContentBuilder builder = messageContentBuilderFactory.getMessageContentBuilder();//消息构建器

            //使用HttpClient里面的方法获取到api网站里面的内容
            String result = HttpClient.doGetMessage(ApiEnum.RandomTx.getValue());
            JSONObject object = JSONObject.parseObject(result);
            //拿到杰森文件imgurl后面的东西
            String imgurl = object.getString("imgurl");
            //下载图片
            String dwImage = SourceDownloader.downloadImg(imgurl);
            File file=new File(dwImage);
            //猫猫码构建图片消息
            String image=catCodeUtil.toCat("image",true,"file="+file.getAbsolutePath());
            MessageContent mct1=builder.at(groupMsg.getAccountInfo().getAccountCode()).build();
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),mct1);
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),image);
            file.delete();
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(),"网络波动，获取头像失败！");
        }
    }

    /**
     * 每日占卜
     * @param groupMsg
     * @param sender
     */
    @Override
    public void getDailyFortune(GroupMsg groupMsg, Sender sender) {
        MessageContentBuilder builder = messageContentBuilderFactory.getMessageContentBuilder();//消息构建器
        String requestCode=groupMsg.getAccountInfo().getAccountCode();//获取申请者的qq号
        //拿到网页里面的信息
        String result=HttpClient.doGetMessage(ApiEnum.Fortune.getValue()+requestCode);
        //获取其中的Json对象
        JSONObject object=JSONObject.parseObject(result);
        //获取杰森里面的data对象
        JSONObject data=object.getJSONObject("data");
        String luckyStar="";
        String signText="";
        String unSignText="";
        try{
            //开始获取data里面的内容
            luckyStar=data.getString("luckyStar");
            signText=data.getString("signText");
            unSignText=data.getString("unSignText");
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"上游API错误,获取信息失败！");
        }
        //@请求者
        MessageContent mc1=builder.at(requestCode).build();
        builder.clear();
        //将获得到的消息封装成MessageContent
        if(!("".equals(luckyStar)||"".equals(signText)||"".equals(unSignText))){
            MessageContent mc2=builder.text("今日运势："+luckyStar+"\n"+"\n").text("占卜结果："+signText+"\n"+"\n").text("人话："+unSignText).build();
            //送信器发送
            sender.sendGroupMsg(groupMsg.getGroupInfo(),mc1);
            sender.sendGroupMsg(groupMsg.getGroupInfo(),mc2);
        }
    }
}
