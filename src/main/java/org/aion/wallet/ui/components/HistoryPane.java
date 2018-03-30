package org.aion.wallet.ui.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.aion.wallet.connector.BlockchainConnector;
import org.aion.wallet.connector.WalletBlockchainConnector;
import org.aion.wallet.connector.dto.TransactionDTO;
import org.aion.wallet.util.BalanceFormatter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HistoryPane implements Initializable {

    private final BlockchainConnector blockchainConnector = new WalletBlockchainConnector();
    @FXML
    private TableView<TxRow> txListOverview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn typeCol = new TableColumn("Type");
        TableColumn addrCol = new TableColumn("Addr");
        TableColumn valueCol = new TableColumn("Value");
        typeCol.setCellValueFactory(new PropertyValueFactory<TxRow, String>("type"));
        addrCol.setCellValueFactory(new PropertyValueFactory<TxRow, String>("address"));
        valueCol.setCellValueFactory(new PropertyValueFactory<TxRow, String>("value"));

        txListOverview.getColumns().addAll(typeCol, addrCol, valueCol);
        reloadHistory();
    }

    private void reloadHistory() {
        String me = blockchainConnector.getAccounts().get(0);
        List<TxRow> txs = blockchainConnector.getTransactions(me).stream()
                .map(t -> new TxRow(me, t))
                .collect(Collectors.toList());
        txListOverview.setItems(FXCollections.observableList(txs));
    }

    public static class TxRow {
        private final SimpleStringProperty type;
        private final SimpleStringProperty address;
        private final SimpleStringProperty value;

        private TxRow(String requestingAddress, TransactionDTO dto) {
            this.type = new SimpleStringProperty(requestingAddress.equals(dto.getFrom()) ? "to" : "from");
            this.address = new SimpleStringProperty(requestingAddress.equals(dto.getFrom()) ? dto.getTo() : dto.getFrom());
            this.value = new SimpleStringProperty(BalanceFormatter.formatBalance(dto.getValue()));
        }

        public String getType() {
            return type.get();
        }

        public void setType(String type) {
            this.type.setValue(type);
        }

        public String getAddress() {
            return address.get();
        }

        public void setAddress(String address) {
            this.address.setValue(address);
        }

        public String getValue() {
            return value.get();
        }

        public void setValue(String value) {
            this.value.setValue(value);
        }

    }
}