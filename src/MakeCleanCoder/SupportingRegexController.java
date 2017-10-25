package MakeCleanCoder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by satopi on 2017/10/17.
 */
public class SupportingRegexController implements Initializable {
    @FXML
    TextField regexField,targetField;
    @FXML
    TextArea resultArea;

    @FXML
    private void handleOnTestRegex(ActionEvent event) {
        try {
            Pattern p = Pattern.compile(regexField.getText());
            Matcher m = p.matcher(targetField.getText());
            if (m.find()) {
                resultArea.setText("true");
            } else {
                resultArea.setText("false");
            }
        }catch(PatternSyntaxException e){
            resultArea.setText(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
