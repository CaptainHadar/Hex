package Logic;

public class Position {

	private int column;
	private int row;
	
	public Position(int column, int row) {
		this.column = column;
		this.row = row;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	public Position(Position pos1) {
		this.column = pos1.column;
		this.row = pos1.row;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public boolean isSame(Position pos) {
		return(this.row == pos.row && this.column == pos.column);
	}
	
	
	public String toString() {
		return(row + ", " + column);
	}
	
	
}
