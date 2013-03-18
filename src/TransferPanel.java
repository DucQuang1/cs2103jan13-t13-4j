import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * GUI class that contains a JPanel to handle adding of entries
 * @author JP
 * @author Bohua
 */
public class TransferPanel {

	//default format for date
	protected static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	protected static Font error_font = new Font("SanSerif", Font.ITALIC, 12);
	
	//hostFrame refers to the pop-up frame that holds this transferPanel
	protected JFrame hostFrame;
	
	protected JPanel transferPanel;
	
	protected JComboBox<String> fromCB;
	protected JComboBox<String> toCB;
	
	protected JTextField amountField;
	protected JTextField dd;
	protected JTextField mm;
	protected JTextField yyyy;
	protected JTextField descriptionField;
	
	protected JLabel lblHeading = new JLabel();
	protected JLabel lblAmount = new JLabel("Amount");
	protected JLabel lblDate = new JLabel("Date");
	protected JLabel lblCategory1 = new JLabel("From");
	protected JLabel lblCategory2 = new JLabel("To");
	protected JLabel lblDescription = new JLabel("Description");
	protected JLabel ErrorDisplay = new JLabel();
	protected String errorMsg = "";
	
	protected final EntryMgr entryMgr;
	protected final HistoryMgr historyMgr;
	
	/**
	 * Default Constructor for creating the panel.
	 */
	public TransferPanel(final JFrame hostFrame, final EntryMgr entryMgr, final HistoryMgr historyMgr){
		
		//pass reference to the panel for checking and adding of transactions
		this.hostFrame = hostFrame;
		this.entryMgr = entryMgr;
		this.historyMgr = historyMgr;
		
		
		transferPanel = new JPanel(new MigLayout("", "[100,left]5[100]25[130,left]5[100]5[grow]", "[25]5[]5[30]5[50]10[50,grow,top]"));
		transferPanel.setBackground(new Color(255, 255, 255));
		transferPanel.setSize(700,300);
		
		lblHeading.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		transferPanel.add(lblHeading, "cell 0 0 2 1,growx");
		
		transferPanel.add(lblCategory1, "cell 0 1,growx");
		transferPanel.add(lblCategory2, "cell 2 1,growx");
		
		fromCB = new JComboBox<String>();
		transferPanel.add(fromCB, "cell 1 1,growx");
		
		toCB = new JComboBox<String>();
		transferPanel.add(toCB, "cell 3 1,growx");
		
		transferPanel.add(lblAmount, "cell 0 2,alignx left");

		amountField = new JTextField();
		amountField.setColumns(10);
		transferPanel.add(amountField, "cell 1 2,growx");
		
		transferPanel.add(lblDate, "cell 2 2,alignx left");

		dd = new JTextField();
		dd.setColumns(5);
		transferPanel.add(dd, "flowx,cell 3 2,growx");
		
		mm = new JTextField();
		mm.setColumns(5);
		transferPanel.add(mm, "cell 3 2,growx");
		
		yyyy = new JTextField();
		transferPanel.add(yyyy, "cell 4 2");
		yyyy.setColumns(8);
		
		transferPanel.add(lblDescription, "cell 0 3");
		
		descriptionField = new JTextField();
		transferPanel.add(descriptionField, "cell 1 3 4 1,grow");
		
		ErrorDisplay.setFont(error_font);
		ErrorDisplay.setForeground(Color.RED);
		transferPanel.add(ErrorDisplay, "cell 1 4 3 1,grow");
		
	}
	
	/**
	 * Gets the transfer panel
	 * @return transferPanel
	 */
	public JPanel getPanel(){
		return transferPanel;
	}
	
	/**
	 * Resets amount to '$$$' and date to current date
	 * Called by child classes to refresh common fields
	 */
	protected void resetDefault(){
		
		amountField.setText("$$$");
		Date currentDate = new Date();
		String date = date_format.format(currentDate);
		StringTokenizer st = new StringTokenizer(date,"/");
		dd.setText(st.nextToken());
		mm.setText(st.nextToken());
		yyyy.setText(st.nextToken());
	}
}
