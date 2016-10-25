package mediapick.testdemo;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by SEELE on 2016/10/6.
 */
public class MoneyTextView extends TextView {

    public MoneyTextView(Context context) {
        this(context, null, 0);
    }

    public MoneyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoneyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/trebuchetmsitalic.otf"));
    }
}
