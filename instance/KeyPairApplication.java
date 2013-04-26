package instance;

/**
 * KeyPairApplication.java
 * operations related to key pair
 * @version 3/31/2013
 * @author Li li
 *
 */
import com.amazonaws.services.ec2.model.KeyPairInfo;

public class KeyPairApplication {
    /**
	 * list all key-pair names
	 */
	public void describeKeyPairs() {
		for(KeyPairInfo key : AWSInstanceManager.ec2.describeKeyPairs().getKeyPairs()) {
			System.out.println(key.getKeyName());
		}
	}
}
