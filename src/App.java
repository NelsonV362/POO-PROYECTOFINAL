import models.DataManager;
import views.LoginView;
import controllers.LoginController;

public class App {
    public static void main(String[] args) {
        DataManager dataManager = new DataManager();
        LoginView loginView = new LoginView();
        new LoginController(loginView, dataManager);
        loginView.setVisible(true);
    }
}