import java.awt.EventQueue;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JOptionPane;


public class Delete {

	private JFrame frmDelete;
	private Object[] id = {0, 1, 2};
	private int index;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Delete window = new Delete();
					window.frmDelete.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Delete() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDelete = new JFrame();
		frmDelete.setTitle("Delete");
		frmDelete.setBounds(100, 100, 450, 300);
		frmDelete.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDelete.getContentPane().setLayout(new MigLayout("", "[grow][grow][139.00,grow][80.00,grow]", "[grow][grow][grow][grow]"));
		
		JLabel lblDeleteId = new JLabel("Delete ID:");
		frmDelete.getContentPane().add(lblDeleteId, "cell 1 1");
		
		final JComboBox idList = new JComboBox(id);
		idList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				index = idList.getSelectedIndex();
			}
		});
		frmDelete.getContentPane().add(idList, "cell 2 1,growx");
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			System.exit(0);
			}
		});
		
		JButton confirm = new JButton("Confirm");
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if(!check()){
				JOptionPane.showMessageDialog(new JFrame(), String.format("Your balance will be negative!", e.getActionCommand()));
			}
			else{
				//do the delete work, specific category is needed
				frmDelete.setVisible(false);
			}
			
			}
		});
		frmDelete.getContentPane().add(confirm, "cell 1 3");
		frmDelete.getContentPane().add(cancel, "cell 2 3,alignx right");
	}
	
	public boolean check(){
			//check, specific category needed
		return false;
	}

}
