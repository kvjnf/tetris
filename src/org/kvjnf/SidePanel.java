package org.kvjnf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.kvjnf.BoardPanel;

public class SidePanel extends JPanel{

	private static final long serialVersionUID = 7200054204010700350L;

	/**
	 * 次回ブロックのプレビューで表示する
	 */
	private static final int BLOCK_SIZE = BoardPanel.BLOCK_SIZE >> 1;//1つ右シフト
	
	private static final int SHADE_WIDTH = BoardPanel.SHADE_WIDTH >> 1;
	
	private static final int BLOCK_COUNT = 5;
	
	private static final int SQUARE_CENTER_X = 130;
	
	private static final int SQUARE_CENTER_Y = 65;
	
	//プレビューボックスの枠サイズ
	private static final int SQUARE_SIZE = (BLOCK_SIZE * BLOCK_COUNT >> 1);
	
	private static final int SMALL_INSET = 20;
	
	private static final int LARGE_INSET = 40;
	
	private static final int STATS_INSET = 175;
	
	private static final int CONTROLS_INSET = 300;
	
	//各文字列のオフセットの距離
	private static final int TEXT_STRIDE = 25;
	
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 11);
	
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 13);
	
	private static final Color DRAW_COLOR = new Color(128, 192, 128);
	
	private TetrisAkidai tetris;
	
	public SidePanel(TetrisAkidai tetris){
		this.tetris = tetris;
		
		setPreferredSize(new Dimension(200, BoardPanel.PANEL_HEIGHT));
		setBackground(Color.BLACK);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(DRAW_COLOR);
		
		int offset;
		
		/**
		 * "成績"カテゴリの描画
		 */
		g.setFont(LARGE_FONT);
		g.drawString("成績", SMALL_INSET, offset = STATS_INSET);
		g.setFont(SMALL_FONT);
		g.drawString("Level" + tetris.getLevel(), LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Score" + tetris.getScore(), LARGE_INSET, offset += TEXT_STRIDE);
		
		/**
		 * "Controls"カテゴリの描画
		 */
		g.setFont(LARGE_FONT);
		g.drawString("操作方法", SMALL_INSET, offset = CONTROLS_INSET);
		g.setFont(SMALL_FONT);
		g.drawString("← - 左に動かす", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("→ - 右に動かす", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("ALT - 反時計回りに回転", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("SHIFT - 時計回りに回転", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("↓ - 下に動かす", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("P - PAUSE", LARGE_INSET, offset += TEXT_STRIDE);
		
		/**
		 * 次のブロックのプレビュー画面の描画
		 */
		g.setFont(LARGE_FONT);
		g.drawString("Next Piece:", SMALL_INSET, 70);
		g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 2);
		
		/**
		 * 次のブロックの描画
		 */
		BlockType type = tetris.getNextPieceType();
		if(!tetris.isGameOver() && type != null){
			/**
			 * 現在のピースのプロパティ値を調べる
			 */
			int cols = type.getCols();
			int rows = type.getRows();
			int dimension = type.getDimension();
			
			//ピースの左上の角を計算する
			int startX = (SQUARE_CENTER_X - (cols * BLOCK_SIZE / 2));
			int startY = (SQUARE_CENTER_Y - (rows * BLOCK_SIZE / 2));
			
			//プレビューのインセットの値を取得する。デフォルトの回転値をセットする。
			int top = type.getTopInset(0);
			int left = type.getLeftInset(0);
			
			//ピースをプレビューに描画
			for(int row = 0; row < dimension; row++){
				for (int col = 0; col < dimension; col++) {
					if(type.isBlock(col, row, 0)){
						drawBlock(type, startX + ((col - left) * BLOCK_SIZE), startY + ((row - top) * BLOCK_SIZE), g);
					}
				}
			}
		}
	}
	
	/**
	 * プレビュー画面にブロックの描画
	 */
	private void drawBlock(BlockType type, int x, int y, Graphics g) {
		g.setColor(type.getBaseColor());
		g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
		
		//ブロックの下と右側に影
		g.setColor(type.getDarkColor());
		g.fillRect(x, y + BLOCK_SIZE - SHADE_WIDTH, BLOCK_SIZE, SHADE_WIDTH);
		g.fillRect(x + BLOCK_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, BLOCK_SIZE);
		
		//明るい影を上と左に
		g.setColor(type.getLightColor());
		for (int i = 0; i < SHADE_WIDTH; i++) {
			g.drawLine(x, y + i, x + BLOCK_SIZE - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + BLOCK_SIZE  - i - 1);
		}
	}
}