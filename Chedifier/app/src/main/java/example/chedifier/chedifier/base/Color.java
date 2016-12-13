package example.chedifier.chedifier.base;

/**
 * Created by chedifier on 2016/11/17.
 */
public enum Color {

    DEFAULT(0xff000000,"黑色"),

    LIGHT_BLUE(0xffeff4ff,"浅蓝"),
    BLUE(0xff2d89ef,"蓝"),
    DARK_BLUE(0xff2b5797,"深蓝"),
    YELLOR(0xffffc40d,"黄"),
    ORANGE(0xffe3a21a,"橙"),
    DARK_PURPLE(0xff603cba,"深紫"),
    TEAL(0xff00aba9,"青"),
    GREEN(0xff00a300,"绿"),
    DARK_GREEN(0xff1e7145,"深绿"),
    MAGENTA(0xffff0097,"水红");


    public static Color valueOf(int value){
        for(Color color:Color.values()){
            if(color.value == value){
                return color;
            }
        }

        return DEFAULT;
    }


    Color(int value,String desc){
        this.value = value;
        this.desc = desc;
    }

    public int value;
    public String desc;
}
