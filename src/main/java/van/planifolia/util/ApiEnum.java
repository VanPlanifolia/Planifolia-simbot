package van.planifolia.util;

/**
 * 仅仅是想要使用枚举类而创建的这个Api枚举类，可以很方便的取到我们对应的api信息。
 */
public enum ApiEnum {
    //随机音乐的Api
    RandomMusic("RandomMusic","https://api.uomg.com/api/rand.music&format=json"),
    //随机st的Api
    RandomSt("RandomSt","https://api.lolicon.app/setu/v2?size=original"),
    //随机二次元的APi
    RandomEcy("RandomEcy","http://api.mtyqx.cn/api/random.php?return=json"),
    //随机Men酱表情包
    RandomMen("RandomMen","https://api.ixiaowai.cn/mcapi/mcapi.php"),
    //随机一言
    RandomHitokoto("RandomHitokoto","https://v1.hitokoto.cn"),
    //历史上的今天api
    TodayInHis("TodayInHis","https://zhufred.gitee.io/zreader/ht/event/"),
    //随机头像api
    RandomTx("RandomTx","http://api.btstu.cn/sjtx/api.php?lx=c1&format=json"),
    //每日运势api
    Fortune("Fortune","https://api.fanlisky.cn/api/qr-fortune/get/"),
    //点歌api
    SongMessage("SongMessage","https://music.cyrilstudio.top/search?keywords=MUSICNAME&limit=20"),
    //获取源的api
    SongSourceSpApi("songSourceSpApi","http://music.163.com/song/media/outer/url?id=SONGID.mp3"),
    //万能搜索的api
    SourceSearch("SourceSearch","https://saucenao.com/search.php?output_type=2&api_key=724da2fccba6a6c519cd0998a1cca6228935ec1a&numres=3&url="),
    //新闻的api
    DailyNews("DailyNews","https://api.vvhan.com/api/60s"),


    ;

    String key;
    String value;

    ApiEnum(String key,String value) {
        this.key=key;
        this.value=value;
    }
    public String getValue(){
        return this.value;
    }
}
