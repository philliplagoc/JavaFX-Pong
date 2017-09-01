package com.lagocp.ui;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class UIPane extends Pane {
	private StackPane bgPane;
	private BorderPane uiPane;
	
	public UIPane() {
		bgPane = new StackPane();
		
		bgPane.getChildren().add(new Label("bgPane"));
		
		uiPane = new BorderPane();
		
		Label uiPaneLabel = new Label("uiPane");
		
		BorderPane.setAlignment(uiPaneLabel, Pos.CENTER);
		
		uiPane.setTop(uiPaneLabel);
	}
}
