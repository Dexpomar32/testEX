package com.task.View;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class StoreView extends JFrame {
    private final JMenuItem getAllItem;
    private final JMenuItem getOneItem;
    private final JMenuItem createItem;
    private final JMenuItem updateItem;
    private final JMenuItem deleteItem;

    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    private final JComboBox<String> tableComboBox;
    private final JButton okButton;
    private final JTable dataTable;

    public StoreView() {
        setTitle("Store Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("CRUD");
        getAllItem = new JMenuItem("Get All");
        getOneItem = new JMenuItem("Get One");
        createItem = new JMenuItem("Create");
        updateItem = new JMenuItem("Update");
        deleteItem = new JMenuItem("Delete");

        menu.add(getAllItem);
        menu.add(getOneItem);
        menu.add(createItem);
        menu.add(updateItem);
        menu.add(deleteItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel crudPanel = new JPanel(new BorderLayout());
        tableComboBox = new JComboBox<>(new String[]{"Categories", "Products"});
        okButton = new JButton("OK");

        JPanel topPanel = new JPanel();
        topPanel.add(tableComboBox);
        topPanel.add(okButton);

        dataTable = new JTable();
        JScrollPane dataScrollPane = new JScrollPane(dataTable);

        crudPanel.add(topPanel, BorderLayout.NORTH);
        crudPanel.add(dataScrollPane, BorderLayout.CENTER);

        mainPanel.add(crudPanel, "crud");

        add(mainPanel);

        cardLayout.show(mainPanel, "crud");
    }

    public void showCrudPanel() {
        cardLayout.show(mainPanel, "crud");
    }

    public String getSelectedTable() {
        return (String) tableComboBox.getSelectedItem();
    }

    public void setDataTableModel(TableModel model) {
        dataTable.setModel(model);
    }

    public void addOkButtonListener(ActionListener listener) {
        okButton.addActionListener(listener);
    }

    public void addCrudMenuListener(ActionListener listener) {
        getAllItem.addActionListener(listener);
        getOneItem.addActionListener(listener);
        createItem.addActionListener(listener);
        updateItem.addActionListener(listener);
        deleteItem.addActionListener(listener);
    }
}
