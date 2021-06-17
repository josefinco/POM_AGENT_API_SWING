package sampleclient;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class TestSDK_WBuilder {

	TextField textField_agentid = new TextField();
	TextField textField_pomIP = new TextField();
	TextField textField_pomport = new TextField();
	TextField textField_ext = new TextField();
	TextField textField_agtpass = new TextField();
	TextField textField_skillname = new TextField();
	TextField textField_skillnumber = new TextField();
	TextField textField_skillpriority = new TextField();
	Panel panel = new Panel();

	public JFrame frame;
	public static JTextArea textArea_logs = new JTextArea();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestSDK_WBuilder window = new TestSDK_WBuilder();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestSDK_WBuilder() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 791, 322);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 208, 332, 0 };
		gridBagLayout.rowHeights = new int[] { 283, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(panel, gbc_panel);

		Label pomip = new Label("IP Pom:");

		textField_pomIP.setText("172.31.254.21");

		Label label_1 = new Label("Port:");

		textField_pomport.setText("9970");

		Label label_2 = new Label("Agent ID:");

		textField_agentid.setText("1210001");

		Label label_3 = new Label("Extension:");

		textField_ext.setText("3651000");

		Label label_4 = new Label("Agent Pass:");

		textField_agtpass.setText("1234");

		Label label_5 = new Label("Skill Name:");

		textField_skillname.setText("Teste");

		Label label_5_1 = new Label("Skill number:");

		textField_skillnumber.setText("122");

		Label label_5_2 = new Label("Skill Priority");

		textField_skillpriority.setText("1");
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		panel.add(pomip);
		panel.add(textField_pomIP);
		panel.add(label_1);
		panel.add(textField_pomport);
		panel.add(label_2);
		panel.add(textField_agentid);
		panel.add(label_3);
		panel.add(textField_ext);
		panel.add(label_4);
		panel.add(textField_agtpass);
		panel.add(label_5);
		panel.add(textField_skillname);
		panel.add(label_5_1);
		panel.add(textField_skillnumber);
		panel.add(label_5_2);
		panel.add(textField_skillpriority);
		panel.add(btnlogin);
		btnlogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String[] skarray = new String[3];
				try {
					Controller.setHost(textField_pomIP.getText());
					Controller.setPort(Integer.parseInt(textField_pomport.getText()));
					Controller.setTrustStore("keystoreGo");
					Controller.setPassword("Team@365");
					Controller.setAgentId(textField_agentid.getText());
					Controller.setAgentExtension(textField_ext.getText());
					Controller.setFipsMode(0);

					skarray[0] = textField_skillname.getText();
					skarray[1] = textField_skillnumber.getText();
					skarray[2] = textField_skillpriority.getText();
					Controller.setSkills(skarray);
					Controller.setAgentPassword(textField_agtpass.getText());
					
					
					new Thread(login).start();
	

				} catch (Exception e2) {
					System.out.println("Exception: " + e2.getMessage());
				}
				
			}
		});
		btnlogin.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(btnlogout);
		btnlogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		GridBagConstraints gbc_textArea_logs = new GridBagConstraints();
		gbc_textArea_logs.insets = new Insets(0, 0, 5, 0);
		gbc_textArea_logs.fill = GridBagConstraints.BOTH;
		gbc_textArea_logs.gridx = 1;
		gbc_textArea_logs.gridy = 1;
		textArea_logs.setTabSize(10);
		textArea_logs.setRows(10);
		textArea_logs.setLineWrap(true);
		textArea_logs.setEditable(false);
		scrollPane.add(textArea_logs);

	}

	private static Runnable login = new Runnable() {
		public void run() {
			try {
				Controller.login();
			} catch (Exception e) {
			}

		}
	};
	private final ScrollPane scrollPane = new ScrollPane();
	private final JButton btnlogin = new JButton("Login");
	private final JButton btnlogout = new JButton("Logout");

//		

}
