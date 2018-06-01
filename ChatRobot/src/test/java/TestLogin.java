
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.cs.controller.LoginController;
import com.cs.sycn.MessageListener;

public class TestLogin {

	@Test
	public void test() {
		LoginController a = new LoginController();
		a.login();
		// Executor exec = Executors.newSingleThreadExecutor();
		// exec.execute(new MessageListener());
		MessageListener d = new MessageListener(true);
		d.start();
	}
}
