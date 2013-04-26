package instance;
/**
 * AMIApplication.java
 * operations related to AMI
 * @version 3/31/2013
 * @author Li Li
 *
 */
import com.amazonaws.services.ec2.model.Image;
public class AMIApplication {
    /**
	 * list 20 amis
	 */
	public void describeAMIs() {
		int count = 20;
		for(Image ami : AWSInstanceManager.ec2.describeImages().getImages()) {
			System.out.println(ami.getImageId() + "\t" + ami.getName());
			if(count --  == 0) {
				break;
			}
		}
	}
}
