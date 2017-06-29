package boot;
import java.util.List;
import entities.DbManager;
import entities.HLevel;
import entities.User;
public class DbRun 
{
	public static void main(String[] args) 
	{
		DbManager manager = DbManager.getInstance();

	// Add a few employee records in the database


		//manager.addUser(new User(208006856));
		//manager.addUser(new User(209283503));
		//manager.addLevel(new HLevel("FirstTryHibernate"));


		List<User> p = manager.getAllUsers();

		for (User player : p)
		{
			System.out.println("#"+player.getUserId());
		}
		
		if(manager.UserisExist(208006858))	
		{
			System.out.println("yes");
		}
		
		if(manager.UserisExist(333333333))	
		{
			System.out.println("no");
		}

		manager.close();
	}
}