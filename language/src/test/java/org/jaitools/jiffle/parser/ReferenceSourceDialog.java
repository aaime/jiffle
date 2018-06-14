package org.jaitools.jiffle.parser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

class ReferenceSourceDialog extends JDialog {
    private static final long serialVersionUID = -8640087805737551918L;

    boolean accept = false;

    public ReferenceSourceDialog(String source) {
        JPanel content = new JPanel(new BorderLayout());
        this.setContentPane(content);
        this.setTitle("SourceAssert");
        final JLabel topLabel =
                new JLabel(
                        "<html><body>Reference source file is missing.<br>"
                                + "This is the result, do you want to make it the referecence?</html></body>");
        content.add(topLabel, BorderLayout.NORTH);
        content.add(new TextArea(source, 400, 400));
        JPanel commands = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton accept = new JButton("Accept as reference");
        accept.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ReferenceSourceDialog.this.accept = true;
                        ReferenceSourceDialog.this.setVisible(false);
                    }
                });
        JButton reject = new JButton("Reject output");
        reject.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ReferenceSourceDialog.this.accept = false;
                        ReferenceSourceDialog.this.setVisible(false);
                    }
                });
        commands.add(accept);
        commands.add(reject);
        content.add(commands, BorderLayout.SOUTH);
        pack();
    }

    public static boolean show(String source) {
        ReferenceSourceDialog dialog = new ReferenceSourceDialog(source);
        dialog.setModal(true);
        dialog.setVisible(true);

        return dialog.accept;
    }
}
