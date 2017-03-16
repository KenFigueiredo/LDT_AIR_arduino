import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Options extends JPanel implements ActionListener{
	
	private JButton connect, refresh;
	private boolean buttonOnOff;
	private JComboBox<String> comPorts, baudRate;
	private JTextField textField, textField2;
	
	public Options(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		buttonOnOff = false;
		
		JPanel jp1 = new JPanel(new FlowLayout());
//			JLabel lab1 = new JLabel("Found COM Ports:");		
//			comPorts = initCOMCombo(Parser.getPorts());
//		jp1.add(lab1);
//		jp1.add(comPorts);
			JLabel lab1 = new JLabel("IP:");
			textField = new JTextField(30);
			textField.addActionListener(this);
		jp1.add(lab1);
		jp1.add(textField);
		
		JPanel jp2 = new JPanel(new FlowLayout());
//			JLabel lab2 = new JLabel("Baud Rate:");
//			baudRate = initBaudCombo();
//		jp2.add(lab2);
//		jp2.add(baudRate);
		
		JLabel lab2 = new JLabel("PORT:");
		textField2 = new JTextField(15);
		textField2.addActionListener(this);
		jp2.add(lab2);
		jp2.add(textField2);

		JPanel jp4 = new JPanel(new FlowLayout());
			JTextField text3 = new JTextField(30);
			text3.addActionListener(this);
			JButton browse = new JButton("Browse");
			browse.setVerticalTextPosition(AbstractButton.CENTER);
			browse.setHorizontalTextPosition(AbstractButton.CENTER);
			browse.setEnabled(true);
			browse.addActionListener(this);
		jp4.add(text3);
		jp4.add(browse);
			
		JPanel jp3 = new JPanel(new FlowLayout());
			JLabel lab3 = new JLabel("Connectivity Options:");
			connect = new JButton("Connect");
			connect.setVerticalTextPosition(AbstractButton.CENTER);
			connect.setHorizontalTextPosition(AbstractButton.CENTER);
			connect.setEnabled(true);
			connect.addActionListener(this);
			
			refresh = new JButton("Refresh");
			refresh.setVerticalTextPosition(AbstractButton.CENTER);
			refresh.setHorizontalTextPosition(AbstractButton.CENTER);
			refresh.setEnabled(true);
			refresh.addActionListener(this);
		jp3.add(lab3);
		jp3.add(connect);
		//jp3.add(refresh);
		
		this.add(jp1);
		this.add(jp2);
		this.add(jp4);
		this.add(jp3);
	}
	
	private JComboBox<String> initCOMCombo(ArrayList<String> list){
		String[] temp = new String[list.size()];
		temp = list.toArray(temp);
		
		final JComboBox<String> jBox = new JComboBox<String>(temp);
		jBox.setPreferredSize(new Dimension(60,30));
		jBox.setSelectedItem("COM3");
		jBox.addItemListener(new ItemListener(){
			
				public void itemStateChanged(ItemEvent e){
					if(e.getStateChange() == ItemEvent.SELECTED){
						Parser.setCommPort(jBox.getSelectedItem().toString());
					}
				}
			}
		);
		
		return jBox;	
	}
	
	private JComboBox<String> initBaudCombo(){
		String[] temp = {"300","1200","2400","4800","9600","19200","38400","57600","74880","115200","230400","250000","1000000","2000000"};
		final JComboBox<String> jBox = new JComboBox<String>(temp);
		jBox.setPreferredSize(new Dimension(80,30));
		jBox.setSelectedItem("9600");
		jBox.addItemListener(new ItemListener(){
			
				public void itemStateChanged(ItemEvent e){
					if(e.getStateChange() == ItemEvent.SELECTED){
						Parser.setBaudRate(jBox.getSelectedItem().toString());
					}
				}
			}
		);
		
		return jBox;
	}
	
	private void refreshCombo(ArrayList<String> list){
		comPorts.removeAllItems();
		
		for(int i = 0; i < list.size(); i++)
			comPorts.addItem(list.get(i));

	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource() == refresh){
			Parser.refreshList();
			refreshCombo(Parser.getPorts());
		}
		
		else{
			if(!buttonOnOff){
				Parser.connect();
				connect.setText("Disconnect");
				baudRate.setEnabled(false);
				buttonOnOff = true;
			}
			else{
				Parser.disconnect();
				connect.setText("Connect");
				baudRate.setEnabled(true);
				buttonOnOff = false;
			}
		}	
	}
}
