package van.planifolia.action;

import catcode.CatCodeUtil;
import catcode.Neko;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.common.utils.Carrier;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.assists.Flag;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;
import van.planifolia.util.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

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

    //点歌的计时器
    TimerPlus songTimer=new TimerPlus();
    public synchronized void song(GroupMsg groupMsg,Sender sender) {
        //一次新的请求先终止之前的回收器,然后清理上一次的请求信息
        if (songTimer.getStartTime()!=null){
            songTimer.cancel();
            songTimer=new TimerPlus();
        }
        Constant.songidList.clear();
        //api的url
        String songUrl=ApiEnum.SongMessage.getValue();
        //消息正文
        String msgContext= groupMsg.getText();
        //音乐的name，音乐资源链接，
        String musicName=null;
        String songSourceUrl="";
        String musicurl="";
        String musicid="";
        String [] msgsplit=msgContext.split(" ");
        //消息构建器
        MessageContentBuilder mcBuilder = messageContentBuilderFactory.getMessageContentBuilder();
        //处理字符串与标签
        try{
            musicName=msgsplit[1];
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"格式不对哦，点歌格式为/点歌+空格+歌曲名字");
            return;
        }
        if(musicName!=null && !musicName.isEmpty()){
            songUrl=songUrl.replace("MUSICNAME",musicName);
        }else {
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"歌曲名字非法！");
            return;
        }
        //拿到网页里面的信息
        String songResult=HttpClient.doGetMessage(songUrl);
        //获取其中的Json对象
        JSONObject object=JSONObject.parseObject(songResult);
        //获取杰森里面的data对象中的songs数组
        JSONObject data=object.getJSONObject("result");
        JSONArray songs=data.getJSONArray("songs");
        JSONObject song = null;
        String[] musicMsg=new String[songs.size()+1];
        //遍历拿到所有的歌曲信息
        mcBuilder.text("歌曲列表：\n");
        StringBuilder musicMsgBuilder=new StringBuilder();
        for (int i = 0; i < songs.size(); i++) {
            //拿到song对象
            song=songs.getJSONObject(i);
            //构建消息String[]
            musicMsgBuilder.append(i + 1).append(".").append(song.getString("name")).append("-").append(song.getJSONArray("artists").getJSONObject(0).getString("name")).append("\n");
            //吧每一次构建的歌曲消息保存到对应的字符数组里
            musicMsg[i]=musicMsgBuilder.toString();
            //往list里面添加一个歌曲id
            Constant.songidList.add(song.getString("id"));
            //清空string builder
            musicMsgBuilder.delete(0, musicMsgBuilder.length());
        }
        musicMsg[songs.size()]="\n  请@我,并且回复数字编号获取歌曲。";
        //调取工具类去获取bufferedImage对象
        BufferedImage image = StringToImage.stringToImg(musicMsg);
        //创建一个临时的文件
        File file=new File(FileSystemView.getFileSystemView() .getHomeDirectory().getAbsolutePath()+"\\temp.jpg");
        //使用imageOutPutStream类来完成图片的输出
        try {
            ImageOutputStream ios= ImageIO.createImageOutputStream(file);
            ImageIO.write(image,"jpg",ios);
            //构建猫猫码。发送，关闭流删除文件
            String imageNeko=catCodeUtil.toCat("image",true,"file="+file.getAbsolutePath());
            sender.sendGroupMsg(groupMsg.getGroupInfo(),imageNeko);
            ios.close();
        }catch (Exception e){}
        //开启点歌监听锁
        Constant.songLock=true;
        //加入定时清理器
        String finalMusicName = musicName;
        songTimer.setStartTime(new Date());
        TimerTask songTask=new TimerTask() {
            @Override
            public void run() {
                sender.sendGroupMsg(groupMsg.getGroupInfo(),"歌单："+ finalMusicName +" 已被清理");
                //清理id列表，关锁
                Constant.songidList.clear();
                songTimer.setStartTime(null);
                Constant.songLock=false;
            }
        };
        //15分钟自动销毁
        songTimer.schedule(songTask,1000*15*60);
    }

    /**
     * 发送点歌的方法
     */
    public void sendMusic(GroupMsg groupMsg,Sender sender){
        String songSourceSpApi=ApiEnum.SongSourceSpApi.getValue();
        System.out.println("进入方法");
        //如果锁为关闭状态就直接退出
        if(!Constant.songLock){
            System.out.println("锁关闭了!");
            return;
        }else {
            System.out.println("进入获取方法");
            int id;
            try {
                id = Integer.parseInt(groupMsg.getText().trim());
            }catch (Exception e){return;}
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"正在获取...");
            //拿到对应的歌曲id
            String realId = Constant.songidList.get(id - 1);
            //拿到歌曲的下载地址
            String songSourceUrl=songSourceSpApi.replace("SONGID",realId);
            VoiceSender.getMusicSources(groupMsg, sender,songSourceUrl);
        }
    }

    /**
     * 按图片查询资源
     */
    public void searchSourceForImage(GroupMsg groupMsg,Sender sender){
        Map<String,String> tempMap=new HashMap<>();
        MessageContentBuilder builder = messageContentBuilderFactory.getMessageContentBuilder();//消息构建器
        //api的地址
        String apiUrl=ApiEnum.SourceSearch.getValue();
        //获取消息正文里面的图片信息
        List<Neko> cats = groupMsg.getMsgContent().getCats();
        //按照指令格式我们可以拿到index为1的Neko码也就是图片的neko码，（这样写显然不够优雅）
        Neko neko = null;
        try {
            neko = cats.get(1);
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"使用格式不对奥，请在指令后面添加一个图片，只支持合并发送");
        }
        //拿到图片的url我们就可以进行请求操作了
        assert neko != null;
        String imageUrl=neko.get("url");
        //拿到结果转化成json对象
        String result=HttpClient.doGetMessage(apiUrl+imageUrl);
        JSONObject allResult=JSONObject.parseObject(result);
        JSONArray resultsArray = allResult.getJSONArray("results");
        JSONObject trageObject= (JSONObject) resultsArray.get(0);
        //拿到检索结果的头信息
        JSONObject headerObject=trageObject.getJSONObject("header");
        //匹配百分比
        String[] similarities = headerObject.getString("similarity").split("\\.");
        int pers = Integer.parseInt(similarities[0]);
        if (pers<=50){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"匹配度过低！");
            return;
        }
        tempMap.put("similarity", headerObject.getString("similarity"));
        //图片img
        String getImgUrl=headerObject.getString("thumbnail").replace("\u0026","&");
        //拿到data的json对象
        JSONObject dataObject=trageObject.getJSONObject("data");
        JSONArray ext_urls = dataObject.getJSONArray("ext_urls");
        //压入第0个番剧信息的数组内容
        tempMap.put("ext_url",ext_urls.getString(0));
        //压入资源信息的名字
        tempMap.put("source",dataObject.getString("source"));
        //如果为视频的话我们就往map中存入在那一集哪一个时刻
        try {
            tempMap.put("part",dataObject.getString("part"));
            tempMap.put("est_time", dataObject.getString("est_time"));
        }catch (Exception e){}
        //构建消息
        MessageContent mc=
                builder.text("检索结果：\n")
                        .text("匹配度："+tempMap.get("similarity")+"%\n")
                        .text("标题："+tempMap.get("source")+"\n")
                        .imageUrl(getImgUrl)
                        .text("位置："+tempMap.get("part")+"--"+tempMap.get("est_time")+"\n")
                        .text("详细信息："+tempMap.get("ext_url")).build();
        sender.sendGroupMsg(groupMsg.getGroupInfo(),mc);
    }

    /**
     * 按照名字搜索动漫播放链接
     * @param groupMsg
     * @param sender
     */
    TimerPlus dmTimer=new TimerPlus(new Date());
    @Override
    public void serachCartoonForName(GroupMsg groupMsg, Sender sender) {
        //一次新的请求先终止之前的回收器,然后清理上一次的请求信息
        if (dmTimer.getStartTime()!=null){
            dmTimer.cancel();
            dmTimer=new TimerPlus();
        }
        String apiUrl=ApiEnum.YhCartoon.getValue();
        //变量提升作用域
        String dmName = null;
        //取到搜索的动漫名
        String[] split = groupMsg.getText().split(" ");
        try{
            dmName=split[1];
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"指令格式不对奥，指令格式为：指令+空格+需要搜索的番剧名！");
            return;
        }
        Constant.dmUrl=apiUrl.replace("DMNAME",dmName);
        String result=HttpClient.doGetMessage(Constant.dmUrl);
        result=result.replace("——————樱花动漫——————","搜索结果:");
        sender.sendGroupMsg(groupMsg.getGroupInfo(),result+"\n请@我并且发送对应的编号来获取内容");
        Constant.dmLock=true;
        songTimer.setStartTime(new Date());
        String finalDmName = dmName;
        TimerTask songTask=new TimerTask() {
            @Override
            public void run() {
                sender.sendGroupMsg(groupMsg.getGroupInfo(),"动漫："+ finalDmName +" 已被清理");
                //清理番剧url，关锁
                Constant.dmUrl=null;
                dmTimer.setStartTime(null);
                Constant.dmLock=false;
            }
        };
        //2分钟自动销毁
        songTimer.schedule(songTask,1000*2*60);
    }

    @Override
    public void sendCartoon(GroupMsg groupMsg, Sender sender) {
        String id = groupMsg.getText().trim();
        if(Constant.dmLock){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),HttpClient.doGetMessage(Constant.dmUrl+id));
        }
    }
}
