package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
 * @author JP
 *
 */
public class ImportPanel {
	
	private final static Font heading_font = new Font("Lucida Grande", Font.BOLD, 20);
	
	private JFrame hostFrame;
	private JPanel importPanel_PNL = new JPanel(new MigLayout("","[]","[grow][grow][grow]"));
	
	private ExcelMgr excelMgr = new ExcelMgr();
	
	ImportPanel(final JFrame hostFrame, final AssetCatMgr assetCatMgr, final LiabilityCatMgr liabilityCatMgr, 
			final IncomeCatMgr incomeCatMgr, final ExpenseCatMgr expenseCatMgr, final EntryMgr entryMgr, final HistoryMgr historyMgr){
		
		this.hostFrame = hostFrame;
		
		importPanel_PNL.setBackground(Color.white);
		hostFrame.getContentPane().add(importPanel_PNL, "cell 0 0 1 2, growy");
		
		JLabel importPanelHeading_LBL = new JLabel(new ImageIcon(Finances.class.getResource("/img/Import.png")));
		importPanelHeading_LBL.setText("Import");
		importPanelHeading_LBL.setFont(heading_font);
		importPanel_PNL.add(importPanelHeading_LBL, "cell 0 0");
		
		JLabel importPanelInstruction_LBL = new JLabel(
				"Please enter in the directory of your desired excel file");
		importPanel_PNL.add(importPanelInstruction_LBL, "cell 0 1");
		
		final JTextArea importPanelFilePath_TA = new JTextArea();
		importPanelFilePath_TA.setColumns(20);
		importPanelFilePath_TA.setLineWrap(true);
		importPanel_PNL.add(importPanelFilePath_TA, "cell 0 2,growy");
		
		JButton importPanelSubmit_BTN = new JButton(new ImageIcon(Finances.class.getResource("/img/Tick.png")));
		importPanelSubmit_BTN.setToolTipText("Import!");
		importPanelSubmit_BTN.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				boolean pipePresent = false;
				String filePath = importPanelFilePath_TA.getText();
				String errorMsg = "";
				
				//try block for importing excel file
				try {
					
					LinkedList<Entry> importList = excelMgr.importExcel(filePath);
				
					//first for loop that checks all entries for any potentials errors
					for(Entry entry : importList){
						
						//check if amount is valid
							if (entry.getAmount() < 0)
								errorMsg += "Amount was not a valid number.<br>";
						
						//check if date is valid
						if (entry.getDate().after(new Date()))
							errorMsg += "Date is in the future.<br>" +
									"Please enter only dates today or before.<br>";
						
						//check for pipes in description
						if(entry.getDescription().indexOf("|") >= 0)
							pipePresent = true;
						
						switch(entry.getTransactionType()){
						
						case 0:	
							
							//check asset category
							if (!(assetCatMgr.checkExisting(entry.getCategory1()))){
								if(entry.getCategory1().indexOf("|") >= 0)
									pipePresent = true;
							}
							
							//check income category
							if (!(incomeCatMgr.checkExisting(entry.getCategory2()))){
								if(entry.getCategory2().indexOf("|") >= 0)
									pipePresent = true;
							}
							
							//check if pipes present
							if(pipePresent){
								errorMsg += "Pipe characters are not supported.<br>" +
										"So sorry about that!<br>";
							}
							
							//if any errors present, throw new exception
							if (errorMsg != ""){
								errorMsg = "<html>Error with ID: " + entry.getId() + "<br>" + errorMsg + 
										"Please try again!" + "</html>";	//to wrap text
								throw new Exception(errorMsg);
							}
							break;
							
						case 1:	
							
							//check asset category
							if (!(assetCatMgr.checkExisting(entry.getCategory1()))){
								errorMsg += "Cannot deduct from a new category.<br>" +
										"You may register an income in the new category first or deduct from an existing category.<br>";
							} else if(assetCatMgr.getAmount(entry.getCategory1()) < entry.getAmount()){
								errorMsg += "Sorry, you do not have enough money in " + entry.getCategory1() + ".<br>" +
										"Please register an income in " + entry.getCategory1() + " or deduct from another category.<br>";
							}
							
							//check expense category
							if (!(expenseCatMgr.checkExisting(entry.getCategory2()))){
								if(entry.getCategory2().indexOf("|") >= 0)
									pipePresent = true;
							}
							
							//check if pipes present
							if(pipePresent){
								errorMsg += "Pipe characters are not supported.<br>" +
										"So sorry about that!<br>";
							}
							
							//if any errors present, throw new exception
							if (errorMsg != ""){
								errorMsg = "<html>Error with ID: " + entry.getId() + "<br>" + errorMsg + 
										"Please try again!" + "</html>";	//to wrap text
								throw new Exception(errorMsg);
							}
							break;
							
						case 2:
							
							//check liability category
							if (!(liabilityCatMgr.checkExisting(entry.getCategory1()))){
								if(entry.getCategory1().indexOf("|") >= 0)
									pipePresent = true;
							}
							
							//check expense category
							if (!(expenseCatMgr.checkExisting(entry.getCategory2()))){
								if(entry.getCategory2().indexOf("|") >= 0)
									pipePresent = true;
							}
							
							//check if pipes present
							if(pipePresent){
								errorMsg += "Pipe characters are not supported.<br>" +
										"So sorry about that!<br>";
							}
							
							//if any errors present, throw new exception
							if (errorMsg != ""){
								errorMsg = "<html>Error with ID: " + entry.getId() + "<br>" + errorMsg + 
										"Please try again!" + "</html>";	//to wrap text
								throw new Exception(errorMsg);
							}
							break;
							
							
						case 3:
							
							//check asset category
							if (!(assetCatMgr.checkExisting(entry.getCategory1()))){
								errorMsg += "Cannot deduct from " + entry.getCategory1() + ".<br>" +
										"You may register an income in the new category first or deduct from an existing category.<br>";
							} else if(assetCatMgr.getAmount(entry.getCategory1()) < entry.getAmount()){
								errorMsg += "Sorry, you do not have enough money in " + entry.getCategory1() + ".<br>" +
										"Please register an income in " + entry.getCategory1() + " or deduct from another category.<br>";
							}
							
							//check liability category
							if (!(liabilityCatMgr.checkExisting(entry.getCategory2()))){
								errorMsg += "Cannot deduct from " + entry.getCategory2() + ".<br>" +
										"You may register an income in the new category first or deduct from an existing category.<br>";
							} else if(liabilityCatMgr.getAmount(entry.getCategory2()) < entry.getAmount()){
								errorMsg += "Sorry, you do not have enough money in " + entry.getCategory2() + ".<br>" +
										"Please register an income in " + entry.getCategory2() + " or deduct from another category.<br>";
							}
							
							//check if pipes present
							if(pipePresent){
								errorMsg += "Pipe characters are not supported.<br>" +
										"So sorry about that!<br>";
							}
							
							//if any errors present, throw new exception
							if (errorMsg != ""){
								errorMsg = "<html>Error with ID: " + entry.getId() + "<br>" + errorMsg + 
										"Please try again!" + "</html>";	//to wrap text
								throw new Exception(errorMsg);
							}
							break;
							
							
						case 4:
							
							//check asset category
							if (!(assetCatMgr.checkExisting(entry.getCategory1()))){
								if(entry.getCategory1().indexOf("|") >= 0)
									pipePresent = true;
							}
							
							//check liability category
							if (!(liabilityCatMgr.checkExisting(entry.getCategory2()))){
								if(entry.getCategory2().indexOf("|") >= 0)
									pipePresent = true;
							}
							
							//check if pipes present
							if(pipePresent){
								errorMsg += "Pipe characters are not supported.<br>" +
										"So sorry about that!<br>";
							}
							
							//if any errors present, throw new exception
							if (errorMsg != ""){
								errorMsg = "<html>Error with ID: " + entry.getId() + "<br>" + errorMsg + 
										"Please try again!" + "</html>";	//to wrap text
								throw new Exception(errorMsg);
							}
							break;
							
							
						case 5:
							
							//check first asset category
							if (!(assetCatMgr.checkExisting(entry.getCategory1()))){
								errorMsg += "Cannot deduct from a new category.<br>" +
										"You may register an income in the new category first or deduct from an existing category.<br>";
							} else if(assetCatMgr.getAmount(entry.getCategory1()) < entry.getAmount()){
								errorMsg += "Sorry, you do not have enough money in " + entry.getCategory1() + ".<br>" +
										"Please register an income in " + entry.getCategory1() + " or deduct from another category.<br>";
							}
							
							//check second asset category
							if (!(assetCatMgr.checkExisting(entry.getCategory2()))){
								if(entry.getCategory2().indexOf("|") >= 0)
									pipePresent = true;
							}
							
							//check if pipes present
							if(pipePresent){
								errorMsg += "Pipe characters are not supported.<br>" +
										"So sorry about that!<br>";
							}
							
							//check if both categories are the same
							if(entry.getCategory1().equals(entry.getCategory2())){
								errorMsg += "Both categories are the same.<br>" +
										"You need to select 2 different categories to make a transfer.<br>";
							}
							
							//if any errors present, throw new exception
							if (errorMsg != ""){
								errorMsg = "<html>Error with ID: " + entry.getId() + "<br>" + errorMsg + 
										"Please try again!" + "</html>";	//to wrap text
								throw new Exception(errorMsg);
							}
							break;
							
							
						case 6:
							
							//check first liability category
							if (!(liabilityCatMgr.checkExisting(entry.getCategory1()))){
								errorMsg += "Cannot deduct from a new category.<br>" +
										"You may register an income in the new category first or deduct from an existing category.<br>";
							} else if(liabilityCatMgr.getAmount(entry.getCategory1()) < entry.getAmount()){
								errorMsg += "Sorry, you do not have enough money in " + entry.getCategory1() + ".<br>" +
										"Please register an income in " + entry.getCategory1() + " or deduct from another category.<br>";
							}
							
							//check second liability category
							if (!(liabilityCatMgr.checkExisting(entry.getCategory2()))){
								if(entry.getCategory2().indexOf("|") >= 0)
									pipePresent = true;
							}
							
							//check if pipes present
							if(pipePresent){
								errorMsg += "Pipe characters are not supported.<br>" +
										"So sorry about that!<br>";
							}
							
							//check if both categories are the same
							if(entry.getCategory1().equals(entry.getCategory2())){
								errorMsg += "Both categories are the same.<br>" +
										"You need to select 2 different categories to make a transfer.<br>";
							}
							
							//if any errors present, throw new exception
							if (errorMsg != ""){
								errorMsg = "<html>Error with ID: " + entry.getId() + "<br>" + errorMsg + 
										"Please try again!" + "</html>";	//to wrap text
								throw new Exception(errorMsg);
							}
							
							break;
						}
						
					}
					
					for(Entry entry : importList){
						
						switch(entry.getTransactionType()){
						
						case 0:	
							
								//update assetCatMgr
								if (assetCatMgr.checkExisting(entry.getCategory1())){
									assetCatMgr.addAmountToCategory(entry.getCategory1(), entry.getAmount());
								}
								else{
									assetCatMgr.addCategory(entry.getCategory1(), entry.getAmount());
								}
								
								//update expenseCatMgr
								if (expenseCatMgr.checkExisting(entry.getCategory2())){
									expenseCatMgr.addAmountToCategory(entry.getCategory2(), entry.getAmount());
								}
								else{
									expenseCatMgr.addCategory(entry.getCategory2(), entry.getAmount());
								}
								
								break;
								
						case 1:

								//update assetCatmgr. note assets are reduced for this transaction type
								assetCatMgr.addAmountToCategory(entry.getCategory1(), -entry.getAmount());
								
								//update expenseCatMgr
								if (expenseCatMgr.checkExisting(entry.getCategory2())){
									expenseCatMgr.addAmountToCategory(entry.getCategory2(), entry.getAmount());
								}
								else{
									expenseCatMgr.addCategory(entry.getCategory2(), entry.getAmount());
								}
								
								break;
								
						case 2:
							
								//update liabilityCatMgr
								if (liabilityCatMgr.checkExisting(entry.getCategory1())){
									liabilityCatMgr.addAmountToCategory(entry.getCategory1(), entry.getAmount());
								}
								else{
									liabilityCatMgr.addCategory(entry.getCategory1(), entry.getAmount());
								}

								//update expenseCatMgr
								if (expenseCatMgr.checkExisting(entry.getCategory2())){
									expenseCatMgr.addAmountToCategory(entry.getCategory2(), entry.getAmount());
								}
								else{
									expenseCatMgr.addCategory(entry.getCategory2(), entry.getAmount());
								}
								
								break;
								
						case 3:
							
								//update assetCatmgr. note assets are reduced for this transaction type
								assetCatMgr.addAmountToCategory(entry.getCategory1(), -entry.getAmount());
							
								//update liabilityCatMgr
								liabilityCatMgr.addAmountToCategory(entry.getCategory2(), -entry.getAmount());
								
								break;
								
						case 4:
							
								//update assetCatmgr
								if (assetCatMgr.checkExisting(entry.getCategory1())){
									assetCatMgr.addAmountToCategory(entry.getCategory1(), entry.getAmount());
								}
								else{
									assetCatMgr.addCategory(entry.getCategory1(), entry.getAmount());
								}
								
								//update liabilityCatMgr
								if (liabilityCatMgr.checkExisting(entry.getCategory2())){
									liabilityCatMgr.addAmountToCategory(entry.getCategory2(), entry.getAmount());
								}
								else{
									liabilityCatMgr.addCategory(entry.getCategory2(), entry.getAmount());
								}
								
								break;
								
						case 5:
							
								//update assetCatmgr
								assetCatMgr.addAmountToCategory(entry.getCategory1(), -entry.getAmount());
								
								//update assetCatMgr
								if (assetCatMgr.checkExisting(entry.getCategory2())){
									assetCatMgr.addAmountToCategory(entry.getCategory2(), entry.getAmount());
								}
								else{
									assetCatMgr.addCategory(entry.getCategory2(), entry.getAmount());
								}
								
								break;
								
						case 6:

								//update liabilityCatmgr
								liabilityCatMgr.addAmountToCategory(entry.getCategory1(), -entry.getAmount());
								
								//update liabilityCatMgr
								if (liabilityCatMgr.checkExisting(entry.getCategory2())){
									liabilityCatMgr.addAmountToCategory(entry.getCategory2(), entry.getAmount());
								}
								else{
									liabilityCatMgr.addCategory(entry.getCategory2(), entry.getAmount());
								}
								break;
						}
						
						
						//update entryMgr. New id is created to prevent id collision
						entryMgr.addEntry(entry.getTransactionType(), entry.getAmount(), entry.getDate(), entry.getCategory1(),
								entry.getCategory2(), entry.getDescription());
						
						//update historyMgr
						int id = entryMgr.getCurrentId();
						historyMgr.addLog(0, id, entry.getTransactionType(), entry.getAmount(), entry.getDate(), 
								entry.getCategory1(), entry.getCategory2(), entry.getDescription());
					}

					//pop up to inform user
					JOptionPane searchMgrConfirm_JOP = new JOptionPane();
					searchMgrConfirm_JOP.setMessage("Imported Successfully!");
					searchMgrConfirm_JOP.setIcon(new ImageIcon(Finances.class.getResource("/img/Tick.png")));
					JDialog dialog = searchMgrConfirm_JOP.createDialog(null);
					dialog.setVisible(true);
					
					hostFrame.dispose();
					
				} catch (Exception e1) {
					
					JOptionPane searchMgrConfirm_JOP = new JOptionPane();
					searchMgrConfirm_JOP.setMessage("Error Importing!\n" + e1.getMessage());
					searchMgrConfirm_JOP.setIcon(new ImageIcon(Finances.class.getResource("/img/Warning.png")));
					JDialog dialog = searchMgrConfirm_JOP.createDialog(null);
					dialog.setVisible(true);
					e1.printStackTrace();
				}
			}
			
		});
		importPanel_PNL.add(importPanelSubmit_BTN, "cell 0 2");
		hostFrame.pack();
		hostFrame.revalidate();
	}

}
