package tk.jackyliao123.ssh;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PromptUI {
	private JTextField host;
	private JTextField port;
	private JTextField username;
	private JPasswordField password;
	private boolean finished = false;
	public void show(){
		final JFrame frame = new JFrame("jSSH");
		JPanel fields = new JPanel();
		frame.getContentPane().add(fields);
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[]{65, 200, 0};
		layout.rowHeights = new int[]{30, 30, 30, 25, 0};
		layout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		fields.setLayout(layout);
		
		ActionListener connectListener = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				finished = true;
			}
		};
		
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;
		constraint.insets = new Insets(0, 0, 5, 5);
		constraint.gridx = 0;
		constraint.gridy = 0;
		fields.add(new JLabel("Host: ", SwingConstants.RIGHT), constraint);
		
		host = new JTextField("192.168.2.17");
		host.addActionListener(connectListener);
		constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;
		constraint.insets = new Insets(0, 0, 5, 0);
		constraint.gridx = 1;
		constraint.gridy = 0;
		fields.add(host, constraint);
		
		constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;
		constraint.insets = new Insets(0, 0, 5, 5);
		constraint.gridx = 0;
		constraint.gridy = 1;
		fields.add(new JLabel("Port: ", SwingConstants.RIGHT), constraint);
		
		port = new JTextField("22");
		port.addActionListener(connectListener);
		constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;
		constraint.insets = new Insets(0, 0, 5, 0);
		constraint.gridx = 1;
		constraint.gridy = 1;
		fields.add(port, constraint);
		
		constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;
		constraint.insets = new Insets(0, 0, 5, 5);
		constraint.gridx = 0;
		constraint.gridy = 2;
		fields.add(new JLabel("Username: ", SwingConstants.RIGHT), constraint);
		
		username = new JTextField("pi");
		username.addActionListener(connectListener);
		constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;
		constraint.insets = new Insets(0, 0, 5, 0);
		constraint.gridx = 1;
		constraint.gridy = 2;
		fields.add(username, constraint);
		
		constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;
		constraint.insets = new Insets(0, 0, 0, 5);
		constraint.gridx = 0;
		constraint.gridy = 3;
		fields.add(new JLabel("Password: ", SwingConstants.RIGHT), constraint);
		
		password = new JPasswordField("raspberry");
		password.addActionListener(connectListener);
		constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.BOTH;
		constraint.gridx = 1;
		constraint.gridy = 3;
		fields.add(password, constraint);
		
		JButton connect = new JButton("Start Session");
		connect.addActionListener(connectListener);
		frame.add(connect, "South");
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		while(!finished){
			try{
				Thread.sleep(50);
			}
			catch(InterruptedException e){
			}
		}
		frame.dispose();
	}
	public String getHost(){
		return host.getText();
	}
	public int getPort(){
		return Integer.parseInt(port.getText());
	}
	public String getUsername(){
		return username.getText();
	}
	public String getPassword(){
		return new String(password.getPassword());
	}
}
