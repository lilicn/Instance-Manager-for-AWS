package instance;

/**
 * ZoneApplication.java
 * operations related to Zone
 * @version 3/31/2013
 * @author Li li
 *
 */
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;

public class ZoneApplication {
    /**
	 * list all zone information
	 */
	public void describeZones() {
		DescribeAvailabilityZonesResult availabilityZonesResult = AWSInstanceManager.ec2.describeAvailabilityZones();
		for(AvailabilityZone zone : availabilityZonesResult.getAvailabilityZones()) {
			System.out.println(zone.getZoneName());
		}
	}
}
