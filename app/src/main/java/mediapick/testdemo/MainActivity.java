package mediapick.testdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GiftRootLayout giftRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        giftRoot = (GiftRootLayout) findViewById(R.id.gift_root);

        findViewById(R.id.a_liwu_1).setOnClickListener(this);
        findViewById(R.id.a_liwu_2).setOnClickListener(this);
        findViewById(R.id.b_liwu_1).setOnClickListener(this);
        findViewById(R.id.b_liwu_2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.a_liwu_1:
                LiveGiftBean bean = new LiveGiftBean();
                bean.setGroup(1);
                bean.setSortNum(11);
                bean.setGiftImage(R.mipmap.ic_launcher);
                bean.setGiftName("礼物1");
                bean.setUserName("A");
                bean.setUserAvatar(R.mipmap.ic_launcher);
                giftRoot.loadGift(bean);
                break;
            case R.id.a_liwu_2:
                LiveGiftBean bean2 = new LiveGiftBean();
                bean2.setGroup(1);
                bean2.setSortNum(11);
                bean2.setGiftImage(R.mipmap.ic_launcher);
                bean2.setGiftName("礼物2");
                bean2.setUserName("A");
                bean2.setUserAvatar(R.mipmap.ic_launcher);
                giftRoot.loadGift(bean2);
                break;
            case R.id.b_liwu_1:
                LiveGiftBean bean3 = new LiveGiftBean();
                bean3.setGroup(1);
                bean3.setSortNum(11);
                bean3.setGiftImage(R.mipmap.ic_launcher);
                bean3.setGiftName("礼物1");
                bean3.setUserName("B");
                bean3.setUserAvatar(R.mipmap.ic_launcher);
                giftRoot.loadGift(bean3);
                break;
            case R.id.b_liwu_2:
                LiveGiftBean bean4 = new LiveGiftBean();
                bean4.setGroup(1);
                bean4.setSortNum(11);
                bean4.setGiftImage(R.mipmap.ic_launcher);
                bean4.setGiftName("礼物2");
                bean4.setUserName("B");
                bean4.setUserAvatar(R.mipmap.ic_launcher);
                giftRoot.loadGift(bean4);
                break;
        }
    }
}
