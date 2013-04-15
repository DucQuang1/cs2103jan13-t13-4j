package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import data.Entry;

import logic.AssetCatMgr;
import logic.EntryMgr;
import logic.ExcelMgr;
import logic.ExpenseCatMgr;
import logic.HistoryMgr;
import logic.IncomeCatMgr;
import logic.LiabilityCatMgr;

/**
 * Class that handles the importing of entries from an excel file
 * @author Wong Jing Ping, A0086581W
 *
 */
public class ExportPanel {
	
	private final static SimpleDateFormat date_format = new SimpleDateFormat("ddMMyyyy");	//default format for printing dates
	private final static Font heading_font = new Font("Lucida Grande", Font.BOLD, 20);
	
	private JPanel exportPanel_PNL = new JPanel(new MigLayout("","[]","[grow][grow][grow]"));
	private JTextArea exportPanelFilePath_TA = new JTextArea();
	private JFrame hostFrame = null;
	
	private ExcelMgr excelMgr = new ExcelMgr();
	
	private LinkedList<Entry> exportList = null;
	private JTable table = null;

	/**
	 * Constructor that takes in a linked list of entries
	 * @param hostFrame
	 * @param transactionList
	 */
	ExportPanel(final JFrame hostFrame, LinkedList<Entry> transactionList){
		
		this.hostFrame = hostFrame;
		hostFrame.getContentPane().add(exportPanel_PNL, "cell 0 0 1 2, growy");
		exportList = transactionList;
		setUp();
		
	}
	
	/**
	 * Constructor that takes in a JTable
	 * @param hostFrame
	 * @param table
	 */
	ExportPanel(final JFrame hostFrame, JTable table){
		
		this.hostFrame = hostFrame;
		hostFrame.getContentPane().add(exportPanel_PNL, "cell 0 0 1 2, growy");
		this.table = table;
		setUp();
		
	}
	
	/**
	 * sets up the JFrame with the necessary cells
	 */
	private void setUp() {
		exportPanel_PNL.setBackground(Color.white);
		
		JLabel exportPanelHeading_LBL = new JLabel(new ImageIcon(Finances.class.getResource("/img/Export.png")));
		exportPanelHeading_LBL.setText("Export");
		exportPanelHeading_LBL.setFont(heading_font);
		exportPanel_PNL.add(exportPanelHeading_LBL, "cell 0 0");
		
		JLabel exportPanelInstruction_LBL = new JLabel(
				"<html>Please enter your desired export destination with the file name</html>");
		exportPanel_PNL.add(exportPanelInstruction_LBL, "cell 0 1");
		
		exportPanelFilePath_TA.setText("\"your desired file path\"/transactions" +
				date_format.format(new Date()) + ".xls");
		exportPanelFilePath_TA.setColumns(20);
		exportPanelFilePath_TA.setLineWrap(true);
		exportPanel_PNL.add(exportPanelFilePath_TA, "cell 0 2,growy");
		
		JButton exportPanelSubmit_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Tick.png")));
		exportPanelSubmit_BTN.setToolTipText("Export!");
		exportPanelSubmit_BTN.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				String filePath = exportPanelFilePath_TA.getText();
				
				//try block for exporting excel file
				try {
					if(exportList != null){
						excelMgr.exportEntryList(exportList, new File(filePath));
					} else if(table != null){
						excelMgr.exportTable(table, new File(filePath));
					} else
						throw new Exception("Nothing to export!");
					
					//pop up to inform user
					JOptionPane searchMgrConfirm_JOP = new JOptionPane();
					searchMgrConfirm_JOP.setMessage("Exported Successfully!");
					searchMgrConfirm_JOP.setIcon(new ImageIcon(Finances.class.getResource("/img/Tick.png")));
					JDialog dialog = searchMgrConfirm_JOP.createDialog(null);
					dialog.setVisible(true);
					
					hostFrame.dispose();
					
				} catch (Exception e1) {
					
					JOptionPane searchMgrConfirm_JOP = new JOptionPane();
					searchMgrConfirm_JOP.setMessage("Error Exporting!\n" + e1.getMessage());
					searchMgrConfirm_JOP.setIcon(new ImageIcon(Finances.class.getResource("/img/Warning.png")));
					JDialog dialog = searchMgrConfirm_JOP.createDialog(null);
					dialog.setVisible(true);
					e1.printStackTrace();
					
					hostFrame.dispose();
				}
			}
			
		});
		exportPanel_PNL.add(exportPanelSubmit_BTN, "cell 0 2");
		hostFrame.pack();
		hostFrame.revalidate();
	}

}
