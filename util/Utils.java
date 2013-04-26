package util;
/**
 * Utils.java
 * Global configuration and utiliy function
 * @version 3/31/2013
 * @author Li Li
 *
 */
import java.io.BufferedReader;
import java.util.List;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateChange;

public class Utils {
  // configuration
	public final static String COMMAND_AVAILABLE_ZONE = "describe-zone";
	public final static String COMMAND_AVAILABLE_INSTANCE_TYPE = "describe-instance-type";
	public final static String COMMAND_AVAILABLE_KEY = "describe-key";
	public final static String COMMAND_AVAILABLE_SECURITY_GROUP = "describe-security-group";
	public final static String COMMAND_AVAILABLE_AMI = "describe-ami";
	// instance operations
	public final static String COMMAND_LAUNCH_INSTANCE = "launch-instance";
	public final static String COMMAND_START_INSTANCE = "start-instance";
	public final static String COMMAND_STOP_INSTANCE = "stop-instance";
	public final static String COMMAND_REBOOT_INSTANCE = "reboot-instance";
	public final static String COMMAND_TERMINATE_INSTANCE = "terminate-instance";
	public final static String COMMAND_DESCRIBE_INSTANCE = "describe-instance";
	// exit
	public final static String COMMAND_EXIT = "exit";
	// instance types
	public final static String INSTANCE_TYPES[] = {"t1.micro", "m1.small", "m1.medium", "m1.large", 
			"m1.xlarge", "m2.xlarge", "m2.2xlarge", "m2.4xlarge", "m3.xlarge", "m3.2xlarge", 
			"c1.medium", "c1.xlarge", "hi1.4xlarge", "hs1.8xlarge", "cc1.4xlarge", 
			"cc2.8xlarge", "cg1.4xlarge"};
	// vpc operations
	public final static String COMMAND_CREATE_VPC = "create-vpc";
	public final static String COMMAND_DELETE_VPC = "delete-vpc";
	public final static String COMMAND_DESCRIBE_VPC = "describe-vpc";
	// subnet operations 
	public final static String COMMAND_DESCRIBE_SUBNET = "describe-subnet";
	public final static String COMMAND_CREATE_SUBNET = "create-subnet";
	public final static String COMMAND_DELETE_SUBNET = "delete-subnet";
	// address/ip operations
	public final static String COMMAND_DESCRIBE_ADDRESS = "describe-address";
	public final static String COMMAND_ALLOCATE_ADDRESS = "allocate-address";
	public final static String COMMAND_RELEASE_ADDRESS = "release-address";
	public final static String COMMAND_ASSOCIATE_ADDRESS = "associate-address";
	public final static String COMMAND_DISASSOCIATE_ADDRESS = "disassociate-address";
	// internet gateway
	public final static String COMMAND_CREATE_GATEWAY = "create-gateway";
	public final static String COMMAND_DESCRIBE_GATEWAY = "describe-gateway";
	public final static String COMMAND_DELETE_GATEWAY = "delete-gateway";
	public final static String COMMAND_ATTACH_GATEWAY = "attach-gateway";
	public final static String COMMAND_DETACH_GATEWAY = "detach-gateway";
	// command format Instance
	public final static String FORMAT_COMMAND_DESCRIBE_ZONE = Utils.COMMAND_AVAILABLE_ZONE;
	public final static String FORMAT_COMMAND_DESCRIBE_KEY = Utils.COMMAND_AVAILABLE_KEY;
	public final static String FORMAT_COMMAND_DESCRIBE_AMI = Utils.COMMAND_AVAILABLE_AMI;
	public final static String FORMAT_COMMAND_DESCRIBE_SECURITY_GROUP = Utils.COMMAND_AVAILABLE_SECURITY_GROUP;
	public final static String FORMAT_COMMAND_DESCRIBE_INSTANCE_TYPE = Utils.COMMAND_AVAILABLE_INSTANCE_TYPE;
	public final static String FORMAT_COMMAND_DESCRIBE_INSTANCE = Utils.COMMAND_DESCRIBE_INSTANCE;
	public final static String FORMAT_COMMAND_LAUNCH_INSTANCE = Utils.COMMAND_LAUNCH_INSTANCE + " [-i ami] [-t instanceType] [-k keyName] [-z zone] [-c minCount maxCount]";
	public final static String FORMAT_COMMAND_START_INSTANCE = Utils.COMMAND_START_INSTANCE + " instanceID1 instanceID2 ...";
	public final static String FORMAT_COMMAND_STOP_INSTANCE = Utils.COMMAND_STOP_INSTANCE + " instanceID1 instanceID2 ...";
	public final static String FORMAT_COMMAND_REBOOT_INSTANCE = Utils.COMMAND_REBOOT_INSTANCE + " instanceID1 instanceID2 ...";
	public final static String FORMAT_COMMAND_TERMINATE_INSTANCE = Utils.COMMAND_TERMINATE_INSTANCE + " instanceID1 instanceID2 ...";
	
	// command format VPC
	public final static String FORMAT_COMMAND_CREATE_GATEWAY = Utils.COMMAND_CREATE_GATEWAY;
	public final static String FORMAT_COMMAND_DELETE_GATEWAY = Utils.COMMAND_DELETE_GATEWAY + " gatewayID";
	public final static String FORMAT_COMMAND_DESCRIBE_GATEWAY = Utils.COMMAND_DESCRIBE_GATEWAY;
	public final static String FORMAT_COMMAND_ATTACH_GATEWAY = Utils.COMMAND_ATTACH_GATEWAY + " -g gatewayID -v VPCID";
	public final static String FORMAT_COMMAND_DETACH_GATEWAY = Utils.COMMAND_DETACH_GATEWAY + " -g gatewayID -v VPCID";
	
	public final static String FORMAT_COMMAND_ALLOCATE_ADDRESS = Utils.COMMAND_ALLOCATE_ADDRESS;
	public final static String FORMAT_COMMAND_DESCRIBE_ADDRESS = Utils.COMMAND_DESCRIBE_ADDRESS;
	public final static String FORMAT_COMMAND_ASSOCIATE_ADDRESS = Utils.COMMAND_ASSOCIATE_ADDRESS + " -a addressID -i instanceID";
	public final static String FORMAT_COMMAND_DISASSOCIATE_ADDRESS = Utils.COMMAND_DISASSOCIATE_ADDRESS + " associateID";
	public final static String FORMAT_COMMAND_RELEASE_ADDRESS = Utils.COMMAND_RELEASE_ADDRESS + " allocationID";
	
	public final static String FORMAT_COMMAND_LAUNCH_INSTANCE_VPC = Utils.COMMAND_LAUNCH_INSTANCE + " -s subnetID [-c minCount maxCount]";
	
	public final static String FORMAT_COMMAND_DESCRIBE_SUBNET = Utils.COMMAND_DESCRIBE_SUBNET;
	public final static String FORMAT_COMMAND_CREATE_SUBNET = Utils.COMMAND_CREATE_SUBNET + " -v VpcID [-c CIDRBlock]";
	public final static String FORMAT_COMMAND_DELETE_SUBNET = Utils.COMMAND_DELETE_SUBNET + " subnetID";
	
	public final static String FORMAT_COMMAND_DESCRIBE_VPC = Utils.COMMAND_DESCRIBE_VPC;
	public final static String FORMAT_COMMAND_CREATE_VPC = Utils.COMMAND_CREATE_VPC + " [-c CIDRBlock]";
	public final static String FORMAT_COMMAND_DELETE_VPC = Utils.COMMAND_DELETE_VPC + " VpcID";
	
	// others
	public final static String WRONG_COMMAND_FORMAT = "Input format is not correct.";
	
	
	
	/**
	 * return -1 if exception happended
	 * @param line
	 * @return
	 */
	public static int handleUseInputNumber(String line) {
		try {
			return Integer.parseInt(line);
		} catch(Exception e) {
			return -1;
		}
	}
	
	public static int letUserSelect(String title, String values[], BufferedReader br) throws Exception {
		// print out options
		System.out.println(title);
		for(int i = 0; i < values.length; i ++) {
			System.out.print((i+1) + "-" + values[i] + " ");
		}
		System.out.println();
		// read user input
		int input = Utils.handleUseInputNumber(br.readLine());
		
		
		return input;
	}
	
	/**
	 * print out information of a given instance list
	 * @param list
	 */
	public static void printInstances(List<Instance> list) {
		System.out.println("InstanceID\tType\tState");
		if(list != null && list.size() > 0) {
			for(Instance instance : list) {
				System.out.println(instance.getInstanceId() + "\t" + instance.getInstanceType() + "\t" + instance.getState().getName());
			}
		}
	}
	/**
	 * print out the state changes of a given instance list
	 * @param list
	 */
	public static void printInstanceStateChanges(List<InstanceStateChange> list) {
		System.out.println("InstanceID\tPreState\tCurState");
		if(list != null && list.size() > 0) {
			for(InstanceStateChange instance : list) {
				System.out.println(instance.getInstanceId() + "\t" + instance.getPreviousState().getName() + "\t" + instance.getCurrentState().getName());
			}
		}
	}
}
