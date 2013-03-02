import java.awt.EventQueue;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.ComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


public class Transfer {
	private String assets[] = {"cash", "bankAcount1", "bankAcount2", "others"};
	private double amount;
	private Object item1, item2;
	private JFrame frmTransfer;
	private JTextField amountField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Transfer window = new Transfer();
					window.frmTransfer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Transfer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTransfer = new JFrame();
		frmTransfer.setTitle("Transfer");
		frmTransfer.setBounds(100, 100, 391, 308);
		frmTransfer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTransfer.getContentPane().setLayout(new MigLayout("", "[19.00][62.00,grow][77.00,grow][28.00,grow][74.00,grow][72.00,grow][31.00,grow]", "[89.00,grow][80.00,grow][grow]"));
		
		JLabel lblNewLabel = new JLabel("Amount:");
		frmTransfer.getContentPane().add(lblNewLabel, "cell 1 0");
		
		amountField = new JTextField();
		amountField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
			String str = amountField.getText();
			amount = Double.parseDouble(str);
			}
		});
		frmTransfer.getContentPane().add(amountField, "cell 2 0 3 1,growx");
		amountField.setColumns(10);
		
		
		JLabel lblNewLabel_1 = new JLabel("From:");
		frmTransfer.getContentPane().add(lblNewLabel_1, "cell 1 1,alignx left,aligny top");
		
		final JComboBox fromList = new JComboBox(assets);
		fromList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
			item1 = fromList.getSelectedItem();
			}
		});
		frmTransfer.getContentPane().add(fromList, "cell 2 1,growx,aligny top");
		
		JLabel lblNewLabel_2 = new JLabel("To:");
		frmTransfer.getContentPane().add(lblNewLabel_2, "cell 4 1,alignx center,aligny top");
		
		final JComboBox toList = new JComboBox(assets);
		toList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			item2 = toList.getSelectedItem();
			}
		});
		frmTransfer.getContentPane().add(toList, "cell 5 1,growx,aligny top");
		
		JButton btnNewButton = new JButton("Confirm");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			/*
			item1 = item1 - amount;
			item2 = item2 + amount;
			*/
			System.exit(0);
			}
		});
		frmTransfer.getContentPane().add(btnNewButton, "cell 2 2");
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
			}
		});
		frmTransfer.getContentPane().add(btnNewButton_1, "cell 4 2");
	}

}
