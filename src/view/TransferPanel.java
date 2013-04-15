package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import logic.EntryMgr;
import logic.HistoryMgr;

/**
 * GUI class that contains a JPanel to handle adding of entries
 * @author Wong Jing Ping, A0086581W 
 * @author Liu Bohua, A0091879J (coded out initial prototype)
 */
public class TransferPanel {

	//default format for date
	protected static SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
	protected static Font error_font = new Font("SanSerif", Font.ITALIC, 12);
	protected static Font heading_font = new Font("Lucida Grande", Font.BOLD, 20);
	
	//hostFrame refers to the pop-up frame that holds this transferPanel
	protected JFrame transferPanel_FRM;
	
	protected JPanel transferPanel_PNL;
	
	protected JComboBox<String> transferPanelFrom_CB;
	protected JComboBox<String> transferPanelTo_CB;
	
	protected JTextField transferPanelAmount_TF;
	protected JTextField transferPanelDD_TF;
	protected JTextField transferPanelMM_TF;
	protected JTextField transferPanelYYYY_TF;
	protected JTextField transferPanelDescription_TF;
	
	protected JLabel transferPanelHeading_LBL = new JLabel(new ImageIcon(Finances.class.getResource("/img/Transfer.png")));
	protected JLabel transferPanelAmount_LBL = new JLabel("Amount");
	protected JLabel transferPanelDate_LBL = new JLabel("Date");
	protected JLabel transferPanelCategory1_LBL = new JLabel("From");
	protected JLabel transferPanelCategory2_LBL = new JLabel("To");
	protected JLabel transferPanelDescription_LBL = new JLabel("Description");
	protected JLabel transferPanelError_LBL = new JLabel();
	protected String errorMsg = "";
	
	protected final EntryMgr entryMgr;
	protected final HistoryMgr historyMgr;
	
	/**
	 * Default Constructor for creating the panel.
	 * @author Wong Jing Ping, A0086581W 
	 */
	public TransferPanel(final JFrame hostFrame, final EntryMgr entryMgr, final HistoryMgr historyMgr){
		
		//pass reference to the panel for checking and adding of transactions
		this.transferPanel_FRM = hostFrame;
		this.entryMgr = entryMgr;
		this.historyMgr = historyMgr;
		
		
		transferPanel_PNL = new JPanel(new MigLayout("", "[100,left]5[100]25[130,left]5[100]5[grow]", "[25]5[]5[30]5[50]10[50,grow,top]"));
		transferPanel_PNL.setBackground(new Color(255, 255, 255));
		transferPanel_PNL.setSize(700,300);
		
		transferPanelHeading_LBL.setFont(heading_font);
		transferPanel_PNL.add(transferPanelHeading_LBL, "cell 0 0 2 1,growx");
		
		transferPanel_PNL.add(transferPanelCategory1_LBL, "cell 0 1,growx");
		transferPanel_PNL.add(transferPanelCategory2_LBL, "cell 2 1,growx");
		
		transferPanelFrom_CB = new JComboBox<String>();
		transferPanel_PNL.add(transferPanelFrom_CB, "cell 1 1,growx");
		
		transferPanelTo_CB = new JComboBox<String>();
		transferPanel_PNL.add(transferPanelTo_CB, "cell 3 1,growx");
		
		transferPanel_PNL.add(transferPanelAmount_LBL, "cell 0 2,alignx left");

		transferPanelAmount_TF = new JTextField();
		transferPanelAmount_TF.setColumns(10);
		transferPanel_PNL.add(transferPanelAmount_TF, "cell 1 2,growx");
		
		transferPanel_PNL.add(transferPanelDate_LBL, "cell 2 2,alignx left");

		transferPanelDD_TF = new JTextField();
		transferPanelDD_TF.setColumns(5);
		transferPanel_PNL.add(transferPanelDD_TF, "flowx,cell 3 2,growx");
		
		transferPanelMM_TF = new JTextField();
		transferPanelMM_TF.setColumns(5);
		transferPanel_PNL.add(transferPanelMM_TF, "cell 3 2,growx");
		
		transferPanelYYYY_TF = new JTextField();
		transferPanel_PNL.add(transferPanelYYYY_TF, "cell 4 2");
		transferPanelYYYY_TF.setColumns(8);
		
		transferPanel_PNL.add(transferPanelDescription_LBL, "cell 0 3");
		
		transferPanelDescription_TF = new JTextField();
		transferPanel_PNL.add(transferPanelDescription_TF, "cell 1 3 4 1,grow");
		
		transferPanelError_LBL.setFont(error_font);
		transferPanelError_LBL.setForeground(Color.RED);
		transferPanel_PNL.add(transferPanelError_LBL, "cell 1 4 3 1,grow");
		
	}
	
	/**
	 * Gets the transfer panel
	 * @return transferPanel
	 * @author Wong Jing Ping, A0086581W
	 */
	public JPanel getPanel(){
		return transferPanel_PNL;
	}
	
	/**
	 * Resets amount to '$$$' and date to current date
	 * Called by child classes to refresh common fields
	 * @author Wong Jing Ping, A0086581W
	 */
	protected void resetDefault(){
		
		transferPanelAmount_TF.setText("$$$");
		Date currentDate = new Date();
		String date = date_format.format(currentDate);
		StringTokenizer st = new StringTokenizer(date,"/");
		transferPanelDD_TF.setText(st.nextToken());
		transferPanelMM_TF.setText(st.nextToken());
		transferPanelYYYY_TF.setText(st.nextToken());
	}
}
