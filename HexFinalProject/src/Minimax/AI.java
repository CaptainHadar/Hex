package Minimax;

import java.util.ArrayList;
import java.util.Random;

import Logic.NextMove;
import Logic.Position;
import unit4.collectionsLib.Queue;

public class AI {

	private char startingPlayer;

	public AI(char[][] originalBoard, char startingPlayer) {
		this.startingPlayer = startingPlayer;
	}

	public char[][] duplicateBoard(char[][] board) {
		char[][] newBoard = new char[board.length][board[0].length];
		for(int i = 0; i < board.length;i++)
			for(int j = 0; j < board[0].length;j++)
				newBoard[i][j] = board[i][j];
		return newBoard;
	}

	public void printBoard(char[][] board) {
		int counter, i, j;
		System.out.println("");
		for(j = 1; j <=board.length; j++) {
			counter = j;
			while(counter != 0) {
				System.out.print("  ");
				counter--;	
				if(j % 2 == 1 && counter == 1)
					System.out.print("");
			}
			if(j < board.length)
				System.out.print(j + " ");
			for(i = 1; i <=  board.length;i++) {

				if(i == board.length  && j != board.length) {
					System.out.println(board[j-1][i-1]);
				}
				else
					System.out.print(board[j-1][i-1] + "  " );
			}
		}
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
				for(Position n :getNeighbors(p, board))
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
				for(Position n :getNeighbors(p, board))
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

	public boolean isGameWiner(char[][] board) {
		if(isWinPlayer(board, 'R')) {
			return true;
		}

		if(isWinPlayer(board, 'B'))
		{
			return true;
		}
		return false;
	}


	public ArrayList<Position> getNeighbors(Position pos, char[][] board) {
		int row = pos.getRow();
		int col = pos.getColumn();
		ArrayList<Position> returnValue = new ArrayList<Position>();

		if(row - 1 >= 0) {

			if(col + 1 < board.length)
				returnValue.add(new Position(col +1, row -1));
			returnValue.add(new Position(col, row -1));
		}

		if(col - 1 >= 0) {
			if(row +1 < board[0].length)
				returnValue.add(new Position(col -1, row + 1));
			returnValue.add(new Position(col -1, row));
		}

		if(row + 1 < board[0].length)
			returnValue.add(new Position(col, row +1));

		if(col + 1 < board.length) 
			returnValue.add(new Position(col +1, row));

		return returnValue;
	}

	
	public NextMove alphaBetaPrunedMiniMax(char[][] board, boolean maximizingPlayer , int depth, int alpha, int beta) {
//		printBoard(board);
//		System.out.println("alpha: " + alpha + ", " + beta);
		int alphaBetaValue;
 		if (depth == 0 || isGameWiner(board)) { // במידה והמשחק נגמר או שהעמוק  שנותר לחפש הוא הגעת לשורש העץ 
			return new NextMove(null,evaluateBoard(board, maximizingPlayer));
		}
		Queue<Position> moves = getAvilabileMoves(board);
		if  (!moves.isEmpty()) {
			if (maximizingPlayer) { // בודק אם כרגע אנחנו השחקן שמחפש את המספר המקסימלי 
				Position bestPos = moves.head();
				int bestValue = Integer.MIN_VALUE;
				while (!moves.isEmpty()){
					Position move = moves.remove();
					char[][] updatedBoard = updateBoardWithMove(board, move, startingPlayer);
						alphaBetaValue = alphaBetaPrunedMiniMax( updatedBoard, false,  depth-1, alpha, beta).getResult();
					Random rnd = new Random();

					if(bestValue < alphaBetaValue || (rnd.nextInt(10) == 5 && bestValue == alphaBetaValue))
						bestPos = move;
					bestValue = Math.max(bestValue, alphaBetaValue);
					alpha = Math.max(alpha, bestValue);
					if (beta <= alpha) {
						break;
					}
				}
				return new NextMove(bestPos,bestValue);
			} else {
				int bestValue = Integer.MAX_VALUE;
				Position bestPos = moves.head();
				while (!moves.isEmpty()) {
					Position move = moves.remove();
					char[][] updatedBoard = updateBoardWithMove(board, move , getOtherPlayer(startingPlayer));
						alphaBetaValue = alphaBetaPrunedMiniMax( updatedBoard, true,  depth-1, alpha, beta).getResult();
					bestValue = Math.min(bestValue, alphaBetaValue);
					Random rnd = new Random();

					if(bestValue > alphaBetaValue || (rnd.nextInt(10) == 5 && bestValue == alphaBetaValue))
						bestPos = move;
					beta = Math.min(beta, bestValue);
					if (beta <= alpha) {
						break;
					}
				}
				return new NextMove(bestPos,bestValue);
			}
		} else {
			return new NextMove(null, evaluateBoard(board, maximizingPlayer));

		}
	}

	
	public int getScoreByPosition(Position pos, char[][] board, char currentPlayer) {
		if(board[pos.getColumn()][pos.getRow()] != 'E')
			return 0;
		
		ArrayList<Position> neighbors = new ArrayList<Position>();
		neighbors = getNeighbors(pos, board);
		int score = 0;
		
		board[pos.getColumn()][pos.getRow()] = currentPlayer;
		if(isWinPlayer(board, currentPlayer) || isWinPlayer(board, getOtherPlayer(currentPlayer))) {
			board[pos.getColumn()][pos.getRow()] = 'E';
			return 1000;
		}
		board[pos.getColumn()][pos.getRow()] = 'E';
		score += (board.length - estDistanceFromCenter(pos, board)) / 5;
		for(Position currentPos: neighbors)
		{

			if(board[currentPos.getColumn()][currentPos.getRow()] == currentPlayer)
				score += 3;
			if(board[currentPos.getColumn()][currentPos.getRow()] == getOtherPlayer(currentPlayer))
				score += 5;
		}
		return score;
	}
	
	public int estDistanceFromCenter(Position pos, char[][] board) {
		int distance = Math.abs(pos.getColumn() - (board.length / 2));
		distance += Math.abs(pos.getRow() - (board.length / 2));
		return distance;
	}
	
	public int evaluateBoard(char[][] board, boolean isMaximizing) {
		int sum = 0;
		if(isMaximizing) {
		for(int i = 0; i < board.length; i++)
			for(int j =0; j < board.length; j++) {
				sum += getScoreByPosition(new Position(i,j), board, startingPlayer);
				if(sum >= 1000)
					return sum;
			}
		}
		else
		{
			for(int i = 0; i < board.length; i++)
				for(int j =0; j < board.length; j++)
					sum += getScoreByPosition(new Position(i,j), board, getOtherPlayer(startingPlayer));
		}
		return sum;
	}

	public char[][] updateBoardWithMove(char[][] board, Position move, char player) {
		char[][] tempArray = duplicateBoard(board);
		tempArray[move.getColumn()][move.getRow()] = player;
		return tempArray;
	}

	public Queue<Position> getAvilabileMoves(char[][] board) {
		Queue<Position> que = new Queue<Position>();
		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board.length; j++)
				if(board[i][j] == 'E')
					que.insert(new Position(i,j));
		return que;
	}


	public char getOtherPlayer(char currentPlayer) {
		if(currentPlayer == 'R')
			return 'B';
		return 'R';
	}




}
