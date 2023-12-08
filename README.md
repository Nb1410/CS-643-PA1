# Image Recognition and Text Detection on AWS: Programming Assignment 1

Welcome to our guide on setting up and deploying a Java-based image recognition and text detection project using various AWS services. This project leverages the powerful AWS ecosystem to process and analyze images, demonstrating a practical application of cloud computing technologies.

## Integrated AWS Services
- **AWS EC2**: We'll be using EC2 instances to run our Java applications.
- **AWS S3**: This service acts as our image storage solution.
- **AWS Rekognition**: A core component, Rekognition will handle both the image classification and text detection tasks.
- **AWS SQS**: To ensure smooth communication between our image classification and text detection processes, we'll utilize SQS as our message queue service.

## Getting Started: A Step-by-Step Guide

### Setting Up in AWS Learner's Lab
1. Log into AWS Learner's Lab with your student credentials.
2. Follow the README file provided there for initial setup instructions and to obtain your AWS credentials.

### Configuring AWS CLI
1. Begin by installing the AWS CLI on your machine.
2. Once installed, run `aws configure` to input your AWS credentials:
   - This includes your Access Key, Secret Key, and Session Token, which you'll find in Vocareum.
   - Remember, you can set up multiple profiles for different AWS accounts if needed.

### Launching AWS EC2 Instances
1. Head over to the AWS Management Console and select "EC2" under "Services".
2. Go to "Instances" and click on "Launch Instances".
3. Choose a name for your instance, select "Amazon Linux" as the OS, and pick an instance type like "t2.micro".
4. Make sure you create or select an existing key pair for SSH access.
5. Finally, launch your instance.

### Accessing EC2 Instances via SSH
1. Once your EC2 instances are up and running, download the SSH key (e.g., CS643-Cloud.pem).
2. Open a terminal and navigate to where you've stored this key.
3. Connect to your instance using:
```
ssh -i <SecurityGroup.pem> ec2-user@<instance-IP>
```
4. Type "yes" to confirm the connection.

### Setting Up AWS SQS
1. In the AWS Console, find and open "Amazon SQS".
2. Create a new queue by clicking "Create Queue" and choose the queue type (like FIFO).
3. Name your queue, for example, "carsqs.fifo", and adjust the settings as per your requirements.

### Developing the Java Applications

#### For Image Recognition
1. Build a Java application that:
- Retrieves images from your S3 bucket.
- Classifies these images using AWS Rekognition, identifying those labeled as "Car" with over 90% confidence.
- Sends the names of these identified images to the AWS SQS message queue.
- Ends the process by sending a termination message (-1).
2. Compile and package this into a JAR file for deployment.

#### For Text Detection
1. Develop another Java application to:
- Pull and process messages from the AWS SQS queue.
- Perform text detection on the images using AWS Rekognition.
- Log the detected text in an "ImageText.txt" file, along with their respective indexes.
- Continuously monitor the queue for new messages.
2. Like the first, compile and package this app into its own JAR file.

### Deploying on EC2 Instances
1. Start by installing Java on your EC2 Instance (see below for detailed steps).
2. Transfer the JAR files to your EC2 instances (you can use the scp command).
3. Run both applications in parallel on your instances:
- For image recognition: `java -jar car-recognition-app-0.0.1-SNAPSHOT.jar`
- For text detection: `java -jar text-detection-app-0.0.1-SNAPSHOT.jar`

## Java Installation on EC2

### How to Install Oracle JDK
1. **Downloading the JDK**:
- Head to Oracle's website and download the Oracle JDK. Remember, you'll need to agree to the Oracle Technology Network License Agreement.
  ```
  wget https://download.oracle.com/java/19/archive/jdk-19.0.1_linux-x64_bin.tar.gz
  ```
- Once downloaded, extract it:
  ```
  tar -xvf jdk-19.0.1_linux-x64_bin.tar.gz
  ```
- Move the JDK to a directory like /usr/local:
  ```
  sudo mv jdk-19.0.1 /usr/local/
  ```

2. **Setting Up Environment Variables**:
- Create a new file in `/etc/profile.d/` for the JDK.
  ```
  sudo touch /etc/profile.d/oraclejdk.sh
  sudo chmod +x /etc/profile.d/oraclejdk.sh
  sudo vim /etc/profile.d/oraclejdk.sh
  ```
- Add these lines to your new file:
  ```
  export JAVA_HOME=/usr/local/jdk-19.0.1
  export PATH=$JAVA_HOME/bin:$PATH
  ```
- Apply the changes:
  ```
  source /etc/profile.d/oraclejdk.sh
  ```

3. **Verify Your Java Installation**:
- Run `java -version` to see if everything's set up correctly.

---

This README aims to guide you through each step of the project setup and deployment, offering a clear and structured approach to using AWS services for image processing tasks. If you encounter any issues or have questions, feel free to reach out for more assistance.
