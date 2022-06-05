package GameGraphics;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import Logic.Board;

public class CvcGUI extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JSlider boardSize;
	private JLabel boardLabel;
	private JButton startButton;
	private JCheckBox isThereSwapRule;  
	private JSlider blueComputerLevel;
	private JSlider redComputerLevel;
	private JRadioButton redBegins;  
	private JRadioButton blueBegins;
	private ButtonGroup turnFirst;
	private JLabel redLabel;
	private JLabel blueLabel;

	public CvcGUI() {

		this.boardLabel = new JLabel("Board Size");
		this.boardLabel.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 1 / 2 + 25, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 1 / 2 + 190, 400, 120);
		this.boardLabel.setFont(new Font(this.boardLabel.getFont().getName(), this.boardLabel.getFont().getStyle(), 30));
		add(this.boardLabel);

		this.boardSize = new JSlider(3, 11, 5);
		this.boardSize.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 1 / 2 - 200, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 1 / 2 + 265, 600, 100);
		this.boardSize.setMinorTickSpacing(1);
		this.boardSize.setMajorTickSpacing(1);
		this.boardSize.setPaintTicks(true);
		this.boardSize.setPaintLabels(true);
		add(this.boardSize);

		this.blueLabel = new JLabel("Choose Blue's Computer Level");
		this.blueLabel.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3 / 4, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 575, 600, 100);
		this.blueLabel.setFont(new Font(this.blueLabel.getFont().getName(), this.blueLabel.getFont().getStyle(), 30));
		add(this.blueLabel);

		redLabel = new JLabel("Choose Red's Computer Level");
		this.redLabel.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 4, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 575, 600, 100);
		this.redLabel.setFont(new Font(this.redLabel.getFont().getName(), this.redLabel.getFont().getStyle(), 30));
		add(this.redLabel);

		this.blueComputerLevel = new JSlider(1,5,1);
		this.blueComputerLevel.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3 / 4 - 200, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 500, 600, 100);
		this.blueComputerLevel.setMinorTickSpacing(1);
		this.blueComputerLevel.setMajorTickSpacing(1);
		this.blueComputerLevel.setPaintTicks(true);
		this.blueComputerLevel.setPaintLabels(true);
		add(this.blueComputerLevel);

		this.redComputerLevel = new JSlider(1,5,1);
		this.redComputerLevel.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 4 - 200, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 500, 600, 100);
		this.redComputerLevel.setMinorTickSpacing(1);
		this.redComputerLevel.setMajorTickSpacing(1);
		this.redComputerLevel.setPaintTicks(true);
		this.redComputerLevel.setPaintLabels(true);
		add(this.redComputerLevel);


		this.redBegins = new JRadioButton("Red begins");
		this.redBegins.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 4, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 262, 200, 50);
		this.redBegins.setFont(new Font(this.redBegins.getFont().getName(), this.redBegins.getFont().getStyle(), 30));
		this.redBegins.setSelected(true);
		add(this.redBegins);
		this.blueBegins = new JRadioButton("Blue begins");
		this.blueBegins.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 4, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 312, 200, 50);
		this.blueBegins.setFont(new Font(this.blueBegins.getFont().getName(), this.blueBegins.getFont().getStyle(), 30));
		add(this.blueBegins);
		this.turnFirst = new ButtonGroup();

		this.turnFirst.add(redBegins);
		this.turnFirst.add(blueBegins);

		this.startButton = new JButton("Start!");
		this.startButton.setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 175, 200, 100);
		this.startButton.setFont(new Font(this.startButton.getFont().getName(), this.startButton.getFont().getStyle(), 40));
		this.startButton.addActionListener(this);
		add(this.startButton);

		this.isThereSwapRule = new JCheckBox("Swap rule");
		this.isThereSwapRule.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3 / 4, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 287, 200, 100);
		this.isThereSwapRule.setFont(new Font(this.isThereSwapRule.getFont().getName(), this.isThereSwapRule.getFont().getStyle(), 30));
		add(isThereSwapRule);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Board brd;
		if(this.redBegins.isSelected())
			brd = new Board('R', this.redComputerLevel.getValue(), this.blueComputerLevel.getValue(), this.isThereSwapRule.isSelected(), this.boardSize.getValue());
		else
			brd = new Board('B', this.redComputerLevel.getValue(), this.blueComputerLevel.getValue(), this.isThereSwapRule.isSelected(), this.boardSize.getValue());


		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		brd.resetBoard();
		brd.previewBoard();
		brd.startGame();

	}


	
	public void preview() {
		setUndecorated(true);
		setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		setLayout(null);
		setVisible(true);
	}



}
