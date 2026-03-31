package com.example.shoppingcart;

import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.ShoppingCartCalculator;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController {

    @FXML private Label titleLabel;
    @FXML private Label languageLabel;
    @FXML private Label itemNameLabel;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private Label overallTotalLabel;
    @FXML private Label resultLabel;

    @FXML private TextField itemNameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;

    @FXML private Button addItemButton;
    @FXML private Button calculateButton;
    @FXML private Button clearButton;

    @FXML private ComboBox<String> languageComboBox;

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> nameColumn;
    @FXML private TableColumn<CartItem, Number> priceColumn;
    @FXML private TableColumn<CartItem, Number> quantityColumn;
    @FXML private TableColumn<CartItem, Number> totalColumn;

    private final ObservableList<CartItem> items = FXCollections.observableArrayList();
    private final ShoppingCartCalculator calculator = new ShoppingCartCalculator();

    private final Map<String, Locale> locales = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        locales.put("English", new Locale("en", "US"));
        locales.put("Suomi", new Locale("fi", "FI"));
        locales.put("Svenska", new Locale("sv", "SE"));
        locales.put("日本語", new Locale("ja", "JP"));
        locales.put("العربية", new Locale("ar", "AR"));

        languageComboBox.getItems().addAll(locales.keySet());
        languageComboBox.setValue("English");

        languageComboBox.setOnAction(event -> {
            Locale selected = locales.get(languageComboBox.getValue());
            if (selected != null) {
                reloadView(selected);
            }
        });

        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName()));

        priceColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getPrice()));

        quantityColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getQuantity()));

        totalColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getTotal()));

        cartTable.setItems(items);
    }

    @FXML
    private void handleAddItem() {
        try {
            String name = itemNameField.getText().trim();
            if (name.isEmpty()) {
                name = "Item";
            }

            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (price < 0 || quantity < 0) {
                showAlert("Invalid input", "Price and quantity must be non-negative.");
                return;
            }

            CartItem item = new CartItem(name, price, quantity);
            items.add(item);

            itemNameField.clear();
            priceField.clear();
            quantityField.clear();

        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Please enter a valid price and quantity.");
        }
    }

    @FXML
    private void handleCalculate() {
        double total = calculator.calculateCartTotal(items);
        resultLabel.setText(String.format("%.2f", total));
    }

    @FXML
    private void handleClear() {
        items.clear();
        resultLabel.setText("0.00");
        itemNameField.clear();
        priceField.clear();
        quantityField.clear();
    }

    private void reloadView(Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("i18n.MessagesBundle", locale);

            java.net.URL fxmlUrl = getClass().getResource("/com/example/shoppingcart/main-view.fxml");

            FXMLLoader loader = new FXMLLoader(fxmlUrl, bundle);
            Scene newScene = new Scene(loader.load(), 700, 500);
            MainController controller = loader.getController();

            controller.items.addAll(this.items);

            String selectedLanguageName = locales.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(locale))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("English");

            controller.languageComboBox.setValue(selectedLanguageName);

            if ("ar".equals(locale.getLanguage())) {
                newScene.getRoot().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                controller.itemNameField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                controller.priceField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                controller.quantityField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            } else {
                newScene.getRoot().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                controller.itemNameField.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                controller.priceField.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                controller.quantityField.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            }

            Stage stage = (Stage) languageComboBox.getScene().getWindow();
            stage.setScene(newScene);
            stage.setTitle("Farah - Shopping Cart");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not reload view for selected language.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}