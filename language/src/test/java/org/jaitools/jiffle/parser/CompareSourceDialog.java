package org.jaitools.jiffle.parser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

class CompareSourceDialog extends JDialog {
    private static final long serialVersionUID = -8640087805737551918L;

    boolean accept = false;

    public CompareSourceDialog(String expected, String actual, boolean showCommands) {
        JPanel content = new JPanel(new BorderLayout());
        this.setContentPane(content);
        this.setTitle("SourceAssert");

        JPanel central = new JPanel(new GridLayout(1, 2));
        central.add(new TextArea(expected));
        central.add(new TextArea( actual));
        content.add(central);

        JPanel commands = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton accept = new JButton("Overwrite reference");
        accept.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        CompareSourceDialog.this.accept = true;
                        CompareSourceDialog.this.setVisible(false);
                    }
                });
        JButton reject = new JButton("Sources are different");
        reject.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        CompareSourceDialog.this.accept = false;
                        CompareSourceDialog.this.setVisible(false);
                    }
                });
        commands.add(accept);
        commands.add(reject);
        commands.setVisible(showCommands);
        content.add(commands, BorderLayout.SOUTH);
        pack();
    }

    public static boolean show(String expected, String actual, boolean showCommands) {
        CompareSourceDialog dialog = new CompareSourceDialog(expected, actual, showCommands);
        dialog.setModal(true);
        dialog.setVisible(true);

        return dialog.accept;
    }
}
