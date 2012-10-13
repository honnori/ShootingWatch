package jp.co.takes.apps.shootingwatch;

import jp.co.takes.apps.shootingwatch.ShootingWatchActivity.Mode;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FunctionShootingMode implements Function {

	private ShootingWatchActivity activity = null;
	
	private int maxValue = 0;

	// 連射時のカウンター
	private Integer counter = 0;

	// 連射時のカウントダウンタイマー
	private TenCountDown cdn = null;

	// カウントダウン開始フラグ
	private boolean startFlag = false;
	
	// MOVE時のABボタン無効フラグ
	private boolean buttonEnable = false;
	
	private  boolean isStart(){
		return this.startFlag;
	}

	/**
	 * コンストラクタ
	 * @param act
	 */
	public FunctionShootingMode(ShootingWatchActivity act) {
		super();
		this.activity = act;

		// 最大値を取り出す
		SharedPreferences pref = this.activity.getSharedPreferences("ShoWatch", Context.MODE_PRIVATE);
		this.maxValue = pref.getInt("maxValue", 0);

	}

	@Override
	public void init() {
		// 最大値を取り出す
		SharedPreferences pref = this.activity.getSharedPreferences("ShoWatch", Context.MODE_PRIVATE);
		this.maxValue = pref.getInt("maxValue", 0);
		
		// ABボタン押下を無効にする
		((Button)this.activity.findViewById(R.id.A_button)).setEnabled(false);
		((Button)this.activity.findViewById(R.id.B_button)).setEnabled(false);
		// MOVE時のABボタンも無効にする
		this.buttonEnable = false;
		
		// カウンタのリセット
		this.counter = 0;
		
		// タイマーが動いていたらキャンセルする
		if (this.cdn != null) {
			this.cdn.cancel();
		}
		
		// ディスプレイに数字表示
		this.activity.setMainDisp(this.counter);
		
		// コロン非表示
		this.activity.viewColon(false);

		// サブディスプレイの状態を初期状態にする
		this.activity.setSubDisp(0);

	}

	@Override
	public void OnclickButton(View view) {
		
		if (this.isStart()) {
			// カウントダウンタイマー開始
			this.cdn = new TenCountDown(10020, 1000, this.activity, this);
			this.cdn.start();
			this.startFlag = false;
		}
		
		
		if(this.buttonEnable == true) {
			// カウントアップ
			this.counter++;
			
			// ディスプレイに数字表示
			this.activity.setMainDisp(this.counter);
		}
	}

	@Override
	public void OnclickStartButton(View view) {
		
		// 初期化
		this.init();

		// カウントダウン開始フラグ
		this.startFlag = true;

		// ディスプレイ表示を開始状態にする
		this.activity.setSubDisp(10);
		
		// ボタンの有効化
		((Button)this.activity.findViewById(R.id.A_button)).setEnabled(true);
		((Button)this.activity.findViewById(R.id.B_button)).setEnabled(true);
		// MOVE時のABボタンも無効にする
		this.buttonEnable = true;

	}

	@Override
	public void OnclickSelectButton(View view) {
		// モードバーの表示切替え
		this.activity.setModeDisp(Mode.SHOOTING);
		this.init();
	}

	/**
	 *  カウントダウン終了時に呼ばれる
	 */
	public void shootingStop() {
		
		// ボタン押下を無効にする
		((Button)this.activity.findViewById(R.id.A_button)).setEnabled(false);
		((Button)this.activity.findViewById(R.id.B_button)).setEnabled(false);
		
		// MOVE時のABボタンも無効にする
		this.buttonEnable = false;

		// ディスプレイ表示を初期化
		this.activity.setSubDisp(0);

		// カウンタがMAXを超えた場合、MAX値として記録する
		if (this.maxValue < this.counter) {
			this.maxValue = this.counter;
			SharedPreferences pref = this.activity.getSharedPreferences("ShoWatch", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putInt("maxValue", this.maxValue);
			editor.commit();
		}
	}
	
	@Override
	public void finish() {
		this.init();
	}

}
