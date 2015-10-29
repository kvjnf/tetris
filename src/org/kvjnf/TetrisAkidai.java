package org.kvjnf;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;

import org.kvjnf.BoardPanel;
import org.kvjnf.SidePanel;
import org.psnbtech.Clock;
import org.psnbtech.TileType;


public class TetrisAkidai extends JFrame{
	
	/**
	 * The BoardPanel instance.
	 */
	private BoardPanel board;
	
	/**
	 * The SidePanel instance.
	 */
	private SidePanel side;
	
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
	
	private boolean isNewGame;
	
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
						
					}
					break;
				
				/**
				 * Move Left
				 */
				case KeyEvent.VK_LEFT:
					break;
				
				/**
				 * Move Right
				 */
				case KeyEvent.VK_RIGHT:
					break;
				
				/**
				 * 反時計回りに回転
				 */
				case KeyEvent.VK_ALT:
					break;
					
				/**
				 * 時計回りに回転
				 */
				case KeyEvent.VK_SHIFT:
					break;
					
				/**
				 * ゲームのPAUSE
				 */
				case KeyEvent.VK_P:
					break;
				
				/**
				 * ゲームのSTART
				 */
				case KeyEvent.VK_ENTER:
					break;
				
				}
				
			}
			
			@Override
			public void keyReleased(KeyEvent e){
				
				switch(e.getKeyCode()){
					
				case KeyEvent.VK_DOWN:
					break;
				
				}
				
			}
			
		});
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
		}
	}
	
	private void updateGame(){
		
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public boolean isNewGame() {
		return isNewGame;
	}
	/**
	 * エントリーポイント
	 * 新規ゲームのスタートを行う
	 * @param args Unused
	 */
	public static void main(String args){
		TetrisAkidai tetris = new TetrisAkidai();
		tetris.startGame();
	}
	
}
