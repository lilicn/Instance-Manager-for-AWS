package instance;

/**
 * InstanceApplication.java
 * operations related to instance
 * launch, start, stop, terminate, reboot
 * @version 3/31/2013
 * @author Li li
 *
 */
 
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import util.Utils;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;

public class InstanceApplication {
  // format
	private Pattern stopInstancePattern = Pattern.compile(Utils.COMMAND_STOP_INSTANCE + "(\\s+[0-9a-zA-Z\\-]+)+");
	private Pattern rebootInstancePattern = Pattern.compile(Utils.COMMAND_REBOOT_INSTANCE + "(\\s+[0-9a-zA-Z\\-]+)+");
	private Pattern terminateInstancePattern = Pattern.compile(Utils.COMMAND_TERMINATE_INSTANCE + "(\\s+[0-9a-zA-Z\\-]+)+");
	private Pattern startInstancePattern = Pattern.compile(Utils.COMMAND_START_INSTANCE + "(\\s+[0-9a-zA-Z\\-]+)+");
	private Pattern launchInstancePattern = Pattern.compile(Utils.COMMAND_LAUNCH_INSTANCE + 
			"((\\s+\\-i\\s+[0-9a-zA-Z\\-]+)?(\\s+\\-t\\s+[0-9a-zA-Z\\-.]+)?" +
			"(\\s+\\-k\\s+[0-9a-zA-Z\\-]+)?(\\s+\\-z\\s+[0-9a-zA-Z\\-]+)?" +
			"(\\s+\\-c\\s+\\d+\\s+\\d+)?)?");
	
	/**
	 * launch instances
	 * @throws Exception
	 */
	public void launch(String command) throws Exception {
		// check format
		if(! this.launchInstancePattern.matcher(command).matches()) {
			System.out.println("Input format is not correct. " + Utils.FORMAT_COMMAND_LAUNCH_INSTANCE);
			return;
		}
		
		//
		RunInstancesRequest request = new RunInstancesRequest();
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		// set default values
		request.withImageId("ami-8c1fece5");
		request.withMinCount(1);
		request.withMaxCount(1);
		request.withInstanceType(Utils.INSTANCE_TYPES[0]);
		while(st.hasMoreTokens()) {
			String tag = st.nextToken();
			String value = st.nextToken();
			if(tag.equalsIgnoreCase("-i")) {
				request.withImageId(value);
			} else if(tag.equalsIgnoreCase("-t")) {
				request.withInstanceType(value);
			} else if(tag.equalsIgnoreCase("-k")) {
				request.withKeyName(value);
			} else if(tag.equalsIgnoreCase("-z")) {
				Placement placement = new Placement();
				placement.setAvailabilityZone(value);
				request.withPlacement(placement);
			} else if(tag.equalsIgnoreCase("-c")) {
				int value1 = Integer.parseInt(value);
				int value2 = Integer.parseInt(st.nextToken());
				value1 = value1 < 1 ? 1 : value1;
				value2 = value2 < 1 ? 1 : value2;
				request.withMinCount(Math.min(value1, value2));
				request.withMaxCount(Math.max(value1, value2));
			}
		}
		
		// -i ami -t instanceType -k keyName -z zone -c minCount maxCount 
		RunInstancesResult result = AWSInstanceManager.ec2.runInstances(request);
		Utils.printInstances(result.getReservation().getInstances());
	}
	
	/**
	 * process a start command
	 * @param command
	 */
	public void start(String command) {
		// check format
		if(!this.startInstancePattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_START_INSTANCE);
			return;
		}
		// read user typed instance id
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		ArrayList<String> instanceList = new ArrayList<String>();
		while(st.hasMoreTokens()) {
			instanceList.add(st.nextToken());
		}
		// send out request
		StartInstancesRequest request = new StartInstancesRequest();
		request.withInstanceIds(instanceList);
		StartInstancesResult result = AWSInstanceManager.ec2.startInstances(request);
		Utils.printInstanceStateChanges(result.getStartingInstances());
	}

	/**
	 * process a stop command
	 * @param command
	 */
	public void stop(String command) {
		// check format
		if(!this.stopInstancePattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_STOP_INSTANCE);
			return;
		}
		// read user typed instance id
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		ArrayList<String> instanceList = new ArrayList<String>();
		while(st.hasMoreTokens()) {
			instanceList.add(st.nextToken());
		}
		// send out request
		StopInstancesRequest request = new StopInstancesRequest();
		request.withInstanceIds(instanceList);
		StopInstancesResult result = AWSInstanceManager.ec2.stopInstances(request);
		Utils.printInstanceStateChanges(result.getStoppingInstances());
	}
	
	/**
	 * process a reboot command
	 * @param command
	 */
	public void reboot(String command) {
		// check format
		if(!this.rebootInstancePattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_REBOOT_INSTANCE);
			return;
		}
		// read user typed instance id
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		ArrayList<String> instanceList = new ArrayList<String>();
		while(st.hasMoreTokens()) {
			instanceList.add(st.nextToken());
		}
		// send out request
		RebootInstancesRequest request = new RebootInstancesRequest();
		request.withInstanceIds(instanceList);
		AWSInstanceManager.ec2.rebootInstances(request);
		//System.out.println("Reboot command has been sent out. This command is asynchronous.");
	}
	
	/**
	 * process a terminate command
	 */
	public void terminate(String command) {
		// check format
		if(!this.terminateInstancePattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_TERMINATE_INSTANCE);
			return;
		}
		// read user typed instance id
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		ArrayList<String> instanceList = new ArrayList<String>();
		while(st.hasMoreTokens()) {
			instanceList.add(st.nextToken());
		}
		// send out request
		TerminateInstancesRequest request = new TerminateInstancesRequest();
		request.withInstanceIds(instanceList);
		TerminateInstancesResult result = AWSInstanceManager.ec2.terminateInstances(request);
		Utils.printInstanceStateChanges(result.getTerminatingInstances());
	}
	
	/**
	 * list all instance types
	 */
	public void describeInstanceTypes() {
		for(String type : Utils.INSTANCE_TYPES) {
			System.out.println(type);
		}
	}
	
	/**
	 * print out all instances belongs to current user
	 */
	public void describeInstances() {
		List<Instance> list = new ArrayList<Instance>();
		for(Reservation r : AWSInstanceManager.ec2.describeInstances().getReservations()) {
			list.addAll(r.getInstances());
		}
		Utils.printInstances(list);
	}
	
}
