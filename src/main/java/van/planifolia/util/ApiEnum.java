package van.planifolia.util;

/**
 * 仅仅是想要使用枚举类而创建的这个Api枚举类，可以很方便的取到我们对应的api信息。
 */
public enum ApiEnum {
    //随机音乐的Api
    RandomMusic("RandomMusic","https://api.uomg.com/api/rand.music&format=json");
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
