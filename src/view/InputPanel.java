package view;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import logic.AssetCatMgr;
import logic.EntryMgr;
import logic.ExpenseCatMgr;
import logic.HistoryMgr;
import logic.IncomeCatMgr;
import logic.LiabilityCatMgr;

/**
 * GUI class that contains a JPanel to handle adding of entries
 * @author Pang Kang Wei,Joshua	A0087809M
 *
 */
public class InputPanel {

	//default format for date
	protected final static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	protected final static Font error_font = new Font("SanSerif", Font.ITALIC, 12);
	
	//inputPanelInput_PNLHost_FRM refers to the pop-up frame that holds this inputPanelInput_PNL
	protected JFrame inputPanel_FRM;
	
	protected JPanel inputPanelInput_PNL;
	protected JTextField inputPanelAmount_TF;
	protected JTextField inputPanelDD_TF;
	protected JTextField inputPanelMM_TF;
	protected JTextField inputPanelYYYY_TF;
	protected JTextField inputPanelCat1_TF;
	protected JTextField inputPanelCat2_TF;
	protected JTextField inputPanelDescription_TF;
	
	protected JLabel inputPanelAmount_LBL = new JLabel("Amount");
	protected JLabel inputPanelDate_LBL = new JLabel("Date");
	protected JLabel inputPanelCreateCat1_LBL = new JLabel("Create Category");
	protected JLabel inputPanelCreateCat2_LBL = new JLabel("Create Category");
	protected JLabel inputPanelDescription_LBL = new JLabel("Description");
	protected JLabel inputPanelError_LBL = new JLabel();
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
		
		this.inputPanel_FRM = hostFrame;
		this.entryMgr = entryMgr;
		this.historyMgr = historyMgr;
		
		inputPanelInput_PNL = new JPanel(new MigLayout("", "[100,left]5[100]25[130,left]5[100]5[grow]", "[20]5[]5[30]5[50]10[50,grow,top]"));
		inputPanelInput_PNL.setSize(700,300);
		
		inputPanelInput_PNL.add(inputPanelAmount_LBL, "cell 0 0,alignx left");
		
		inputPanelAmount_TF = new JTextField();
		inputPanelAmount_TF.setColumns(10);
		inputPanelInput_PNL.add(inputPanelAmount_TF, "cell 1 0,growx");

		inputPanelInput_PNL.add(inputPanelDate_LBL, "cell 2 0,alignx left");
		
		inputPanelDD_TF = new JTextField();
		inputPanelDD_TF.setColumns(5);
		inputPanelInput_PNL.add(inputPanelDD_TF, "cell 3 0,growx");
		
		inputPanelMM_TF = new JTextField();
		inputPanelMM_TF.setColumns(5);
		inputPanelInput_PNL.add(inputPanelMM_TF, "cell 3 0,growx");
		
		inputPanelYYYY_TF = new JTextField();
		inputPanelInput_PNL.add(inputPanelYYYY_TF, "cell 4 0,");
		inputPanelYYYY_TF.setColumns(8);
		
		inputPanelInput_PNL.add(inputPanelCreateCat1_LBL, "cell 0 2,alignx left");
		
		inputPanelCat1_TF = new JTextField();
		inputPanelInput_PNL.add(inputPanelCat1_TF, "cell 1 2,growx");
		inputPanelCat1_TF.setColumns(10);
		
		inputPanelInput_PNL.add(inputPanelCreateCat2_LBL, "cell 2 2,alignx left");
		
		inputPanelCat2_TF = new JTextField();
		inputPanelInput_PNL.add(inputPanelCat2_TF, "cell 3 2,growx");
		inputPanelCat2_TF.setColumns(10);
		
		inputPanelInput_PNL.add(inputPanelDescription_LBL , "cell 0 3");
		
		inputPanelDescription_TF = new JTextField();
		inputPanelInput_PNL.add(inputPanelDescription_TF, "cell 1 3 4 1,grow");
		
		inputPanelError_LBL.setFont(error_font);
		inputPanelError_LBL.setForeground(Color.RED);
		inputPanelInput_PNL.add(inputPanelError_LBL, "cell 1 4 3 1,grow");
		
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
		return inputPanelInput_PNL;
	}
	
	/**
	 * Resets all input fields
	 */
	public void resetFields(){
		 
		inputPanelAmount_TF.setText("$$$");
		Date currentDate = new Date();
		String date = date_format.format(currentDate);
		StringTokenizer st = new StringTokenizer(date,"/");
		inputPanelDD_TF.setText(st.nextToken());
		inputPanelMM_TF.setText(st.nextToken());
		inputPanelYYYY_TF.setText(st.nextToken());
		inputPanelCat2_TF.setText("");
		inputPanelCat1_TF.setText("");
		inputPanelDescription_TF.setText("");
	}
	
}
