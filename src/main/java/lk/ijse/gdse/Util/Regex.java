package lk.ijse.gdse.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static boolean isTextFieldValid(TextField  textField, String text){
        String filed = "";

        switch (textField){
            case ID:
                filed ="^C[0-9]{3}$";
                break;
            case INQUIRYID:
                filed ="^I[0-9]{3}$";
                break;
            case ACCOUNTID:
                filed ="^A[0-9]{3}$";
                break;
            case BALANCEID:
                filed ="^B[0-9]{3}$";
                break;
            case EMPLOYEEID:
                filed ="^E[0-9]{3}$";
                break;
            case INTERESTRATEID:
                filed ="^INR[0-9]{3}$";
                break;
            case LOANID:
                filed ="^L[0-9]{3}$";
                break;
            case PAYMENTID:
                filed ="^P[0-9]{3}$";
                break;
            case REMINDERID:
                filed ="^R[0-9]{3}$";
                break;
            case NAME:
                filed = "^[a-z ,.'-]+$";
                break;
            case EMAIL:
                filed = "^([A-z])([A-z0-9.]){1,}[@]([A-z0-9]){1,10}[.]([A-z]){2,5}$";
                break;
            case NIC:
                filed="^(([5,6,7,8,9]{1})([0-9]{1})([0,1,2,3,5,6,7,8]{1})([0-9]{6})([v|V|x|X]))|(([1,2]{1})([0,9]{1})([0-9]{2})([0,1,2,3,5,6,7,8]{1})([0-9]{7}))";
                break;
            case DATE:
                filed="^(\\d{4})-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$";
                break;

            case PERCENTAGE:
                filed="^£?(([0-9]{1,3}(,\\d{3})*(\\.\\d{2})?)|(0\\.[0-9]\\d)|(00[0-9]))$";
                break;
            case TERM:
                filed="^\\d+(?:\\.\\d+)?\\s*(?:years?|Y)$";
                break;
            case CONTACT:
                filed="^(?:\\+94|0)(?:\\d{9}|\\d{2,3}-\\d{7})$";
                break;
            case BIRTH:
                filed="^\\d{4}-\\d{2}-\\d{2}$";
                break;
            case AGE:
                filed="^\\b\\d{1,2}\\b$";
                break;
            case AMOUNT:
                filed="^£?(([0-9]{1,20}(,\\d{3})*(\\.\\d{2})?)|(0\\.[0-9]\\d)|(00[0-9]))$";
                break;

        }

        Pattern pattern = Pattern.compile(filed);

        if (text != null){
            if (text.trim().isEmpty()){
                return false;
            }
        }else {
            return false;
        }

        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()){
            return true;
        }
        return false;
    }

    public static boolean setTextColor(TextField location, javafx.scene.control.TextField textField){
        if (Regex.isTextFieldValid(location, textField.getText())){
            textField.setStyle("-fx-text-fill: Green;");
            textField.setStyle("-fx-text-fill: Green;");
            return true;
        }else {
            textField.setStyle("-fx-text-fill: Red;");
            textField.setStyle("-fx-text-fill: Red;");
            return false;
        }
    }
}

