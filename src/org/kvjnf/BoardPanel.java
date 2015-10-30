package org.kvjnf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	
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
	
	private TetrisAkidai tetris;
	
	private BlockType[][] blocks;
	
	
	public BoardPanel(TetrisAkidai tetris){
		this.tetris = tetris;
		this.blocks = new BlockType[ROW_COUNT][COL_COUNT];
		
		
	}
	
}