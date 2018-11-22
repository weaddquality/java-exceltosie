package se.addq.exceltosie.gui;

import javafx.scene.control.Alert;

class AlertHandler {


    static void alertError(String reason, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fel Dialog");
        if (reason != null) {
            alert.setHeaderText(reason);
        }
        if (e.getCause() != null) {
            {
                if (e.getCause().getMessage() != null) {
                    alert.setContentText(e.getCause().getMessage());
                }
            }
        }
        alert.showAndWait();
    }
}
