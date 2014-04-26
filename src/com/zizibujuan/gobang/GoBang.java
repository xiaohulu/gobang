package com.zizibujuan.gobang;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GoBang extends JFrame implements MouseListener{

	private static final long serialVersionUID = -4429337347862575403L;

	private Dimension fullScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private int windowWidth = 500;
	private int windowHeight = 500;
	
	private int left = 13;
	private int top = 70;
	private int right = 373;
	private int bottom = 428;
	private int span = 20;
	private int lineNum = 19;
	
	private int pointSize = 6;
	private int chessManSize = 14;
	
	private BufferedImage backgroudImage;
	
	private boolean canPlay = true;
	// 黑方 TODO: 重命名
	private boolean isBlank = true;
	
	// 所有下过的棋子
	private int[][] usedPoints = new int[lineNum][lineNum]; 
	
	private static final String START_MESSAGE = "黑方先行";
	private static final String BLACK_MESSAGE = "轮到黑方";
	private static final String WHITR_MESSAGE = "轮到白方";
	private String turnMessage = START_MESSAGE;
	
	private static final int BLACK_CHESS_MAN = 1;
	private static final int WHITE_CHESS_MAN = 2;
	
	private int getWindowLeft(){
		return (fullScreenSize.width - windowWidth) / 2;
	}
	
	private int getWindowTop(){
		return (fullScreenSize.height - windowHeight) / 2;
	}
	
	public GoBang(){
		super.setTitle("五子棋");
		super.setSize(windowWidth, windowHeight);
		super.setLocation(getWindowLeft(), getWindowTop());
		super.setResizable(false);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			backgroudImage = ImageIO.read(getClass().getResourceAsStream("background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		super.addMouseListener(this);
		
		this.repaint();
	}
	
	
	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		BufferedImage bi = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		// TODO: 将3和20改为变量
		g.drawImage(backgroudImage, 3, 20, this);
		
		drawBaseInfo(g);
		
		drawChessboard(g);
		
		drawFivePoints(g);
		
		drawChessMans(g);
		
		graphics.drawImage(bi, 0, 0, this);
	}

	public void run() {
		super.setVisible(true);
		
	}
	
	/**
	 * 绘制基本信息
	 * @param g
	 */
	private void drawBaseInfo(Graphics g){
		g.setFont(new Font("黑体",Font.BOLD, 20));
		g.drawString("游戏信息：" + turnMessage, 120, 60);
	}
	
	/**
	 * 绘制棋盘
	 */
	private void drawChessboard(Graphics g){
		g.setColor(Color.BLACK);
		for(int i = 0; i < lineNum; i++){
			// 画横线
			g.drawLine(left, top + span * i, right, top + span * i);
			
			// 画竖线
			g.drawLine(left + span * i, top, left + span * i, bottom);
		}
		
	}
	
	/**
	 * 绘制棋子
	 * @param g
	 */
	private void drawChessMans(Graphics g){
		for(int i = 0; i < lineNum; i++){
			for(int j = 0; j < lineNum; j++){
				int x = i * span + left;
				int y = j * span + top;
				if(usedPoints[i][j] == BLACK_CHESS_MAN){
					g.fillOval(x - chessManSize / 2 , y - chessManSize / 2, chessManSize, chessManSize);
				}else if(usedPoints[i][j] == WHITE_CHESS_MAN){
					g.setColor(Color.WHITE);
					g.fillOval(x - chessManSize / 2 , y - chessManSize / 2, chessManSize, chessManSize);
					g.setColor(Color.BLACK);
					g.drawOval(x - chessManSize / 2 , y - chessManSize / 2, chessManSize, chessManSize);
				}
			}
		}
	}
	
	/**
	 * 绘制5个定位实心点
	 * @param g
	 */
	private void drawFivePoints(Graphics g){
		List<Point> list = getFivePoints();
		int half = pointSize / 2;
		for(Point p : list){
			g.fillOval(left + span * p.x - half , top + span * p.y - half, pointSize, pointSize);
		}
	}

	private List<Point> getFivePoints() {
		List<Point> list = new ArrayList<Point>();
		Point p1 = new Point(3, 3);
		Point p2 = new Point(15, 3);
		Point p3 = new Point(3, 15);
		Point p4 = new Point(15, 15);
		Point p5 = new Point(9, 9);
		
		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		list.add(p5);
		return list;
	}

	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if(outBound(x, y)){
			return;
		}
		
		if(!canPlay){
			return;
		}
		
		int hNum = (x - top) / span;
		int vNum = (y - left) / span;
		
		if(hasChessMan(hNum, vNum)){
			JOptionPane.showMessageDialog(this, "此位置有棋子");
			return;
		}
		
		int color = 0;
		if(isBlank){
			color = BLACK_CHESS_MAN;
			turnMessage = WHITR_MESSAGE;
		}else{
			color = WHITE_CHESS_MAN;
			turnMessage = BLACK_MESSAGE;
		}
		putChessMan(hNum, vNum, color);
		isBlank = !isBlank;
		
		
		super.repaint();
		
		if(checkWin(hNum, vNum)){
			JOptionPane.showMessageDialog(this, (usedPoints[hNum][vNum] == 1? "黑方" : "白方") + "胜利");
			
		}
		
			
	}

	private void putChessMan(int hNum, int vNum, int color) {
		usedPoints[hNum][vNum] = color;
	}

	private boolean hasChessMan(int hNum, int vNum) {
		return usedPoints[hNum][vNum] != 0;
	}

	private boolean outBound(int x, int y) {
		return x < left || x > right  || y < top || y > bottom;
	}
	
	private boolean checkWin(int hNum, int vNum){
		int count = 1;
		int color = usedPoints[hNum][vNum];
		
		// 竖向计数
		//	先算上边的同一个色的棋子
		int hCount = vNum;
		while(hCount > 0 && usedPoints[hNum][--hCount] == color){
			count++;
		}
		//  再算下边的同一个色的棋子
		//	把两个结果加起来
		hCount = vNum + 1;
		while(hCount < lineNum && usedPoints[hNum][++hCount] == color){
			count++;
		}
		
		if(count >= 5){
			return true;
		}
		
		// 判断横向
		// 左
		count = 1;
		int vCount = hNum;
		while(vCount > 0 && usedPoints[--vCount][vNum] == color){
			count++;
		}
			
		// 右
		vCount = hNum + 1;
		while(vCount < lineNum && usedPoints[++vCount][vNum] == color){
			count++;
		}
		
		if(count >= 5){
			return true;
		}
		
		
		return false;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
