import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * GUI class that contains a JPanel to handle adding of entries
 * @author Pang Kang Wei,Joshua	A0087809M
 *
 */
public class InputPanel {

	//default format for date
	protected final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	protected final static Font error_font = new Font("SanSerif", Font.ITALIC, 12);
	
	//hostFrame refers to the pop-up frame that holds this InputPanel
	protected JFrame hostFrame;
	
	protected JPanel inputPanel;
	protected JTextField amountField;
	protected JTextField dd;
	protected JTextField mm;
	protected JTextField yyyy;
	protected JTextField category2Field;
	protected JTextField category1Field;
	protected JTextField descriptionField;
	
	protected JLabel lblAmount = new JLabel("Amount");
	protected JLabel lblDate = new JLabel("Date");
	protected JLabel lblCreateType = new JLabel("Create Category");
	protected JLabel lblOrCreateCat = new JLabel("Create Category");
	protected JLabel lblDescription = new JLabel("Description");
	protected JLabel ErrorDisplay = new JLabel();
	protected String errorMsg = "";
	
	protected  AssetCatMgr assetCatMgr;
	protected  LiabilityCatMgr liabilityCatMgr;
	protected  IncomeCatMgr incomeCatMgr;
	protected  ExpenseCatMgr expenseCatMgr;
	protected  EntryMgr entryMgr;
	protected  HistoryMgr historyMgr;
	
	/**
	 * Create the panel.
	 */
	public InputPanel(JFrame hostFrame, EntryMgr entryMgr, HistoryMgr historyMgr) {
		
		//pass reference to the panel for checking and adding of transactions
		this.hostFrame = hostFrame;
		this.entryMgr = entryMgr;
		this.historyMgr = historyMgr;
		
		inputPanel = new JPanel(new MigLayout("", "[100,left]5[100]25[130,left]5[100]5[grow]", "[20]5[]5[30]5[50]10[50,grow,top]"));
		inputPanel.setSize(700,300);
		
		inputPanel.add(lblAmount, "cell 0 0,alignx left");
		
		amountField = new JTextField();
		amountField.setColumns(10);
		inputPanel.add(amountField, "cell 1 0,growx");

		inputPanel.add(lblDate, "cell 2 0,alignx left");
		
		dd = new JTextField();
		dd.setColumns(5);
		inputPanel.add(dd, "cell 3 0,growx");
		
		mm = new JTextField();
		mm.setColumns(5);
		inputPanel.add(mm, "cell 3 0,growx");
		
		yyyy = new JTextField();
		inputPanel.add(yyyy, "cell 4 0,");
		yyyy.setColumns(8);
		
		inputPanel.add(lblCreateType, "cell 0 2,alignx left");
		
		category1Field = new JTextField();
		inputPanel.add(category1Field, "cell 1 2,growx");
		category1Field.setColumns(10);
		
		inputPanel.add(lblOrCreateCat, "cell 2 2,alignx left");
		
		category2Field = new JTextField();
		inputPanel.add(category2Field, "cell 3 2,growx");
		category2Field.setColumns(10);
		
		inputPanel.add(lblDescription, "cell 0 3");
		
		descriptionField = new JTextField();
		inputPanel.add(descriptionField, "cell 1 3 4 1,grow");
		
		ErrorDisplay.setFont(error_font);
		ErrorDisplay.setForeground(Color.RED);
		inputPanel.add(ErrorDisplay, "cell 1 4 3 1,grow");
		
		resetFields();

	}
	
	/**
	 * For setting the 2 category managers specific to the inherited panel
	 * @param assetCatMgr
	 * @param incomeCatMgr
	 */
	public void setCatMgr(final AssetCatMgr assetCatMgr, final IncomeCatMgr incomeCatMgr){
		this.assetCatMgr = assetCatMgr;
		this.incomeCatMgr = incomeCatMgr;
	}
	
	/**
	 * For setting the 2 category managers specific to the inherited panel
	 * @param assetCatMgr
	 * @param expenseCatMgr
	 */
	public void setCatMgr(final AssetCatMgr assetCatMgr, final ExpenseCatMgr expenseCatMgr){
		this.assetCatMgr = assetCatMgr;
		this.expenseCatMgr = expenseCatMgr;
	}
	
	/**
	 * For setting the 2 category managers specific to the inherited panel
	 * @param liabilityCatMgr
	 * @param expenseCatMgr
	 */
	public void setCatMgr(final LiabilityCatMgr liabilityCatMgr, final ExpenseCatMgr expenseCatMgr){
		this.liabilityCatMgr = liabilityCatMgr;
		this.expenseCatMgr = expenseCatMgr;
	}
	
	/**
	 * For setting the 2 category managers specific to the inherited panel
	 * @param assetCatMgr
	 * @param liabilityCatMgr
	 */
	public void setCatMgr(final AssetCatMgr assetCatMgr, final LiabilityCatMgr liabilityCatMgr){
		this.assetCatMgr = assetCatMgr;
		this.liabilityCatMgr = liabilityCatMgr;
	}
	
	public JPanel getPanel(){
		return inputPanel;
	}
	
	/**
	 * Resets all input fields
	 */
	public void resetFields(){
		 
		amountField.setText("$$$");
		Date currentDate = new Date();
		String date = date_format.format(currentDate);
		StringTokenizer st = new StringTokenizer(date,"/");
		dd.setText(st.nextToken());
		mm.setText(st.nextToken());
		yyyy.setText(st.nextToken());
		category2Field.setText("");
		category1Field.setText("");
		descriptionField.setText("");
	}
}
