/**
 * 
 */
package edu.jhuapl.dorset.components.tools;

import java.util.Scanner;

import edu.jhuapl.dorset.Application;
import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.agents.AbstractAgent;
import edu.jhuapl.dorset.routing.Router;
import edu.jhuapl.dorset.routing.SingleAgentRouter;

/**
 * Simple, command line access to a single agent, router, or application.
 * Whatever you enter at the command line is sent, and the text response is
 * provided to System.out.
 * 
 * The default character to exit the system is 'q'.
 * 
 * Example:
 * 
 * <pre>
 * Agent agent = new CalculatorAgent();
 * CommandLineClient client = new CommandLineClient(agent);
 * client.go();
 * </pre>
 * 
 * @author david.patrone
 *
 */
public class CommandLineClient {

	private Application app;

	public CommandLineClient(AbstractAgent agent) {
		this(new SingleAgentRouter(agent));
	}

	public CommandLineClient(Router router) {
		this(new Application(router));
	}

	public CommandLineClient(Application app) {
		this.app = app;
	}

	public void go() {
		String input = "";
		Scanner in = new Scanner(System.in);

		while (true) {
			System.out.print("> ");
			input = in.nextLine();

			if (isQuitString(input)) {
				break;
			}

			Request request = new Request(input);
			Response response = app.process(request);

			System.out.println(response.getText());
		}

		System.out.println("\nBye.");
		in.close();
	}

	/**
	 * Override-able method for defining what input string will stop the system.
	 * The default is a single character 'q'.
	 * 
	 * @param input
	 * @return
	 */
	protected boolean isQuitString(String input) {
		return "q".equals(input);
	}

}
