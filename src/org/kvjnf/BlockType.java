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
	
	TypeI(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX), 4, 4, 1, new boolean[][]{
		{
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
			false,	false,	false,	false,
		},
		{
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
		},
		{
			false,	false,	false,	false,
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
		},
		{
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
		}
	}),
	
	/**
	 * Block Type J
	 */
	TypeJ(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX), 3, 3, 2, new boolean[][] {
		{
			true,	false,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	true,
			false,	true,	false,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	false,	true,
		},
		{
			false,	true,	false,
			false,	true,	false,
			true,	true,	false,
		}
	}),
	
	/**
	 * Block TypeL
	 */
	TypeL(new Color(BoardPanel.COLOR_MAX, 127, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
		{
			false,	false,	true,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	false,
			false,	true,	true,
		},
		{
			false,	false,	false,
			true,	true,	true,
			true,	false,	false,
		},
		{
			true,	true,	false,
			false,	true,	false,
			false,	true,	false,
		}
	}),
	
	/**
	 * Block Type O
	 */
	TypeO(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), 2, 2, 2, new boolean[][]{
		{
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		},
		{	
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		}
	}),
	
	/**
	 * Block TypeS
	 */
	TypeS(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][]{
		{
			false,	true,	true,
			true,	true,	false,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	false,	true,
		},
		{
			false,	false,	false,
			false,	true,	true,
			true,	true,	false,
		},
		{
			true,	false,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	
	/**
	 * Block TypeT
	 */
	TypeT(new Color(128, BoardPanel.COLOR_MIN, 128), 3, 3, 2, new boolean[][]{
		{
			false,	true,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	true,	false,
		},
		{
			false,	true,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	
	TypeZ(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][]{
		{
			true,	true,	false,
			false,	true,	true,
			false,	false,	false,
		},
		{
			false,	false,	true,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	false,
			false,	true,	true,
		},
		{
			false,	true,	false,
			true,	true,	false,
			true,	false,	false,
		}
	});
	
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
		
		this.spawnCol = 5 - (dimension >> 1);//dimensionの値を右へ１シフト(1/2)
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
	 * @param x x座標
	 * @param y y座標
	 * @param rotation 回転値
	 * @return 
	 */
	public boolean isBlock(int x, int y, int rotation) {
		return blocks[rotation][y * dimension + x];
	}
	
	/**
	 * 
	 * @param rotation
	 * @return
	 */
	public int getTopInset(int rotation){
		/**
		 * 上から下までループで回してブロックがあれば、
		 * ある行を返す。
		 */
		for(int y = 0; y < dimension; y++){
			for(int x = 0; x < dimension; x++){
				if(isBlock(x, y, rotation)){
					return dimension - y;
				}
			}
		}
		
		return -1;
	}
	
	public int getLeftInset(int rotation){
		
		for(int x = 0; x < dimension; x++){
			for(int y = 0; y < dimension; y++){
				if(isBlock(x, y, rotation)){
					return x;
				}
			}
		}
		
		return -1;
	}
	
	public int getRightInset(int rotation){
		for(int x = dimension - 1; x >= 0; x--){
			for(int y = 0; y < dimension; y++){
				if(isBlock(x, y, rotation)){
					return dimension - x;
				}
			}
		}
		
		return -1;
	}
	
	public int getBottomInset(int rotation){
		for(int y = dimension - 1; y >= 0; y--){
			for(int x = 0; x < dimension; x++){
				if(isBlock(x, y, rotation)){
					return dimension - y;
				}
			}
		}
		
		return -1;
	}
	
}