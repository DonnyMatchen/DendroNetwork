package com.donny.dendronetwork.gui;

import com.donny.dendronetwork.gui.customswing.RegisterFrame;

import javax.swing.*;
import java.util.ArrayList;

public class MainGui extends JFrame {
    public final ArrayList<RegisterFrame> FRAME_REGISTRY;

    public MainGui() {
        super();
        FRAME_REGISTRY = new ArrayList<>();

        //draw gui
        {
            //TODO gui element initialization
            //group layout
            {

            }
            pack();
        }
    }

    public void conclude(boolean save, boolean exit) {
        //TODO conclude
    }
}
