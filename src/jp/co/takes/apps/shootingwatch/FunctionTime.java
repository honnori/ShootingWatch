package jp.co.takes.apps.shootingwatch;

import java.util.Calendar;

import jp.co.takes.apps.shootingwatch.ShootingWatchActivity.Mode;
import android.os.Handler;
import android.view.View;

public class FunctionTime implements Function {

	private ShootingWatchActivity activity = null;
	
	// ハンドラーを取得
	private Handler mHandler = new Handler();

	// 定期的に呼び出されるためのRunnnableのインナークラス定義
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			// ディスプレイ表示
			viewCurrentTime();
			mHandler.postDelayed(mUpdateTimeTask, 1000);
		}
	};

	
	public FunctionTime(ShootingWatchActivity act) {
		super();
		this.activity = act;
	}

	@Override
	public void init() {
		this.viewCurrentTime();
		
		// 新たなハンドラを追加する前に、ハンドラにある既存のコールバックをすべて削除
		mHandler.removeCallbacks(mUpdateTimeTask);

		// Handler に対し、" 1000 ms 後に mUpdateTimeTask() を呼び出す
		mHandler.postDelayed(mUpdateTimeTask, 1000);

	}
	
	/**
	 * 現在時刻をディスプレイに表示する
	 */
	public void viewCurrentTime(){
		
		// 現在時刻を取得
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		// ディスプレイ表示領域に時刻を設定
		this.activity.setMainDisp(
				Integer.parseInt(String.format("%02d", hour) + String.format("%02d", minute)));
		this.activity.setSubDisp(second);
		this.activity.viewColon(true);
		
	}

	@Override
	public void OnclickButton(View view) {

	}

	@Override
	public void OnclickStartButton(View view) {

	}

	@Override
	public void OnclickSelectButton(View view) {
		// モードバーの表示切替え
		this.activity.setModeDisp(Mode.WATCH);
		this.init();
	}

	@Override
	public void finish() {
		// タイマー停止
		mHandler.removeCallbacks(mUpdateTimeTask);
		
		// 表示の初期化
		this.activity.setMainDisp(0);
		this.activity.setSubDisp(0);
		this.activity.viewColon(false);
	}

}
