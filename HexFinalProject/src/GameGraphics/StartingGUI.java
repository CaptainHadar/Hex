package GameGraphics;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

public class StartingGUI extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JLabel boardLabel;
	private JButton startButton;
	private JRadioButton pvp;  
	private JRadioButton pvc;
	private JRadioButton cvc;
	private ButtonGroup gamemode;
	private JLabel blueLabel;
	private JLabel hexPicture;

	public StartingGUI() {
		this.hexPicture = new JLabel();

		this.hexPicture.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("MenuPic.png")));

		this.hexPicture.setBounds(600, -50, 1000, 1000);
		this.hexPicture.revalidate();
		add(this.hexPicture);
		this.boardLabel = new JLabel("Hex");
		this.boardLabel.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 1 / 2 + 30, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 15 , 400, 120);
		this.boardLabel.setFont(new Font(this.boardLabel.getFont().getName(), this.boardLabel.getFont().getStyle(), 45));
		add(this.boardLabel);

		this.blueLabel = new JLabel("Please choose the game mode!");
		this.blueLabel.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 1 / 2 - 65, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() -350, 600, 100);
		this.blueLabel.setFont(new Font(this.blueLabel.getFont().getName(), this.blueLabel.getFont().getStyle(), 35));
		add(this.blueLabel);		

		this.pvp = new JRadioButton("Player Versus Player");
		this.pvp.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 1 / 2  -45, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 270, 500, 45);
		this.pvp.setFont(new Font(this.pvp.getFont().getName(), this.pvp.getFont().getStyle(), 30));
		this.pvp.setSelected(true);
		add(this.pvp);
		this.pvc = new JRadioButton("Computer Versus Player");
		this.pvc.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 1 / 2 - 45, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 225, 500, 45);
		this.pvc.setFont(new Font(this.pvc.getFont().getName(), this.pvc.getFont().getStyle(), 30));
		add(this.pvc);
		this.cvc = new JRadioButton("Computer Versus Computer");
		this.cvc.setBounds((int)  Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 1 / 2 - 45, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() -180, 500, 45);
		this.cvc.setFont(new Font(this.cvc.getFont().getName(), this.cvc.getFont().getStyle(), 30));
		add(this.cvc);
		this.gamemode = new ButtonGroup();

		this.gamemode.add(cvc);
		this.gamemode.add(pvp);
		this.gamemode.add(pvc);

		this.startButton = new JButton("Proceed!");
		this.startButton.setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 125, 200, 100);
		this.startButton.setFont(new Font(this.startButton.getFont().getName(), this.startButton.getFont().getStyle(), 35));
		this.startButton.addActionListener(this);
		add(this.startButton);

		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.pvp.isSelected()){
			PvpGUI newMenu = new PvpGUI();
			newMenu.preview();
		}

		else if(this.pvc.isSelected()) {
			PvcGUI newMenu = new PvcGUI();
			newMenu.preview();

		}

		else {
			CvcGUI newMenu = new CvcGUI();
			newMenu.preview();
		}

		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}


	public void preview() {
		setUndecorated(true);
		setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		setLayout(null);
		setVisible(true);
	}


}