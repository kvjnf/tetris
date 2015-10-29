package org.kvjnf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	
	private TetrisAkidai tetris;
	
	public BoardPanel(TetrisAkidai tetris){
		this.tetris = tetris;
	}
	
}