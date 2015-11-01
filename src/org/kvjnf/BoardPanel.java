package org.kvjnf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.psnbtech.TileType;

public class BoardPanel extends JPanel {
	
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
	
	/**
	 * ブロックの高さの総計
	 */
	public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * BLOCK_SIZE + BORDER_WIDTH * 2;
	
	/**
	 * ブロックの幅の総計 
	 */
	public static final int PANEL_WIDTH = COL_COUNT * BLOCK_SIZE + BORDER_WIDTH * 2;
	
	private TetrisAkidai tetris;
	
	private BlockType[][] blocks;
	
	
	public BoardPanel(TetrisAkidai tetris){
		this.tetris = tetris;
		this.blocks = new BlockType[ROW_COUNT][COL_COUNT];
		
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.BLACK);
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
				setBlocks(col + x, row + y, type);
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
	
}