package com.lyneon.customview;

import android.widget.TextView;
import android.content.Context;
import android.util.AttributeSet;
import android.text.TextUtils;
import android.graphics.Canvas;

public class MarqueeTextView extends TextView {

    private int speed; // 滚动的速度，单位为字符/秒

    public MarqueeTextView(Context context) {
        super(context);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setSingleLine(true); // 设置单行显示
        setEllipsize(TextUtils.TruncateAt.MARQUEE); // 设置跑马灯效果
        setFocusable(true); // 设置可获得焦点
        setMarqueeRepeatLimit(-1); // 设置无限重复
        speed = 0; // 默认速度为0字符/秒
    }

    @Override
    public boolean isFocused() {
        return true; // 始终返回true，让跑马灯效果一直运行
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        scrollBy(speedToInterval(speed), 0); // 根据速度滚动文本位置
    }

    private int speedToInterval(int speed) {
        if (speed <= 0) return 0; // 如果速度小于等于0，则不滚动
        float textWidth = getPaint().measureText(getText().toString()); // 获取文本宽度
        float viewWidth = getWidth(); // 获取视图宽度
        if (textWidth <= viewWidth) return 0; // 如果文本宽度小于等于视图宽度，则不滚动
        float interval = textWidth / speed * 1000 / (textWidth + viewWidth); // 计算每个字符滚动所需的时间间隔，单位为毫秒
        return (int) interval;
    }

    public void setSpeed(int speed) {
        this.speed = speed; // 设置滚动的速度，单位为字符/秒 
        invalidate(); // 刷新视图 
    }
}
