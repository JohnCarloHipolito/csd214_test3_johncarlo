package com.triosstudent.csd214_test3_johncarlo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Map;

public class OrderController {

    @FXML
    protected TextField orderIdTF;
    @FXML
    protected TextField customerNameTF;
    @FXML
    protected TextField mobileNumberTF;
    @FXML
    protected ToggleGroup pizzaSizeTG;
    @FXML
    protected TextField numberOfToppingsTF;

    @FXML
    protected TableView<PizzaOrder> pizzaOrderTbl;
    @FXML
    protected TableColumn<PizzaOrder, Long> idCol;
    @FXML
    protected TableColumn<PizzaOrder, String> customerNameCol;
    @FXML
    protected TableColumn<PizzaOrder, String> mobileNumberCol;
    @FXML
    protected TableColumn<PizzaOrder, String> pizzaSizeCol;
    @FXML
    protected TableColumn<PizzaOrder, Integer> noOfToppingsCol;
    @FXML
    protected TableColumn<PizzaOrder, Double> totalBillCol;

    @FXML
    protected Label messageLbl;

    @FXML
    protected void initialize() {
        // Initialize table columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        mobileNumberCol.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        pizzaSizeCol.setCellValueFactory(new PropertyValueFactory<>("pizzaSize"));
        noOfToppingsCol.setCellValueFactory(new PropertyValueFactory<>("noOfToppings"));
        totalBillCol.setCellValueFactory(new PropertyValueFactory<>("totalBill"));
        // Set format to two decimal places and align to right
        totalBillCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    DecimalFormat df = new DecimalFormat("#.00");
                    setText(df.format(item));
                    setAlignment(Pos.CENTER_RIGHT);
                }
            }
        });
        // Select row on double click and populate text fields
        pizzaOrderTbl.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                PizzaOrder pizzaOrder = pizzaOrderTbl.getSelectionModel().getSelectedItem();
                orderIdTF.setText(String.valueOf(pizzaOrder.getId()));
                customerNameTF.setText(pizzaOrder.getCustomerName());
                mobileNumberTF.setText(pizzaOrder.getMobileNumber());
                pizzaSizeTG.getToggles().stream()
                        .filter(toggle -> ((RadioButton) toggle).getText().equals(pizzaOrder.getPizzaSize()))
                        .findFirst()
                        .ifPresent(toggle -> pizzaSizeTG.selectToggle(toggle));
                numberOfToppingsTF.setText(String.valueOf(pizzaOrder.getNoOfToppings()));
                messageLbl.setText("");
            }
        });

        // Add listeners to text fields to restrict input to numbers
        orderIdTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^([1-9]\\d*)?$")) {
                orderIdTF.setText(oldValue);
            }
        });
        // Add listeners to text fields to restrict input to numbers and dashes
        mobileNumberTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^([0-9][0-9-]*)?$")) {
                mobileNumberTF.setText(oldValue);
            }
        });
        // Add listeners to text fields to restrict input to numbers
        numberOfToppingsTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^(0|([1-9]\\d*))?$")) {
                numberOfToppingsTF.setText(oldValue);
            }
        });
    }

    @FXML
    protected void onCreateOrderAction(ActionEvent event) throws SQLException {
        // Validate input fields
        if (!orderIdTF.getText().isEmpty()) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Order ID is should be empty.");
            return;
        }
        if (customerNameTF.getText().isEmpty()) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Customer Name is required.");
            return;
        }
        if (mobileNumberTF.getText().isEmpty()) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Mobile Number is required.");
            return;
        }
        if (!mobileNumberTF.getText().matches("^(\\d{3}-\\d{3}-\\d{4})?$")) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Mobile Number should be in XXX-XXX-XXXX format.");
            return;
        }
        if (pizzaSizeTG.getSelectedToggle() == null) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Pizza Size is required.");
            return;
        }
        if (numberOfToppingsTF.getText().isEmpty()) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("No. of Toppings is required.");
            return;
        }

        // Create PizzaOrder object and populate with input values
        PizzaOrder pizzaOrder = new PizzaOrder();
        pizzaOrder.setCustomerName(customerNameTF.getText());
        pizzaOrder.setMobileNumber(mobileNumberTF.getText());
        pizzaOrder.setPizzaSize(((RadioButton) pizzaSizeTG.getSelectedToggle()).getText());
        pizzaOrder.setNoOfToppings(Integer.parseInt(numberOfToppingsTF.getText()));
        pizzaOrder.setTotalBill(calculateTotalBill(pizzaOrder.getPizzaSize(), pizzaOrder.getNoOfToppings()));

        // Insert PizzaOrder object to database
        Connection connection = DBConnection.getInstance().getConnection();
        String createOrderQuery = "INSERT INTO pizza_order (customer_name, mobile_number, pizza_size, no_of_toppings, total_bill) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(createOrderQuery, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, pizzaOrder.getCustomerName());
        preparedStatement.setString(2, pizzaOrder.getMobileNumber());
        preparedStatement.setString(3, pizzaOrder.getPizzaSize());
        preparedStatement.setInt(4, pizzaOrder.getNoOfToppings());
        preparedStatement.setDouble(5, pizzaOrder.getTotalBill());
        preparedStatement.executeUpdate();

        // Get generated keys and set to PizzaOrder object
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            pizzaOrder.setId(generatedKeys.getLong(1));
            pizzaOrderTbl.getItems().add(pizzaOrder);
            messageLbl.setTextFill(Color.GREEN);
            messageLbl.setText("Order created successfully.");
            clearFields();
        } else {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Failed to create order.");
        }

    }

    @FXML
    protected void onReadOrderAction(ActionEvent event) throws SQLException {
        // Read all orders from database and populate table
        Connection connection = DBConnection.getInstance().getConnection();
        String readOrderQuery = "SELECT * FROM pizza_order";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(readOrderQuery);
        pizzaOrderTbl.getItems().clear();
        while (resultSet.next()) {
            PizzaOrder pizzaOrder = new PizzaOrder();
            pizzaOrder.setId(resultSet.getLong("id"));
            pizzaOrder.setCustomerName(resultSet.getString("customer_name"));
            pizzaOrder.setMobileNumber(resultSet.getString("mobile_number"));
            pizzaOrder.setPizzaSize(resultSet.getString("pizza_size"));
            pizzaOrder.setNoOfToppings(resultSet.getInt("no_of_toppings"));
            pizzaOrder.setTotalBill(resultSet.getDouble("total_bill"));
            pizzaOrderTbl.getItems().add(pizzaOrder);
        }
        messageLbl.setTextFill(Color.GREEN);
        messageLbl.setText("Orders read successfully.");
        clearFields();

    }

    @FXML
    protected void onUpdateOrderAction(ActionEvent event) throws SQLException {
        // Validate input fields
        if (orderIdTF.getText().isEmpty()) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Order ID is required.");
            return;
        }
        if (customerNameTF.getText().isEmpty()) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Customer Name is required.");
            return;
        }
        if (mobileNumberTF.getText().isEmpty()) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Mobile Number is required.");
            return;
        }
        if (!mobileNumberTF.getText().matches("^(\\d{3}-\\d{3}-\\d{4})?$")) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Mobile Number should be in XXX-XXX-XXXX format.");
            return;
        }
        if (pizzaSizeTG.getSelectedToggle() == null) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Pizza Size is required.");
            return;
        }
        if (numberOfToppingsTF.getText().isEmpty()) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("No. of Toppings is required.");
            return;
        }

        // Create PizzaOrder object and populate with input values
        PizzaOrder pizzaOrder = new PizzaOrder();
        pizzaOrder.setId(Long.parseLong(orderIdTF.getText()));
        pizzaOrder.setCustomerName(customerNameTF.getText());
        pizzaOrder.setMobileNumber(mobileNumberTF.getText());
        pizzaOrder.setPizzaSize(((RadioButton) pizzaSizeTG.getSelectedToggle()).getText());
        pizzaOrder.setNoOfToppings(Integer.parseInt(numberOfToppingsTF.getText()));
        pizzaOrder.setTotalBill(calculateTotalBill(pizzaOrder.getPizzaSize(), pizzaOrder.getNoOfToppings()));

        // Update PizzaOrder object to database
        Connection connection = DBConnection.getInstance().getConnection();
        String updateOrderQuery = "UPDATE pizza_order SET customer_name = ?, mobile_number = ?, pizza_size = ?, no_of_toppings = ?, total_bill = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateOrderQuery);
        preparedStatement.setString(1, pizzaOrder.getCustomerName());
        preparedStatement.setString(2, pizzaOrder.getMobileNumber());
        preparedStatement.setString(3, pizzaOrder.getPizzaSize());
        preparedStatement.setInt(4, pizzaOrder.getNoOfToppings());
        preparedStatement.setDouble(5, pizzaOrder.getTotalBill());
        preparedStatement.setLong(6, pizzaOrder.getId());
        preparedStatement.executeUpdate();

        // Update PizzaOrder object in tableview
        if (preparedStatement.getUpdateCount() > 0) {
            pizzaOrderTbl.getItems().stream()
                    .filter(order -> order.getId().equals(pizzaOrder.getId()))
                    .findFirst()
                    .ifPresent(order -> {
                        order.setCustomerName(pizzaOrder.getCustomerName());
                        order.setMobileNumber(pizzaOrder.getMobileNumber());
                        order.setPizzaSize(pizzaOrder.getPizzaSize());
                        order.setNoOfToppings(pizzaOrder.getNoOfToppings());
                        order.setTotalBill(pizzaOrder.getTotalBill());
                        pizzaOrderTbl.refresh();
                        messageLbl.setTextFill(Color.GREEN);
                        messageLbl.setText("Order updated successfully.");
                        clearFields();
                    });
        } else {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Failed to update order.");
        }

    }

    @FXML
    protected void onDeleteOrderAction(ActionEvent event) throws SQLException {
        // Validate order id field
        if (orderIdTF.getText().isEmpty()) {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Order ID is required.");
            return;
        }

        // Delete order from database
        Connection connection = DBConnection.getInstance().getConnection();
        String deleteOrderQuery = "DELETE FROM pizza_order WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteOrderQuery);
        preparedStatement.setLong(1, Long.parseLong(orderIdTF.getText()));
        preparedStatement.executeUpdate();

        // Delete order from tableview
        if (preparedStatement.getUpdateCount() > 0) {
            pizzaOrderTbl.getItems().removeIf(order -> order.getId().equals(Long.parseLong(orderIdTF.getText())));
            messageLbl.setTextFill(Color.GREEN);
            messageLbl.setText("Order deleted successfully.");
            clearFields();
        } else {
            messageLbl.setTextFill(Color.RED);
            messageLbl.setText("Failed to delete order.");
        }
    }

    private void clearFields() {
        orderIdTF.clear();
        customerNameTF.clear();
        mobileNumberTF.clear();
        pizzaSizeTG.selectToggle(null);
        numberOfToppingsTF.clear();
    }

    public Double calculateTotalBill(String size, int toppings) {
        if (!size.matches("S|M|L|XL")) {
            throw new IllegalArgumentException("Invalid pizza size.");
        }
        if (toppings < 0) {
            throw new IllegalArgumentException("Invalid number of toppings.");
        }
        Map<String, Double> sizePriceMap = Map.of("S", 8.0, "M", 10.0, "L", 12.0, "XL", 15.0);
        double totalBill = (sizePriceMap.get(size) + toppings * 1.50) * 1.15;
        totalBill = Math.round(totalBill * 100.0) / 100.0;

        return totalBill;
    }
}