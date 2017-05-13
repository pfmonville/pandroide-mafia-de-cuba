package view;

import java.io.File;
import java.util.Arrays;

import controller.App;
import controller.ai.StrategyFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.Theme;

public class SettingsView extends View{

	private File fileGodFather = null;
	private File fileLoyayHenchman = null;
	private File fileCleaner = null;
	private File fileAgent = null;
	private File fileThief = null;
	private File fileStreetUrchin = null;
	private File fileDriver = null;
	private File fileFirst = null;
	private File fileSecond = null;
	private File fileLast = null;
	private File fileMiddle = null;
	
	
	public SettingsView(int x, int y) {
		super(x, y);
		
		Label title = new Label("Réglages");
		title.setStyle("-fx-text-fill: rgb(200, 80, 10);");
		title.setId("title");
		title.setPrefWidth(x);
		VBox.setMargin(title, new Insets(0,0,0,y/4));
		
		VBox mainBox = new VBox();
		mainBox.setPrefSize(x, y);
		mainBox.setSpacing(30);
		
		
		Label firstLabel = new Label("Pour les stratégies concernant les rôles:");
		firstLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		firstLabel.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		
		FileChooser fc = new FileChooser();
		fc.setTitle("Choisir le fichier class");
		fc.setTitle("View Pictures");
        fc.setInitialDirectory(
            new File(System.getProperty("user.home"))
        ); 
		
		GridPane firstPane = new GridPane();
		
		Label godFather = new Label("Parrain");
		godFather.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		godFather.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(godFather, 0, 0);
		TextField godFatherField = new TextField();
		godFatherField.setMinWidth(500);
		Button btngf = new Button("choisir le fichier");
		btngf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fc.showOpenDialog(App.sv.settingsStage);
                if(file != null){
                	if(file.getParentFile().isDirectory()){
                		fc.setInitialDirectory(file.getParentFile());
                	}
                	int i = file.getPath().lastIndexOf('.');
                    if (i > 0) {
                    	if (file != null && "class".equals(file.getPath().substring(i+1))) {
                    		godFatherField.setText(file.getPath());
                    		fileGodFather = file;
                        }else{
                        	fileGodFather = null;
                        	App.createPopUp("Fichier non reconnu ou l'extension n'était pas '.class'", "Erreur lors de la lecture du fichier", 1, App.sv.settingsStage);
                        }
                    }
                }    
            }
        });
		firstPane.add(new Text("\t"), 1, 0);
		firstPane.add(btngf, 2, 0);
		firstPane.add(new Text("\t"), 3, 0);
		firstPane.add(godFatherField, 4, 0);
		
		Label loyalHenchman = new Label("Fidèle");
		loyalHenchman.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		loyalHenchman.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(loyalHenchman, 0, 1);
		TextField loyalHenchmanField = new TextField();
		Button btnlh = new Button("choisir le fichier");
		btnlh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fc.showOpenDialog(App.sv.settingsStage);
                if(file != null){
                	if(file.getParentFile().isDirectory()){
                		fc.setInitialDirectory(file.getParentFile());
                	}
                    int i = file.getPath().lastIndexOf('.');
                    if (i > 0) {
                    	if (file != null && "class".equals(file.getPath().substring(i+1))) {
                    		loyalHenchmanField.setText(file.getPath());
                    		fileLoyayHenchman = file;
                        }else{
                        	fileLoyayHenchman = null;
                        	App.createPopUp("Fichier non reconnu ou l'extension n'était pas '.class'", "Erreur lors de la lecture du fichier", 1, App.sv.settingsStage);
                        }
                    }
                }
                
            }
        });
		firstPane.add(btnlh, 2, 1);
		firstPane.add(loyalHenchmanField, 4, 1);
		
		Label cleaner = new Label("Nettoyeur");
		cleaner.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		cleaner.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(cleaner, 0, 2);
		TextField cleanerField = new TextField();
		Button btnc = new Button("choisir le fichier");
		btnc.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fc.showOpenDialog(App.sv.settingsStage);
                if(file != null){
                	if(file.getParentFile().isDirectory()){
                		fc.setInitialDirectory(file.getParentFile());
                	}
                    int i = file.getPath().lastIndexOf('.');
                    if (i > 0) {
                    	if (file != null && "class".equals(file.getPath().substring(i+1))) {
                    		fileCleaner = file;
                    		cleanerField.setText(file.getPath());
                        }else{
                        	fileCleaner = null;
                        	App.createPopUp("Fichier non reconnu ou l'extension n'était pas '.class'", "Erreur lors de la lecture du fichier", 1, App.sv.settingsStage);
                        }
                    }
                }
                
            }
        });
		firstPane.add(btnc, 2, 2);
		firstPane.add(cleanerField, 4, 2);
		
		Label agent = new Label("Agent");
		agent.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		agent.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(agent, 0, 3);
		TextField agentField = new TextField();
		Button btna = new Button("choisir le fichier");
		btna.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fc.showOpenDialog(App.sv.settingsStage);
                if(file != null){
                	if(file.getParentFile().isDirectory()){
                		fc.setInitialDirectory(file.getParentFile());
                	}
                    int i = file.getPath().lastIndexOf('.');
                    if (i > 0) {
                    	if (file != null && "class".equals(file.getPath().substring(i+1))) {
                    		fileAgent = file;
                    		agentField.setText(file.getPath());
                        }else{
                        	fileAgent = null;
                        	App.createPopUp("Fichier non reconnu ou l'extension n'était pas '.class'", "Erreur lors de la lecture du fichier", 1, App.sv.settingsStage);
                        }
                    }
                }
                
            }
        });
		firstPane.add(btna, 2, 3);
		firstPane.add(agentField, 4, 3);

		
		
		Label thief = new Label("Voleur");
		thief.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		thief.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(thief, 0, 4);
		TextField thiefField = new TextField();
		Button btnt = new Button("choisir le fichier");
		btnt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fc.showOpenDialog(App.sv.settingsStage);
                if(file != null){
                	if(file.getParentFile().isDirectory()){
                		fc.setInitialDirectory(file.getParentFile());
                	}
                    int i = file.getPath().lastIndexOf('.');
                    if (i > 0) {
                    	if (file != null && "class".equals(file.getPath().substring(i+1))) {
                    		fileThief = file;
                    		thiefField.setText(file.getPath());
                        }else{
                        	file = null;
                        	App.createPopUp("Fichier non reconnu ou l'extension n'était pas '.class'", "Erreur lors de la lecture du fichier", 1, App.sv.settingsStage);
                        }
                    }
                }
                
            }
        });
		firstPane.add(btnt, 2, 4);
		firstPane.add(thiefField, 4, 4);
		
		
		Label streetUrchin = new Label("Enfant des Rues");
		streetUrchin.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		streetUrchin.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(streetUrchin, 0, 4);
		TextField streetUrchinField = new TextField();
		Button btnsu = new Button("choisir le fichier");
		btnsu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fc.showOpenDialog(App.sv.settingsStage);
                if(file != null){
                	if(file.getParentFile().isDirectory()){
                		fc.setInitialDirectory(file.getParentFile());
                	}
                    int i = file.getPath().lastIndexOf('.');
                    if (i > 0) {
                    	if (file != null && "class".equals(file.getPath().substring(i+1))) {
                    		fileStreetUrchin = file;
                    		streetUrchinField.setText(file.getPath());
                        }else{
                        	file = null;
                        	App.createPopUp("Fichier non reconnu ou l'extension n'était pas '.class'", "Erreur lors de la lecture du fichier", 1, App.sv.settingsStage);
                        }
                    }
                }
                
            }
        });
		firstPane.add(btnsu, 2, 5);
		firstPane.add(streetUrchinField, 4, 5);
		
		Label driver = new Label("Chauffeur");
		driver.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		driver.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(driver, 0, 5);
		TextField driverField = new TextField();
		Button btnd = new Button("choisir le fichier");
		btnd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fc.showOpenDialog(App.sv.settingsStage);
                if(file != null){
                	if(file.getParentFile().isDirectory()){
                		fc.setInitialDirectory(file.getParentFile());
                	}
                    int i = file.getPath().lastIndexOf('.');
                    if (i > 0) {
                    	if (file != null && "class".equals(file.getPath().substring(i+1))) {
                    		fileDriver = file;
                    		driverField.setText(file.getPath());
                        }else{
                        	fileDriver = null;
                        	App.createPopUp("Fichier non reconnu ou l'extension n'était pas '.class'", "Erreur lors de la lecture du fichier", 1, App.sv.settingsStage);
                        }
                    }
                }
                
            }
        });
		firstPane.add(btnd, 2, 6);
		firstPane.add(driverField, 4, 6);
		
		
		
		
		Label secondLabel = new Label("Pour les stratégies concernant les positions:");
		secondLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		secondLabel.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		
		
		
		GridPane secondPane = new GridPane();
		
		Label first = new Label("Premier");
		first.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		first.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		secondPane.add(first, 0, 0);
		TextField firstField = new TextField();
		Button btnf = new Button("choisir le fichier");
		btnf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fc.showOpenDialog(App.sv.settingsStage);
                if(file != null){
                	if(file.getParentFile().isDirectory()){
                		fc.setInitialDirectory(file.getParentFile());
                	}
                    if(file != null){
                    	int i = file.getPath().lastIndexOf('.');
                        if (i > 0) {
                        	if (file != null && "class".equals(file.getPath().substring(i+1))) {
                        		fileFirst = file;
                        		firstField.setText(file.getPath());
                            }else{
                            	fileFirst = null;
                            	App.createPopUp("Fichier non reconnu ou l'extension n'était pas '.class'", "Erreur lors de la lecture du fichier", 1, App.sv.settingsStage);
                            }
                        }
                    } 
                }
                
            }
        });
		secondPane.add(new Text("\t"), 1, 0);
		secondPane.add(firstField, 4, 0);
		secondPane.add(new Text("\t"), 3, 0);
		secondPane.add(btnf, 2, 0);
		
		Label second = new Label("Second");
		second.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		second.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		secondPane.add(second, 0, 1);
		TextField secondField = new TextField();
		Button btns = new Button("choisir le fichier");
		btns.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fc.showOpenDialog(App.sv.settingsStage);
                if(file != null){
                	if(file.getParentFile().isDirectory()){
                		fc.setInitialDirectory(file.getParentFile());
                	}
                    int i = file.getPath().lastIndexOf('.');
                    if (i > 0) {
                    	if (file != null && "class".equals(file.getPath().substring(i+1))) {
                    		fileSecond = file;
                    		secondField.setText(file.getPath());
                        }else{
                        	fileSecond = null;
                        	App.createPopUp("Fichier non reconnu ou l'extension n'était pas '.class'", "Erreur lors de la lecture du fichier", 1, App.sv.settingsStage);
                        }
                    }
                }
                
            }
        });
		secondPane.add(secondField, 4, 1);
		secondPane.add(btns, 2, 1);
		
		Label last = new Label("Dernier");
		last.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		last.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		secondPane.add(last, 0, 2);
		TextField lastField = new TextField();
		Button btnl = new Button("choisir le fichier");
		btnl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fc.showOpenDialog(App.sv.settingsStage);
                if(file != null){
                	if(file.getParentFile().isDirectory()){
                		fc.setInitialDirectory(file.getParentFile());
                	}
                    int i = file.getPath().lastIndexOf('.');
                    if (i > 0) {
                    	if (file != null && "class".equals(file.getPath().substring(i+1))) {
                    		fileLast = file;
                    		lastField.setText(file.getPath());
                        }else{
                        	fileLast = null;
                        	App.createPopUp("Fichier non reconnu ou l'extension n'était pas '.class'", "Erreur lors de la lecture du fichier", 1, App.sv.settingsStage);
                        }
                    }
                }
                
            }
        });
		secondPane.add(lastField, 4, 2);
		secondPane.add(btnl, 2, 2);
		
		Label middle = new Label("Millieu");
		middle.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		middle.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		secondPane.add(middle, 0, 3);
		TextField middleField = new TextField();
		Button btnm = new Button("choisir le fichier");
		btnm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fc.showOpenDialog(App.sv.settingsStage);
                if(file != null){
                	if(file.getParentFile().isDirectory()){
                		fc.setInitialDirectory(file.getParentFile());
                	}
                    int i = file.getPath().lastIndexOf('.');
                    if (i > 0) {
                    	if (file != null && "class".equals(file.getPath().substring(i+1))) {
                    		fileMiddle = file;
                        	middleField.setText(file.getPath());
                        }else{
                        	file = null;
                        	App.createPopUp("Fichier non reconnu ou l'extension n'était pas '.class'", "Erreur lors de la lecture du fichier", 1, App.sv.settingsStage);
                        }
                    }
                }
 
            }
        });
		secondPane.add(middleField, 4, 3);
		secondPane.add(btnm, 2, 3);
		
		

		
		Label behaviourTitle = new Label("Section pour personnaliser le comportement des agents");
		behaviourTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		behaviourTitle.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		
		GridPane thirdPane = new GridPane();
		Label honesty = new Label("Honnêteté");
		honesty.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		honesty.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		thirdPane.add(honesty,0,0);
		Slider slider = new Slider(0, 100, 50);
		thirdPane.add(new Text("\t"), 1, 0);
		thirdPane.add(slider, 2, 0);
		
		HBox buttonBox = new HBox();
		buttonBox.setCenterShape(true);
		Button btnValidate = new Button("Valider");
		btnValidate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
            	StrategyFactory.update(fileGodFather, fileLoyayHenchman, fileCleaner, fileAgent, fileThief, fileStreetUrchin, fileDriver, fileFirst, fileSecond, fileLast, fileMiddle);
            	App.sv.settingsStage.close();
            	App.sv.settingsViewOpen = false;
            	App.mainStage.requestFocus();
            }
        });
		Button btnCancel = new Button("Annuler");
		btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
            	StrategyFactory.reset();
                App.sv.settingsStage.close();
                App.sv.settingsViewOpen = false;
                App.setv = new SettingsView(Theme.windowWidth, Theme.windowHeight);
            }
        });
		buttonBox.setTranslateX(300);
		buttonBox.getChildren().addAll(btnValidate, new Text("\t"), btnCancel);
		
		mainBox.getChildren().addAll(Arrays.asList(title, firstLabel, firstPane, secondLabel, secondPane, behaviourTitle, thirdPane, buttonBox));
		mainBox.setTranslateX(250);
		super.getPanel().getChildren().add(mainBox);
	}

}
