package com.task.Controller;

import com.task.Dao.DaoImpl.CategoryImpl;
import com.task.Dao.DaoImpl.ProductImpl;
import com.task.Dao.DaoModels.DaoCategory;
import com.task.Dao.DaoModels.DaoProduct;
import com.task.Dao.Exceptions.DaoException;
import com.task.Model.Category;
import com.task.Model.Product;
import com.task.View.StoreView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StoreController {
    private final DaoCategory daoCategory;
    private final DaoProduct daoProduct;
    private final StoreView view;
    private String currentCrudAction;

    public StoreController( StoreView view) {
        this.daoCategory = new CategoryImpl();
        this.daoProduct = new ProductImpl();
        this.view = view;

        this.view.addCrudMenuListener(new CrudMenuListener());
        this.view.addOkButtonListener(new OkButtonListener());
    }

    class CrudMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem) e.getSource();
            currentCrudAction = source.getText();
            view.showCrudPanel();
        }
    }

    class OkButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedTable = view.getSelectedTable();
            switch (currentCrudAction) {
                case "Get All":
                    try {
                        handleGetAll(selectedTable);
                    } catch (DaoException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "Get One":
                    handleGetOne(selectedTable);
                    break;
                case "Create":
                    try {
                        handleCreate(selectedTable);
                    } catch (DaoException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "Update":
                    handleUpdate(selectedTable);
                    break;
                case "Delete":
                    handleDelete(selectedTable);
                    break;
            }
        }
    }

    private void handleGetAll(String table) throws DaoException {
        if ("Categories".equals(table)) {
            List<Category> categories = daoCategory.getAll();
            String[] columnNames = {"ID", "Name"};
            Object[][] data = new Object[categories.size()][2];
            for (int i = 0; i < categories.size(); i++) {
                data[i][0] = categories.get(i).getId();
                data[i][1] = categories.get(i).getName();
            }
            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
            view.setDataTableModel(tableModel);
        } else if ("Products".equals(table)) {
            List<Product> products = daoProduct.getAll();
            String[] columnNames = {"ID", "Name", "Price", "Category ID"};
            Object[][] data = new Object[products.size()][4];
            for (int i = 0; i < products.size(); i++) {
                data[i][0] = products.get(i).getId();
                data[i][1] = products.get(i).getName();
                data[i][2] = products.get(i).getPrice();
                data[i][3] = products.get(i).getCategoryId();
            }
            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
            view.setDataTableModel(tableModel);
        }
    }

    private void handleGetOne(String table) {
        String id = JOptionPane.showInputDialog(view, "Enter ID:");
        if (id != null && !id.isEmpty()) {
            try {
                if ("Categories".equals(table)) {
                    Category category = this.daoCategory.getOne(id);
                    String[] columnNames = {"ID", "Name"};
                    Object[][] data = {{category.getId(), category.getName()}};
                    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
                    view.setDataTableModel(tableModel);
                } else if ("Products".equals(table)) {
                    Product product = daoProduct.getOne(id);
                    String[] columnNames = {"ID", "Name", "Price", "Category ID"};
                    Object[][] data = {{product.getId(), product.getName(), product.getPrice(), product.getCategoryId()}};
                    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
                    view.setDataTableModel(tableModel);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Invalid ID format");
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleCreate(String table) throws DaoException {
        if ("Categories".equals(table)) {
            String name = JOptionPane.showInputDialog(view, "Enter category name:");
            if (name != null && !name.isEmpty()) {
                Category category = new Category();
                category.setName(name);
                daoCategory.create(category);
                JOptionPane.showMessageDialog(view, "Category added successfully!");
            }
        } else if ("Products".equals(table)) {
            JTextField nameField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField categoryIdField = new JTextField();
            Object[] message = {
                    "Name:", nameField,
                    "Price:", priceField,
                    "Category ID:", categoryIdField
            };
            int option = JOptionPane.showConfirmDialog(view, message, "Enter product details", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int categoryId = Integer.parseInt(categoryIdField.getText());
                    Product product = new Product();
                    product.setName(name);
                    product.setPrice(price);
                    product.setCategoryId(categoryId);
                    daoProduct.create(product);
                    JOptionPane.showMessageDialog(view, "Product added successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Invalid input format");
                }
            }
        }
    }

    private void handleUpdate(String table) {
        String id = JOptionPane.showInputDialog(view, "Enter ID:");
        if (id != null && !id.isEmpty()) {
            try {
                if ("Categories".equals(table)) {
                    String name = JOptionPane.showInputDialog(view, "Enter new category name:");
                    if (name != null && !name.isEmpty()) {
                        Category category = new Category();
                        category.setName(name);
                        daoCategory.update(id, category);
                        JOptionPane.showMessageDialog(view, "Category updated successfully!");
                    }
                } else if ("Products".equals(table)) {
                    JTextField nameField = new JTextField();
                    JTextField priceField = new JTextField();
                    JTextField categoryIdField = new JTextField();
                    Object[] message = {
                            "Name:", nameField,
                            "Price:", priceField,
                            "Category ID:", categoryIdField
                    };
                    int option = JOptionPane.showConfirmDialog(view, message, "Enter new product details", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        try {
                            String name = nameField.getText();
                            double price = Double.parseDouble(priceField.getText());
                            int categoryId = Integer.parseInt(categoryIdField.getText());
                            Product product = new Product();
                            product.setName(name);
                            product.setPrice(price);
                            product.setCategoryId(categoryId);
                            daoProduct.update(id, product);
                            JOptionPane.showMessageDialog(view, "Product updated successfully!");
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(view, "Invalid input format");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Invalid ID format");
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleDelete(String table) {
        String id = JOptionPane.showInputDialog(view, "Enter ID:");
        if (id != null && !id.isEmpty()) {
            try {
                if ("Categories".equals(table)) {
                    daoCategory.delete(id);
                    JOptionPane.showMessageDialog(view, "Category deleted successfully!");
                } else if ("Products".equals(table)) {
                    daoCategory.delete(id);
                    JOptionPane.showMessageDialog(view, "Product deleted successfully!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Invalid ID format");
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
