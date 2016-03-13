/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.inspector.table;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import self.lesfoster.cylindrical_alignment.utils.GuiUtils;

/**
 * An action to deal with viewing long strings.
 *
 * @author lfoster
 */
public class ViewStringAction extends AbstractAction {

	private static final long serialVersionUID = -1L;
	private final String name;
	private final String value;

	public ViewStringAction(String[] nameValue) {
		super("View Full Value");
		this.name = nameValue[0];
		this.value = nameValue[1];
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		final JFrame subFrame = new JFrame(name);
		JTextArea area = new JTextArea(breakUp(value));
		Font oldFont = area.getFont();
		area.setFont(new Font("Courier", oldFont.getStyle(), oldFont.getSize()));
		subFrame.getContentPane().setLayout(new BorderLayout());
		JScrollPane pane = new JScrollPane(area);
		subFrame.getContentPane().add(pane, BorderLayout.CENTER);
		JButton dismiss = new JButton("Dismiss");
		dismiss.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				subFrame.setVisible(false);
			}
		});
		subFrame.getContentPane().add(dismiss, BorderLayout.SOUTH);
		GuiUtils.setupScreenRealestate(subFrame, 500, 250);
		subFrame.setVisible(true);
	}

	private String breakUp(String value) {
		StringBuilder returnBuffer = new StringBuilder();
		for (int i = 0; i < value.length(); i += 60) {
			returnBuffer.append(value.substring(i, Math.min(i + 60, value.length())));
			returnBuffer.append("\n");
		}
		return returnBuffer.toString();
	}

}
