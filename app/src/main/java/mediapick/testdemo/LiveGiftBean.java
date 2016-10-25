package mediapick.testdemo;

/**
 * Created by SEELE on 2016/9/23.
 * 礼物实体类
 */
public class LiveGiftBean {

    private String giftName;
    private int giftImage;
    private int userAvatar;
    private String userName;
    private int group;
    private long sortNum;

//    // 进场动画时间
//    private int time1 = 500;
//    // 数字动画时间
//    private int time2 = 200;
//    // 等待动画时间
//    private int time3 = 500;
//    // 消失动画时间
//    private int time4 = 200;

//    public int getTime1() {
//        return time1;
//    }
//
//    public void setTime1(int time1) {
//        this.time1 = time1;
//    }
//
//    public int getTime2() {
//        return time2;
//    }
//
//    public void setTime2(int time2) {
//        this.time2 = time2;
//    }
//
//    public int getTime3() {
//        return time3;
//    }
//
//    public void setTime3(int time3) {
//        this.time3 = time3;
//    }
//
//    public int getTime4() {
//        return time4;
//    }
//
//    public void setTime4(int time4) {
//        this.time4 = time4;
//    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public long getSortNum() {
        return sortNum;
    }

    public void setSortNum(long sortNum) {
        this.sortNum = sortNum;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getGiftImage() {
        return giftImage;
    }

    public void setGiftImage(int giftImage) {
        this.giftImage = giftImage;
    }

    public int getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(int userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
