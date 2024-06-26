package com.task.View;

import com.task.Dao.DaoImpl.CategoryImpl;
import com.task.Dao.DaoImpl.ProductImpl;
import com.task.Dao.DaoModels.DaoCategory;
import com.task.Dao.DaoModels.DaoProduct;
import com.task.Dao.Exceptions.DaoException;
import com.task.Model.Category;
import com.task.Model.Product;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

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

    private final DaoCategory daoCategory;
    private final DaoProduct daoProduct;
    @Getter
    @Setter
    private String currentCrudAction;

    public StoreView() {
        this.daoCategory = new CategoryImpl();
        this.daoProduct = new ProductImpl();

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

    public void handleGetAll() throws DaoException {
        String table = getSelectedTable();
        if ("Categories".equals(table)) {
            List<Category> categories = daoCategory.getAll();
            String[] columnNames = {"Code", "Name"};
            Object[][] data = new Object[categories.size()][2];
            for (int i = 0; i < categories.size(); i++) {
                data[i][0] = categories.get(i).getCode();
                data[i][1] = categories.get(i).getName();
            }
            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
            setDataTableModel(tableModel);
        } else if ("Products".equals(table)) {
            List<Product> products = daoProduct.getAll();
            String[] columnNames = {"Code", "Name", "Price", "Category Code"};
            Object[][] data = new Object[products.size()][4];
            for (int i = 0; i < products.size(); i++) {
                data[i][0] = products.get(i).getCode();
                data[i][1] = products.get(i).getName();
                data[i][2] = products.get(i).getPrice();
                data[i][3] = products.get(i).getCategory();
            }
            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
            setDataTableModel(tableModel);
        }
    }

    public void handleGetOne() {
        String table = getSelectedTable();
        String code = JOptionPane.showInputDialog(this, "Enter Code:");
        if (code != null && !code.isEmpty()) {
            try {
                if ("Categories".equals(table)) {
                    handleGetAll();
                    Category category = this.daoCategory.getOne(code);
                    String[] columnNames = {"Code", "Name"};
                    Object[][] data = {{category.getCode(), category.getName()}};
                    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
                    setDataTableModel(tableModel);
                } else if ("Products".equals(table)) {
                    handleGetAll();
                    Product product = daoProduct.getOne(code);
                    String[] columnNames = {"Code", "Name", "Price", "Category Code"};
                    Object[][] data = {{product.getCode(), product.getName(), product.getPrice(), product.getCategory()}};
                    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
                    setDataTableModel(tableModel);
                }
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handleCreate() throws DaoException {
        String table = getSelectedTable();
        if ("Products".equals(table)) {
            JTextField codeField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField priceField = new JTextField();
            JComboBox<Category> categoryComboBox = new JComboBox<>(daoCategory.getAll().toArray(new Category[0]));

            Object[] message = {
                    "Code:", codeField,
                    "Name:", nameField,
                    "Price:", priceField,
                    "Category:", categoryComboBox
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Enter product details", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String code = codeField.getText();
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    Category selectedCategory = (Category) categoryComboBox.getSelectedItem();

                    Product product = new Product();
                    product.setCode(code);
                    product.setName(name);
                    product.setPrice(price);
                    product.setCategory(selectedCategory);

                    handleGetAll();
                    daoProduct.create(product);
                    JOptionPane.showMessageDialog(this, "Product added successfully!");
                    handleGetAll();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input format");
                }
            }
        }
    }

    public void handleUpdate() {
        String table = getSelectedTable();
        String code = JOptionPane.showInputDialog(this, "Enter Code:");
        if (code != null && !code.isEmpty()) {
            try {
                if ("Products".equals(table)) {
                    JTextField nameField = new JTextField();
                    JTextField priceField = new JTextField();
                    JComboBox<Category> categoryComboBox = new JComboBox<>(daoCategory.getAll().toArray(new Category[0]));

                    Object[] message = {
                            "Name:", nameField,
                            "Price:", priceField,
                            "Category:", categoryComboBox
                    };

                    int option = JOptionPane.showConfirmDialog(this, message, "Enter new product details", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        try {
                            String name = nameField.getText();
                            double price = Double.parseDouble(priceField.getText());
                            Category selectedCategory = (Category) categoryComboBox.getSelectedItem();

                            Product product = new Product();
                            product.setCode(code);
                            product.setName(name);
                            product.setPrice(price);
                            product.setCategory(selectedCategory);

                            handleGetAll();
                            daoProduct.update(code, product);
                            JOptionPane.showMessageDialog(this, "Product updated successfully!");
                            handleGetAll();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid input format");
                        }
                    }
                }
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public void handleDelete() {
        String table = getSelectedTable();
        String code = JOptionPane.showInputDialog(this, "Enter Code:");
        if (code != null && !code.isEmpty()) {
            try {
                if ("Categories".equals(table)) {
                    handleGetAll();
                    daoCategory.delete(code);
                    JOptionPane.showMessageDialog(this, "Category deleted successfully!");
                    handleGetAll();
                } else if ("Products".equals(table)) {
                    handleGetAll();
                    daoProduct.delete(code);
                    JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                    handleGetAll();
                }
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
