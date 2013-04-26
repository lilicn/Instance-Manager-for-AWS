package vpc;
/**
 * GatewayApplication.java
 * operations related to Gateway
 * create, delete, attach, 
 * @version 3/31/2013
 * @author Li Li
 *
 */
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import util.Utils;

import com.amazonaws.services.ec2.model.AttachInternetGatewayRequest;
import com.amazonaws.services.ec2.model.CreateInternetGatewayResult;
import com.amazonaws.services.ec2.model.DeleteInternetGatewayRequest;
import com.amazonaws.services.ec2.model.DetachInternetGatewayRequest;
import com.amazonaws.services.ec2.model.InternetGateway;

public class GatewayApplication {
  // format
	private Pattern deleteGatewayPattern = Pattern.compile(Utils.COMMAND_DELETE_GATEWAY + "\\s+[0-9a-zA-Z\\-.]+");
	private Pattern attachGatewayPattern = Pattern.compile(Utils.COMMAND_ATTACH_GATEWAY + "\\s+\\-g\\s+[0-9a-zA-Z\\-.]+\\s+\\-v\\s+[0-9a-zA-Z\\-]+");
	private Pattern detachGatewayPattern = Pattern.compile(Utils.COMMAND_DETACH_GATEWAY + "\\s+\\-g\\s+[0-9a-zA-Z\\-.]+\\s+\\-v\\s+[0-9a-zA-Z\\-]+");

	/**
	 * create a new gateway
	 * @param command
	 */
	public void createGateway() {
		CreateInternetGatewayResult result = AWSVPCManager.ec2.createInternetGateway();
		System.out.println("ID");
		System.out.println(result.getInternetGateway().getInternetGatewayId());
	}
	
	/**
	 * list all gateway
	 */
	public void describeGateway() {
		System.out.println("ID");
		for(InternetGateway gateway : AWSVPCManager.ec2.describeInternetGateways().getInternetGateways()) {
			System.out.println(gateway.getInternetGatewayId());
		}
	}
	
	/**
	 * delete a Gateway
	 * @param command
	 */
	public void deleteGateway(String command) {
		// check format
		if(! this.deleteGatewayPattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_DELETE_GATEWAY);
			return;
		}
		// get gatewayID
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String gatewayID = st.nextToken();
		// send request
		DeleteInternetGatewayRequest request = new DeleteInternetGatewayRequest();
		request.withInternetGatewayId(gatewayID);
		AWSVPCManager.ec2.deleteInternetGateway(request);
	}
	
	/**
	 * attach a gateway to a vpc
	 * @param command
	 */
	public void attachGateway(String command) {
		if(! this.attachGatewayPattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_ATTACH_GATEWAY);
			return;
		}
		
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String gatewayID = "";
		String vpcID = "";
		while(st.hasMoreTokens()) {
			String tag = st.nextToken();
			if(tag.equalsIgnoreCase("-g")) {
				gatewayID = st.nextToken();
			} else if(tag.equalsIgnoreCase("-v")) {
				vpcID = st.nextToken();
			}
		}
		
		AttachInternetGatewayRequest request = new AttachInternetGatewayRequest();
		request.withInternetGatewayId(gatewayID);
		request.withVpcId(vpcID);
		AWSVPCManager.ec2.attachInternetGateway(request);
	}
	
	public void detachGateway(String command) {
		if(! this.detachGatewayPattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_DETACH_GATEWAY);
			return;
		}
		
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String gatewayID = "";
		String vpcID = "";
		while(st.hasMoreTokens()) {
			String tag = st.nextToken();
			if(tag.equalsIgnoreCase("-g")) {
				gatewayID = st.nextToken();
			} else if(tag.equalsIgnoreCase("-v")) {
				vpcID = st.nextToken();
			}
		}
		
		DetachInternetGatewayRequest request = new DetachInternetGatewayRequest();
		request.withInternetGatewayId(gatewayID);
		request.withVpcId(vpcID);
		AWSVPCManager.ec2.detachInternetGateway(request);
	}
}
