package com.task.Controller;

import com.task.View.StoreView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StoreController {
    private final StoreView view;

    public StoreController(StoreView view) {
        this.view = view;

        this.view.addCrudMenuListener(new CrudMenuListener());
        this.view.addOkButtonListener(new OkButtonListener());
    }

    class CrudMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem) e.getSource();
            view.setCurrentCrudAction(source.getText());
            view.showCrudPanel();
        }
    }

    class OkButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String action = view.getCurrentCrudAction();
            try {
                switch (action) {
                    case "Get All":
                        view.handleGetAll();
                        break;
                    case "Get One":
                        view.handleGetOne();
                        break;
                    case "Create":
                        view.handleCreate();
                        break;
                    case "Update":
                        view.handleUpdate();
                        break;
                    case "Delete":
                        view.handleDelete();
                        break;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "An error occurred: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void start() {
        view.setVisible(true);
    }
}
