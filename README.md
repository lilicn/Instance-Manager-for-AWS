Introduction
============

The whole code contains three packages: instance, vpc and util. 
- The instance package, which consists of six classes, is responsible for the instance manager. 
- The vpc package including six classes is responsible for the VPC manager. Each class is related to certain operations of one object, such as instance, address, and subnet and so on. 
- The Utils class in the util package, which contains only static variables and functions, is responsible for global configuration and utility function.

Environment configuration
-------------------------

- Install Eclipse IDE for Java EE Developers and install AWS SDK for Java in Eclipse.
- Create an account in Amazon AWS, go to MyAccount/SecurityCredentials and create Access Key.
- Go to the preference of your Eclipse, click AWS Toolkit, and copy and paste the information of your Access Key.

Specific operation
------------------

- By running AWSInstanceManager.java, we can launch, start, stop and terminate one or multiple instances.
- By running AWSVPCManager.java, we can create and delete VPC; create and delete subnet; launch, start, stop, reboot, and terminate instance in a specific subnet; create, attach, detach and delete gateway; allocate, associate, disassociate and release address.
- By typing describe plus the object such as instance, vpc and so on, we can see the current state of the object. By typing help, we can see all supported commands and format. Exit command is to exit the manager.


Help
----
If you want to make your own instance manage for aws, please check the following url:
http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/welcome.html
