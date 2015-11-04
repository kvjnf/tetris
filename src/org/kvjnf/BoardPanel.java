package org.kvjnf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.kvjnf.BlockType;

public class BoardPanel extends JPanel {

	private static final long serialVersionUID = -1842342739248458931L;

	public static final int COLOR_MIN = 35;
	
	public static final int COLOR_MAX = 255 - COLOR_MIN;
	
	/**
	 * ゲームの周りを囲う線の幅
	 */
	private static final int BORDER_WIDTH = 5;
	
	/**
	 * タテ列
	 */
	public static final int COL_COUNT = 10;
	
	private static final int VISIBLE_ROW_COUNT = 20;
	
	private static final int HIDDEN_ROW_COUNT = 2;
	
	/**
	 * 横の行の最終合計値
	 */
	public static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
	
	public static final int BLOCK_SIZE = 24;
	
	public static final int SHADE_WIDTH = 4;
	
	/**
	 * ゲーム盤におけるx座標の中央値
	 */
	private static final int CENTER_X = COL_COUNT * BLOCK_SIZE / 2;
	
	/**
	 * ゲーム盤におけるy座標の中央値
	 */
	private static final int CENTER_Y = VISIBLE_ROW_COUNT * BLOCK_SIZE / 2; 
	
	/**
	 * ブロックの幅の総計 
	 */
	public static final int PANEL_WIDTH = COL_COUNT * BLOCK_SIZE + BORDER_WIDTH * 2;
	
	/**
	 * ブロックの高さの総計
	 */
	public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * BLOCK_SIZE + BORDER_WIDTH * 2;
	
	/**
	 * 大きなフォント
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);
	
	/**
	 * 小さなフォント
	 */
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);
	
	
	private TetrisAkidai tetris;
	
	private BlockType[][] blocks;
	
	
	public BoardPanel(TetrisAkidai tetris){
		this.tetris = tetris;
		this.blocks = new BlockType[ROW_COUNT][COL_COUNT];
		
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.BLACK);
	}
	
	// ゲーム画面のリセット
	public void clear() {
		for (int i = 0; i < ROW_COUNT; i++) {
			for (int j = 0; j < COL_COUNT; j++) {
				blocks[i][j] = null;
			}
		}
	}
	
	/**
	 * 指定された座標にブロックを配置できるかの判定をする。
	 * @param type
	 * @param x
	 * @param y
	 * @param rotation
	 * @return
	 */
	public boolean isValidAndEmpty(BlockType type, int x, int y, int rotation){
		
		if(x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT){
			return false;
		}
		
		if(y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= ROW_COUNT){
			return false;
		}
		
		for(int col = 0; col < type.getDimension(); col++){
			for(int row = 0; row < type.getDimension(); row++){
				if(type.isBlock(col, row, rotation) && isOccupied(x + col, y + row)){
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * ブロックをゲーム内に追加する
	 * 現在存在するブロックについては考慮せず。
	 * もし存在していた場合は上書きする。
	 * @param type
	 * @param x
	 * @param y
	 * @param rotation
	 */
	public void addPiece(BlockType type, int x, int y, int rotation){
		for(int col = 0; col < type.getDimension(); col++){
			for(int row = 0; row < type.getDimension(); row++){
				if(type.isBlock(col, row, rotation)){
					setBlocks(col + x, row + y, type);	
				}
			}
		}
	}
	
	/**
	 * ゲーム板内にクリアされた行があるか確認する。
	 * もし存在する場合はそれを取り除く
	 * @return クリアされた行数を返す
	 */
	public int checkLines(){
		int completedLines = 0;
		
		for(int row = 0; row < ROW_COUNT; row++){
			if(checkLine(row)){
				completedLines++;
			}
		}
		
		return completedLines;
	}
	
	/**
	 * 行がブロックで満ちているかを判定
	 * @param line 確認する行
	 * @return この行がブロックで満ちているか
	 */
	private boolean checkLine(int line){
		
		/**
		 * 指定した列を各タテ列ごとに確認
		 * いずれかでも空きマスがあればfalseを返して終了
		 */
		for(int col = 0; col < COL_COUNT; col++){
			if(!isOccupied(col, line)){
				return false;
			}
		}
		
		/**
		 * 行がブロックで満ちているので段を下げる
		 */
		for(int row = line - 1; row >= 0; row--){
			for(int col = 0; col < COL_COUNT; col++){
				setBlocks(col, row + 1, getBlocks(col, row));
			}
		}
		return true;
	}
	
	private boolean isOccupied(int x, int y){
		return blocks[y][x] != null;
	}
	
	private void setBlocks(int  x, int y, BlockType type) {
		blocks[y][x] = type;
	}
	
	/**
	 * 列と行からブロックを得る
	 * @param x
	 * @param y
	 * @return
	 */
	private BlockType getBlocks(int x, int y) {
		return blocks[y][x];
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//位置を平行移動させる
		g.translate(BORDER_WIDTH, BORDER_WIDTH);
		
		/**
		 * 現在のゲーム状況によって、ボードに書き込む
		 */
		if(tetris.isPaused()){//ユーザーがポーズした時に処理
			
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			String msg = "ぽーず中";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
			
		} else if(tetris.isNewGame() || tetris.isGameOver()){//新規ゲームスタート時、またはゲームオーバ時に再描画
			
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			
			String msg = tetris.isNewGame()? "akiTETRIS" : "げーむおーばー";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
			g.setFont(SMALL_FONT);
//			msg = "エンターキーを押してね" + (tetris.isNewGame()? "" : "もう一度プレーできるお");
//			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
			msg = "エンターキーを押してね";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
			if(!tetris.isNewGame()){
				String msgGameOver = "もう一度ぷれーできるお";
				g.drawString(msgGameOver, CENTER_X - g.getFontMetrics().stringWidth(msgGameOver) / 2, 300 + g.getFontMetrics().getHeight());
			}
		
		} else {
			
			//ブロックをボードに描画
			for(int x = 0; x < COL_COUNT; x++){
				for(int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++){
					BlockType block = getBlocks(x, y);
					if(block != null){
						drawBlock(block, x * BLOCK_SIZE, (y - HIDDEN_ROW_COUNT) * BLOCK_SIZE, g);
					}
				}
			}
			
			/**
			 * 現在のブロックを描画する
			 */
			BlockType type = tetris.getPieceType();
			int pieceCol = tetris.getPieceCol();
			int pieceRow = tetris.getPieceRow();
			int rotation = tetris.getPieceRotation();
			
			//ピースをゲーム盤に描画
			for(int col = 0; col < type.getDimension(); col++){
				for(int row = 0; row < type.getDimension(); row++){
					if(pieceRow + row >= 2 && type.isBlock(col, row, rotation)){
						drawBlock(type, (pieceCol + col) * BLOCK_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * BLOCK_SIZE, g);
					}
				}
			}
			
			/**
			 * ピースの移動先を半透明のピースで表す
			 * 
			 */
			Color base = type.getBaseColor();
			base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
			for(int lowest = pieceRow; lowest < ROW_COUNT; lowest++){
				//もしも衝突がなければ、次の行を試す
				if(isValidAndEmpty(type, pieceCol, lowest, rotation)){
					continue;
				}
				
				//衝突が起きる行の一つ前に描画するため
				lowest--;
				
				//描画する
				for(int col = 0; col < type.getDimension(); col++){
					for(int row = 0; row < type.getDimension(); row++){
						if(lowest + row >= 2 && type.isBlock(col, row, rotation)){
							drawBlock(base, base.brighter(), base.darker(), (pieceCol + col) * BLOCK_SIZE, (lowest + row - HIDDEN_ROW_COUNT) * BLOCK_SIZE, g);
						}
					}
				}
				
				break;
			}
			
			g.setColor(Color.DARK_GRAY);
			for (int x = 0; x < COL_COUNT; x++) {
				for (int y = 0; y < VISIBLE_ROW_COUNT; y++) {
					g.drawLine(0, y * BLOCK_SIZE, COL_COUNT * BLOCK_SIZE, y * BLOCK_SIZE);
					g.drawLine(x * BLOCK_SIZE, 0, x * BLOCK_SIZE, VISIBLE_ROW_COUNT * BLOCK_SIZE);
				}
			}
		}
		
		/**
		 * 外枠を描画する
		 */
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, BLOCK_SIZE * COL_COUNT, BLOCK_SIZE * VISIBLE_ROW_COUNT);
	}
	
	/**
	 * ブロックの描画
	 * @param type
	 * @param x
	 * @param y
	 * @param g
	 */
	private void drawBlock(BlockType type, int x, int y, Graphics g){
		drawBlock(type.getBaseColor(), type.getLightColor(), type.getDarkColor(), x, y, g);
	}
	
	/**
	 * ブロックの描画
	 * @param base
	 * @param light
	 * @param dark
	 * @param x
	 * @param y
	 * @param g
	 */
	private void drawBlock(Color base, Color light, Color dark, int x, int y, Graphics g){
		
		/**
		 * ブロックの前範囲をbaseColorで埋める
		 */
		g.setColor(base);
		g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
		
		/**
		 * ブロックの底と右側を黒の影で埋める
		 */
		g.setColor(dark);
		g.fillRect(x, y + BLOCK_SIZE - SHADE_WIDTH, BLOCK_SIZE, SHADE_WIDTH);
		g.fillRect(x + BLOCK_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, BLOCK_SIZE);
		
		/**
		 * topとleftの端を明るい影で埋める
		 */
		g.setColor(light);
		for(int i = 0; i < SHADE_WIDTH; i++) {
			g.drawLine(x, y + i, x + BLOCK_SIZE - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + BLOCK_SIZE - i - 1);
		}
		
	}
	
}