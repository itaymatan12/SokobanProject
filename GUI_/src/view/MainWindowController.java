package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import entities.User;
import entities.UserRecord;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.data.Level;

public class MainWindowController extends Observable implements Initializable , View {

	//data members
	private FXMLLoader fxmlLoader;
	private FXMLLoader fxmlLoader1;
	private FXMLLoader fxmlLoader2;
	private int count =0;
	private File f;
	private int steps;
	private int rescount =0;
	private boolean timerFlag;
	private Stage stageControls;
	private Stage stageUsers;
	private Stage stageLevelScore;
	private	String levelName;
	
	
	@FXML
	private Button lscore;
	
	@FXML
	private Button add;
	
	@FXML
	private TextField time;

	@FXML
	private Label step;
	
	@FXML
	SokobanDisplayer soko;
	
	@FXML
	SetControlsController controls;
	
	@FXML
	LevelScoreBoardController levelscore;
	
	@FXML
	PlayersListController Users;
	
	//default constructor
	public MainWindowController() 
	{
		stageControls = null;
		stageLevelScore = null;
		stageUsers = null;
		this.step = new Label();
		this.timerFlag =true;
		this.steps = 0;
		this.soko = new SokobanDisplayer();
	  	this.f = null;
	  	this.levelName = null;
			
		controls = new SetControlsController();
		Users = new PlayersListController(this);
		levelscore =  new LevelScoreBoardController(this);
		
		
		//Gui controls
		try {
				controls = new SetControlsController();
				levelscore = new LevelScoreBoardController(this);
				fxmlLoader = new FXMLLoader(getClass().getResource("SetControls.fxml"));
				fxmlLoader.setController(controls);
				Parent root1;
				root1 = (Parent) fxmlLoader.load();
				stageControls = new Stage();
				stageControls.initStyle(StageStyle.UTILITY);
				stageControls.setResizable(false);
				stageControls.setScene(new Scene(root1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		////////////////////
		
		
		// PlayersList Window
		try{
			
			FXMLLoader fxmlLoader5 = new FXMLLoader(getClass().getResource("UsersList.fxml"));
			fxmlLoader5.setController(Users);
			Parent root2 = (BorderPane) fxmlLoader5.load();
			stageUsers = new Stage();
			stageUsers.initStyle(StageStyle.UTILITY);
			stageUsers.setResizable(false);
			stageUsers.setScene(new Scene(root2));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		/////////////////////
		
		// LevelScoreBoard Window
		try
		{
			FXMLLoader fxmlLoader6 = new FXMLLoader(getClass().getResource("LevelScoreBoard.fxml"));
			fxmlLoader6.setController(levelscore);
			Parent root3 = (Parent) fxmlLoader6.load();
			stageLevelScore = new Stage();
			stageLevelScore.initStyle(StageStyle.UTILITY);
			stageLevelScore.setResizable(false);
			stageLevelScore.setScene(new Scene(root3));
		}

		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	
	//cleaning the level so we able to maintain restart button an load on load ability
	public void cleanlevel()
	{
		
		String command = "cleanlevel";
		LinkedList<String> params = new LinkedList<String>();
		params.add(command);
		

		this.setChanged();
		this.notifyObservers(params);
		
	}
	
	//loading a level
	public void load_file()
	{	
		FileChooser fc = new FileChooser();
		fc.setTitle("Load File");
		fc.setInitialDirectory(new File("./resources"));
		
		//filters that makes us able to load files that their types are( TEXT / OBJ /XML)
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("OBJ files (*.obj)", "*.obj"));

		
        //opening load file dialog
       File chosen= fc.showOpenDialog(null);
       this.f =chosen;//we saving the level that we loades so we able to do restart
       
       //if we already load a level
       if(count>0)
       {
    	   this.cleanlevel();    	   
       }
       
       
       if (chosen!= null)
       {	
    	   ++count;//count that represent if we already loaded a level
    	   //caling the load command
			String command = "loadfile";
			LinkedList<String> params = new LinkedList<String>();
			params.add(command);
			params.add(chosen.getPath());

			this.setChanged();
			this.notifyObservers(params);
			
			// Add Level to DB
			int i = 0;
			while(chosen.getName().charAt(i) != '.')
			{
							i++;
			}

			levelName = chosen.getName().substring(0, i);

			command = "AddLevelDB";
			params = new LinkedList<String>();
			params.add(command);
			params.add(levelName);

			this.setChanged();
			this.notifyObservers(params); // notify Controller - tell Model to add new Level to database
					
			//call timer thread
			this.lscore.setVisible(true);
			this.lscore.setDisable(false);
			timerFlag = false;
			Platform.runLater(() -> time.setText("0"));
			startTimer();
		}
	}

	//save file function
	public void save_file()
   	{
   		FileChooser fc = new FileChooser();
   		fc.setTitle("Save File");
   		fc.setInitialDirectory(new File("./resources"));
   		
   			//filters that makes us able to save to files that their types are( TEXT / OBJ /XML)
           fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt"));
           fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
           fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("OBJ files (*.obj)", "*.obj"));
           
           //opening a save dialog
          File chosen= fc.showSaveDialog(null);
          if (chosen!= null)
          {
        	  //calling the save command
   			String command = "savefile";
   			LinkedList<String> params = new LinkedList<String>();
   			params.add(command);
   			params.add(chosen.getPath());

   			this.setChanged();
   			this.notifyObservers(params);
   			
   			//call timer thread
			timerFlag = false;
			Platform.runLater(() -> time.setText("0"));
			startTimer();
   		}
   	}

	//exiting the game
    public void exit()
    {
    	
    	
    	//opening a confirmation dialog
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to exit?");//asking the user if he sure he want to exit the game

		//waiting to the user response
		Optional<ButtonType> result = alert.showAndWait();
		
		//if the user decide to close the game
		if (result.get() == ButtonType.OK)
		{
				this.timerFlag = false;
			
				//calling the exit command
        	    String command = "exit";
        	    
        	    LinkedList<String> params = new LinkedList<String>();
        	    params.add(command);
        	    params.add("regularexit");
        	    this.setChanged();
     	
        	    this.notifyObservers(params);	 
   
        	 
		}
    }

    //initializing function that runs in the background
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{		
		//background music loading
		Media sound = new Media(new File("./resources/gummy.wav").toURI().toString());
		AudioClip mediaPlayer = new AudioClip(sound.getSource());
		mediaPlayer.setCycleCount(100);
		mediaPlayer.play();
			
		//getting the focus on objects in the GUI by clicking on them with the mouse 
		this.soko.addEventFilter(MouseEvent.MOUSE_CLICKED,(e)->soko.requestFocus());		
		
		//keyboard press event
		this.soko.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
           
			@Override
			public void handle(KeyEvent event)
			{
				String direction = null;
				
				//if the user want to move up
				if(event.getCode().getName().equals(controls.getGoUp()))
				{
					direction = "up";
				}

				//if the user want to move down
				else if(event.getCode().getName().equals(controls.getGoDown()))
				{
					direction = "down";
				}

				//if the user want to move left
				else if(event.getCode().getName().equals(controls.getGoLeft()))
				{
					direction = "left";
				}

				//if the user want to move right
				else if(event.getCode().getName().equals(controls.getGoRight()))
				{
					direction = "right";
				}
				
				if(direction!=null)
				{
				
					//calling the move command
					String command = "move";
					LinkedList<String> params = new LinkedList<String>();
	     			params.add(command);
	     			params.add(direction);

	     			setChanged();
	     			notifyObservers(params);
	
				}
			}
		});
		}
	
	//restart function
	public void restart()
	{
	       if(count>0)
	       {
	    	   //clening the level
	    	   this.cleanlevel();  
	   
	    	   //loading again
	    	   String command = "loadfile";
	    	   LinkedList<String> params = new LinkedList<String>();
	    	   params.add(command);
	    	   params.add(f.getPath());

	    	   this.setChanged();
	    	   this.notifyObservers(params);
	       }  
	       
	       //restarting the step and the timer
	       rescount++;
		   this.add.setVisible(false);
		   this.add.setDisable(true);
	       time.setText("0");
	       step.setText("0");	
	       timerFlag = true;
	       startTimer();
	       this.soko.gc.getCanvas().setDisable(false);
	       
	}
	
	//displaying a level
	@Override
	public void displayLevel(Level l, Display d)
	{
		
		if(l.ifWon())
		{
			if(rescount ==1)
			{
				l.setNum_box_on_targets(0);
				rescount=0;
			}
			else
			{
				this.add.setVisible(true);
				this.add.setDisable(false);
				this.soko.gc.getCanvas().setDisable(true);
				Won();
			}
		}
		//setting the displayer level to the level
		this.soko.setLevelim(l.getLevelim());
		this.setSteps(l.getSteps());//getting every move the step because every move the level changes 
		
		//step thread
		Platform.runLater(new Runnable() {
			
			@Override
			public void run()
			{
				//converting int to String
				StringBuilder sb = new StringBuilder();
				sb.append("");
				sb.append(getSteps());
				String strI = sb.toString();
				step.setText(strI);//setting the step label to the number of the steps we done
			}
		});
		
		
	}
	
	
	public void Won()
	{
		timerFlag = false;
		
		Platform.runLater(
				() -> {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Save Score");
					alert.setHeaderText(null);
					alert.setContentText("Do you want to save your score?");

					ButtonType YesButton = new ButtonType("Yes", ButtonData.YES);
					ButtonType NoButton = new ButtonType("No", ButtonData.NO);
					alert.getButtonTypes().setAll(YesButton,NoButton);

					Optional<ButtonType> result = alert.showAndWait();
					
					if (result.get() == YesButton)
					{
						if(Users.getAcount() == null)
						{
							Users.SetFlag(true);
							ShowSelectUser();
						}
						
						else
						{
							addUserScore();
						}
			   
					}
					
					else
					{
						return;
					}
				});
	}

	//Timer function	
	private void startTimer()
	{
		try
		{
			Thread.sleep(1000);
		}

		catch (InterruptedException e)
		{
			displayMessage(e.getMessage());
		}


		timerFlag = true;

		//creating the thread
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(timerFlag)
				{
					Platform.runLater(() ->time.setText(String.valueOf(Integer.parseInt(time.getText())+1)));
					try
					{
						Thread.sleep(1000);
					}

					catch (InterruptedException e)
					{
						displayMessage(e.getMessage());
					}
				}
			}
		});
		t.start();
	}
	
	//Displaying a message to the user
	@Override
	public void displayMessage(String string) {
		System.out.println(string);		
	}

	//Doing Nothing
	@Override
	public void start() 
	{
			
			
	}
	
	//closing the game
	public void close()
	{
		//exiting the game
		timerFlag = false;
		System.exit(0);
	}

	//Doing Nothing
	@Override
	public void stop() {
		
			
	}
	
	//opening the controls setting window
	public void setControl()
	{
		if(stageControls != null)
			stageControls.show();
		
	}
	
	//opening the user selection window
	public void ShowSelectUser()
	{
		getUsers();//getting all the users from the DB

		if(stageUsers != null)
			stageUsers.show();
	}	
	
	//getting all the users from the DB
	public void getUsers()
	{
		String command = "GetAllUsersDB";
		LinkedList<String> params = new LinkedList<String>();
		params.add(command);

		this.setChanged();
		this.notifyObservers(params);
	}
	
	//adding a new user to the DB
	public void addUser(int UserId)
	{
		Integer x = UserId;
		String command = "AddUser";
		LinkedList<String> params = new LinkedList<String>();
		params.add(command);
		params.add(x.toString());
		
		this.setChanged();
		this.notifyObservers(params);
	}

	//setting the users list of the user selection window to the users from the DB
	public void setPlayersList(List<User> p)
	{
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
				Users.setUsersList(p);
		    }
		});
	}
	
	//adding a ascore to the DB
	public void addUserScore()
	{
		Integer x = Users.getAcount().getUserId();
		
		String command = "AddUserScore";
		LinkedList<String> params = new LinkedList<String>();
		params.add(command);
		params.add(this.levelName);
		params.add(x.toString());
		params.add(this.step.getText());
		params.add(this.time.getText());
		
		this.setChanged();
		this.notifyObservers(params);
	}
	
	//getting all the scores of a level from the DB
	public void getScores()
	{
		System.out.println("3");
		String command = "GetAllScoresDB";
		LinkedList<String> params = new LinkedList<String>();
		params.add(command);

		this.setChanged();
		this.notifyObservers(params);
	}
	
	//getting the scores list from the DB and opening the scoreBoard Window
	public void setLevelScore()
	{
		if(stageLevelScore!=null)
		{
			System.out.println("2");
			String command = "LevelScoreSortSteps";
			LinkedList<String> params = new LinkedList<String>();
			params.add(command);
			params.add(levelName);

			this.setChanged();
			this.notifyObservers(params); // notify Controller - update scores
			getScores();
			System.out.println("5");
			stageLevelScore.show();
		}
	}

	//defining the default keys to the keys that we load from the XML file in the beginning of the game
	@Override
	public void setcontrols(String up, String down, String left, String right)
	{
		
		this.controls.setGoUp(up);
		this.controls.setGoDown(down);
		this.controls.setGoLeft(left);
		this.controls.setGoRight(right);
	}

	//Getters and Setters
	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	//sorting level scores by steps
	public void sortLevelScoresBySteps() {
		String command = "LevelScoreSortSteps";
		LinkedList<String> params = new LinkedList<String>();
		params.add(command);
		params.add(levelName);

		this.setChanged();
		this.notifyObservers(params); // notify Controller - get PlayersRecordsList sorted by steps
		getScores();
		
	}

	//sorting level scores by time
	public void sortLevelScoresByTime() 
	{
		String command = "LevelScoreSortTime";
		LinkedList<String> params = new LinkedList<String>();
		params.add(command);
		params.add(levelName);
		
		this.setChanged();
		this.notifyObservers(params); // notify Controller - get PlayersRecordsList sorted by time
		getScores();
		
	}

	public void getChosenPlayerScores(int userId) 
	{
		Integer x = userId;
		
		String command = "GetChosenPlayerScores";
		LinkedList<String> params = new LinkedList<String>();
		params.add(command);
		params.add(x.toString());

		this.setChanged();
		this.notifyObservers(params); // notify Controller - send me this playerId all scores
		
	}

	
	//setting the scores list of the scoreBoard window to the scores from the DB
	@Override
	public void setLevelScores(List<UserRecord> p) 
	{
		System.out.println("4");
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	levelscore.setScores(p);
		    }
		});
		
	}

	@Override
	public void setUserScores(List<UserRecord> p) 
	{
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
				levelscore.setPlayerScores(p);
		    }
		});

		
	}
	
	public void sortUserRecordBySteps(int userid)
	{
		Integer x =userid;
		String command = "UserScoreSortSteps";
		LinkedList<String> params = new LinkedList<String>();
		params.add(command);
		params.add(x.toString());
		
		this.setChanged();
		this.notifyObservers(params); // notify Controller - get PlayersRecordsList sorted by time
		
	}
		
	public void sortUserRecordByTime(int userid)
	{
		Integer x =userid;
		String command = "UserScoreSortTime";
		LinkedList<String> params = new LinkedList<String>();
		params.add(command);
		params.add(x.toString());
		
		this.setChanged();
		this.notifyObservers(params); // notify Controller - get PlayersRecordsList sorted by time
		
	}
	
	public void sortUserRecordByLexicalOrder(int userid)
	{
		Integer x =userid;

		String command = "UserScoreSortLexical";
		LinkedList<String> params = new LinkedList<String>();
		params.add(command);
		params.add(x.toString());
		
		this.setChanged();
		this.notifyObservers(params); // notify Controller - get PlayersRecordsList sorted by time
	}
	
	
	// Function "SearchScoreBoard" - show Level or Player ScoreBoard by name
	public void SearchScoreBoard()
	{
		List<String> choices = new ArrayList<>();
		choices.add("Search Level ScoreBoard");
		choices.add("Search Player ScoreBoard");

		ChoiceDialog<String> dialog = new ChoiceDialog<>("Search Level ScoreBoard", choices);
		dialog.setTitle("Search ScoreBoard");
		dialog.setHeaderText(null);
		dialog.setContentText("Select your searcher:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent())
		{
			dialog.hide();

			TextInputDialog dialog2 = new TextInputDialog("");
			dialog2.setTitle(result.get());
			dialog2.setHeaderText(null);
			dialog2.initStyle(StageStyle.UTILITY);

			if(result.get().equals("Search Level ScoreBoard"))
				dialog2.setContentText("Enter Level-Name:");

			else
				dialog2.setContentText("Enter User-Id:");

			Optional<String> result2 = dialog2.showAndWait();
			if (result2.isPresent())
			{
				if(result.get().equals("Search Level ScoreBoard"))
				{
					System.out.println("1");
					this.levelName =result2.get();
					setLevelScore();
				}

				else
				{
					getChosenPlayerScores(Integer.parseInt(result2.get()));
					levelscore.showPlayerScores();
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}