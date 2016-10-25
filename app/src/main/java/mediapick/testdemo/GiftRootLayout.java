package mediapick.testdemo;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.Comparator;
import java.util.TreeMap;

public class GiftRootLayout extends LinearLayout implements GiftAnimListener, Animation.AnimationListener {

    private final String TAG = GiftRootLayout.class.getSimpleName();

    private GiftItemLayout firstGiftLayout, secondGiftLayout;
    private Animation firstGiftEnterAnim, secondGiftEnterAnim, firstGiftExitAnim, secondGiftExitAnim;

    private final TreeMap<Long, LiveGiftBean> treeMap = new TreeMap<>(new Comparator<Long>() {

        @Override
        public int compare(Long o1, Long o2) {
            return o2.compareTo(o1);
        }
    });

    public GiftRootLayout(Context context) {
        this(context, null);
    }

    public GiftRootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        firstGiftEnterAnim = AnimationUtils.loadAnimation(context, R.anim.anim_gift_left_in);
        firstGiftEnterAnim.setFillAfter(true);
        secondGiftEnterAnim = AnimationUtils.loadAnimation(context, R.anim.anim_gift_left_in);
        secondGiftEnterAnim.setFillAfter(true);

        firstGiftExitAnim = AnimationUtils.loadAnimation(context, R.anim.anim_gift_left_out);
        firstGiftExitAnim.setFillAfter(true);
        secondGiftExitAnim = AnimationUtils.loadAnimation(context, R.anim.anim_gift_left_out);
        secondGiftExitAnim.setFillAfter(true);

        // 添加动画结束监听
        firstGiftExitAnim.setAnimationListener(this);
        secondGiftExitAnim.setAnimationListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!changed || getChildCount() == 0) return;

        firstGiftLayout = (GiftItemLayout) findViewById(R.id.gil_left_gift_top);
        secondGiftLayout = (GiftItemLayout) findViewById(R.id.gil_left_gift_bottom);

        secondGiftLayout.setGiftAnimationListener(this);
        firstGiftLayout.setGiftAnimationListener(this);

        if (firstGiftLayout != null)
            firstGiftLayout.getCurrentPosition();

        if (secondGiftLayout != null)
            secondGiftLayout.getCurrentPosition();
    }

    @Override
    public void giftAnimEnd(int index) {
        switch (index) {
            case Activity.DEFAULT_KEYS_DISABLE:
                firstGiftLayout.startAnimation(firstGiftExitAnim);
                break;
            case Activity.DEFAULT_KEYS_DIALER:
                secondGiftLayout.startAnimation(secondGiftExitAnim);
                break;
        }
    }

    /**
     * 礼物退出动画监听
     */
    @Override
    public void onAnimationEnd(Animation animation) {
        if (secondGiftLayout.getCurrentType() == Activity.DEFAULT_KEYS_SHORTCUT) {
            secondGiftLayout.setVisibility(View.INVISIBLE);
            secondGiftLayout.setCurrentType(Activity.DEFAULT_KEYS_DISABLE);
        }

        if (firstGiftLayout.getCurrentType() == Activity.DEFAULT_KEYS_SHORTCUT) {
            firstGiftLayout.setVisibility(View.INVISIBLE);
            firstGiftLayout.setCurrentType(Activity.DEFAULT_KEYS_DISABLE);
        }

        showGift();
    }

    /**
     * 显示礼物
     */
    public void showGift() {

        if (isEmpty()) return;

        if (firstGiftLayout.getCurrentType() == Activity.DEFAULT_KEYS_DISABLE) {

            LiveGiftBean gift = getGift();

            firstGiftLayout.setData(gift);
            firstGiftLayout.setVisibility(View.VISIBLE);
            firstGiftLayout.startAnimation(secondGiftEnterAnim);
            firstGiftLayout.startAnimation();
        } else if (secondGiftLayout.getCurrentType() == Activity.DEFAULT_KEYS_DISABLE) {

            LiveGiftBean gift = getGift();

            secondGiftLayout.setData(gift);
            secondGiftLayout.setVisibility(View.VISIBLE);
            secondGiftLayout.startAnimation(firstGiftEnterAnim);
            secondGiftLayout.startAnimation();
        }
    }

    /**
     * 加入礼物
     */
    public void loadGift(LiveGiftBean data) {

        if (treeMap == null) return;

        String tag = data.getUserName() + data.getGiftName();

        if (firstGiftLayout.getCurrentType() == Activity.DEFAULT_KEYS_DIALER && firstGiftLayout.getCurrentTag().equals(tag)) {

            firstGiftLayout.addCount(data.getGroup());
            return;
        }

        if (secondGiftLayout.getCurrentType() == Activity.DEFAULT_KEYS_DIALER && secondGiftLayout.getCurrentTag().equals(tag)) {

            secondGiftLayout.addCount(data.getGroup());
            return;
        }

        addGift(data);
    }

    private void addGift(LiveGiftBean data) {

        if (treeMap == null) return;

        // 队列为空时, 显示动画
        if (treeMap.size() == 0) {
            treeMap.put(data.getSortNum(), data);
            showGift();
            return;
        }

        // 相同礼物, 并且同一用户送的, 连击数+1
        for (Long key : treeMap.keySet()) {

            LiveGiftBean result = treeMap.get(key);

            String tagNew = data.getUserName() + data.getGiftName();
            String tagOld = result.getUserName() + result.getGiftName();

            if (tagNew.equals(tagOld)) {

                data.setGroup(result.getGroup() + 1);
                treeMap.remove(result.getSortNum());
                treeMap.put(data.getSortNum(), data);
                return;
            }
        }

        treeMap.put(data.getSortNum(), data);
    }

    /**
     * 取出礼物
     */
    private LiveGiftBean getGift() {
        LiveGiftBean gift = null;
        if (treeMap.size() != 0) {
            // 获取队列首个礼物实体
            gift = treeMap.firstEntry().getValue();
            // 移除队列首个礼物实体
            treeMap.remove(treeMap.firstKey());
        }
        return gift;
    }

    /**
     * 清除所有礼物
     */
    public void cleanAll() {
        secondGiftLayout.setCurrentType(Activity.DEFAULT_KEYS_SHORTCUT);
        firstGiftLayout.setCurrentType(Activity.DEFAULT_KEYS_SHORTCUT);
        if (treeMap == null) return;
        treeMap.clear();
    }

    /**
     * 礼物是否为空
     */
    public boolean isEmpty() {
        return (treeMap == null || treeMap.size() == 0) ? true : false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }
}
