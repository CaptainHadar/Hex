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

public class PvcGUI extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JSlider boardSize;
	private JLabel boardLabel;
	private JButton startButton;
	private JCheckBox isThereSwapRule;
	private JRadioButton redColor;  
	private JRadioButton blueColor;  
	private JSlider ComputerLevel;
	private	ButtonGroup colorGroup;    
	private JRadioButton redBegins;  
	private JRadioButton blueBegins;
	private ButtonGroup turnFirst;
	private JLabel chooseColor;
	private JLabel blueLabel;

	public PvcGUI() {

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

		this.blueLabel = new JLabel("Choose The Computer Level");
		this.blueLabel.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3 / 4 - 100, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 800, 500, 75);
		this.blueLabel.setFont(new Font(this.blueLabel.getFont().getName(), this.blueLabel.getFont().getStyle(), 30));
		add(this.blueLabel);

		this.ComputerLevel = new JSlider(1,5,1);
		this.ComputerLevel.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3 / 4 - 200, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 725, 600, 75);
		this.ComputerLevel.setMinorTickSpacing(1);
		this.ComputerLevel.setMajorTickSpacing(1);
		this.ComputerLevel.setPaintTicks(true);
		this.ComputerLevel.setPaintLabels(true);
		add(this.ComputerLevel);


		this.redColor = new JRadioButton("Red");
		this.blueColor = new JRadioButton("Blue");
		this.colorGroup = new ButtonGroup();

		this.colorGroup.add(redColor);
		this.colorGroup.add(blueColor);


		this.redColor.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 4, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 700, 300, 50);
		this.redColor.setFont(new Font(this.redColor.getFont().getName(), this.redColor.getFont().getStyle(), 25));
		add(this.redColor);
		this.blueColor.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 4, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() -750, 300, 50);
		this.blueColor.setFont(new Font(this.blueColor.getFont().getName(), this.blueColor.getFont().getStyle(), 25));
		this.blueColor.setSelected(true);
		add(this.blueColor);

		this.chooseColor = new JLabel("Choose Your Color");
		this.chooseColor.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 4 - 100, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() -800, 300, 50);
		this.chooseColor.setFont(new Font(this.chooseColor.getFont().getName(), this.chooseColor.getFont().getStyle(), 30));
		add(this.chooseColor);

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
		{
			if(this.redColor.isSelected())
				brd = new Board('R', -1, this.ComputerLevel.getValue(), this.isThereSwapRule.isSelected(), this.boardSize.getValue());
			else
				brd = new Board('R', this.ComputerLevel.getValue(), -1, this.isThereSwapRule.isSelected(), this.boardSize.getValue());
		}
		else
		{
			if(this.redColor.isSelected())
				brd = new Board('B', -1, this.ComputerLevel.getValue(), this.isThereSwapRule.isSelected(), this.boardSize.getValue());
			else
				brd = new Board('B', this.ComputerLevel.getValue(), -1, this.isThereSwapRule.isSelected(), this.boardSize.getValue());
		}

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
