package mediapick.testdemo;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class GiftItemLayout extends LinearLayout implements Handler.Callback, Animation.AnimationListener {

    private final String TAG = GiftItemLayout.class.getSimpleName();

    private final Handler handler = new Handler(this);

    private ImageView headIcon, giftIcon;
    private TextView nameText, infoText, countText;

    public int show_time = 2000; // 礼物展示时间
    private int count, num = 1; // 礼物连击数,当前播放连击数
    private String currentTag; // 当前TAG
    private boolean isDoubleAnim = false; // 是否是连击动画,是否存在连击消息

    /**
     * 显示状态
     * Activity.DEFAULT_KEYS_DISABLE -- 等待中
     * Activity.DEFAULT_KEYS_DIALER -- 正在显示
     * Activity.DEFAULT_KEYS_SHORTCUT -- 显示结束或者未显示
     */
    private int currentType = Activity.DEFAULT_KEYS_DISABLE;

    /**
     * 显示位置
     * Activity.DEFAULT_KEYS_DIALER -- 1
     * Activity.DEFAULT_KEYS_SHORTCUT -- 2
     */
    private int currentPosition = Activity.DEFAULT_KEYS_DIALER;

    /**
     * 透明度动画(200ms), 连击动画(200ms)
     */
    private Animation translateAnim, numAnim;

    /**
     * 动画结束监听
     */
    private GiftAnimListener animListener;

    public GiftItemLayout(Context context) {
        this(context, null);
    }

    public GiftItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        View.inflate(context, R.layout.custom_gift2, this);
        headIcon = (ImageView) findViewById(R.id.image_left_gift_head_icon);
        nameText = (TextView) findViewById(R.id.txt_left_gift_username);
        infoText = (TextView) findViewById(R.id.txt_left_gift_name);
        giftIcon = (ImageView) findViewById(R.id.image_left_gift_icon);
        countText = (TextView) findViewById(R.id.txt_left_gift_count);

        translateAnim = new TranslateAnimation(-300, 0, 0, 0);
        translateAnim.setDuration(200);
        translateAnim.setFillAfter(true);
        translateAnim.setAnimationListener(this);

        numAnim = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        numAnim.setDuration(200);
        numAnim.setAnimationListener(this);

        if (null == attrs) return;
        TypedArray typed = context.obtainStyledAttributes(attrs, R.styleable.GiftItemLayout, 0, 0);
        if (null == typed) return;
        currentPosition = typed.getInteger(R.styleable.GiftItemLayout_gift_index, Activity.DEFAULT_KEYS_DIALER);
        typed.recycle();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            // 结束动画
            case Activity.DEFAULT_KEYS_SEARCH_LOCAL:
                // 移除所有消息
                handler.removeCallbacksAndMessages(null);

                currentType = Activity.DEFAULT_KEYS_SHORTCUT;
                if (animListener == null) break;
                animListener.giftAnimEnd(currentPosition);
                break;
            // 检测连击动画数量
            case Activity.DEFAULT_KEYS_SHORTCUT:
                if (count > num) {
                    // 执行连击动画
                    handler.sendEmptyMessage(Activity.DEFAULT_KEYS_SEARCH_GLOBAL);
                } else {
                    // 发送结束动画消息
                    if (!handler.hasMessages(Activity.DEFAULT_KEYS_SEARCH_LOCAL))
                        handler.sendEmptyMessageDelayed(Activity.DEFAULT_KEYS_SEARCH_LOCAL, show_time);
                }
                break;
            // 连击动画
            case Activity.DEFAULT_KEYS_SEARCH_GLOBAL:
                num++;
                countText.setText(getContext().getString(R.string.txt_gift_multiplication) + num);
                countText.startAnimation(numAnim);
                break;
        }
        return true;
    }

    /**
     * 设置数据
     */
    public void setData(LiveGiftBean data) {

        currentTag = data.getUserName() + data.getGiftName();

        // 用户头像
        Glide.with(getContext()).load(data.getUserAvatar()).into(headIcon);
        // 礼物图片
        Glide.with(getContext()).load(data.getGiftImage()).into(giftIcon);

        num = 1;
        count = data.getGroup();
        isDoubleAnim = count > num ? true : false;
        nameText.setText(data.getUserName());
        infoText.setText(getContext().getString(R.string.txt_gift_give_one) + data.getGiftName());
        countText.setText(getContext().getString(R.string.txt_gift_multiplication) + num);
    }

    /**
     * 开始动画
     */
    public void startAnimation() {
        giftIcon.startAnimation(translateAnim);
        currentType = Activity.DEFAULT_KEYS_DIALER;
    }

    /**
     * 获取当前显示礼物TAG
     */
    public String getCurrentTag() {
        return currentTag;
    }

    /**
     * 增加礼物数量,用于连击效果
     */
    public void addCount(int count) {

        this.count += count;
        if (this.count == 1 || isDoubleAnim) return;

        isDoubleAnim = true;
        handler.sendEmptyMessageDelayed(Activity.DEFAULT_KEYS_SHORTCUT, 200);
    }

    /**
     * 获取当前显示状态
     */
    public int getCurrentType() {
        return currentType;
    }

    /**
     * 更新当前显示状态
     */
    public void setCurrentType(int status) {
        this.currentType = status;
    }

    /**
     * 动画完成监听
     */
    public void setGiftAnimationListener(GiftAnimListener animListener) {
        this.animListener = animListener;
    }

    /**
     * 设置item显示位置
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * 获取ite显示位置
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void onAnimationEnd(Animation animation) {

        // 透明度动画
        if (animation == translateAnim) {

            giftIcon.clearAnimation();
            countText.startAnimation(numAnim);
        }

        // 连击动画, 不是连击动画
        else {

            // 连击动画
            if (isDoubleAnim) {

                // 没有执行完
                if (count > num) {
                    handler.sendEmptyMessage(Activity.DEFAULT_KEYS_SEARCH_GLOBAL);
                }
                // 执行完毕
                else {
                    isDoubleAnim = false;
                    handler.sendEmptyMessage(Activity.DEFAULT_KEYS_SHORTCUT);
                }
            }

            // 不是连击动画
            else
                handler.sendEmptyMessageDelayed(Activity.DEFAULT_KEYS_SEARCH_LOCAL, show_time);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
