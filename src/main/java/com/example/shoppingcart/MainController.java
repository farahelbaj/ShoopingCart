package com.example.shoppingcart;

import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.ShoppingCartCalculator;
import com.example.shoppingcart.service.CartService;
import com.example.shoppingcart.service.LocalizationService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    @FXML private Button saveButton;

    @FXML private ComboBox<String> languageComboBox;

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> nameColumn;
    @FXML private TableColumn<CartItem, Number> priceColumn;
    @FXML private TableColumn<CartItem, Number> quantityColumn;
    @FXML private TableColumn<CartItem, Number> totalColumn;

    private final ObservableList<CartItem> items = FXCollections.observableArrayList();
    private final ShoppingCartCalculator calculator;
    private final LocalizationService localizationService;
    private final CartService cartService;
    private final Map<String, String> languageMap = new LinkedHashMap<>();
    private Map<String, String> localizedTexts = new HashMap<>();
    private String currentLanguage = "en";
    public MainController() {
        this(new ShoppingCartCalculator(), new LocalizationService(), new CartService());
    }

    public MainController(ShoppingCartCalculator calculator,
                          LocalizationService localizationService,
                          CartService cartService) {
        this.calculator = calculator;
        this.localizationService = localizationService;
        this.cartService = cartService;
    }

    @FXML
    public void initialize() {
        languageMap.put("English", "en");
        languageMap.put("Suomi", "fi");
        languageMap.put("Svenska", "sv");
        languageMap.put("日本語", "ja");
        languageMap.put("العربية", "ar");

        languageComboBox.getItems().addAll(languageMap.keySet());
        languageComboBox.setValue("English");

        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName()));

        priceColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getPrice()));

        quantityColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getQuantity()));

        totalColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getTotal()));

        cartTable.setItems(items);

        languageComboBox.setOnAction(event -> {
            String selectedLanguageName = languageComboBox.getValue();
            String selectedLanguageCode = languageMap.getOrDefault(selectedLanguageName, "en");
            loadLanguage(selectedLanguageCode);
            applyOrientation(selectedLanguageCode);
        });

        loadLanguage("en");
        applyOrientation("en");
    }

    private String t(String key) {
        return localizedTexts.getOrDefault(key, key);
    }

    private void loadLanguage(String language) {
        currentLanguage = language;
        localizedTexts = localizationService.getLocalizedStrings(language);
        updateTexts();
    }

    private void updateTexts() {
        titleLabel.setText(t("app.title"));
        languageLabel.setText(t("label.language"));
        itemNameLabel.setText(t("label.itemName"));
        priceLabel.setText(t("label.price"));
        quantityLabel.setText(t("label.quantity"));
        overallTotalLabel.setText(t("label.overallTotal"));

        itemNameField.setPromptText(t("prompt.itemName"));
        priceField.setPromptText(t("prompt.price"));
        quantityField.setPromptText(t("prompt.quantity"));

        addItemButton.setText(t("button.addItem"));
        calculateButton.setText(t("button.calculate"));
        clearButton.setText(t("button.clear"));
        saveButton.setText(t("button.save"));

        nameColumn.setText(t("table.name"));
        priceColumn.setText(t("table.price"));
        quantityColumn.setText(t("table.quantity"));
        totalColumn.setText(t("table.total"));
    }

    private void applyOrientation(String language) {
        if (titleLabel.getScene() == null) {
            return;
        }

        if ("ar".equals(language)) {
            titleLabel.getScene().getRoot().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            itemNameField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            priceField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            quantityField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            titleLabel.getScene().getRoot().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            itemNameField.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            priceField.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            quantityField.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    @FXML
     void handleAddItem() {
        try {
            String name = itemNameField.getText().trim();
            if (name.isEmpty()) {
                name = t("default.itemName");
            }

            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (price < 0 || quantity < 0) {
                showError(t("alert.invalidTitle"), t("alert.invalidNonNegative"));
                return;
            }

            CartItem item = new CartItem(name, price, quantity);
            items.add(item);

            itemNameField.clear();
            priceField.clear();
            quantityField.clear();

        } catch (NumberFormatException e) {
            showError(t("alert.invalidTitle"), t("alert.invalidNumber"));
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

    @FXML
    private void handleSave() {
        try {
            double totalCost = calculator.calculateCartTotal(items);
            int totalItems = 0;

            for (CartItem item : items) {
                totalItems += item.getQuantity();
            }

            cartService.saveCartRecord(totalItems, totalCost, currentLanguage, items);
            showInfo(t("alert.successTitle"), t("message.saved"));

        } catch (Exception e) {
            e.printStackTrace();
            showError(t("alert.errorTitle"), t("message.error"));
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}