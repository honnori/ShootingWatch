package jp.co.takes.apps.shootingwatch;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


public class ShoRelativeLayout extends RelativeLayout {

	public ShoRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ShoRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ShoRelativeLayout(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		// ここでtrueを返せば親ViewのonTouchEvent
		// ここでfalseを返せば子ViewのonClickやらonLongClickやら
//		return false;
		return super.onInterceptTouchEvent(ev);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		
//		switch(event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			Log.d("TouchEvent", "getAction()" + "▲ACTION_DOWN");
//			break;
//		case MotionEvent.ACTION_UP:
//			Log.d("TouchEvent", "getAction()" + "▲ACTION_UP");
//			break;
//		case MotionEvent.ACTION_MOVE:
//			Log.d("TouchEvent", "getAction()" + "▲ACTION_MOVE");
//			break;
//		case MotionEvent.ACTION_CANCEL:
//			Log.d("TouchEvent", "getAction()" + "▲ACTION_CANCEL");
//			break;
//		case MotionEvent.ACTION_OUTSIDE:
//			Log.d("TouchEvent", "getAction()" + "▲ACTION_OUTSIDE");
//			break;
//		}
//
//		return super.onTouchEvent(event);
//	}
	
	

}
