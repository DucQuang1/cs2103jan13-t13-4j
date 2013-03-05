import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;

/**
 * GUI class that handles an income entry
 * @author Pang Kang Wei,Joshua	A0087809M
 *
 */
public class InputPanel extends JPanel {
	
	//have no idea why the compiler asked me to add this -.-
	private static final long serialVersionUID = 1L;

	//default format for date
	protected final static SimpleDateFormat date_format = new SimpleDateFormat("dd/mm/yy");
	protected final static Font error_font = new Font("SanSerif", Font.ITALIC, 12);
	
	protected JFrame hostFrame;
	protected JTextField amountField;
	protected JTextField dd;
	protected JTextField mm;
	protected JTextField yyyy;
	protected JTextField category2Field;
	protected JTextField category1Field;
	protected JTextField descriptionField;
	
	protected JLabel lblAmount = new JLabel("Amount");
	protected JLabel ErrorDisplay;
	protected String errorMsg = "";
	
	protected AssetCatMgr assetCatMgr;
	protected LiabilityCatMgr liabilityCatMgr;
	protected IncomeCatMgr incomeCatMgr;
	protected ExpenseCatMgr expenseCatMgr;
	protected EntryMgr entryMgr;
	protected HistoryMgr historyMgr;
	
	/**
	 * Create the panel.
	 */
	public InputPanel(final JFrame hostFrame, final EntryMgr entryMgr, final HistoryMgr historyMgr) {
		
		//pass reference to the panel for checking and adding of transactions
		this.hostFrame = hostFrame;
		this.entryMgr = entryMgr;
		this.historyMgr = historyMgr;

		setSize(700,300);
		setLayout(new MigLayout("", "[100,left]5[100]25[130,left]5[100]5[grow]", "[20]5[]5[30]5[50]10[50,grow,top]"));
		
		add(lblAmount, "cell 0 0,alignx left");
		
		amountField = new JTextField();
		amountField.setColumns(10);
		add(amountField, "cell 1 0,growx");
		
		JLabel lblDate = new JLabel("Date");
		add(lblDate, "cell 2 0,alignx left");
		
		dd = new JTextField();
		dd.setColumns(5);
		add(dd, "cell 3 0,growx");
		
		mm = new JTextField();
		mm.setColumns(5);
		add(mm, "cell 3 0,growx");
		
		yyyy = new JTextField();
		add(yyyy, "cell 4 0,");
		yyyy.setColumns(8);
		
		JLabel lblCreateType = new JLabel("Create Category");
		add(lblCreateType, "cell 0 2,alignx left");
		
		category1Field = new JTextField();
		add(category1Field, "cell 1 2,growx");
		category1Field.setColumns(10);
		
		JLabel lblOrCreateCat = new JLabel("Create Category");
		add(lblOrCreateCat, "cell 2 2,alignx left");
		
		category2Field = new JTextField();
		add(category2Field, "cell 3 2,growx");
		category2Field.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description");
		add(lblDescription, "cell 0 3");
		
		descriptionField = new JTextField();
		add(descriptionField, "cell 1 3 4 1,grow");
		
		ErrorDisplay = new JLabel();
		ErrorDisplay.setFont(error_font);
		ErrorDisplay.setForeground(Color.RED);
		add(ErrorDisplay, "cell 1 4 3 1,grow");
		
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
	
	/**
	 * Resets all input fields
	 * TODO reset to today's date
	 */
	public void resetFields(){
		 amountField.setText("$$$");
		 dd.setText("DD");
		 mm.setText("MM");
		 yyyy.setText("YYYY");
		 category2Field.setText("");
		 category1Field.setText("");
		 descriptionField.setText("");
	}
}
