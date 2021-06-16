package sampleclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Calendar;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.avaya.sdk.PAMSocketInfo;
import com.avaya.sdk.POMAgentFactory;
import com.avaya.sdk.Agent.POMAgent;
import com.avaya.sdk.Data.POMAgentSkill;

/**
 *  This is main class for Sample Desktop Java API implementation. User needs to import 
 *  SamplePOMDesktopJavaAPI project into eclipse and follow below steps to run the sample
 *  application,
 *  1. Make sure you have run the preview/Predictive/Progressive campaign on POM system. 
 *  2. Right click on TestSDK.java and go to Run As ( Run Configurations )
 *  3. Go to Arguments tab and provide arguments in below sequence,
 *  	<POM IP><POM SDK Port><trustStore file><truststore password ><Agent Id> <agentExtension> <agentPassword> <fipsMode 0 or 1>< Agent Skill> where 
 *  	Agent Skill should have skill name, skill Id and skill level separated by 
 *  	Semicolon (;) e.g CreditCard;55;1
 *      10.133.36.122 9970 "FipsTrustStore" changeit 720056 710056 1234 1 "CreditCard";12;1
 *  `	Note : Skill of the agent should match with the skill provided in Campaign Strategy 
 *  	for the campaign run in step one.
 *  	
 *  	Create truststore in project base directory and import the POM server certificate on which 
 *  	Agent Manager service for the Default zone is running. Provide truststore file name and password 
 *  	in the command line argument.
 *  	
 *  	e.g 127.0.0.1 9970 pomTrustStore password 20000 CreditCard;1;1
 *  
 *   4.  Click on Run to run the project.
 *   
 *   After project is run it will perform below operations,
 *    1. Initialize SDK Library with logger object which implements ILogger interface.
 *    2. Create Instance of POMAgent for the Agent Id.
 *    3. Login the Agent
 *    4. Change Agent State to Ready.
 *    5. After Agent State change response is received send AGTAvailableForNailup to POM.
 *    6. After Nail up change is received from POM send AGTReadyForNailup.
 *    7. POM will Nailed agent and attached to  the Job.
 *    8. Send AGTGetCustomerDetails after AGTCallNotify is received from POM. 
 *    9. If preview campaign is run then send AGTPreviewDial.
 *    10.After call is answered agent move to taking state. After 30 sec send AGTReleaseLine
 *    11.Move agent to Not ready State ( Pending Not Ready ) 
 *    12.Warp up contact by calling AGTWrapupContact after 30 sec of AGTReleaseLine response is received from POM.
 *    13.Log off agent by calling AGTLogoff.
 *    14.After AGTLogoffRESP response  is received form POM remove agent from SDK library.
 *    15.DeInititalize SDK library.
 *    
 *    Logger creates SDK log file in current project directory with name POM_SDK.log file.
 */

public class TestSDK
{
	
	private static  String host = "127.0.0.1";
			
	private static  int port = 9970;
	
	private static POMAgentSkill[] skills = new POMAgentSkill[1];
	
	private static String agentId; 
	
	private static String agentExtension;
	
	private static String agentPassword;
	
	private static boolean exit = false;
	
	private static long callTime = 30000; // 30sec

	private static long wrapTime = 30000; // 15 sec
	
	private static String trustStore = null ; 
	private static String password = null;
	
	private static int fipsMode = 0;
	public static void main(String args[])
	{
		try
		{			
			
			/* Create PAMSocketInfo object which is required in init() */ 
			if( args.length >= 8 )
			{
				
				host = args[0];
				port = Integer.parseInt(args[1]);
				PAMSocketInfo pamSktInfo = new PAMSocketInfo();
				pamSktInfo.port = port;
				pamSktInfo.ipAddress = host;
				
				PAMSocketInfo [] pamSktInfoArry = new PAMSocketInfo[1];
				pamSktInfoArry[0] = pamSktInfo;
				
				trustStore = args[2].trim();
				password = args[3].trim();
				agentId = args[4];				
				agentExtension = args[5];
				agentPassword = args[6];
				fipsMode = Integer.parseInt(args[7]);
				/* For AACC, AACC sends Skill along with AGTLogon command so setting Skill information. */
				if (args.length == 9)
				{
					String[] skill = args[8].split(";");
										
					POMAgentSkill newSkill = new POMAgentSkill();
					
					if( skill.length == 1 )
					{
						
						newSkill.setname(skill[0]);
						newSkill.setid(skill[0]);
						
					}else if( skill.length == 2)
					{
						newSkill.setname(skill[0]);
						newSkill.setid(skill[1]);
						
					}else if(skill.length == 3)
					{
						newSkill.setname(skill[0]);
						newSkill.setid(skill[1]);					
						newSkill.setlevel(skill[2]);
					}
										
					skills[0] = newSkill;
														
				}
				
				
				try 
				{
					/* Initialize SDK Library	*/				
					if( POMAgentFactory.init(pamSktInfoArry, ILoggerImpl.getTracer(), getSSLContext()) ) 
					{
					
						/* Create object  of class which implements POMAgentHandlerInterface */
						SDKWorker worker = new SDKWorker( agentId);
					
						/* Get POMAgent Object for Agent */
						POMAgent pomAgtObj = POMAgentFactory.getPOMAgent(agentId, worker);
					
						worker.setPomAgtObj(pomAgtObj);		
					
					
						if ( args.length == 10 )
						{
							pomAgtObj.AGTLogon(agentExtension, agentPassword, true, "en-US", "IST", "Default", "Agent1", skills);
												
						}else
						{
							pomAgtObj.AGTLogon(agentExtension, agentPassword, true, "en-US", "IST", "Default");
						}
										
						boolean releaseLineCalled  = false;
						boolean wrapUpCalled = false;
					 
						while( exit == false )
						{
						
							if( worker.getCallStartTime() != null  && releaseLineCalled == false )
							{
								if( ( Calendar.getInstance().getTimeInMillis() - worker.getCallStartTime().getTime()) > callTime )
								{
									System.out.println("Release call.");
									pomAgtObj.AGTReleaseLine(worker.getSessionID());
									releaseLineCalled= true;
								}
							}
						
							if( worker.getWrapUpTime() != null  && wrapUpCalled == false)
							{
								if( ( Calendar.getInstance().getTimeInMillis() - worker.getWrapUpTime().getTime()) > wrapTime )
								{
									System.out.println("Wrap up the call. ");
									pomAgtObj.AGTWrapupContact(worker.getPOMCompletionCode(), worker.getSessionID());
									wrapUpCalled = true;
								}
							}
						
							Thread.sleep(1000);
						}
					
						/* Remove Agent from Library after AGTLogoff is proce4ssed. */	
						POMAgentFactory.removePOMAgent(agentId);
						
						/* 	Cleanup SDK Library */			
						POMAgentFactory.deinit();
						
					}else
					{
						System.out.println("POMAgentFactory initialization failed.");
					}
					
				} catch (Exception e)
				{
					
					e.printStackTrace();
				}
				
												
				System.out.println("Exit");
														
			}else
			{
				System.out.println("Invalid Parameters. ");
			}
			
			
																			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void exitThread()
	{
		exit= true;
	}
	
	/**
	 * Gets you SLLContext object which looks for certificates to validate in trustStore provided in the argument.	 
	 * 
	 * @return SLLContext object which looks for certificates to validate in trustStore provided in the argument
	 */
	private static SSLContext getSSLContext()
	{
		FileInputStream stream = null;
		try
		{
			System.out.println("Received cert File path: " + trustStore);
			char[] passphrase = password.toCharArray();
			KeyStore ks = null;
			SecureRandom secureRandom = null;
			if (fipsMode == 1)
			{
				System.out.println("Arguments provided in FIPS mode: ");
				secureRandom = SecureRandom.getInstance("DEFAULT", "BCFIPS");
				ks = KeyStore.getInstance("BCFKS", "BCFIPS");
			} else
			{
				System.out.println("Running in normal mode: ");
				ks = KeyStore.getInstance("JKS");
				//				secureRandom = SecureRandom.getInstance("DEFAULT");
			}
			stream = new FileInputStream(trustStore);
			ks.load(stream, passphrase);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, passphrase);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(ks);
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			stream.close();
			return sslContext;
		} catch (Exception e)
		{
			System.out.println("Exception in getSSLContext.");
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (stream != null)
					stream.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	
}