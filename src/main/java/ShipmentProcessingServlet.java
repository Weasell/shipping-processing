

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.*;
import javax.inject.*;
import javax.jms.*;

/**
 * Servlet implementation class ShipmentProcessingServlet
 */
@WebServlet("/initiation")
public class ShipmentProcessingServlet extends HttpServlet {
	@Inject
	@JMSConnectionFactory("jms/shipmentQCF")
	private JMSContext jmsContext;

	@Resource(lookup="jms/shipmentQ")
	private Queue queue;

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShipmentProcessingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		
		System.out.println("Receiving message...");
		PrintWriter out = response.getWriter();
		Message message = jmsContext.createConsumer(queue).receive(5000);
		if (message != null && message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			try {
				System.out.println("Received: " + textMessage.getText());
				out.println("Received: " + textMessage.getText());
			} catch (JMSException e) {
				out.println("Error: " + e.getMessage());
			}
		} else {
			System.out.println("Unknown or missing message content");
			out.println("Unknown or missing message content");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
