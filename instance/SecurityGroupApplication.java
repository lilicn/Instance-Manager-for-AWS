package instance;

/**
 * SecurityGroupApplication.java
 * operations related to security group
 * @version 3/31/2013
 * @author Li li
 *
 */
 
import com.amazonaws.services.ec2.model.SecurityGroup;

public class SecurityGroupApplication {
    /**
	 * list all security groups
	 */
	public void describeSecurityGroups() {
		for(SecurityGroup group : AWSInstanceManager.ec2.describeSecurityGroups().getSecurityGroups()) {
			System.out.println(group.getGroupName());
		}
	}
}
