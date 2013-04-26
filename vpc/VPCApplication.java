package vpc;
/**
 * VPCApplication.java
 * operations related to VPC
 * create, delete
 * @version 3/31/2013
 * @author Li Li
 *
 */
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import util.Utils;

import com.amazonaws.services.ec2.model.CreateVpcRequest;
import com.amazonaws.services.ec2.model.CreateVpcResult;
import com.amazonaws.services.ec2.model.DeleteVpcRequest;
import com.amazonaws.services.ec2.model.Vpc;

public class VPCApplication {
  // format
	private Pattern createVpcPattern = Pattern.compile(Utils.COMMAND_CREATE_VPC + "(\\s+-c\\s+[\\d]+.[\\d]+.[\\d]+.[\\d]+/[\\d]+)?");
	private Pattern deleteVpcPattern = Pattern.compile(Utils.COMMAND_DELETE_VPC + "\\s+[0-9a-zA-Z\\-]+");
	
	/**
	 * list all Vpcs belong to current users
	 */
	public void describeVpcs() {
		for(Vpc vpc : AWSVPCManager.ec2.describeVpcs().getVpcs()) {
			System.out.println(vpc.getVpcId() + "\t" + vpc.getState());
		}
	}
	/**
	 * create a new Vpc
	 */
	public void createVpcs(String command) {
		// -c CIDRBlock
		if(!this.createVpcPattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_CREATE_VPC);
			return;
		}
		// CIDRBlock
		String cidrBlock = "10.0.0.0/16";		// default
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		if(st.hasMoreTokens()) {
			st.nextToken();
			cidrBlock = st.nextToken();
		}
		// create and send request
		CreateVpcRequest request = new CreateVpcRequest();
		request.setCidrBlock(cidrBlock);
		CreateVpcResult result = AWSVPCManager.ec2.createVpc(request);
		
		System.out.println("ID\tStatus");
		System.out.println(result.getVpc().getVpcId() + "\t" + result.getVpc().getState());
	}
	/**
	 * delete a vpc
	 * @param command
	 */
	public void deleteVpcs(String command) {
		// check format
		if(! this.deleteVpcPattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_DELETE_VPC);
			return;
		}
		// read user typed vpc id and send request
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		DeleteVpcRequest request = new DeleteVpcRequest();
		request.withVpcId(st.nextToken());
		AWSVPCManager.ec2.deleteVpc(request);
	}
}
