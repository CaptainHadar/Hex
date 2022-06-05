package GameGraphics;
import javax.swing.*;

public class HexagonPattern extends JPanel {
    private static final long serialVersionUID = 1L;
    public int ROWS;
    public int COLUMNS;
    private HexagonButton[][] hexButton;


    public HexagonPattern(int sizeOfBoard) {
    	this.ROWS = sizeOfBoard;
    	this.COLUMNS = sizeOfBoard;
        setLayout(null);
        hexButton = new HexagonButton[COLUMNS][ROWS];
        initGUI(sizeOfBoard);
    }



    public void initGUI(int sizeOfBoard) {
        int offsetX = 35;
        int offsetY = 300;
        for(int row = 0; row < sizeOfBoard; row++) {
            for(int col = 0; col < sizeOfBoard; col++){
                hexButton[col][row] = new HexagonButton(col, row);

                add(hexButton[col][row]);
                hexButton[col][row].setBounds(offsetY, offsetX, 87, 100);
                offsetX += 75;
                offsetY +=  43;
            }

            offsetX = 35;
            offsetY = 300 + ((row + 1) * 86);
            
        }
    }

  
  public HexagonButton[][] getGraphicalBoard() {
	  return this.hexButton;
  }
    

}