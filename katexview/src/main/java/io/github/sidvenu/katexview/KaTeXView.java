package io.github.sidvenu.katexview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class KaTeXView extends WebView {

    String text, textColor = null;
    int textSize = -1;
    boolean clickable = false;

    public KaTeXView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // if text is set in XML, call setText() with that text
        TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.KaTeXView);
        String text = a.getString(R.styleable.KaTeXView_android_text);
        if (!TextUtils.isEmpty(text))
            setText(text);
        a.recycle();

        getSettings().setJavaScriptEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * Renders KaTeX code that is found in the passed-in string
     *
     * @param text Text that contains the KaTeX to be rendered
     */
    public void setText(String text) {
        this.text = text;
        String document = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/katex/katex.min.css\">\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/themes/style.css\">\n" +
                "script type=\"text/javascript\" src=\"file:///android_asset/katex/katex.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/katex/contrib/auto-render.min.js\"></script>\n" +
                "<style type='text/css'>" +
                "body {" +
                "margin: 0px;" +
                "padding: 0px;" +
                (textSize == -1 ? "" : "font-size:" + textSize + "pt;") +
                (TextUtils.isEmpty(textColor) ? "" : ("color:" + textColor + ";")) +
                " }" +
                "</style>" +
                "</head>\n" +
                "<body>\n" +
                text +
                "<script>\n" +
                "renderMathInElement(\n" +
                "   document.body\n" +
                ");\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        loadDataWithBaseURL("about:blank",
                document, "text/html", "utf-8", "");
    }

    /**
     * Returns the KaTeX code that was passed into using setText
     *
     * @return raw KaTeX code
     */
    public String getText() {
        return text;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return clickable;
    }
}
