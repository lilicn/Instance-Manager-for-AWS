package vpc;
/**
 * AddressApplication.java
 * operations related to Address
 * allocate release, associate, disassociate
 * @version 3/31/2013
 * @author Li Li
 *
 */
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import util.Utils;

import com.amazonaws.services.ec2.model.Address;
import com.amazonaws.services.ec2.model.AllocateAddressRequest;
import com.amazonaws.services.ec2.model.AllocateAddressResult;
import com.amazonaws.services.ec2.model.AssociateAddressRequest;
import com.amazonaws.services.ec2.model.DisassociateAddressRequest;
import com.amazonaws.services.ec2.model.DomainType;
import com.amazonaws.services.ec2.model.ReleaseAddressRequest;

public class AddressApplication {
  // format
	private Pattern associateAddressPattern = Pattern.compile(Utils.COMMAND_ASSOCIATE_ADDRESS + "\\s+\\-a\\s+[0-9a-zA-Z\\-.]+\\s+\\-i\\s+[0-9a-zA-Z\\-]+");
	private Pattern releaseAddressPattern = Pattern.compile(Utils.COMMAND_RELEASE_ADDRESS + "\\s+[0-9a-zA-Z\\-.]+");
	private Pattern disassociateAddressPattern = Pattern.compile(Utils.COMMAND_DISASSOCIATE_ADDRESS + "\\s+[0-9a-zA-Z\\-.]+");
	
	/**
	 * list add allocated ips
	 */
	public void describeAddress() {
		System.out.println("AllocationID\tAssociateID\tPublicIP\tInstanceID");
		for(Address addr : AWSVPCManager.ec2.describeAddresses().getAddresses()) {
			System.out.println(addr.getAllocationId() + "\t" + addr.getAssociationId() + "\t" + addr.getPublicIp() + "\t" + addr.getInstanceId());
		}
	}
	
	/**
	 * allocate a new public ip
	 */
	public void allocateAddress() {
//		AllocateAddressResult result = AWSVPCManager.ec2.allocateAddress();
		AllocateAddressRequest request = new AllocateAddressRequest();
		request.withDomain(DomainType.Vpc);
		AllocateAddressResult result = AWSVPCManager.ec2.allocateAddress(request);
		System.out.println("ID\tPublicIP");
		System.out.println(result.getAllocationId() + "\t" + result.getPublicIp());
	}
	
	/**
	 * release an associated address
	 */
	public void releaseAddress(String command) {
		// check format 
		if(! this.releaseAddressPattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_RELEASE_ADDRESS);
			return;
		}
		// get input
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String value = st.nextToken();
		ReleaseAddressRequest request = new ReleaseAddressRequest();
		request.withAllocationId(value);
		AWSVPCManager.ec2.releaseAddress(request);
	}
	
	/**
	 * associate an address to a given instance
	 * @param command
	 */
	public void associateAddress(String command) {
		// check format
		if(! this.associateAddressPattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_ASSOCIATE_ADDRESS);
			return;
		}
		// get inputs
		String instanceID = "";
		String addressID = "";
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		while(st.hasMoreTokens()) {
			String tag = st.nextToken();
			if(tag.equalsIgnoreCase("-a")) {
				addressID = st.nextToken();
			} else if(tag.equalsIgnoreCase("-i")) {
				instanceID = st.nextToken();
			}
		}
		// send out request
		AssociateAddressRequest request = new AssociateAddressRequest();
		request.withInstanceId(instanceID);
		request.withAllocationId(addressID);
		AWSVPCManager.ec2.associateAddress(request);
	}

	/**
	 * disassociate address
	 * @param command
	 */
	public void disassociateAddress(String command) {
		// check format
		if(! this.disassociateAddressPattern.matcher(command).matches()) {
			System.out.println(Utils.WRONG_COMMAND_FORMAT + " " + Utils.FORMAT_COMMAND_ASSOCIATE_ADDRESS);
			return;
		}
		// get inputs
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String value = st.nextToken();
		
		DisassociateAddressRequest request = new DisassociateAddressRequest();
//		if(value.contains(".")) {
//			request.withPublicIp(value);
//		} else {
			request.withAssociationId(value);
//		}
		AWSVPCManager.ec2.disassociateAddress(request);
	}
}
