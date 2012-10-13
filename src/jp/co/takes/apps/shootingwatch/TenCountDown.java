package jp.co.takes.apps.shootingwatch;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * シューティングモード機能使用時のカウントダウンタイマー
 *
 */
public class TenCountDown extends CountDownTimer {

	// メインアクティビティ参照
	private ShootingWatchActivity activity = null;

	private FunctionShootingMode func = null;

	// 表示するカウントダウン数字
	private int countNum = 10;

	/**
	 * コンストラクタ
	 * @param millisInFuture
	 * @param countDownInterval
	 */
	public TenCountDown(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}
	 
	public TenCountDown(long millisInFuture, long countDownInterval, ShootingWatchActivity activity, FunctionShootingMode func) {
		super(millisInFuture, countDownInterval);
		this.activity = activity;
		this.func = func;
	}

	@Override
	public void onFinish() {

		// カウンターを初期化
		this.countNum = 10;
		
		this.func.shootingStop();
	}

	@Override
	public void onTick(long millisUntilFinished) {
		// コンストラクタで指定したcountDownInterval間隔で呼ばれるメソッド
		// カウントダウン表示する
		this.activity.setSubDisp(this.countNum);
		this.countNum--;
	}

}
