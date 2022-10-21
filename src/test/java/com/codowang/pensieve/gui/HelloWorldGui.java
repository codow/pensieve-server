package com.codowang.pensieve.gui;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HelloWorldGui {
    public static void main(String[] args) {
        new AwtHelloWorldGui().run();
    }
}

class AwtHelloWorldGui extends Frame {

    public void run () {
        createFrame();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void createFrame () {
        setTitle("Hello World");
        setLayout(new FlowLayout());
        setSize(200, 200);
        setLocation(100, 100);
    }
}
