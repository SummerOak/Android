/**
 * Created by chedifier on 2016/12/21.
 */


package example.chedifier.chedifier.bdls;

public class BDLSPortalConfiguration {

    public static final class PORTALS {
        public static final int POST_BAR    = 1;//贴吧
        public static final int NEWS        = 2;//新闻
        public static final int PICTURES    = 3;//图片
        public static final int ANSWERS     = 4;//知道
        public static final int BAIKE       = 5;//百科
        public static final int LIBRARY     = 6;//文库
        public static final int HAO123      = 7;//hao123
        public static final int VIDEOS      = 8;//视频
        public static final int BOARD       = 9;//风云榜
        public static final int JOKE        = 10;//笑话
        public static final int WEBSITE     = 11;//网址
        public static final int APP_MARKET  = 12;//应用
        public static final int LIFE        = 13;//生活
    }

    public static final BDLSPortalModel queryPortal(int id){
        switch (id){
            case PORTALS.POST_BAR:
                return new BDLSPortalModel(id,"贴吧","http://tieba.baidu.com");
            case PORTALS.NEWS:
                return new BDLSPortalModel(PORTALS.NEWS,"新闻","ext:info_flow_open_channel:ch_id=100&from=15");
            case PORTALS.PICTURES:
                return new BDLSPortalModel(PORTALS.PICTURES,"图片","http://image.baidu.com");
            case PORTALS.ANSWERS:
                return new BDLSPortalModel(PORTALS.ANSWERS,"知道","https://zhidao.baidu.com");
            case PORTALS.BAIKE:
                return new BDLSPortalModel(PORTALS.BAIKE,"百科","http://baike.baidu.com");
            case PORTALS.LIBRARY:
                return new BDLSPortalModel(PORTALS.LIBRARY,"文库","http://wk.baidu.com");
            case PORTALS.HAO123:
                return new BDLSPortalModel(PORTALS.HAO123,"hao123","http://wap.hao123.com");
            case PORTALS.VIDEOS:
                return new BDLSPortalModel(PORTALS.VIDEOS,"视频","http://Tv.uc.cn");
            case PORTALS.BOARD:
                return new BDLSPortalModel(PORTALS.BOARD,"风云榜", "http://top.baidu.com/m/");
            case PORTALS.JOKE:
                return new BDLSPortalModel(PORTALS.JOKE,"笑话","http://Qiqu.uc.cn");
            case PORTALS.WEBSITE:
                return new BDLSPortalModel(PORTALS.WEBSITE,"网址","http://Hao.uc.cn");
            case PORTALS.APP_MARKET:
                return new BDLSPortalModel(PORTALS.APP_MARKET, "应用","http://m.pp.cn");
            case PORTALS.LIFE:
                return new BDLSPortalModel(PORTALS.LIFE,"生活","go.uc.cn/page/life/life?source=web#!/meituan");
        }

        return null;
    }

}
