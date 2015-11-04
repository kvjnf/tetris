package org.kvjnf;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;

import org.kvjnf.BoardPanel;
import org.kvjnf.SidePanel;
import org.kvjnf.Clock;
import org.kvjnf.BlockType;


public class TetrisAkidai extends JFrame{
	
	private static final long serialVersionUID = -142869693720626412L;

	private static final long FRAME_TIME = 1000L / 50L;
	
	private static final int TYPE_COUNT = BlockType.values().length;
	
	/**
	 * The BoardPanel instance.
	 */ 
	private BoardPanel board;
	
	/**
	 * The SidePanel instance.
	 */
	private SidePanel side;
	
	private int score;
	
	/**
	 * gameがポーズされているかの判定
	 */
	private boolean isPaused;
	
	/**
	 * ブロックの現在のタイプ
	 */
	private BlockType currentType;
	
	/**
	 * ブロックの次のタイプ
	 */
	private BlockType nextType;
	
	/**
	 * ブロックが生成されてから、落とせるようになるまでの時間
	 */
	private int dropCooldown;
	
	/**
	 * ブロックの現在の縦列
	 */
	private int currentCol;
	
	/**
	 * ブロックの現在の横列
	 */
	private int currentRow;
	
	/**
	 * ブロックの現在の回転
	 */
	private int currentRotation;
	
	private Random random;
	
	private Clock logicTimer;
	
	private int level;
	
	private boolean isNewGame;
	
	private boolean isGameOver;
	
	private float gameSpeed;
	
	/**
	 * Tetris インスタンスの作成
	 * ウィンドウプロパティのセットアップと
	 * コントローラリスナーの追加
	 */
	private TetrisAkidai(){
		
		super("TetrisAkidai");//JFrameの引数
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		this.board = new BoardPanel(this);
		this.side  = new SidePanel(this);
		
		add(board, BorderLayout.CENTER);
		add(side, BorderLayout.EAST);
		
		/**
		 * カスタムキーリスナをフレームに追加
		 */
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e){
				
				switch(e.getKeyCode()){
					
				/**
				 * Move Drop
				 */
				case KeyEvent.VK_DOWN:
					if(!isPaused && dropCooldown == 0){
						logicTimer.setCyclesPerSecond(25.0f);
					}
					break;
				
				/**
				 * Move Left
				 */
				case KeyEvent.VK_LEFT:
					if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)){
						currentCol--;
					}
					break;
				
				/**
				 * Move Right
				 */
				case KeyEvent.VK_RIGHT:
					if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)){
						currentCol++;
					}
					break;
				
				/**
				 * 反時計回りに回転 0 3 2 1 0 3 2 1...
				 */
				case KeyEvent.VK_ALT:
					if(!isPaused){
						rotatePiece((currentRotation == 0) ? 3: currentRotation - 1);
					}
					break;
					
				/**
				 * 時計回りに回転 0 1 2 3 0 1 2 3...
				 */
				case KeyEvent.VK_SHIFT:
					if(!isPaused){
						rotatePiece((currentRotation == 3)? 0 : currentRotation + 1);
					}
					break;
					
				/**
				 * ゲームのPAUSE
				 */
				case KeyEvent.VK_P:
					if(!isGameOver && !isNewGame){
						isPaused = !isPaused;
						logicTimer.setPaused(isPaused);
					}
					break;
				
				/**
				 * ゲームのSTART
				 */
				case KeyEvent.VK_ENTER:
					if(isGameOver || isNewGame){
						resetGame();
					}
					break;
				}
				
			}
			
			@Override
			public void keyReleased(KeyEvent e){
				
				switch(e.getKeyCode()){
					
				case KeyEvent.VK_DOWN:
					logicTimer.setCyclesPerSecond(gameSpeed);
					logicTimer.reset();
					break;
				
				}
				
			}
			
		});
		
		//BoardPanel SidePanelのサイズを決めてから画面の中央に配置する
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public void startGame(){
		this.random = new Random();
		this.isNewGame = true;
		this.gameSpeed = 1.0f;
		
		this.logicTimer = new Clock(gameSpeed);
		logicTimer.setPaused(true);
		
		while(true){
			//フレームがスタートした時間の取得
			long start = System.nanoTime();
			
			//logic timerの時間を更新
			logicTimer.update();
			
			/**
			 * １サイクルが経過したか確認
			 * 経過していたら新しいブロックを落とす
			 */
			if(logicTimer.hasElapsedCycle()) {
				updateGame();
			}
			
			if(dropCooldown > 0){
				dropCooldown--;
			}
			
			renderGame();
			
			long delta = (System.nanoTime() - start) / 1000000L;//経過ミリ秒
			if(delta < FRAME_TIME){
				try{
					Thread.sleep(FRAME_TIME - delta);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private void updateGame(){
		/**
		 * ブロックの位置が次の行に下げられるかを判定する
		 */
		if(board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)){
			currentRow++;//trueならば、現在の行から次の行へ
		} else {
			
			/**
			 * falseなので、別のブロックか枠の底に到達したので
			 * 新たにブロックをおとす
			 */
			board.addPiece(currentType, currentCol, currentRow, currentRotation);
			
			/**
			 * ブロックの追加でラインが消えたかどうかの判定
			 * もし消えたラインがあるのならば
			 * プレイヤーのスコアをあげる
			 * [1 = 100pts, 2 = 200pts, 3 = 400pts, 4 = 800pts]
			 */
			int cleared = board.checkLines();
			if(cleared > 0){
				score += 50 << cleared; //消されたラインの数だけ左シフト
			}
			
			/**
			 * 次のブロックから僅かにスピードをあげる
			 */
			gameSpeed += 0.035f;
			logicTimer.setCyclesPerSecond(gameSpeed);
			logicTimer.reset();
			
			/**
			 * 次のブロックがすぐに落ちてこないようにバッファを持たせる
			 * (~0.5 second buffer).
			 */
			dropCooldown = 25;
			
			/**
			 * サイドパネルに表示する難易度をあげる
			 */
			level = (int)(gameSpeed * 1.70f);
			
			spawnPiece();
		}
	}
	
	/**
	 * 強制的に再描画させる
	 */
	private void renderGame(){
		board.repaint();
		side.repaint();
	}
	
	/**
	 * リセットしてニューゲーム状態に戻す
	 */
	private void resetGame(){
		this.level = 1;
		this.score = 0;
		this.gameSpeed = 1.0f;
		this.nextType = BlockType.values()[random.nextInt(TYPE_COUNT)];
		this.isNewGame = false;
		this.isGameOver = false;
		board.clear();
		logicTimer.reset();
		logicTimer.setCyclesPerSecond(gameSpeed);
		spawnPiece();
	}
	
	/**
	 * 新しいブロックを創る
	 * ブロックのための変数をリセットしてデフォルトの値を変える
	 */
	private void spawnPiece(){

		this.currentType = nextType;
		this.currentCol = currentType.getSpawnColumn();
		this.currentRow = currentType.getSpawnRow();
		this.currentRotation = 0;
		this.nextType = BlockType.values()[random.nextInt(TYPE_COUNT)];
		
		/**
		 * ブロックが生成された場所にブロックが存在する場合
		 * ゲームオーバーにする。
		 */
		if(!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)){
			this.isGameOver = true;
			logicTimer.setPaused(true);
		}
	}
	
	/**
	 * ブロックを回転させる
	 * @param newRotation
	 */
	private void rotatePiece(int newRotation) {
		int newColumn = currentCol;
		int newRow = currentRow;
		
		int left = currentType.getLeftInset(newRotation);
		int right = currentType.getRightInset(newRotation);
		int top = currentType.getTopInset(newRotation);
		int bottom = currentType.getBottomInset(newRotation);
		
		//現在のピースが左右のから離れすぎていた場合には端から動かす。
		if(currentCol < -left){
			newColumn -= currentCol - left;
		} else if(currentCol + currentType.getDimension() - right >= BoardPanel.COL_COUNT){
			newColumn -= (currentCol + currentType.getDimension() - right) - BoardPanel.COL_COUNT + 1;
		}
		
		//現在のピースが上下から離れすぎていた場合には端から動かす。
		if(currentRow < -top){
			newRow -= currentRow - top;
		} else if(currentRow + currentType.getDimension() - bottom >= BoardPanel.ROW_COUNT){
			newRow -= (currentRow + currentType.getDimension() - bottom) - BoardPanel.ROW_COUNT + 1;
		}
		
		//回転した時にぶつからないかどうか
		if(board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)){
			currentRotation = newRotation;
			currentRow = newRow;
			currentCol = newColumn;
		}
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public boolean isNewGame() {
		return isNewGame;
	}
	
	public boolean isGameOver() {
		return isGameOver;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getLevel() {
		return level;
	}
	
	public BlockType getPieceType() {
		return currentType;
	}
	
	public BlockType getNextPieceType() {
		return nextType;
	}
	
	public int getPieceCol() {
		return currentCol;
	}
	
	public int getPieceRow() {
		return currentRow;
	}
	
	public int getPieceRotation() {
		return currentRotation;
	}
	/**
	 * エントリーポイント
	 * 新規ゲームのスタートを行う
	 * @param args Unused
	 */
	public static void main(String[] args){
		TetrisAkidai tetris = new TetrisAkidai();
		tetris.startGame();
	}
	
}
