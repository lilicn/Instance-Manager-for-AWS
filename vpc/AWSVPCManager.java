package vpc;
/**
 * AWSVPCManager.java
 * AWS VPC manager
 * @version 3/31/2013
 * @author Li li
 *
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import util.Utils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;

public class AWSVPCManager {
  // the ec2
	public static AmazonEC2 ec2 = null;
	// singleton
	public static AWSVPCManager INSTANCE = new AWSVPCManager();
	// applications
	private SubnetApplication subnetApp = new SubnetApplication();
	private AddressApplication addressApp = new AddressApplication();
	private InstanceApplication instanceApp = new InstanceApplication();
	private VPCApplication vpcApp = new VPCApplication();
	private GatewayApplication gatewayApp = new GatewayApplication();
	
	private AWSVPCManager(){}
	
	/**
	 * launch a VPC manager and run
	 */
	public void run() {
		// connect to AWS
		AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
		ec2 = new AmazonEC2Client(credentialsProvider);
		// set default service end point to us-east-1
		ec2.setEndpoint("ec2.us-east-1.amazonaws.com");
				
		System.out.println("******************** AWS Vpc Manager ********************");
		System.out.println("provide operations for AWS Vpc");
		System.out.println("type \"help\" to get all supported commands and format, \"exit\" to exit manager");
		System.out.println("*******************************************************");
				
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			try{
				System.out.print(">> ");
				String command = reader.readLine().trim();
				if(command.startsWith(Utils.COMMAND_EXIT)) {
					ec2.shutdown();
					// quit
					break;
				} else if(command.startsWith(Utils.COMMAND_DESCRIBE_ADDRESS)) {
					this.addressApp.describeAddress();
				} else if(command.startsWith(Utils.COMMAND_ALLOCATE_ADDRESS)) {
					this.addressApp.allocateAddress();
				} else if(command.startsWith(Utils.COMMAND_ASSOCIATE_ADDRESS)) {
					this.addressApp.associateAddress(command);
				} else if(command.startsWith(Utils.COMMAND_DISASSOCIATE_ADDRESS)) {
					this.addressApp.disassociateAddress(command);
				} else if(command.startsWith(Utils.COMMAND_RELEASE_ADDRESS)) {
					this.addressApp.releaseAddress(command);
				} else if(command.startsWith(Utils.COMMAND_CREATE_SUBNET)) {
					this.subnetApp.createSubnet(command);
				} else if(command.startsWith(Utils.COMMAND_DESCRIBE_SUBNET)) {
					this.subnetApp.describeSubnets();
				} else if(command.startsWith(Utils.COMMAND_DELETE_SUBNET)) {
					this.subnetApp.deleteSubnet(command);
				} else if(command.startsWith(Utils.COMMAND_CREATE_VPC)) {
					this.vpcApp.createVpcs(command);
				} else if(command.startsWith(Utils.COMMAND_DELETE_VPC)) {
					this.vpcApp.deleteVpcs(command);
				} else if(command.startsWith(Utils.COMMAND_DESCRIBE_VPC)) {
					this.vpcApp.describeVpcs();
				} else if(command.startsWith(Utils.COMMAND_LAUNCH_INSTANCE)) {
					this.instanceApp.launch(command);
				} else if(command.startsWith(Utils.COMMAND_TERMINATE_INSTANCE)) {
					this.instanceApp.terminate(command);
				} else if(command.startsWith(Utils.COMMAND_DESCRIBE_INSTANCE)) {
					this.instanceApp.describeInstances();
				} else if(command.startsWith(Utils.COMMAND_STOP_INSTANCE)) {
					this.instanceApp.stop(command);
				} else if(command.startsWith(Utils.COMMAND_REBOOT_INSTANCE)) {
					this.instanceApp.reboot(command);
				} else if(command.startsWith(Utils.COMMAND_START_INSTANCE)) {
					this.instanceApp.start(command);
				} else if(command.startsWith(Utils.COMMAND_DESCRIBE_GATEWAY)) {
					this.gatewayApp.describeGateway();
				} else if(command.startsWith(Utils.COMMAND_CREATE_GATEWAY)) {
					this.gatewayApp.createGateway();
				} else if(command.startsWith(Utils.COMMAND_DELETE_GATEWAY)) {
					this.gatewayApp.deleteGateway(command);
				} else if(command.startsWith(Utils.COMMAND_ATTACH_GATEWAY)) {
					this.gatewayApp.attachGateway(command);
				} else if(command.startsWith(Utils.COMMAND_DETACH_GATEWAY)) {
					this.gatewayApp.detachGateway(command);
				} else if(command.startsWith("help")) {
					help();
				} else {
					System.out.println("Cannot figure out your typed command. Type \"help\" if you need any help.");
				}
			} catch(Exception e) {
				System.out.println("Exception happened. " + e.getMessage());
			}
		}
		System.out.println("AWS VPC Manager has been shutted down.");
	}
	
	public void help() {
		// several commands
		System.out.println("Here are all commands supported.");
		System.out.println("\t" + Utils.COMMAND_ALLOCATE_ADDRESS + ": allocate a new address/IP.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_ALLOCATE_ADDRESS);
		System.out.println("\t" + Utils.COMMAND_ASSOCIATE_ADDRESS + ": associate an address to an instance.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_ASSOCIATE_ADDRESS);
		System.out.println("\t" + Utils.COMMAND_DESCRIBE_ADDRESS + ": describle all own addresses.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DESCRIBE_ADDRESS);
		System.out.println("\t" + Utils.COMMAND_DISASSOCIATE_ADDRESS + ": disassociate address.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DISASSOCIATE_ADDRESS);
		System.out.println("\t" + Utils.COMMAND_RELEASE_ADDRESS + ": release an address.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_RELEASE_ADDRESS);
		
		System.out.println("\t" + Utils.COMMAND_DESCRIBE_GATEWAY + ": list all own gateways.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DESCRIBE_GATEWAY);
		System.out.println("\t" + Utils.COMMAND_CREATE_GATEWAY + ": create a new internet gateway.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_CREATE_GATEWAY);
		System.out.println("\t" + Utils.COMMAND_DELETE_GATEWAY + ": delete a gateway.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DELETE_GATEWAY);
		System.out.println("\t" + Utils.COMMAND_ATTACH_GATEWAY + ": attach a gateway to a vpc.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_ATTACH_GATEWAY);
		System.out.println("\t" + Utils.COMMAND_DETACH_GATEWAY + ": dettach a gateway from a vpc.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DETACH_GATEWAY);
		
		System.out.println("\t" + Utils.COMMAND_DESCRIBE_INSTANCE + ": list all own instances.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DESCRIBE_INSTANCE);
		System.out.println("\t" + Utils.COMMAND_LAUNCH_INSTANCE + ": launch a new instance inside a subnet.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_LAUNCH_INSTANCE_VPC);
		System.out.println("\t" + Utils.COMMAND_START_INSTANCE + ": start one or several instances.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_START_INSTANCE);
		System.out.println("\t" + Utils.COMMAND_STOP_INSTANCE + ": stop one or several instances.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_STOP_INSTANCE);
		System.out.println("\t" + Utils.COMMAND_REBOOT_INSTANCE + ": reboot one or several instances");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_REBOOT_INSTANCE);
		System.out.println("\t" + Utils.COMMAND_TERMINATE_INSTANCE + ": terminate one or several instances");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_TERMINATE_INSTANCE);
		
		System.out.println("\t" + Utils.COMMAND_DESCRIBE_SUBNET + ": list all own subnets.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DESCRIBE_SUBNET);
		System.out.println("\t" + Utils.COMMAND_CREATE_SUBNET + ": create a subnet for Vpc.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_CREATE_SUBNET);
		System.out.println("\t" + Utils.COMMAND_DELETE_SUBNET + ": delete a subnet");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DELETE_SUBNET);
		
		System.out.println("\t" + Utils.COMMAND_DESCRIBE_VPC + ": list all own Vpcs.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DESCRIBE_VPC);
		System.out.println("\t" + Utils.COMMAND_CREATE_VPC + ": create a new vpc.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_CREATE_VPC);
		System.out.println("\t" + Utils.COMMAND_DELETE_VPC + ": remove a vpc.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DELETE_VPC);
	}
	
	public static void main(String args[]) throws Exception {
		AWSVPCManager.INSTANCE.run();
	}
}
