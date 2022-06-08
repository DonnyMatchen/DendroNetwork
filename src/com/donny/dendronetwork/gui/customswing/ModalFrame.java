package com.donny.dendronetwork.gui.customswing;

import javax.swing.*;

public abstract class ModalFrame extends JDialog {

    public ModalFrame(JFrame caller, String name) {
        super(caller, name, true);
    }
}
