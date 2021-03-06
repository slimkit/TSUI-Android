package com.zhiyicx.tsui.widget.textview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.ImageSpan;

import com.zhiyicx.tsui.R;
import com.zhiyicx.tsui.utils.TSConvertUtils;

/**
 * @Author Jliuer
 * @Date 2017/07/15
 * @Email Jliuer@aliyun.com
 * @Description 控制图片位置
 */
public class TSCenterImageSpan extends ImageSpan {

    String text = "匿";
    boolean isText;
    private static final float DEFAULT_RATIO = 3.5f;
    private static int OFFSET = 20;

    private Drawable verified;
    private Context mContext;

    public TSCenterImageSpan(Context context, Bitmap b) {
        super(context, b);
        this.mContext = context;
    }

    public TSCenterImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
        this.mContext = context;
    }

    public TSCenterImageSpan(Drawable d) {
        super(d);
    }

    public TSCenterImageSpan(Drawable d, boolean isText) {
        super(d);
        this.isText = isText;
    }

    public TSCenterImageSpan(Drawable d, Drawable v, boolean isText) {
        super(d);
        this.verified = v;
        this.isText = isText;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt
            fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right + OFFSET;// x 偏移，不贴紧文字
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        Paint textP = new TextPaint(paint);
        Paint textB = new TextPaint(paint);
        if (isText) {
            textB.setColor(mContext.getResources().getColor(R.color.tsui_config_color_gray_7));
            textP.setColor(Color.WHITE);
            canvas.drawCircle(b.getBounds().centerX(), b.getBounds().centerY(), b.getBounds()
                    .right - b.getBounds().centerX()-2, textB);
            textP.setTextSize(TSConvertUtils.sp2px(mContext, 12));
            canvas.drawText("匿", b.getBounds().centerX() - textP.measureText("匿") / 2, b
                    .getBounds().centerY() - (textP.descent() + textP.ascent()) / 2, textP);
        } else {
            canvas.save();
            int transY = ((bottom - top) - b.getBounds().height()) / 2 + top;
            // y 轴居中对齐
            canvas.translate(x, transY);
            b.draw(canvas);

            if (verified != null) {
                float radio = b.getBounds().height() / DEFAULT_RATIO;
                float cx = (float) (radio / Math.sqrt(2));
                canvas.translate(b.getBounds().height() / 2 + cx, b.getBounds().height() / 2 + cx);
                verified.draw(canvas);
            }
            canvas.restore();
        }
    }

    @Override
    public Drawable getDrawable() {
        return super.getDrawable();
    }
}
