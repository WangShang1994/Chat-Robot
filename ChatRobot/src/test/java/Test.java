import com.cs.controller.LoginController;
import com.cs.sycn.MessageListener;

public class Test {

	public static void main(String[] args) {
		LoginController a = new LoginController();
		a.login();
		MessageListener d = new MessageListener(true);
		d.start();
	}

}
