package instance;
/**
 * AWSInstanceManager.java
 * AWS instance manager
 * @version 3/31/2013
 * @author Li li
 *
 */
 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import util.Utils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.KeyPairInfo;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;

public class AWSInstanceManager {
    // the EC2
	public static AmazonEC2 ec2 = null;
	// singleton
	public static AWSInstanceManager INSTANCE = new AWSInstanceManager();
	// applications
	private AMIApplication amiApp = new AMIApplication();
	private InstanceApplication instanceApp = new InstanceApplication();
	private KeyPairApplication keyPairApp = new KeyPairApplication();
	private SecurityGroupApplication securityApp = new SecurityGroupApplication();
	private ZoneApplication zoneApp = new ZoneApplication();
	
	private AWSInstanceManager() {}	
	/**
	 * launch AWSInstanceManager
	 */
	public void run() {
		// connect to AWS
		AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
		ec2 = new AmazonEC2Client(credentialsProvider);
		// set default service end point to us-east-1
		ec2.setEndpoint("ec2.us-east-1.amazonaws.com");
		
		System.out.println("******************** AWS Instance Manager ********************");
		System.out.println("provide operations for AWS instances");
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
				} else if(command.startsWith(Utils.COMMAND_AVAILABLE_ZONE)) {
					this.zoneApp.describeZones();
				} else if(command.startsWith(Utils.COMMAND_AVAILABLE_SECURITY_GROUP)) {
					this.securityApp.describeSecurityGroups();
				} else if(command.startsWith(Utils.COMMAND_AVAILABLE_INSTANCE_TYPE)) {
					this.instanceApp.describeInstanceTypes();
				} else if(command.startsWith(Utils.COMMAND_AVAILABLE_KEY)) {
					this.keyPairApp.describeKeyPairs();
				} else if(command.startsWith(Utils.COMMAND_AVAILABLE_AMI)) {
					this.amiApp.describeAMIs();
				} else if(command.startsWith(Utils.COMMAND_LAUNCH_INSTANCE)) {
					this.instanceApp.launch(command);
				} else if(command.startsWith(Utils.COMMAND_REBOOT_INSTANCE)) {
					this.instanceApp.reboot(command);
				} else if(command.startsWith(Utils.COMMAND_START_INSTANCE)) {
					this.instanceApp.start(command);
				} else if(command.startsWith(Utils.COMMAND_STOP_INSTANCE)) {
					this.instanceApp.stop(command);
				} else if(command.startsWith(Utils.COMMAND_TERMINATE_INSTANCE)) {
					this.instanceApp.terminate(command);
				} else if(command.startsWith(Utils.COMMAND_DESCRIBE_INSTANCE)) {
					this.instanceApp.describeInstances();
				} else if(command.startsWith("help")) {
					help();
				} else {
					System.out.println("Cannot figure out your typed command. Type \"help\" if you need any help.");
				}
			} catch(Exception e) {
				System.out.println("Exception happened. " + e.getMessage());
			}
		}
		System.out.println("AWS Instance Manager has been shutted down.");
	}
	
	public static void main(String args[]) throws Exception {
		AWSInstanceManager.INSTANCE.run();
	}
	
	/**
	 * print out all help information
	 */
	public void help() {
		// several commands
		System.out.println("Here are all commands supported.");
		System.out.println("\t" + Utils.COMMAND_AVAILABLE_ZONE + ": describe all available zones.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DESCRIBE_ZONE);
		System.out.println("\t" + Utils.COMMAND_AVAILABLE_INSTANCE_TYPE + ": describe all available instance types.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DESCRIBE_INSTANCE_TYPE);
		System.out.println("\t" + Utils.COMMAND_AVAILABLE_KEY + ": describe all available key pairs.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DESCRIBE_KEY);
		System.out.println("\t" + Utils.COMMAND_AVAILABLE_SECURITY_GROUP + ": describe all available security groups.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DESCRIBE_SECURITY_GROUP);
		System.out.println("\t" + Utils.COMMAND_AVAILABLE_AMI + ": describe all available AMIs.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DESCRIBE_AMI);
		System.out.println("\t" + Utils.COMMAND_DESCRIBE_INSTANCE + ": describe all running instances.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_DESCRIBE_INSTANCE);
		System.out.println("\t" + Utils.COMMAND_LAUNCH_INSTANCE + ": launch one or several instances.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_LAUNCH_INSTANCE);
		System.out.println("\t" + Utils.COMMAND_START_INSTANCE + ": start one or several instances.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_START_INSTANCE);
		System.out.println("\t" + Utils.COMMAND_STOP_INSTANCE + ": stop one or several instances.");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_STOP_INSTANCE);
		System.out.println("\t" + Utils.COMMAND_REBOOT_INSTANCE + ": reboot one or several instances");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_REBOOT_INSTANCE);
		System.out.println("\t" + Utils.COMMAND_TERMINATE_INSTANCE + ": terminate one or several instances");
		System.out.println("\t\t" + Utils.FORMAT_COMMAND_TERMINATE_INSTANCE);
	}
}
