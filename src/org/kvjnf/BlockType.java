package org.kvjnf;

import java.awt.Color;

/**
 * {@code BlockType}はゲーム内で利用される
 * 様々な種類のブロックを定義する。
 * @author akiyama_daisuke
 *
 */
public enum BlockType{
	
	/**
	 * Block TypeI
	 */
	
	private Color baseColor;
	
	private Color lightColor;
	
	private Color darkColor;
	
	/**
	 * ブロックが生み出されるタテ列
	 */
	private int spawnCol;
	
	/**
	 * ブロックが生み出される横列
	 */
	private int spawnRow;
	
	private int dimension;
	
	private int rows;
	
	private int cols;
	
	private boolean[][] blocks;
	
	private BlockType(Color color, int dimension, int cols, int rows, boolean[][] blocks){
		this.baseColor = color;
		this.lightColor = color.brighter();
		this.darkColor = color.darker();
		this.dimension = dimension;
		this.blocks = blocks;
		this.cols = cols;
		this.rows = rows;
		
		this.spawnCol = 5 - (dimension >> 1);
		this.spawnRow = getTopInset(0);
	}
	
	public Color getbaColor() {
		return baseColor;
	}
	
	public Color getLightColor(){
		return lightColor;
	}
	
	public Color getDarkColor() {
		return darkColor;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public int getSpawnColumn() {
		return spawnCol;
	}
	
	public int getSpawnRow() {
		return spawnRow;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	/**
	 * 引数の座標と回転の値内にブロックが存在するかを
	 * 判定
	 * @param x
	 * @param y
	 * @param rotation
	 * @return
	 */
	public boolean isBlock(int x, int y, int rotation) {
		return blocks[rotation][y * dimension + x];
	}
	
}