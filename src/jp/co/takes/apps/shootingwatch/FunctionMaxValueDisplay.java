package jp.co.takes.apps.shootingwatch;

import jp.co.takes.apps.shootingwatch.ShootingWatchActivity.Mode;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FunctionMaxValueDisplay implements Function {

	private ShootingWatchActivity activity = null;
	
	private int maxValue = 0;

	public FunctionMaxValueDisplay(ShootingWatchActivity act) {
		super();
		this.activity = act;
	}

	@Override
	public void init() {
		// ABボタン押下を無効にする
		((Button)this.activity.findViewById(R.id.A_button)).setEnabled(false);
		((Button)this.activity.findViewById(R.id.B_button)).setEnabled(false);

		// 最大値を取り出す
		SharedPreferences pref = this.activity.getSharedPreferences("ShoWatch", Context.MODE_PRIVATE);
		this.maxValue = pref.getInt("maxValue", 0);
		
		// 表示の初期化
		this.activity.setMainDisp(this.maxValue);
		this.activity.viewColon(false);
		
	}

	@Override
	public void OnclickButton(View view) {
		// 何もしない
	}

	@Override
	public void OnclickStartButton(View view) {
		// 何もしない
	}

	@Override
	public void OnclickSelectButton(View view) {
		// モードバーの表示切替え
		this.activity.setModeDisp(Mode.MAX_VALUE);
		this.init();
	}

	@Override
	public void finish() {
		// 何もしない
	}

}
