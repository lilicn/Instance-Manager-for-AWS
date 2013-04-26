package vpc;
/**
 * SubnetApplication.java
 * operations related to Subnet
 * create, delete
 * @version 3/31/2013
 * @author Li Li
 *
 */
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import util.Utils;

import com.amazonaws.services.ec2.model.CreateSubnetRequest;
import com.amazonaws.services.ec2.model.CreateSubnetResult;
import com.amazonaws.services.ec2.model.DeleteSubnetRequest;
import com.amazonaws.services.ec2.model.Subnet;

public class SubnetApplication {
  // format
	private Pattern createSubnetPattern = Pattern.compile(Utils.COMMAND_CREATE_SUBNET + "\\s+\\-v\\s+[0-9a-zA-Z\\-]+(\\s+-c\\s+[\\d]+.[\\d]+.[\\d]+.[\\d]+/[\\d]+)?");
	private Pattern deleteSubnetPattern = Pattern.compile(Utils.COMMAND_DELETE_SUBNET + "\\s+[0-9a-zA-Z\\-]+");
	
	/**
	 * create a new subnet for a given VPC
	 * @param command
	 */
	public void createSubnet(String command) {
		// check format
		if(! this.createSubnetPattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_CREATE_SUBNET);
			return;
		}
		// get inputs
		String vpcId = "";
		String cidrBlock = "10.0.0.0/16";
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		while(st.hasMoreTokens()) {
			String tag = st.nextToken();
			if(tag.equalsIgnoreCase("-c")) {
				cidrBlock = st.nextToken();
			} else if(tag.equalsIgnoreCase("-v")) {
				vpcId = st.nextToken();
			}
		}
		// send request
		CreateSubnetRequest request = new CreateSubnetRequest();
		request.withCidrBlock(cidrBlock);
		request.withVpcId(vpcId);
		CreateSubnetResult result = AWSVPCManager.ec2.createSubnet(request);
		Subnet subnet = result.getSubnet();
		System.out.println("SubnetID\tVpcID\tCIDR\tState");
		System.out.println(subnet.getSubnetId() + "\t" + subnet.getVpcId() + "\t" + subnet.getCidrBlock() + "\t" + subnet.getState());
	}
	
	/**
	 * list all subnets
	 */
	public void describeSubnets() {
		System.out.println("SubnetID\tVpcID\tCIDR\tState");
		for(Subnet subnet : AWSVPCManager.ec2.describeSubnets().getSubnets()) {
			System.out.println(subnet.getSubnetId() + "\t" + subnet.getVpcId() + "\t" + subnet.getCidrBlock() + "\t" + subnet.getState());
		}
	}
	
	/**
	 * delete a subnet
	 * @param command
	 */
	public void deleteSubnet(String command) {
		// check format
		if(! this.deleteSubnetPattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_DELETE_SUBNET);
			return;
		}
		// get subnetID
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String subnetID = st.nextToken();
		// send request
		DeleteSubnetRequest request = new DeleteSubnetRequest();
		request.withSubnetId(subnetID);
		AWSVPCManager.ec2.deleteSubnet(request);
	}
}
