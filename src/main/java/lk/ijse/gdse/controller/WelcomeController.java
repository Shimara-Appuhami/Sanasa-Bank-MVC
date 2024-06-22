package lk.ijse.gdse.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {
    public AnchorPane node;
    public ProgressBar progressBar;
    @FXML
    public ImageView ball1;
    public ImageView imgLoan;


    @FXML
    private Text loadingText;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sideImage(imgLoan);
        simulateLoadingAnimation();
        applyAnimations();
        rotateBall(ball1);
//        rotateBall(ball2);
//        rotateBall(ball3);
//        rotateBall(ball4);
    }

    private void rotateBall(ImageView ball) {
        // Rotation animation
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), ball);
        rotateTransition.setByAngle(360); // Rotate by 360 degrees
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // Rotate indefinitely

        // Translation animation to move horizontally
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), ball);
        translateTransition.setByX(575); // Move right by 500 pixels
        translateTransition.setAutoReverse(true); // Move back to starting position
        translateTransition.setInterpolator(Interpolator.EASE_BOTH); // Smooth transition

        ParallelTransition parallelTransition = new ParallelTransition(rotateTransition, translateTransition);
        parallelTransition.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        parallelTransition.play();
    }
    private void simulateLoadingAnimation() {
        Duration animationDuration = Duration.seconds(2);
        double endProgress = 1.0;

        KeyFrame startFrame = new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0));
        KeyFrame endFrame = new KeyFrame(animationDuration, new KeyValue(progressBar.progressProperty(), endProgress));

        Timeline timeline = new Timeline(startFrame, endFrame);
        timeline.setOnFinished(event -> navigateToLoginPage());
        timeline.play();
    }

    private void applyAnimations() {

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), loadingText);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(1);
        fadeTransition.play();

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), loadingText);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }



    private void navigateToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login-form.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) progressBar.getScene().getWindow(); // Get the stage from the progress bar scene
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error loading the login page
        }
    }
    private void sideImage (ImageView imgLoan){
        /*TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), imgLoan);
        translateTransition.setByX(10); // Move right by 500 pixels
        translateTransition.setAutoReverse(true); // Move back to starting position
        translateTransition.setInterpolator(Interpolator.EASE_BOTH); // Smooth transition

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(1), imgLoan);
        translateTransition.setByY(10); // Move right by 500 pixels
        translateTransition.setAutoReverse(true); // Move back to starting position
        translateTransition.setInterpolator(Interpolator.EASE_BOTH); // Smooth transition

        ParallelTransition parallelTransition = new ParallelTransition( translateTransition,translateTransition2);
        parallelTransition.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        parallelTransition.play();*/

    }

}
