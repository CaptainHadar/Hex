package Path;

import java.util.Random;
import Logic.Position;
import unit4.collectionsLib.Queue;

public class PathBoard {

	private PathInfo[][] board;
	private char playersColor;
	private boolean isStuck;
	
	public PathBoard(char[][] colorsBoard, char playersColor, Position startPos) {
		isStuck = false;
		this.board = new PathInfo[colorsBoard.length][colorsBoard.length];
		this.playersColor = playersColor;
		for(int i =0; i < this.board.length; i++)
			for(int j =0; j < this.board.length; j++) 
				if((colorsBoard[i][j] != playersColor && colorsBoard[i][j] == 'R' && playersColor == 'B') || (colorsBoard[i][j] != playersColor && colorsBoard[i][j] == 'B' && playersColor == 'R'))
					board[i][j] = new PathInfo(new Position(i,j), 100);
				else if((colorsBoard[i][j] == playersColor && colorsBoard[i][j] == 'R' && playersColor == 'R') || (colorsBoard[i][j] == playersColor && colorsBoard[i][j] == 'B' && playersColor == 'B'))
					board[i][j] = new PathInfo(new Position(i,j), -1);

		if(this.board[startPos.getColumn()][startPos.getRow()] == null) 
			this.board[startPos.getColumn()][startPos.getRow()] = new PathInfo(startPos,0);

		if(this.board[startPos.getColumn()][startPos.getRow()].getMovementCost() == -1)
		{
			Queue<Position> neighbor = getNeighbors(startPos);
			while(!neighbor.isEmpty()) {
				this.board[neighbor.head().getColumn()][neighbor.head().getRow()] = new PathInfo(neighbor.head(), 0);
				neighbor.remove();
			} 
		} 
		//				else
		//				this.board[startPos.getColumn()][startPos.getRow()] = new PathInfo(startPos,100);

	}

	
	public void printPathBoard() {
		System.out.println("Board:");
		for(int i =0;i < board.length; i++)  {
			System.out.println(" ");
			for(int j=0; j < board.length; j++)
				if(board[i][j] != null)
					System.out.print(board[i][j].getMovementCost() + " ");
				else
					System.out.print("X ");
		}
		System.out.println(" ");
	}

	
	public Queue<PathInfo> findPositionByCost(int cost) {
		Queue<PathInfo> positions = new Queue<PathInfo>();
		for(int i =0; i < this.board.length; i++)
			for(int j =0; j < this.board.length; j++)
				if(board[i][j] != null)
					if(board[i][j].getMovementCost() == cost)
						positions.insert(board[i][j]);
		return positions;
	}

	
	public void setBoardPosition(int cost) {
		Queue<PathInfo> temp;
		if(cost < 49)
			temp = findPositionByCost(cost);
		else
			temp = new Queue<PathInfo>();
		Queue<Position> needToBeSetted = new Queue<Position>();
		while(!temp.isEmpty()) {
			Queue<Position> transferQueue = new Queue<Position>();
			transferQueue = getNeighbors(temp.remove().getPosition());
			while(!transferQueue.isEmpty() && cost < 70) {
				//			System.out.println(transferQueue.head().toString());
				needToBeSetted.insert(transferQueue.remove());	
			}
		}
		Queue<Position> transferQueue = new Queue<Position>();

		while(!needToBeSetted.isEmpty()) {
			if(this.board[needToBeSetted.head().getColumn()][needToBeSetted.head().getRow()] == null)
				this.board[needToBeSetted.head().getColumn()][needToBeSetted.head().getRow()] = new PathInfo(needToBeSetted.head(), cost+1);
			else if(this.board[needToBeSetted.head().getColumn()][needToBeSetted.head().getRow()].getMovementCost() == -1) {
				setAllConected(lowestNeighborCost(needToBeSetted.head()), needToBeSetted.head());
//				if(isIncludingEnd())
//					return;
				int lowestCost = lowestNeighborCost(needToBeSetted.head());
				transferQueue = this.getNeighbors(new Position(needToBeSetted.head().getColumn(),needToBeSetted.head().getRow()));
				while(!transferQueue.isEmpty()) {
					this.board[needToBeSetted.head().getColumn()][needToBeSetted.head().getRow()] = new PathInfo(new Position(needToBeSetted.head()), 99);
					if(this.board[transferQueue.head().getColumn()][transferQueue.head().getRow()] == null || (this.board[transferQueue.head().getColumn()][transferQueue.head().getRow()].getMovementCost() > lowestCost &&  this.board[transferQueue.head().getColumn()][transferQueue.head().getRow()].getMovementCost() != 100))
							this.board[transferQueue.head().getColumn()][transferQueue.head().getRow()] = new PathInfo(transferQueue.head(), lowestCost + 1);
						
				
					transferQueue.remove();	
				}
			}
			needToBeSetted.remove();
		}
	}


	public int lowestNeighborCost(Position pos) {
		Queue<Position> neighbors = getNeighbors(pos);
		int lowestCost = Integer.MAX_VALUE;
		while(!neighbors.isEmpty()) {
			if(this.board[neighbors.head().getColumn()][neighbors.head().getRow()] != null) {		
				if(this.board[neighbors.head().getColumn()][neighbors.head().getRow()].getMovementCost() == 99) {
					return 99;
				}
				
				if(this.board[neighbors.head().getColumn()][neighbors.head().getRow()].getMovementCost() < lowestCost && this.board[neighbors.head().getColumn()][neighbors.head().getRow()].getMovementCost() != 100 && this.board[neighbors.head().getColumn()][neighbors.head().getRow()].getMovementCost() != -1) {
						lowestCost = this.board[neighbors.head().getColumn()][neighbors.head().getRow()].getMovementCost();
//						System.out.println("LowestCost: " + lowestCost);
						
				
					}
					
				}
						
			neighbors.remove();
		}

		return lowestCost;
	}


	public Position findLowestNeighborCost(Position pos) {
		Queue<Position> poses = getNeighbors(pos);
		int lowest = lowestNeighborCost(pos);

		while(!poses.isEmpty())
		{
			if(this.board[poses.head().getColumn()][poses.head().getRow()] != null)
				if (lowest == this.board[poses.head().getColumn()][poses.head().getRow()].getMovementCost())
					return poses.head();
			poses.remove();
		}

		return pos;

	}




	
	public Queue<Position> getNeighbors(Position pos) {
		int row = pos.getRow();
		int col = pos.getColumn();
		Queue<Position> returnValue = new Queue<Position>();

		if(row + 1 < this.board[0].length)
			returnValue.insert(new Position(col, row +1));

		if(col - 1 >= 0) {
			if(row +1 < this.board[0].length)
				returnValue.insert(new Position(col -1, row + 1));
			returnValue.insert(new Position(col -1, row));
		}

		
		if(col + 1 < this.board.length) 
			returnValue.insert(new Position(col +1, row));

		if(row - 1 >= 0) {

			if(col + 1 < this.board.length)
				returnValue.insert(new Position(col +1, row -1));
			returnValue.insert(new Position(col, row -1));
		}



		return returnValue;
	}


	public void setAllConected(int lowestCost, Position startingPos) {
		if(lowestCost < 70 && lowestCost > -1)
		{
		Position currentPos = new Position(startingPos);
		Queue<Position> totalPlacedConnected = new Queue<Position>();
		Queue<Position> temp = new Queue<Position>();
		Queue<Position> twoWaysQueue = new Queue<Position>();
		while(!isDeadEnd(getNeighbors(currentPos)) && !isIncludingEnd() && !twoWaysQueue.isEmpty()) {
		
			Queue<Position> neighbors = getNeighbors(currentPos);
			boolean didFoundAlready = false;
			
			if(!isDeadEnd(getNeighbors(currentPos)) && twoWaysQueue.isEmpty())
			currentPos = twoWaysQueue.remove();
				
			while(!neighbors.isEmpty()) { 
				if(this.board[neighbors.head().getColumn()][neighbors.head().getRow()] != null)
					if(this.board[neighbors.head().getColumn()][neighbors.head().getRow()].getMovementCost() == -1)
					{
						totalPlacedConnected.insert(neighbors.head());
						this.board[neighbors.head().getColumn()][neighbors.head().getRow()] = new PathInfo(neighbors.head(), 99);
						if(!didFoundAlready)
						currentPos = new Position(neighbors.head());
						else 
						twoWaysQueue.insert(neighbors.head());
					}
				neighbors.remove();
			}
		}
			
//			System.out.println();
//			printPathBoard();
			
			
			while(!totalPlacedConnected.isEmpty()) {
				temp = getNeighbors(totalPlacedConnected.head());
				while(!temp.isEmpty()) {
			
					if(this.board[temp.head().getColumn()][temp.head().getRow()] == null && lowestCost != 99)
						this.board[temp.head().getColumn()][temp.head().getRow()] = new PathInfo(temp.head(), lowestCost+1);					
					else
						if(this.board[temp.head().getColumn()][temp.head().getRow()] == null || (this.board[temp.head().getColumn()][temp.head().getRow()].getMovementCost() < lowestCost && this.board[temp.head().getColumn()][temp.head().getRow()].getMovementCost() != 100 && this.board[temp.head().getColumn()][temp.head().getRow()].getMovementCost() != -1 &&  this.board[temp.head().getColumn()][temp.head().getRow()].getMovementCost() != 99)) 
							this.board[temp.head().getColumn()][temp.head().getRow()] = new PathInfo(temp.head(), lowestCost+1);
					temp.remove();
				}
				
				totalPlacedConnected.remove();
			}
			
			while(!temp.isEmpty())
			setAllConected(lowestCost, temp.remove());
		}
	}
	
	
	public boolean isDeadEnd(Queue<Position> neighbors) {
		while(!neighbors.isEmpty())
		{
			if(this.board[neighbors.head().getColumn()][neighbors.head().getRow()] != null)
			if(this.board[neighbors.head().getColumn()][neighbors.head().getRow()].getMovementCost() == -1)
				return false;
			neighbors.remove();
		}
		return true;
	}
	
	public boolean isIncludingEnd(){
		for(int i =0;i < this.board.length; i++) {
			//		System.out.println("playersColor: " + playersColor + ", index: " + i);
			if(this.board[i][this.board.length-1] != null && playersColor == 'B')
				if(this.board[i][this.board.length-1].getMovementCost() != 100 && this.board[i][this.board.length-1].getMovementCost() != -1) {
					return true; }
				else if(this.board[i][this.board.length-1].getMovementCost() == -1 && isThereNeighborNotNull(new Position(i, this.board.length-1))) {
					this.board[i][this.board.length-1] = new PathInfo(new Position(i,this.board.length-1), 99);
					return true; }
				else if(this.board[i][this.board.length-1].getMovementCost() == 50 || this.board[i][this.board.length-1].getMovementCost() == 51)
					return true;

			if(this.board[this.board.length-1][i] != null && playersColor == 'R')
				if(this.board[this.board.length-1][i].getMovementCost() != 100 && this.board[this.board.length-1][i].getMovementCost() != -1) {
					return true; }
				else if(this.board[this.board.length-1][i].getMovementCost() == -1 && isThereNeighborNotNull(new Position(this.board.length-1, i))) {
					this.board[this.board.length-1][i] = new PathInfo(new Position(this.board.length-1,i), 99);
					return true; 
				}
//				else if(this.board[this.board.length-1][i].getMovementCost() == 50 || this.board[this.board.length-1][i].getMovementCost() == 51)
//					return true;

		}
		return false;
	}


	public boolean isThereNeighborNotNull(Position pos) {
		Queue<Position> neighbors = getNeighbors(pos);
		while(!neighbors.isEmpty()) {
			if(this.board[neighbors.head().getColumn()][neighbors.head().getRow()] != null)
				if(this.board[neighbors.head().getColumn()][neighbors.head().getRow()].getMovementCost() != 100 && this.board[neighbors.head().getColumn()][neighbors.head().getRow()].getMovementCost() != -1)
					return true;
			neighbors.remove();
		}
		return false;
	}

	
	public PathInfo BFS(int currentCost) {
		if(currentCost >= 45)
			return new PathInfo(new Position(-2,-2),200);
//		System.out.println(currentCost);
		if(isIncludingEnd()) {
			for(int i =0;i < this.board.length; i++) {
				if((this.board[i][this.board.length-1] != null && playersColor == 'B'))
					if((this.board[i][this.board.length-1].getMovementCost() != 100 && this.board[i][this.board.length-1].getMovementCost() != -1 && this.playersColor == 'B'))
						return this.board[i][this.board.length-1];
				if(this.board[this.board.length-1][i] != null && playersColor == 'R')
					if(this.board[this.board.length-1][i].getMovementCost() != 100 && this.board[this.board.length-1][i].getMovementCost() != -1 && this.playersColor == 'R')
						return this.board[this.board.length-1][i];
			}
		}
		else { setBoardPosition(currentCost);
//		System.out.println(currentCost);
		}
		if(!isStuck)
		return BFS(currentCost+1); 
		else return new PathInfo(new Position(-2,-2),200);


	}


	public Queue<Position> getShortestChain() {
		PathInfo endOfChain = BFS(0);
		if(endOfChain.getMovementCost() == 200)
			return new Queue<Position>();
		int i = 0;
		Queue<Position> chain = new Queue<Position>();
		Position currentPos = new Position(endOfChain.getPosition());
		while(i < 150) {
//			System.out.println("s");
//			printPathBoard();
			if(this.board[currentPos.getColumn()][currentPos.getRow()].getMovementCost() == 0 || ((currentPos.getColumn() == 0 && playersColor == 'R') || (currentPos.getRow() == 0 && playersColor == 'B'))) {
				chain.insert(this.board[currentPos.getColumn()][currentPos.getRow()].getPosition());
				this.board[currentPos.getColumn()][currentPos.getRow()] = new PathInfo(currentPos,50);
				return chain;
			}
			if(this.board[currentPos.getColumn()][currentPos.getRow()].getMovementCost() == -1) {
				this.board[currentPos.getColumn()][currentPos.getRow()] = new PathInfo(currentPos,51);
			}
			else  {
				chain.insert(currentPos);
			}
//			System.out.println("The lowest cost of: " + currentPos + " is: " + findLowestNeighborCost(currentPos) + " with the cost of: " + lowestNeighborCost(currentPos));
			if(this.board[currentPos.getColumn()][currentPos.getRow()].getMovementCost() != 99) {
			this.board[currentPos.getColumn()][currentPos.getRow()] = new PathInfo(currentPos,50);
			}
			else {
				this.board[currentPos.getColumn()][currentPos.getRow()] = new PathInfo(currentPos,51);
			}
				currentPos = findLowestNeighborCost(currentPos);
				i++;
		}
		Queue<Position> que = new Queue<Position>();
		que.insert(randomValidPosition());
		isStuck = true;
		return que;
	}

	
	public Position randomValidPosition() {
		Random rnd = new Random();
		int row = rnd.nextInt(this.board.length);
		int col = rnd.nextInt(this.board.length);
		while(true)
		{
			row = rnd.nextInt(this.board.length);
			col = rnd.nextInt(this.board.length);
			if(this.board[col][row] == null) {
				this.board[col][row] = new PathInfo(new Position(col,row),51);
				return new Position(col,row);
			}
			else
			 if(this.board[col][row].getMovementCost() == 100 || this.board[col][row].getMovementCost() == 51 || this.board[col][row].getMovementCost() == 99 || this.board[col][row].getMovementCost() == -1) {
					this.board[col][row] = new PathInfo(new Position(col,row),51);
				 return new Position(col,row);		 
			 }
		}
		
	}
	

	public int chainAmountRequired() {
		int mone = 0;
		Queue<Position> chain = getShortestChain();
		if(chain.isEmpty())
			return 5000;
		while(!chain.isEmpty()) {
			if(this.board[chain.head().getColumn()][chain.head().getRow()] != null)
			if(this.board[chain.head().getColumn()][chain.head().getRow()].getMovementCost() == 50)
				mone++;
			chain.remove();
		}
//		System.out.println("Required: " + mone);
//		printPathBoard();
		if(!isStuck)
		return mone;
		else
			return 5000;
	}
	
}