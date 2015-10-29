package org.kvjnf;

/**
 * {@code Clock}は経過時間を管理するためのクラスです。
 * @author akiyama_daisuke
 *
 */
public class Clock{
	
	/**
	 * pause状態の判定
	 */
	private boolean isPaused;
	
	/**
	 * 一サイクルを形成するミリセカンド
	 */
	private float millisPerCycle;
	
	/**
	 * 経過したサイクルの数
	 */
	private int elapsedCycles;
	
	/**
	 * 次の経過サイクルに対する超過時間
	 */
	private float excessCycles;
	
	/**
	 * clockが更新された最後の時間(デルタ時間を計算するために使われる)
	 */
	private long lastUpdate;
	
	public Clock(float cyclesPerSecond){
		//game Speed: cyclesPerSecond
		setCyclesPerSecond(cyclesPerSecond);
		reset();
	}
	
	public void setCyclesPerSecond(float cyclesPerSecond) {
		this.millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
	}
	
	public void reset() {
		this.elapsedCycles = 0;
		this.excessCycles = 0.0f;
		this.lastUpdate = getCurrentTime();
		this.isPaused = false;
	}
	
	/**
	 * 最終更新から現在までの経過したサイクルと超過サイクルを計算する
	 */
	public void update(){
		
		long currUpdate = getCurrentTime();
		//現在の時間から前回の時間の変化量を計算
		float delta = (float)(currUpdate - lastUpdate) + excessCycles;
		
		if(!isPaused){
			//経過サイクルの計算(切り下げ) ＝＞ 変化量 / ゲームサイクル(ミリ秒)
			this.elapsedCycles += (int)Math.floor(delta / millisPerCycle);
			//超過したサイクル(先ほどの時間で切り下げた分)を計算
			this.excessCycles = delta % millisPerCycle;
		}
		
		this.lastUpdate = currUpdate;
	}
	
	/**
	 * もう１サイクルが経過したかどうかの確認
	 * 経過したのであれば、１デクリメントする。
	 * @return
	 */
	public boolean hasElapsedCycle(){
		if(elapsedCycles > 0){
			this.elapsedCycles--;
			return true;
		}
		return false;
	}
	
	/**
	 * {@code System.getCurrentTimeMillis()}よりも正確に測定できる
	 * @return 現在のミリセカンドを返す
	 */
	private static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L);//Lはlong型の数値リテラル
	}
	
	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
}