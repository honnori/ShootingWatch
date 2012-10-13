package jp.co.takes.apps.shootingwatch;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class ShootingWatchActivity extends Activity implements OnTouchListener {

	// デジタル数字の画像保持用の配列（０－９）
	public Bitmap digits[] =new Bitmap[11];
	public Bitmap digits_s[] =new Bitmap[11];
	
	// ボタンの押下状態フラグ
	private boolean[] buttonStatusList = {false, false, false, false, false};

	// シュウォッチのモード
	public enum Mode {
		SHOOTING,	// シューティングカウンターモード
		WATCH,		// 時計モード
		STOPWATCH,	// ストップウォッチモード
		MAX_VALUE	// 最高記録表示モード
	}
	
	private Map<Mode, Function> functions = new HashMap<Mode, Function>();

	// 現在のモード
	private Mode currentMode = Mode.SHOOTING;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.shooting_watch);
		
		// モード別機能を登録
		this.functions.put(Mode.SHOOTING, new FunctionShootingMode(this));
		this.functions.put(Mode.WATCH, new FunctionTime(this));
		this.functions.put(Mode.STOPWATCH, new FunctionStopWatch(this));
		this.functions.put(Mode.MAX_VALUE, new FunctionMaxValueDisplay(this));

		// ディスプレイのデジタル数字の生成
		this.createDigitalNumber();
		
		// モード初期処理実行
		this.functions.get(this.currentMode).OnclickSelectButton(null);

		// イベントリスナー設定
		this.setListener();
		
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// モードの終了処理
		this.functions.get(this.currentMode).finish();
	}

	/**
	 * 各種リスナ設定処理
	 */
	private void setListener() {
		// ABボタンのタッチイベントリスナー設定
		((Button)this.findViewById(R.id.A_button)).setOnTouchListener(this);
		((Button)this.findViewById(R.id.B_button)).setOnTouchListener(this);
	}

	/**
	 * ディスプレイのデジタル数字の生成
	 * 以降、setNumberDisp()メソッドにて文字を設定可能になる。
	 */
	private void createDigitalNumber() {
		
		Resources resource = this.getResources();
		this.digits[0] = BitmapFactory.decodeResource(resource, R.drawable.d0);
		this.digits[1] = BitmapFactory.decodeResource(resource, R.drawable.d1);
		this.digits[2] = BitmapFactory.decodeResource(resource, R.drawable.d2);
		this.digits[3] = BitmapFactory.decodeResource(resource, R.drawable.d3);
		this.digits[4] = BitmapFactory.decodeResource(resource, R.drawable.d4);
		this.digits[5] = BitmapFactory.decodeResource(resource, R.drawable.d5);
		this.digits[6] = BitmapFactory.decodeResource(resource, R.drawable.d6);
		this.digits[7] = BitmapFactory.decodeResource(resource, R.drawable.d7);
		this.digits[8] = BitmapFactory.decodeResource(resource, R.drawable.d8);
		this.digits[9] = BitmapFactory.decodeResource(resource, R.drawable.d9);
		this.digits[10] = BitmapFactory.decodeResource(resource, R.drawable.colon);
		
		this.digits_s[0] = BitmapFactory.decodeResource(resource, R.drawable.sd0);
		this.digits_s[1] = BitmapFactory.decodeResource(resource, R.drawable.sd1);
		this.digits_s[2] = BitmapFactory.decodeResource(resource, R.drawable.sd2);
		this.digits_s[3] = BitmapFactory.decodeResource(resource, R.drawable.sd3);
		this.digits_s[4] = BitmapFactory.decodeResource(resource, R.drawable.sd4);
		this.digits_s[5] = BitmapFactory.decodeResource(resource, R.drawable.sd5);
		this.digits_s[6] = BitmapFactory.decodeResource(resource, R.drawable.sd6);
		this.digits_s[7] = BitmapFactory.decodeResource(resource, R.drawable.sd7);
		this.digits_s[8] = BitmapFactory.decodeResource(resource, R.drawable.sd8);
		this.digits_s[9] = BitmapFactory.decodeResource(resource, R.drawable.sd9);
		
	}

	/**
	 * ディスプレイに指定したデジタル数字を表示する
	 * @param id 画像を設定先のImageViewのID
	 * @param number 表示する数字
	 */
	public void setNumberDisp(int id, int number) {
		ImageView display = (ImageView)this.findViewById(id);
		display.setImageBitmap(this.digits[number]);
	}

	/**
	 * ディスプレイに指定したデジタル数字を表示する(文字サイズ小)
	 * @param id 画像を設定先のImageViewのID
	 * @param number 表示する数字
	 */
	public void setMiniNumberDisp(int id, int number) {
		ImageView display = (ImageView)this.findViewById(id);
		display.setImageBitmap(this.digits_s[number]);
	}

	/**
	 * メインの4ケタ数字に文字設定
	 * @param number
	 */
	public void setMainDisp(int number){
		if ((0 <= number) && (number <= 9999)) {
			this.setNumberDisp(R.id.display1, number/1000);
			this.setNumberDisp(R.id.display2, (number/100)%10);
			this.setNumberDisp(R.id.display3, (number/10)%10);
			this.setNumberDisp(R.id.display4, number%10);
		}
		else {
			throw new IllegalArgumentException("number is illegal value.");
		}
	}

	/**
	 * サブの2ケタ数字に文字設定
	 * @param number
	 */
	public void setSubDisp(int number){
		if ((0 <= number) && (number <= 99)) {
			this.setMiniNumberDisp(R.id.display5, number/10);
			this.setMiniNumberDisp(R.id.display6, number%10);
		}
		else {
			throw new IllegalArgumentException("number is illegal value.");
		}
	}
	
	/**
	 * コロン表示/非表示　（時間、タイマー時に表示）
	 * @param flag
	 */
	public void viewColon(boolean flag) {
		((ImageView)this.findViewById(R.id.colon)).setImageResource(
				flag ? R.drawable.colon : R.drawable.colon_non);
		
	}
	
	/**
	 * モード表示
	 * @param mode
	 */
	public void setModeDisp(Mode mode) {

		// mode毎に表示/非表示を切り替え
		((ImageView)this.findViewById(R.id.bar_shoot)).setImageResource(
				Mode.SHOOTING.equals(mode) ? R.drawable.bar : R.drawable.bar_no);

		((ImageView)this.findViewById(R.id.bar_time)).setImageResource(
				Mode.WATCH.equals(mode) ? R.drawable.bar : R.drawable.bar_no);

		((ImageView)this.findViewById(R.id.bar_stop)).setImageResource(
				Mode.STOPWATCH.equals(mode) ? R.drawable.bar : R.drawable.bar_no);

		((ImageView)this.findViewById(R.id.bar_max)).setImageResource(
				Mode.MAX_VALUE.equals(mode) ? R.drawable.bar : R.drawable.bar_no);

	}

	/**
	 *  ABボタンク押下時に呼び出される
	 * @param view
	 */
	public void buttonOnClick(View view) {
		// ボタン押下で振動
		((Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(30);
		
		this.functions.get(this.currentMode).OnclickButton(view);
		
	}
	
	/**
	 * スタートボタン押下時に呼び出される
	 * @param view
	 */
	public void startOnClick(View view) {
		// ボタン押下で振動
		((Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
		this.functions.get(this.currentMode).OnclickStartButton(view);
	}

	/**
	 * セレクトボタン押下時に呼び出される
	 * @param view
	 */
	public void selectOnClick(View view) {
		// ボタン押下で振動
		((Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);

		// モード終了処理
		this.functions.get(this.currentMode).finish();
		
		// 次モード切替え
		if (this.currentMode == Mode.SHOOTING) {
			this.currentMode = Mode.WATCH;
		}
		else if (this.currentMode == Mode.WATCH) {
			this.currentMode = Mode.STOPWATCH;
		}
		else if (this.currentMode == Mode.STOPWATCH) {
			this.currentMode = Mode.MAX_VALUE;
		}
		else if (this.currentMode == Mode.MAX_VALUE) {
			this.currentMode = Mode.SHOOTING;
		}
		
		// 次モードの開始
		this.functions.get(this.currentMode).OnclickSelectButton(view);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		ShoButton button = (ShoButton)view;
		
		int action = event.getAction();
		int id = (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;

		int count = event.getPointerCount();
		Log.d("TouchEvent", "event.getPointerCount() : count = " + count);
//		int id = event.getPointerId(pointerIndex);

//		Log.d("TouchEvent", "getAction()" + "");

		switch(action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.d("TouchEvent", "getAction()" + "ACTION_DOWN" + " : id = " + id);
			
			// 既に片方が
			
			// ボタンダウン時にカウントアップする
			this.buttonOnClick(button);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			Log.d("TouchEvent", "getAction()" + "ACTION_UP" + " : id = " + id);
			break;
		case MotionEvent.ACTION_MOVE:
//			Log.d("TouchEvent", "getAction()" + "ACTION_MOVE" + " : id = " + id);
			
			for (int i=0; i < count; i++) {
				id = event.getPointerId(i);
				Log.d("TouchEvent", "getAction()" + "ACTION_MOVE" + " : id = " + id);
				// MOVEイベントもある条件を満たした場合はクリックとみなす
				// 擦り連打対応のため
				if(this.multiTapClickEvent(event, id, (ShoButton)view)) {
//					Log.d("TouchEvent", "getAction()" + "ACTION_CLICK");
					this.buttonOnClick(button);
				}
			}

			break;
		case MotionEvent.ACTION_CANCEL:
			Log.d("TouchEvent", "getAction()" + "ACTION_CANCEL");
			break;
		case MotionEvent.ACTION_OUTSIDE:
			Log.d("TouchEvent", "getAction()" + "ACTION_OUTSIDE");
			break;
		}

		return false;
	}
	
	
	/**
	 * アクションイベントが、ボタンクラス領域内でのイベントかどうかを判定する
	 * @param event
	 * @return true : ボタン内イベントの場合 false: ボタン外のイベント
	 */
	private boolean multiTapClickEvent(MotionEvent event, int id, ShoButton button) {
		boolean retFlag = false;
		ShoButton Abutton = (ShoButton)this.findViewById(R.id.A_button);
		ShoButton Bbutton = (ShoButton)this.findViewById(R.id.B_button);

		// 押下されたボタンの基準座標からのオフセットポジションを取得
		float xPos = event.getX(id);
		float yPos = event.getY(id);
		
		// DOWNされたボタンではない別ボタンを基点とした座標位置のオフセット値
		float xAnotherPos = 0;
		float yAnotherPos = 0;
		if (button.getId() == R.id.A_button) {
			xAnotherPos = xPos + (Abutton.getLeft() - Bbutton.getLeft());
			yAnotherPos = yPos + (Abutton.getTop() - Bbutton.getTop());
		}
		else if(button.getId() == R.id.B_button) {
			xAnotherPos = xPos - (Abutton.getLeft() - Bbutton.getLeft());
			yAnotherPos = yPos - (Abutton.getTop() - Bbutton.getTop());
		}

//		Log.d("TouchEvent", "this.getLeft(): " + button.getLeft() + "  event.getX(id) : " + event.getX(id));
//		Log.d("TouchEvent", "this.getTop() : " + button.getTop()  + "  event.getY(id) : " + event.getY(id));

		if ((button.isXPosRange(xPos) && button.isYPosRange(yPos)) 
			|| (button.isXPosRange(xAnotherPos) && button.isYPosRange(yAnotherPos))){
			// タッチされた位置が、いずれかのボタン範囲の場合
			if (this.buttonStatusList[id]) {
				// 既にボタンが押されている状態の場合、ただのボタン上で指をずらしただけなので
				// Downとみなさない.
				retFlag = false;
			}
			else {
				// ボタンが押されていない状態の場合に押された場合にtrueを返す
				retFlag = true;
				// ボタン押下状態に設定
				this.buttonStatusList[id] = true;
			}
		}
		else {
			// ABボタンの範囲外のため、
			this.buttonStatusList[id] = false;
		}
		
		return retFlag;
	}


	private int layoutMarginX = 0;
	private int layoutMarginY = 0;
	boolean once = true;

	private void setLayoutMargin() {
		// OnCreateなどハンドリングメソッドで実装すると、Viewの値がとれないので
		// OnTouchが呼ばれた一回目にだけ処理するようにする
		
		if(this.once) {
			// スクリーンのサイズを取得
			Display disp = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			int screenWidth = disp.getWidth();
			int screenHeight = disp.getHeight();
			
			// スクリーンの幅/高さから大元の親レイアウトの幅/高さを引いて、差分を取得する
			View shoLayout = (View)this.findViewById(R.id.ShoLayout1);
			
			this.layoutMarginX = screenWidth - shoLayout.getWidth();
			this.layoutMarginY = screenHeight - shoLayout.getHeight();
			
			this.once = false;
		}
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int action = event.getAction();
		int id = (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;

		int count = event.getPointerCount();

		// スクリーンサイズとレイアウトの座標差分値を算出して保持しておく
		this.setLayoutMargin();

		switch(action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.d("TouchEvent", "getAction()" + "■ACTION_DOWN");
			this.judgeWetherClick(event, count);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			Log.d("TouchEvent", "getAction()" + "■ACTION_UP");
			break;
		case MotionEvent.ACTION_MOVE:
//			Log.d("TouchEvent", "getAction()" + "■ACTION_MOVE");
			// ボタンの範囲外でDOWNしてMOVEでボタン内に入って生きた場合
			// MOVEアクションをある条件下でClickとみなす。
			this.judgeWetherClick(event, count);
			break;
		}

		return super.onTouchEvent(event);
	}


	private void judgeWetherClick(MotionEvent event, int count) {
		int id;
		for (int i = 0; i < count; i++) {
			id = event.getPointerId(i);
			Button button = this.isMoveeventForClick(event, id);
			if( button != null ) {
				this.buttonOnClick(button);
			}
		}
	}
	
	

	private ShoButton isMoveeventForClick(MotionEvent event, int id) {
		boolean retFlag = false;
		
		ShoButton clickableButton = null;
		
		ShoButton Abutton = (ShoButton)this.findViewById(R.id.A_button);
		ShoButton Bbutton = (ShoButton)this.findViewById(R.id.B_button);
		
		// 押下されたボタンの基準座標からのオフセットポジションを取得
		float xPos = event.getX(id);
		float yPos = event.getY(id);

		// Aボタンを基点とした座標位置のオフセット値
		float xAButtonPos = xPos - Abutton.getLeft() - this.layoutMarginX;
		float yAButtonPos = yPos - Abutton.getTop() - this.layoutMarginY;

//		Log.d("TouchEvent", "this.layoutMarginX : " + this.layoutMarginX);
//		Log.d("TouchEvent", "this.layoutMarginY : " + this.layoutMarginY);

		// Bボタンを基点とした座標位置のオフセット値
		float xBButtonPos = xPos - Bbutton.getLeft() - this.layoutMarginX;
		float yBButtonPos = yPos - Bbutton.getTop() - this.layoutMarginY;
		
		if (Abutton.isXPosRange(xAButtonPos) && Abutton.isYPosRange(yAButtonPos)) {
			clickableButton = Abutton;
		}
		else if (Bbutton.isXPosRange(xBButtonPos) && Bbutton.isYPosRange(yBButtonPos)){
			clickableButton = Bbutton;
		}
		
		if (clickableButton != null){
				// タッチされた位置が、いずれかのボタン範囲の場合
				if (this.buttonStatusList[id]) {
					// 既にボタンが押されている状態の場合、ただのボタン上で指をずらしただけなので
					// Downとみなさない.
					retFlag = false;
				}
				else {
					// ボタンが押されていない状態の場合に押された場合にtrueを返す
					retFlag = true;
					// ボタン押下状態に設定
					this.buttonStatusList[id] = true;
				}
			}
			else {
			// ABボタンの範囲外のため、
			this.buttonStatusList[id] = false;
		}
		
		return retFlag ? clickableButton : null;
	}

}
