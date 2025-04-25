package org.example;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javax.swing.plaf.ComponentUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

//TO DO: new threads/tasks shouldn't operate in this class
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GUI extends Application {

    static String version = "ver. 0.0.1";
    ImageView loadingGif;   // global imageView for changing visibility of loading screen
    String loadFileName;

    @Override
    public void start(Stage menuStage) throws Exception {

        //Menu bar icons
        VBox submenuVBox = new VBox(6);

        loadingScreen(); // Create loading screen, default visibility = false

        Image menuImage = new Image(new FileInputStream("./Icons/icons8-menu-button-48.png"));  //main menu icon
        ImageView menuView = new ImageView(menuImage);
        menuView.setX(2);
        menuView.setY(2);
        menuView.setOnMouseClicked(e -> submenuVBox.setVisible(!submenuVBox.isVisible()));
        menuView.setOnMouseEntered(e -> menuView.setStyle(
                "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, black, 3, 0.5, 0, 0);"));
        menuView.setOnMouseExited(e -> menuView.setStyle(
                "-fx-effect: dropshadow(gaussian, transparent, 0, 0, 0, 0);"
        ));

        // new measurements view
        ImageView measurementMenuView = getSubmenuIconView("./Icons/icons8-mat-64.png");
        measurementMenuView.setOnMouseClicked(e -> {
            try {
                loadingGif.setVisible(true);    // show loading screen for the time needed to connect to the device
                startNewMeasurementsScreen();   // Connect to the device + create new threads needed for data visualization
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        // loaded measurements view
        ImageView loadDataMenuView = getSubmenuIconView("./Icons/icons8-load-from-file-48.png");
        loadDataMenuView.setOnMouseClicked(e -> {
            loadMeasurementsScreen(loadFileName);
        });

        ImageView exportDataMenuView = getSubmenuIconView("./Icons/icons8-change-48.png");
        ImageView updateMenuView = getSubmenuIconView("./Icons/icons8-update-50.png");

        submenuVBox.getChildren().addAll(measurementMenuView, loadDataMenuView, exportDataMenuView, updateMenuView);
        submenuVBox.setLayoutY(56);
        submenuVBox.setLayoutX(4);
        submenuVBox.setVisible(false);


        //Logo Bar
        HBox logoBox = createLogoBox();


        //Last measurements list
        File measurementsFolder = new File("./Measurements/");
        File[] measurements = measurementsFolder.listFiles();
        if(measurements == null) measurements = new File[0];

        Arrays.sort(measurements, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified())); //sorting for newest files

        ListView<String> listView = new ListView<>();   // listing only newest 10 files
        for (int i = 0; i < measurements.length; i++){//Math.min(10, measurements.length); i++) {
            listView.getItems().add(measurements[i].getName());
        }

        listView.setMaxWidth(165);
        listView.setLayoutX(300);
        listView.setMaxHeight(175);
        listView.setLayoutY(200);
        listView.setOnMouseClicked(e -> {
            loadFileName = listView.getSelectionModel().getSelectedItem();
        });

        Label lastMeasurements = new Label("Ostatnie Pomiary:");
        lastMeasurements.setStyle("-fx-font-size: 18");

        VBox lastMeasurementsBox = new VBox(lastMeasurements, listView);
        VBox.setMargin(listView, new Insets(0, 100, 10, 50));
        VBox.setMargin(lastMeasurements, new Insets(0, 115, 10, 50));
        lastMeasurementsBox.setStyle("-fx-alignment: CENTER_RIGHT;");


        //Measurements counter
        Label measurementsNumber = new Label("Liczba Pomiarow: " + measurements.length);
        measurementsNumber.setStyle("-fx-font-size: 18;");

        HBox measurementsNumberBox = new HBox(measurementsNumber, lastMeasurementsBox);

        measurementsNumberBox.setStyle("-fx-alignment: CENTER_RIGHT;");
        HBox.setMargin(measurementsNumber, new Insets(0, 80, 10, 50));


        StackPane layout = new StackPane(logoBox, measurementsNumberBox);

        Group menuGroup = new Group();
        menuGroup.getChildren().addAll(menuView, submenuVBox, loadingGif);

        HBox mainBox = new HBox();
        HBox.setHgrow(layout, Priority.ALWAYS);
        mainBox.getChildren().addAll(menuGroup,layout);

        menuStage.setTitle("WalkWise");

        Scene scene = new Scene(mainBox, 680, 350);

        menuStage.setScene(scene);
        menuStage.show();

    }

    private static ImageView getSubmenuIconView(String fileSrc) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(fileSrc));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        imageView.setOnMouseEntered(e -> imageView.setStyle(
                "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, black, 3, 0.5, 0, 0);"));
        imageView.setOnMouseExited(e -> imageView.setStyle(
                "-fx-effect: dropshadow(gaussian, transparent, 0, 0, 0, 0);"
        ));
        return imageView;
    }

    private static HBox createLogoBox() throws FileNotFoundException {
        Image logoNameImage = new Image(new FileInputStream("./Icons/walkwise-primary-logo.png"));
        ImageView logoNameView = new ImageView(logoNameImage);
        logoNameView.setFitHeight(60);
        logoNameView.setFitWidth(210);

        Image logoImage = new Image(new FileInputStream("./Icons/imagen-accidente-de-trabajo.jpg"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(60);
        logoView.setFitWidth(60);

        Label verisonLabel = new Label(version);

        HBox logoBox = new HBox(80);

        logoNameView.fitWidthProperty().bind(logoBox.widthProperty().divide(5));    //Responsive image placement
        logoView.fitWidthProperty().bind(logoBox.widthProperty().divide(5));

        logoBox.setStyle("-fx-alignment: TOP_CENTER;");
        logoBox.getChildren().addAll(logoView, logoNameView, verisonLabel);
        logoBox.setLayoutX(100);
        logoBox.setLayoutY(20);

        logoNameView.setPreserveRatio(true);
        logoView.setPreserveRatio(true);

        logoBox.setVisible(true);

        return logoBox;
    }

    // Loading screen gif
    public void loadingScreen() throws FileNotFoundException {
        Image loadingDog = new Image(new FileInputStream("./Icons/loadingDog.gif"));
        loadingGif = new ImageView(loadingDog);
        loadingGif.setFitHeight(350);
        loadingGif.setFitWidth(680);
        loadingGif.setVisible(false);
    }

    void loadIcons(){
    }

    public void menuScreen(){
    }

    public void loadMeasurementsScreen(String fileName){
        if(fileName != null){
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {

                    BlockingQueue<String> testQueue = new LinkedBlockingQueue<>();
                    Data testPrzesylDaty = new Data(testQueue);

                    Visualization visualization = new Visualization();

                    System.out.println(testPrzesylDaty.sampledData);

                    visualization.displayHeatmapExternalDat(testPrzesylDaty, fileName);
                    return null;
                }

                @Override
                protected void succeeded() {
                }

                @Override
                protected void failed() {
                    System.err.println("Task Error!");
                }
            };

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        } else {
            System.out.println("No File chosen");
            //TO DO: CHOOSE FILE ERROR SCREEN
        }
    }


    public void startNewMeasurementsScreen() throws IOException, InterruptedException {

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {

                Communication.findAllDevice();  // find all available devices

                if(Communication.connectToDevice(Communication.findSpecificDevice("WalkWise")) == true){    // find if there is a WalkWise device and connect to it
                    //
                    BlockingQueue<String> dataQueue = new LinkedBlockingQueue<>(); // Queue for received data
                    Communication receiveData = new Communication(dataQueue);
                    Data processData = new Data(dataQueue);
                    Thread receiverThread = new Thread(receiveData);    // Receiving data thread
                    Thread dataThread = new Thread(processData);    // Data processing thread

                    receiverThread.start();
                    dataThread.start();

                    loadingGif.setVisible(false);

                    Visualization visualization = new Visualization();
                    try {
                        visualization.displayRTHeatmap(processData);    // Show heatmap form RT processed data
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //
                } else {
                    // TO DO: DISPLAY NOT CONNECTED SCREEN
                }
                return null;
            }

            @Override
            protected void succeeded() {
                loadingGif.setVisible(false);
            }

            @Override
            protected void failed() {
                loadingGif.setVisible(false);
                System.err.println("Task Error!");
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void exportDataScreen(){
    }

    public void updateScreen() throws FileNotFoundException {
        Image loadingImage = new Image(new FileInputStream("./Icons/icons8-baby-footprint-50"));    //submenu update icon
        ImageView loadingView = new ImageView(loadingImage);

    }
}
