package com.udacity.myappportfolio.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewGothamBook extends TextView {
	private static Typeface defaultTypeface;

	public TextViewGothamBook(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}

	public TextViewGothamBook(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	public TextViewGothamBook(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		// TODO Auto-generated constructor stub
	}

	private void init() {
		if (!isInEditMode()) {
			setTypeface(getDefaultTypeface());
		}
	}

	public  Typeface getDefaultTypeface() {
		if (defaultTypeface == null)
			defaultTypeface = Typeface.createFromAsset(
					getContext().getAssets(), "gothambook.ttf");
		return defaultTypeface;
	}
}
