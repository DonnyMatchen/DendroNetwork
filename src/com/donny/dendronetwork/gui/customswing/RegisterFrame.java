package com.donny.dendronetwork.gui.customswing;

import com.donny.dendronetwork.DendroNetwork;
import com.donny.dendronetwork.gui.MainGui;

import javax.swing.*;

public abstract class RegisterFrame extends JFrame {
    protected final MainGui CALLER;

    public RegisterFrame(MainGui caller, String name) {
        super(name);
        CALLER = caller;
        CALLER.FRAME_REGISTRY.add(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        String[] clss = getClass().toString().split("\\.");
        DendroNetwork.LOG_HANDLER.trace(getClass(), clss[clss.length - 1] + " created");
    }

    @Override
    public void dispose() {
        CALLER.FRAME_REGISTRY.remove(this);
        String[] clss = getClass().toString().split("\\.");
        DendroNetwork.LOG_HANDLER.trace(getClass(), clss[clss.length - 1] + " destroyed");
        super.dispose();
    }
}
