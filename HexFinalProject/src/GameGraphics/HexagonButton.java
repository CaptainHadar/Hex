package GameGraphics;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JButton;

public class HexagonButton extends JButton {
        private static final long serialVersionUID = 1L;
        public static final int LENGTH = 100;
        public static final int WIDTH = 86;
        private int row = 0;
        private int col = 0;

        // בנאי של הכפתור
        public HexagonButton(int col, int row) {    		
        	setOpaque(false);
        	setContentAreaFilled(false);
        	setBorderPainted(false);
            setPreferredSize(new Dimension(WIDTH, LENGTH));
            this.row = row;
            this.col = col;
        }
        
        // מצייר את הכפתור לצורת משושה, החישוב נעשה ע"פ מישור גאוס
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Polygon hex = new Polygon();
            
            hex.addPoint(43, 0); 
            hex.addPoint(0, 25);
            hex.addPoint(0, 75);
            hex.addPoint(43, 100);
            hex.addPoint(85 , 75);
            hex.addPoint(85, 25);

            g.drawPolygon(hex);
        }
        // מחזיר את השורה שבה נמצא הכפתור
        public int getRow() {
            return row;
        }

        // מחזיר את העמודה שבה נמצא הכפתור
        public int getCol() {
            return col;
        }
    
}