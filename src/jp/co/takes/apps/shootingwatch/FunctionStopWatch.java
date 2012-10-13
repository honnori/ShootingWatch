package jp.co.takes.apps.shootingwatch;

import java.util.Calendar;

import jp.co.takes.apps.shootingwatch.ShootingWatchActivity.Mode;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class FunctionStopWatch implements Function {

	private ShootingWatchActivity activity = null;
	
	// ハンドラーを取得
	private Handler mHandler = new Handler();

	// 定期的に呼び出されるためのRunnnableのインナークラス定義
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			// カウンターをUP
			timeCounter++;
			// ディスプレイ表示
			viewCountTime();
			mHandler.postDelayed(mUpdateTimeTask, 10);
		}
	};

	// タイマー状態フラグ
	private boolean timer = false;

	public FunctionStopWatch(ShootingWatchActivity act) {
		super();
		this.activity = act;
	}

	@Override
	public void init() {
		
		// カウントタイマーのリセット
		this.resetCountTime();
		
		// ボタンの有効化
		((Button)this.activity.findViewById(R.id.A_button)).setEnabled(true);
		((Button)this.activity.findViewById(R.id.B_button)).setEnabled(true);
	}

	@Override
	public void OnclickButton(View view) {
		// ボタンが押下されたら
		if(this.timer) {
			// タイマーがすでに開始している場合はカウント停止
			this.stopCountTime();
		}
		else {
			// タイマーが止まっている場合はカウント開始
			this.startCountTime();
		}
	}

	@Override
	public void OnclickStartButton(View view) {
		// カウントタイマーのリセット
		this.resetCountTime();
	}

	@Override
	public void OnclickSelectButton(View view) {
		// モードバーの表示切替え
		this.activity.setModeDisp(Mode.STOPWATCH);
		this.init();
	}

	@Override
	public void finish() {
		this.stopCountTime();
	}
	
	long timeCounter = 0;

	/**
	 * カウントタイムをディスプレイに表示する
	 */
	public void viewCountTime(){
		
		// 現在時刻を取得
//		Calendar calendar = Calendar.getInstance();
////		int hour = calendar.get(Calendar.HOUR_OF_DAY);
//		int minute = calendar.get(Calendar.MINUTE);
//		int second = calendar.get(Calendar.SECOND);
//		int ms = calendar.get(Calendar.MILLISECOND);
		
		
		final long totaltime = this.timeCounter *10;
//		int hour = (int) (totaltime / (1000*60*60));
		int minute = (int)(totaltime / (1000*60)%60);
		int second = (int) (totaltime / 1000)%60;
		int ms = (int)(totaltime % 1000);

		
		// ディスプレイ表示領域に時刻を設定
		this.activity.setMainDisp(
				Integer.parseInt(String.format("%02d", minute) + String.format("%02d", second)));
		this.activity.setSubDisp(ms/10);
		this.activity.viewColon(true);
		
	}
	
	/**
	 * カウント開始
	 */
	private void startCountTime(){
		// 新たなハンドラを追加する前に、ハンドラにある既存のコールバックをすべて削除
		mHandler.removeCallbacks(mUpdateTimeTask);

		// Handler に対し、" 10 ms 後に mUpdateTimeTask() を呼び出す
		mHandler.postDelayed(mUpdateTimeTask, 10);
		
		this.timer = true;
		
	}
	
	/**
	 * カウント停止
	 */
	private void stopCountTime(){
		mHandler.removeCallbacks(mUpdateTimeTask);
		this.timer = false;
	}

	/**
	 * カウントリセット
	 */
	private void resetCountTime(){
		this.stopCountTime();
		this.timeCounter = 0;
		
		// ディスプレイ表示領域に時刻を設定
		this.activity.setMainDisp(0);
		this.activity.setSubDisp(0);
	}


}
