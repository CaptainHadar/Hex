package Logic;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import GameGraphics.*;
import Minimax.AI;
import Path.PathBoard;
import unit4.collectionsLib.Queue;

public class Board {

	private char[][] board;
	private HexagonPattern hexPattern;
	private char currentPlayer;
	private JFrame frame;
	private int redLevel;
	private int blueLevel;
	private boolean swipeRule;
	private boolean gameOver;
	private JLabel winnerLabel;
	private JButton closeButton;
	
	public Board(char startingPlayer, int redLevel, int blueLevel, boolean swipeRule, int sizeOfBoard) {
		this.hexPattern = new HexagonPattern(sizeOfBoard);
		this.board = new char[hexPattern.ROWS][hexPattern.COLUMNS];
		resetBoard();
		this.gameOver = false;
		this.currentPlayer = startingPlayer;
		this.blueLevel = blueLevel;
		this.redLevel = redLevel;
		this.swipeRule = swipeRule;
		this.frame = new JFrame();
		this.winnerLabel = new JLabel();
		closeButton = new JButton("End Game");
		closeButton.setBounds( (int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 150, 50, 100, 50);
	}

	public void changePlayer() {
		if(this.currentPlayer == 'B') this.currentPlayer = 'R';
		else this.currentPlayer = 'B';
	}


	public int movesPlayed(char[][] brd) {
		int sum = 0;
		for(int i = 0; i < brd.length; i++)
			for(int j = 0; j < brd[0].length; j++)
				if(brd[i][j] != 'E')
					sum++;
		return sum;
	}


	public void printBoard() {
		int counter, i, j;
		System.out.println("");
		for(j = 1; j <=hexPattern.COLUMNS; j++) {
			counter = j;
			while(counter != 0) {
				System.out.print("  ");
				counter--;	
				if(j % 2 == 1 && counter == 1)
					System.out.print("");
			}
			if(j < hexPattern.ROWS)
							System.out.print(j + " ");
				for(i = 1; i <=  hexPattern.ROWS;i++) {

					if(i == this.board.length  && j != this.board.length) {
						System.out.println(board[j-1][i-1]);
					}
					else
						System.out.print(board[j-1][i-1] + "  " );
				}
		}
	}

	public void resetBoard() {
		for(int i = 1; i <= this.board.length; i++)
			for(int j = 1; j <= this.board.length;j++)
				this.board[i-1][j-1] = 'E';
	}


	public char[][] getBoard() {
		return this.board;
	}	

	public Position getHighestScore2ndHuriestics() {
		Position highestScorePos = new Position(-2,-2);
		int bestScore = Integer.MIN_VALUE;
		for(int col = 0; col < this.board.length;col++)
			for(int row = 0; row < this.board.length;row++)
				if(bestScore <= getScoreByPosition(new Position(col,row)))
					{
					bestScore = getScoreByPosition(new Position(col,row));
					highestScorePos  = new Position(col,row);
					}
		return highestScorePos;
	}


	public int getScoreByPosition(Position pos) {
		if(this.board[pos.getColumn()][pos.getRow()] != 'E')
			return 0;
		
		ArrayList<Position> neighbors = new ArrayList<Position>();
		neighbors = getNeighbors(pos);
		int score = 0;
		
		this.board[pos.getColumn()][pos.getRow()] = currentPlayer;
		if(isWinPlayer(this.board, this.currentPlayer)) {
			this.board[pos.getColumn()][pos.getRow()] = 'E';
			return 5000;
		}
		else if (isWinPlayer(this.board, getOtherPlayer(this.currentPlayer))) {
			this.board[pos.getColumn()][pos.getRow()] = 'E';
			return 1000;
		}
		this.board[pos.getColumn()][pos.getRow()] = 'E';
		score += (this.board.length - estDistanceFromCenter(pos)) / 5;
		for(Position currentPos: neighbors)
		{

			if(this.board[currentPos.getColumn()][currentPos.getRow()] == currentPlayer)
				score += 3;
			if(this.board[currentPos.getColumn()][currentPos.getRow()] == getOtherPlayer(currentPlayer))
				score += 5;
		}
		return score;
	}
	
	public HexagonPattern getHexPattern() {
		return this.hexPattern;
	}

	
	public boolean isValidMove(int column, int row) {
		if(row >= this.board.length || row < 0 || column >= this.board.length || column < 0 || isGameWinner())
			return false;

		if(this.swipeRule && movesPlayed(this.board) == 1) {
			this.swipeRule = false;
			this.getHexPattern().getGraphicalBoard()[column][row].removeAll();
			return true;
		}

		if(this.board[column][row] == 'E')
			return true;
		else
			return false;
	}
	
	/* There are 3 options
	 * - Winner Found
	 * - Dead end Found
	 * - Neighbor Found
	 * The function true if winner is found else returns false */

	public boolean isGameWinner() {
		if(isWinPlayer(this.board, this.currentPlayer)) {
		if(this.currentPlayer == 'R')
		winnerLabel.setText("Red Won!");
		else
		winnerLabel.setText("Blue Won!");
		return true;
		}
		changePlayer();
		if(isWinPlayer(this.board, this.currentPlayer))
		{
			if(this.currentPlayer == 'R')
				winnerLabel.setText("Red Won!");
				else
				winnerLabel.setText("Blue Won!");
			return true;
		}
		changePlayer();
		return false;
	}


	public void enterRandom(char player) {
		Random rnd = new Random();
		int rndColumn = rnd.nextInt(this.board.length);
		int rndRow = rnd.nextInt(this.board.length);
		while(!isValidMove(rndColumn, rndRow))
		{
			rndColumn = rnd.nextInt(this.board.length);
			rndRow = rnd.nextInt(this.board.length);
		}
		this.board[rndColumn][rndRow] = player;

	}

	
	public void previewWinner() {
		if(isGameWinner())
		{
			if(currentPlayer == 'R')
				winnerLabel.setText("Red Won!");
			else
				winnerLabel.setText("Blue Won!");
			winnerLabel.revalidate();
		}

	}

	
	
	public Queue<Position> getShortestPath() {
		int index = 0;
		int shortestIndex = 55;
		int shortestLength = Integer.MAX_VALUE;
		if(currentPlayer == 'R') {
		while(index != this.board.length) {
			if(this.board[0][index] != 'B' && currentPlayer ==  'R')
			{
			PathBoard board = new PathBoard(getBoard(), currentPlayer,new Position(0,index));
			if(board.chainAmountRequired() < shortestLength && shortestLength != 200 && shortestLength != 0)
			{
				shortestIndex = index;
				board = new PathBoard(getBoard(), currentPlayer,new Position(0,index));
				shortestLength = board.chainAmountRequired();
//				System.out.println("Shortest length:" +shortestLength);
			}
			}
			index++;
		}
//		System.out.println("Shortest index:" +shortestIndex);
			PathBoard board = new PathBoard(getBoard(), currentPlayer,new Position(0,shortestIndex));
			Queue<Position> chain = board.getShortestChain();
			return chain;
		}
		else {
			while(index != this.board.length) {
				if(this.board[index][0] != 'R' && currentPlayer ==  'B')
				{
				PathBoard board = new PathBoard(getBoard(), currentPlayer,new Position(index,0));
				if(board.chainAmountRequired() < shortestLength && shortestLength != 200)
				{
					shortestIndex = index;
					board = new PathBoard(getBoard(), currentPlayer,new Position(index,0));
					shortestLength = board.chainAmountRequired();
				}
				}
				index++;
			}
//			System.out.println("Shortest index:" +shortestIndex);
				PathBoard board = new PathBoard(getBoard(), currentPlayer,new Position(shortestIndex,0));
				Queue<Position> chain = board.getShortestChain();
				return chain;

		}
	}
	
	
	public Queue<Position> filterToValid(Queue<Position> positions) {
		Queue<Position> temp = new Queue<Position>();
		while(!positions.isEmpty()) {
			if(isValidMove(positions.head().getColumn(), positions.head().getRow()))
				temp.insert(positions.head());
		positions.remove();	
		}
		return temp;
	}
	
	
	public int estDistanceFromCenter(Position pos) {
		int distance = Math.abs(pos.getColumn() - (this.board.length / 2));
		distance += Math.abs(pos.getRow() - (this.board.length / 2));
		return distance;
	}
	
	
	public void playerVsPlayer() {
		HexagonButton[][] hexButton = this.hexPattern.getGraphicalBoard();		
		for(int row = 0; row < this.board.length; row++)
			for(int col = 0; col < this.board.length; col++) 
				hexButton[col][row].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(gameOver)
							return;
						HexagonButton clickedButton = (HexagonButton) e.getSource();

						JLabel lab1 = new JLabel("");
						lab1.setBounds(clickedButton.getX(), clickedButton.getY(), 100, 30);

							if(isValidMove(clickedButton.getCol(), clickedButton.getRow())) {
								board[clickedButton.getCol()][clickedButton.getRow()] = currentPlayer;
								lab1.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getCurrentPlayerIcon())));
								if(isGameWinner()) {
									gameOver = true;	
									previewWinner();
								}
								changePlayer();
							} else
								System.out.println("its an invalid move!");
					
						clickedButton.add(lab1);
						clickedButton.revalidate();
					}
				});
	}

	
	public String getCurrentPlayerIcon() {
		if(currentPlayer == 'R') 
			return "redCircle.png";
		else
			return "blueCircle.png";
	}


	public void playerVsRandom(char startingPlayer) {
		HexagonButton[][] hexButton = this.hexPattern.getGraphicalBoard();
		if(startingPlayer != currentPlayer)
		{
			enterRandomGraphicalMove();
		}

		for(int row = 0; row < this.board.length; row++)
			for(int col = 0; col < this.board.length; col++) 
				hexButton[col][row].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						HexagonButton clickedButton = (HexagonButton) e.getSource();

						if(!isGameWinner()) {
							JLabel lab1 = new JLabel("");
							lab1.setBounds(clickedButton.getX(), clickedButton.getY(), 100, 30);

								if(isValidMove(clickedButton.getCol(), clickedButton.getRow())) {
										if(!isGameWinner()) {
											board[clickedButton.getCol()][clickedButton.getRow()] = currentPlayer;
											lab1.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getCurrentPlayerIcon())));
											clickedButton.add(lab1);
											lab1.revalidate();
											if(isGameWinner())
												previewWinner();
											changePlayer();
											enterRandomGraphicalMove();
											if(isGameWinner())
												previewWinner();	
										}
								} else
									System.out.println("its an invalid move!");
							clickedButton.revalidate();
						}
					}
				});
	}

	
	public void startGame() {
		if(this.blueLevel == -1 && this.redLevel == -1)
			playerVsPlayer();
		else if(this.blueLevel != -1 && this.redLevel !=  -1) {
			computerVsComputer();
		}
		else
			playerVsComputer();
	}

	
	public void computerVsComputer() {
		if(currentPlayer == 'B')	{
			
				if(this.blueLevel == 1)
					enterRandomGraphicalMove();
				else if(this.blueLevel == 2)
					enterNegamaxMove();
				else if(this.blueLevel == 3)
				enterSecondHeuristicsMove();
				else if(this.blueLevel == 4)
					enterHeuristicsMove();
				else if(this.blueLevel == 5)
					enterAlphaBetaMove();
				if(!isGameWinner()) {
				if(this.redLevel == 1)
					enterRandomGraphicalMove();
				else if(this.redLevel == 2)
					enterNegamaxMove();
				else if(this.redLevel == 3)
					enterSecondHeuristicsMove();
				else if(this.redLevel == 4)
					enterHeuristicsMove();
				else if(this.redLevel == 5)
					enterAlphaBetaMove();
				}
		}
		
		else if(currentPlayer == 'R')	{
				if(this.redLevel == 1)
					enterRandomGraphicalMove();
				else if(this.redLevel == 2)
					enterNegamaxMove();
				else if(this.redLevel == 3)
					enterSecondHeuristicsMove();
				else if(this.redLevel == 4)
					enterHeuristicsMove();
				else if(this.redLevel == 5)
					enterAlphaBetaMove();
				if(!isGameWinner()) {
				if(this.blueLevel == 1)
					enterRandomGraphicalMove();
				else if(this.blueLevel == 2)
					enterNegamaxMove();
				else if(this.blueLevel == 3)
					enterSecondHeuristicsMove();
				else if(this.blueLevel == 4)
					enterHeuristicsMove();
				else if(this.blueLevel == 5)
					enterAlphaBetaMove();
				}
		}
		return;
	}

	
	public void playerVsComputer() {
		char playerColor;
		if(this.blueLevel == -1)
			playerColor = 'B';
		else
			playerColor = 'R';

		int computerLevel;
		if(this.blueLevel ==  -1)
			computerLevel = this.redLevel;
		else
			computerLevel = this.blueLevel;

		if(computerLevel == 1)
		playerVsRandom(playerColor);
		else if (computerLevel == 2)
		playerVsNegamax(playerColor);
		else if (computerLevel == 3)
		playerVsSecondHeuristics(playerColor);
		else if (computerLevel == 4)
		playerVsHeuristics(playerColor);
		else if (computerLevel == 5)
		playerVsAlphaBetaMinimax(playerColor);
	}

	
	public void makeMove(int col, int row) {
		if(isValidMove(col, row)) {
			JLabel lab2 = new JLabel("");
			HexagonButton[][] hexButton = this.hexPattern.getGraphicalBoard();
							lab2.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getCurrentPlayerIcon())));

			hexButton[col][row].add(lab2);
			hexButton[col][row].revalidate();
			this.board[col][row] = this.currentPlayer;
		}
	}


	public void enterAlphaBetaMove(){
		HexagonButton[][] hexButton = this.hexPattern.getGraphicalBoard();
		JLabel lab2 = new JLabel("");
		AI coolmm = new AI(board, getOtherPlayer(currentPlayer));
		Position pos;
		 pos = coolmm.alphaBetaPrunedMiniMax(board, true, 5, Integer.MIN_VALUE, Integer.MAX_VALUE).getPosition();
		 int col = pos.getColumn();
		 int row = pos.getRow();

		if(!isGameWinner()) {
				lab2.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getCurrentPlayerIcon())));
			hexButton[col][row].add(lab2);
			hexButton[col][row].revalidate();
			this.board[col][row] = this.currentPlayer;

			changePlayer();
		}
		else
			System.out.println("Game is over!");
	}
	
	public void enterSecondHeuristicsMove(){
		HexagonButton[][] hexButton = this.hexPattern.getGraphicalBoard();
		JLabel lab2 = new JLabel("");
		Position pos = getHighestScore2ndHuriestics();
		 int col = pos.getColumn();
		 int row = pos.getRow();

		if(!isGameWinner()) {
				lab2.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getCurrentPlayerIcon())));
			hexButton[col][row].add(lab2);
			hexButton[col][row].revalidate();
			this.board[col][row] = this.currentPlayer;

			changePlayer();
		}
		else
			System.out.println("Game is over!");
	}
	
	public void playerVsNegamax(char playerColor) {
		HexagonButton[][] hexButton = this.hexPattern.getGraphicalBoard();

		if(playerColor != currentPlayer)
			enterNegamaxMove();
		
		
		for(int row = 0; row < this.board.length; row++)
			for(int col = 0; col < this.board.length; col++) 
				hexButton[col][row].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(gameOver)
							return;
						HexagonButton clickedButton = (HexagonButton) e.getSource();

						JLabel lab1 = new JLabel("");
						lab1.setBounds(clickedButton.getX(), clickedButton.getY(), 100, 30);

							if(isValidMove(clickedButton.getCol(), clickedButton.getRow())) {
								board[clickedButton.getCol()][clickedButton.getRow()] = currentPlayer;
								lab1.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getCurrentPlayerIcon())));
								clickedButton.add(lab1);
								clickedButton.revalidate();
								if(isGameWinner()) {
									gameOver = true;	
								}
								else {
									changePlayer();
									enterNegamaxMove();
									if(isGameWinner()) {
										gameOver = true;	
									}
								}

							} else
								System.out.println("its an invalid move!");
					}

				});


	}
	

	public void enterNegamaxMove(){
		if(!isGameWinner()) {
		HexagonButton[][] hexButton = this.hexPattern.getGraphicalBoard();
		NextMove move = negamax(this.board, this.currentPlayer, true, 0);
		JLabel lab2 = new JLabel("");
		int row = move.getPosition().getRow();
		int col = move.getPosition().getColumn();

		if(!isGameWinner()) {
				lab2.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getCurrentPlayerIcon())));
			hexButton[col][row].add(lab2);
			hexButton[col][row].revalidate();
			this.board[col][row] = this.currentPlayer;
			changePlayer();

		}
		else System.out.println("Game is over!");
		
		}
	}


	public void enterRandomGraphicalMove(){
		HexagonButton[][] hexButton = this.hexPattern.getGraphicalBoard();
		Random rnd = new Random();
		JLabel lab2 = new JLabel("");
		int row = rnd.nextInt(hexPattern.ROWS);
		int col = rnd.nextInt(hexPattern.COLUMNS);
		while((!isValidMove(col, row)) && !isGameWinner())
		{
			row = rnd.nextInt(hexPattern.ROWS);
			col = rnd.nextInt(hexPattern.COLUMNS);
		}

//		System.out.println(isValidMove(col, row));
		if(!isGameWinner()) {
				lab2.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getCurrentPlayerIcon())));
			hexButton[col][row].add(lab2);
			hexButton[col][row].revalidate();
			this.board[col][row] = this.currentPlayer;

			changePlayer();
		}
		else
			System.out.println("Game is over!");
	}

	public Position randomInChain(Queue<Position> path) {
		Queue<Position> filteredQueue = filterToValid(path);
		Random rnd = new Random();
		if(!filteredQueue.isEmpty()) {
		int index = rnd.nextInt(lengthQueue(filteredQueue));
		while(index >= 1)
		{
			index--;
			filteredQueue.remove();
		}
		if(!filteredQueue.isEmpty())
		return filteredQueue.head();
		else
		{
			int rndCol = rnd.nextInt(this.board.length);
			int rndRow = rnd.nextInt(this.board.length);
			while(!isValidMove(rndCol, rndRow))
			{
				 rndCol = rnd.nextInt(this.board.length);
				 rndRow = rnd.nextInt(this.board.length);
			}
					return new Position(rndCol,rndRow);
		}
		}
		else
		{
			int rndCol = rnd.nextInt(this.board.length);
			int rndRow = rnd.nextInt(this.board.length);
			while(!isValidMove(rndCol, rndRow))
			{
				 rndCol = rnd.nextInt(this.board.length);
				 rndRow = rnd.nextInt(this.board.length);
			}
					return new Position(rndCol,rndRow);
		}
	}
	
	
	public int lengthQueue(Queue<Position> q1) {
		int mone = 0;
		Queue<Position> temp = new Queue<Position>();
		while(!q1.isEmpty())
		{
			mone++;
			temp.insert(q1.remove());	
		}

		while(!temp.isEmpty())
			q1.insert(temp.remove());

		return mone;

	}
	
	public void enterHeuristicsMove(){
		Queue<Position> path = getShortestPath();
		Position bestPos = randomInChain(path);
		int row = bestPos.getRow();
		int col = bestPos.getColumn();
		if(!isGameWinner() ) {
			if(isValidMove(col, row)) {
			makeMove(col, row);
			this.board[col][row] = this.currentPlayer;
			changePlayer();
			}
			else enterNegamaxMove();
		}
		else
			System.out.println("Game is over!");

	}
	
	public void playerVsHeuristics(char playerColor) {
		HexagonButton[][] hexButton = this.hexPattern.getGraphicalBoard();
		if(playerColor != currentPlayer)
			enterHeuristicsMove();
		
		for(int row = 0; row < this.board.length; row++)
			for(int col = 0; col < this.board.length; col++) 
				hexButton[col][row].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(gameOver)
							return;
						HexagonButton clickedButton = (HexagonButton) e.getSource();

						JLabel lab1 = new JLabel("");
						lab1.setBounds(clickedButton.getX(), clickedButton.getY(), 100, 30);

							if(isValidMove(clickedButton.getCol(), clickedButton.getRow())) {
								board[clickedButton.getCol()][clickedButton.getRow()] = currentPlayer;
								lab1.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getCurrentPlayerIcon())));
								clickedButton.add(lab1);
								clickedButton.revalidate();
								if(isGameWinner()) {
									gameOver = true;	
									previewWinner();
								}
								else {
									changePlayer();
									enterHeuristicsMove();
									if(isGameWinner()) {
										gameOver = true;	
									}
								}

							} else
								System.out.println("its an invalid move!");
					}

				});

	}
	
	public void playerVsSecondHeuristics(char playerColor) {
		HexagonButton[][] hexButton = this.hexPattern.getGraphicalBoard();
		if(playerColor != currentPlayer)
			enterSecondHeuristicsMove();
		
		for(int row = 0; row < this.board.length; row++)
			for(int col = 0; col < this.board.length; col++) 
				hexButton[col][row].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(gameOver)
							return;
						HexagonButton clickedButton = (HexagonButton) e.getSource();

						JLabel lab1 = new JLabel("");
						lab1.setBounds(clickedButton.getX(), clickedButton.getY(), 100, 30);

							if(isValidMove(clickedButton.getCol(), clickedButton.getRow())) {
								board[clickedButton.getCol()][clickedButton.getRow()] = currentPlayer;
								lab1.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getCurrentPlayerIcon())));
								clickedButton.add(lab1);
								clickedButton.revalidate();
								if(isGameWinner()) {
									gameOver = true;	
									previewWinner();
								}
								else {
									changePlayer();
									enterSecondHeuristicsMove();
									if(isGameWinner()) {
										gameOver = true;	
									}
								}

							} else
								System.out.println("its an invalid move!");
					}

				});

	}
	
	
	public void playerVsAlphaBetaMinimax(char playerColor) {
		HexagonButton[][] hexButton = this.hexPattern.getGraphicalBoard();
		if(playerColor != currentPlayer)
			enterAlphaBetaMove();
		
		for(int row = 0; row < this.board.length; row++)
			for(int col = 0; col < this.board.length; col++) 
				hexButton[col][row].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(gameOver)
							return;
						HexagonButton clickedButton = (HexagonButton) e.getSource();

						JLabel lab1 = new JLabel("");
						lab1.setBounds(clickedButton.getX(), clickedButton.getY(), 100, 30);

							if(isValidMove(clickedButton.getCol(), clickedButton.getRow())) {
								board[clickedButton.getCol()][clickedButton.getRow()] = currentPlayer;
								lab1.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getCurrentPlayerIcon())));
								clickedButton.add(lab1);
								clickedButton.revalidate();
								if(isGameWinner()) {
									gameOver = true;	
									previewWinner();
								}
								else {
									changePlayer();
									enterAlphaBetaMove();
									if(isGameWinner()) {
										gameOver = true;	
									}
								}

							} else
								System.out.println("its an invalid move!");
					}

				});

	}
	
	public char getOtherPlayer(char currentPlayer) {
		if(currentPlayer == 'R')
			return 'B';
		else
			return 'R';
	}


	public char[][] duplicateBoard(char[][] board) {
		char[][] newBoard = new char[board.length][board[0].length];
		for(int i = 0; i < board.length;i++)
			for(int j = 0; j < board[0].length;j++)
				newBoard[i][j] = board[i][j];
		return newBoard;
	}



	public NextMove negamax(char[][] board, char nextPlayer, boolean isMe, int depth) {
		// במידה ועומק 3 ולא נמצא שווי המהלך הוא 0, לא הפסד ולא נצחון.
		if(depth > 3)
			return new NextMove(null,0);
		
		//האם יש מנצח כלשהו
		if(isWinPlayer(board, getOtherPlayer(nextPlayer))) {
			if(isMe)
				return new NextMove(null, -1); // האם המנצח הוא לא אתה תחזיר -1
			else
				return new NextMove(null, 1); // 1 אם המנצח הוא אתה תחזיר 
		}

		Map<Position, char[][]> boardMap = new HashMap<Position, char[][]>();
		for(int i = 0; i < board.length; i++) // תכניס את כל המהלכים האפשריים למאפ של פוזישאן ולוח תאורתי.
			for(int j = 0; j < board[0].length; j++)
				if(board[i][j] == 'E') {
					char[][] newBoard = duplicateBoard(board);
					newBoard[i][j] = nextPlayer;
					boardMap.put(new Position(i,j), newBoard);
				}
		Map<Position,NextMove> nextMap = new HashMap<Position, NextMove>(); // תיצור לנו עוד מאפ רק הפעם תהיה של מיקום ונאקסט מוב
		// Map<Key,Value>
		for(Position key:boardMap.keySet()) { // תכניס את כל האפשרויות לנאקסט מאפ, בזמן שאתה שומר על המיקום הראשון שהוכנס
			nextMap.put(key, negamax( boardMap.get(key), getOtherPlayer(nextPlayer), !isMe, depth + 1)); 	// תעלה את העמוק כדי שלא נתקע בלולאה אינסופית בנוסף לכך עכשיו תורו של השחקן השני וגם תקבל את הלוח לאחר שהוכנס אליו מהלך 
		}

		NextMove bestMove = null;
		Position bestPosition = null;
		// קודם כל יתחיל עומק 1 מכיוון שהוא הראשון שהעלה בקשה
		// במקרה שזה אנחנו לכל הפעם שהחזרנו אם יצא לנו 1 תחזיר אותו, אין סיבה להמשיך לבדוק.
		// במקרה שזה לא אנחנו זה -1
		for(Position key: nextMap.keySet())
		{
			NextMove next = nextMap.get(key);
			if(isMe) {
				if(bestMove == null || next.getResult() > bestMove.getResult()) {
					bestMove = next;
					bestPosition = key;
					if(bestMove.getResult() == 1)
						return new NextMove(bestPosition, bestMove.getResult());
				}
			}
			else {
				if(bestMove == null || next.getResult() < bestMove.getResult()) {
					bestMove = next;
					bestPosition = key;
					if(bestMove.getResult() == -1)
						return new NextMove(bestPosition, bestMove.getResult());
				}
			}
			// לשכפל את המיניקמיקס כמספרים הפעמיים שאנו צריכים
			// לקרוא למינימקס כמספרים הפעמיים שאנו צריכים
			// למצוא את המהלך הטוב ביותר 
			// להחזיר אותו
		}
		return new NextMove(bestPosition,bestMove.getResult());

	}

	public boolean isWinPlayer(char[][] board, char player) {
		if(player == 'R')
			return isWinColumns(board, player);
		else
			return isWinRows(board, player);
	}

	public boolean isWinColumns(char[][] board, char player) {
		Queue<Position> current = new Queue<Position>();
		ArrayList<Position> lookedAt = new ArrayList<Position>();
		for(int i =0; i < board[0].length;i++) {
			if(board[0][i] == player) {
				current.insert(new Position(0,i));
				lookedAt.add(new Position(0,i));
			}
		}
		Queue<Position> next = new Queue<Position>();
		while(!(current.isEmpty() && next.isEmpty())) {
			while(!current.isEmpty())
			{
				Position p = current.remove();
				for(Position n :getNeighbors(p))
					if(board[n.getColumn()][n.getRow()] == player && !lookedAt.contains(n)) {
						lookedAt.add(n);
						next.insert(n);
					}


			}
			current = next;
			next = new Queue<Position>();
		}

		for(Position p: lookedAt) 
			if(p.getColumn() == board.length - 1)
				return true;
		return false;
	}


	public boolean isWinRows(char[][] board, char player) {
		Queue<Position> current = new Queue<Position>();
		ArrayList<Position> lookedAt = new ArrayList<Position>();
		for(int i =0; i < board.length;i++) {
			if(board[i][0] == player) {
				current.insert(new Position(i,0));
				lookedAt.add(new Position(i,0));
			}
		}
		Queue<Position> next = new Queue<Position>();
		while(!(current.isEmpty() && next.isEmpty())) {
			while(!current.isEmpty())
			{
				Position p = current.remove();
				for(Position n :getNeighbors(p))
					if(board[n.getColumn()][n.getRow()] == player && !lookedAt.contains(n)) {
						lookedAt.add(n);
						next.insert(n);
					}


			}
			current = next;
			next = new Queue<Position>();
		}

		for(Position p: lookedAt) 
			if(p.getRow() == board.length - 1)
				return true;
		return false;
	}

	public ArrayList<Position> getNeighbors(Position pos) {
		int row = pos.getRow();
		int col = pos.getColumn();
		ArrayList<Position> returnValue = new ArrayList<Position>();

		if(row - 1 >= 0) {

			if(col + 1 < this.board.length)
				returnValue.add(new Position(col +1, row -1));
			returnValue.add(new Position(col, row -1));
		}

		if(col - 1 >= 0) {
			if(row +1 < this.board[0].length)
				returnValue.add(new Position(col -1, row + 1));
			returnValue.add(new Position(col -1, row));
		}

		if(row + 1 < this.board[0].length)
			returnValue.add(new Position(col, row +1));

		if(col + 1 < this.board.length) 
			returnValue.add(new Position(col +1, row));

		return returnValue;
	}


	public Queue<Position> getNeighborsQueue(Position pos) {
		int row = pos.getRow();
		int col = pos.getColumn();
		Queue<Position> returnValue = new Queue<Position>();

		if(row - 1 >= 0) {

			if(col + 1 < this.board.length)
				returnValue.insert(new Position(col +1, row -1));
			returnValue.insert(new Position(col, row -1));
		}

		if(col - 1 >= 0) {
			if(row +1 < this.board[0].length)
				returnValue.insert(new Position(col -1, row + 1));
			returnValue.insert(new Position(col -1, row));
		}

		if(row + 1 < this.board[0].length)
			returnValue.insert(new Position(col, row +1));

		if(col + 1 < this.board.length) 
			returnValue.insert(new Position(col +1, row));

		return returnValue;
	}

	public void previewBoard() { 

		winnerLabel = new JLabel("");
		winnerLabel.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 1 / 3, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3, 500, 500);
		winnerLabel.setFont(new Font(winnerLabel.getFont().getName(), winnerLabel.getFont().getStyle(), 100));
		frame.add(winnerLabel);
		winnerLabel.setText("");

		if(this.blueLevel != -1 && this.redLevel != -1) {
			JButton button = new JButton("Continue!");
			button.setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 175, 200, 100);	
			this.frame.add(button);

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!isGameWinner())
						startGame();
					else {
						if(currentPlayer == 'R')
							winnerLabel.setText("Red Won!");
						else
							if(currentPlayer == 'B')
								winnerLabel.setText("Blue Won!");

						button.setSize(new Dimension(0,0));
					}
				}
			});
		}	 
		this.frame.add(closeButton);
		this.closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				
			}
		});
		this.frame.setUndecorated(true);
		this.frame.setTitle("Hexagon Pattern");
		this.frame.setResizable(false);
		this.frame.setFocusable(false);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.add(this.hexPattern);
		this.frame.setSize((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		this.frame.setVisible(true); 
	}

}
