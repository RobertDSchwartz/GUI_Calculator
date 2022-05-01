import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.lang.Math;

/**
 * 
 * 
 * @author RobertDSchwarts
 * @since 2022.03.01
 * @version 1.0 beta
 */
public class JavaFXCalculator extends Application {
	/**
	 * shows the user the current result and/or their input
	 */
	private TextField tfDisplay;    // display textfield
	/**
	 * shows the user what is currently the value of memory
	 */
	private Label memLabel = new Label();
	/**
	 * every button that the user can push
	 */
	private Button[] btns;          // 24 buttons
	/**
	 * labels on the buttons 
	 */
	private String[] btnLabels = {  // Labels of 24 buttons

			"MC", "MR", "M-", "M+",
			"\u221A", "^", "\u2190", "+",
			"7", "8", "9", "-",
			"4", "5", "6", "x", 
			"1", "2", "3", "\u00F7", 
			"C", "0", ".", "="
	};
	// For computation
	/**
	 * stores a number for later use when running
	 */
	private double memory = 0;
	/**
	 * the result of the input amount
	 */
	private double result = 0;      // Result of computation
	/**
	 * string of numbers that the user put together by pressing the buttons
	 */
	private String inStr = "0";  // Input number as String
	// Previous operator: ' '(nothing), '+', '-', '*', '/', '^', '=', 
	/**
	 * operator button that the user pushed is stored in here
	 */
	private char lastOperator = ' ';

	/**
	 * Event handler for all the 24 Buttons
	 */
	EventHandler handler = evt -> {
		String currentBtnLabel = ((Button)evt.getSource()).getText();
		switch (currentBtnLabel) {
		// Number buttons
		case "0": case "1": case "2": case "3": 
		case "4": case "5": case "6": case "7": 
		case "8": case "9": case ".":
			if (inStr.equals("0")) {
				inStr = currentBtnLabel;  // no leading zero
			} else {
				inStr += currentBtnLabel; // append input digit
			}
			tfDisplay.setText(inStr);
			// Clear buffer if last operator is '='
			if (lastOperator == '=') {
				result = 0;
				lastOperator = ' ';
			}
			break;

			// Operator buttons: '+', '-', 'x', '/', '^', 'sqrt', 'M+', 'M-', 'MR', 'MC', '<-' and '='
		case "+":
			compute();
			lastOperator = '+';
			break;
		case "-":
			compute();
			lastOperator = '-';
			break;
		case "x":
			compute();
			lastOperator = '*';
			break;
		case "\u00F7":
			compute();
			lastOperator = '/';
			break;
		case "^":
			compute();
			lastOperator = '^';
			break;
		case "\u221A":
			if (result != 0)
				result = Math.sqrt(result);
			else 
				result = Math.sqrt(Double.parseDouble(inStr));
			inStr = (result + "");
			tfDisplay.setText(result + "");
			break;
		case "M+":
			if (lastOperator == '=') {
				memory += result;
			}else {
				memory += Double.parseDouble(inStr);
				result = Double.parseDouble(inStr);
			}
			memLabel.setText("memory is: " + memory);
			inStr = "0";
			break;
		case "M-":
			if (result != 0) {
				memory -= result;
			}else {
				memory -= Double.parseDouble(inStr);
				result = Double.parseDouble(inStr);
			}
			memLabel.setText("memory is: " + memory);
			inStr = "0";
			break;
		case "MR":
			inStr = String.valueOf(memory);
			tfDisplay.setText(memory + "");
			break;
		case "MC":
			memory = 0;
			memLabel.setText("memory is: " + memory);
			break;
		case "\u2190":
			if (inStr.length() > 1)
				inStr = inStr.substring(0, inStr.length() - 1);
			else if (inStr.length() == 1)
				inStr = "0";
			tfDisplay.setText(inStr);
			break;
		case "=":
			compute();
			lastOperator = '=';
			break;

			// Clear button
		case "C":
			result = 0;
			inStr = "0";
			lastOperator = ' ';
			tfDisplay.setText("0");
			break;
		}
	};

	/**
	 * User pushes '+', '-', '*', '/' '^' or '=' button.
	 * Perform computation on the previous result and the current input number,
	 * based on the previous operator.
	 */
	private void compute() {
		double inNum = Double.parseDouble(inStr);
		inStr = "0";
		if (lastOperator == ' ') {
			result = inNum;
		} else if (lastOperator == '+') {
			result += inNum;
		} else if (lastOperator == '-') {
			result -= inNum;
		} else if (lastOperator == '*') {
			result *= inNum;
		} else if (lastOperator == '/') {
			result /= inNum;
		}else if (lastOperator == '^') {
			result = Math.pow(result, inNum);
		} else if (lastOperator == '=') {
			// Keep the result for the next operation
		}
		tfDisplay.setText(result + "");
		
	}

	/**
	 * puts together the user interface using btns and btnLabels
	 */
	@Override
	public void start(Stage primaryStage) {
		// Setup the Display TextField
		tfDisplay = new TextField("0");
		tfDisplay.setEditable(false);
		tfDisplay.setAlignment(Pos.CENTER_RIGHT);

		// Setup a GridPane for 4x6 Buttons
		int numCols = 4;
		GridPane paneButton = new GridPane();
		paneButton.setPadding(new Insets(15, 0, 15, 0));  // top, right, bottom, left
		paneButton.setVgap(5);  // Vertical gap between nodes
		paneButton.setHgap(5);  // Horizontal gap between nodes
		// Setup 4 columns of equal width, fill parent
		ColumnConstraints[] columns = new ColumnConstraints[numCols];
		for (int i = 0; i < numCols; ++i) {
			columns[i] = new ColumnConstraints();
			columns[i].setHgrow(Priority.ALWAYS) ;  // Allow column to grow
			columns[i].setFillWidth(true);  // Ask nodes to fill space for column
			paneButton.getColumnConstraints().add(columns[i]);
		}

		// Setup 24 Buttons and add to GridPane; and event handler
		btns = new Button[24];
		for (int i = 0; i < btns.length; ++i) {
			btns[i] = new Button(btnLabels[i]);
			switch (btnLabels[i]) {
			case "MR": case "MC": case "M+": case "M-":
				btns[i].setStyle("-fx-background-color: Yellow");
				break;
			case "+": case "-": case "x": case "\u00F7": case "=":
				btns[i].setStyle("-fx-background-color: #626ce6; -fx-text-fill: #FFFFFF");
				break;
			}
			btns[i].setOnAction(handler);  // Register event handler
			btns[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  // full-width
			paneButton.add(btns[i], i % numCols, i / numCols);  // control, col, row
		}

		// Setup up the scene graph rooted at a BorderPane (of 5 zones)
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(15, 15, 15, 15));  // top, right, bottom, left
		root.setTop(tfDisplay);     // Top zone contains the TextField
		root.setCenter(paneButton); // Center zone contains the GridPane of Buttons
		root.setBottom(memLabel);   // Bottom zone contains the current memory value

		//sets up the label thaht displays the memory value
		memLabel.setText("memory is: " + memory);
		
		// Set up scene and stage 
		primaryStage.setScene(new Scene(root, 300, 330));
		primaryStage.setTitle("JavaFX Calculator");
		primaryStage.show();
		
	}

	/**
	 * launches the program
	 * 
	 * @param args no command line input args are used for this application
	 */
	public static void main(String[] args) {
		launch(args);
	}
}