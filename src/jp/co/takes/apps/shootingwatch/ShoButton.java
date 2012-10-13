package jp.co.takes.apps.shootingwatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

public class ShoButton extends Button {
	
	private Context context = null;

	public ShoButton(Context context) {
		super(context);
		this.context = context;
	}

	public ShoButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public ShoButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public boolean isXPosRange(float xPos) {
		boolean retFlag = false;
		
//		Log.d("TouchEvent", "xPos: " + xPos + "  ★MAX_X : " + this.getWidth());
		
		if ((0 <= xPos) && (xPos <= this.getWidth())){
			retFlag = true;
		}
		
		return retFlag;
	}

	public boolean isYPosRange(float yPos) {
		boolean retFlag = false;
		
//		Log.d("TouchEvent", "yPos: " + yPos + "  ★MAX_Y : " + this.getHeight());
		
		if ((0 <= yPos) && (yPos <= this.getHeight())){
			retFlag = true;
		}
		
		return retFlag;
	}

	// ボタンの押下状態フラグ
	private boolean buttonStatus = false;

	/**
	 * Moveイベントのうちある条件を満たした場合、ボタンクリックとみなすメソッド
	 * @param event
	 * @return true : ボタンクリックあり false: ボタンクリックなし
	 */
	public boolean isMoveClickButton(MotionEvent event, int id) {
		boolean retFlag = false;

		// ボタンの基準座標からのオフセットポジションを取得
		float xPos = event.getX(id)- this.getLeft();
		float yPos = event.getY(id)- this.getTop();
		
		// MOVEイベントがボタン上で発生している場合に押下していると判断してtrueにする
		// ボタンの範囲から外れた場合ボタンUPしたものと認識して、押下フラグをfalseにする
		if (this.isXPosRange(xPos) && this.isYPosRange(yPos)) {

			if (this.buttonStatus) {
				// 既にボタンが押されている状態の場合、ただのボタン上で指をずらしただけなので
				// Downとみなさない.
				retFlag = false;
			}
			else {
				// ボタンが押されていない状態の場合に押された場合にtrueを返す
				retFlag = true;
				// ボタン押下状態に設定
				this.buttonStatus = true;
			}
		}
		else {
			// 範囲外なのでボタンがUPしたと判断する
			this.buttonStatus = false;
		}
		
		return retFlag;
	}

}
