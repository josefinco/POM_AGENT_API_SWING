package sampleclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Calendar;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JTextArea;

import com.avaya.sdk.PAMSocketInfo;
import com.avaya.sdk.POMAgentFactory;
import com.avaya.sdk.Agent.POMAgent;
import com.avaya.sdk.Data.POMAgentSkill;

public class Controller {

	private static String host = "127.0.0.1";

	private static int port = 9970;

	private static POMAgentSkill[] skills = new POMAgentSkill[1];

	private static String agentId;

	private static String agentExtension;

	private static String agentPassword;

	private static boolean exit = false;

	private static long callTime = 30000; // 30sec

	private static long wrapTime = 30000; // 15 sec

	private static String trustStore = null;
	private static String password = null;

	private static int fipsMode = 0;
	private static POMAgentSkill newSkill = new POMAgentSkill();

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		Controller.host = host;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Controller.port = port;
	}

	public static POMAgentSkill[] getSkills() {
		return skills;
	}

	public static void setSkills(POMAgentSkill[] skills) {
		Controller.skills = skills;
	}

	public static String getAgentId() {
		return agentId;
	}

	public static void setAgentId(String agentId) {
		Controller.agentId = agentId;
	}

	public static String getAgentExtension() {
		return agentExtension;
	}

	public static void setAgentExtension(String agentExtension) {
		Controller.agentExtension = agentExtension;
	}

	public static String getAgentPassword() {
		return agentPassword;
	}

	public static void setAgentPassword(String agentPassword) {
		Controller.agentPassword = agentPassword;
	}

	public static boolean isExit() {
		return exit;
	}

	public static void setExit(boolean exit) {
		Controller.exit = exit;
	}

	public static long getCallTime() {
		return callTime;
	}

	public static void setCallTime(long callTime) {
		Controller.callTime = callTime;
	}

	public static long getWrapTime() {
		return wrapTime;
	}

	public static void setWrapTime(long wrapTime) {
		Controller.wrapTime = wrapTime;
	}

	public static String getTrustStore() {
		return trustStore;
	}

	public static void setTrustStore(String trustStore) {
		Controller.trustStore = trustStore;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		Controller.password = password;
	}

	public static int getFipsMode() {
		return fipsMode;
	}

	public static void setFipsMode(int fipsMode) {
		Controller.fipsMode = fipsMode;
	}

	public static void setSkills(String[] skarray) throws Exception {
		newSkill.setname(skarray[0]);
		newSkill.setid(skarray[1]);
		newSkill.setlevel(skarray[2]);
		skills[0] = newSkill;

	}

	public static void login() {
		TestSDK_WBuilder.textArea_logs.append("\nteste");
		

		try {

			PAMSocketInfo pamSktInfo = new PAMSocketInfo();
			pamSktInfo.port = port;
			pamSktInfo.ipAddress = host;

			PAMSocketInfo[] pamSktInfoArry = new PAMSocketInfo[1];
			pamSktInfoArry[0] = pamSktInfo;

			try {
				/* Initialize SDK Library */
				if (POMAgentFactory.init(pamSktInfoArry, ILoggerImpl.getTracer(), getSSLContext())) {

					/* Create object of class which implements POMAgentHandlerInterface */
					SDKWorker worker = new SDKWorker(agentId);

					/* Get POMAgent Object for Agent */
					POMAgent pomAgtObj = POMAgentFactory.getPOMAgent(agentId, worker);

					worker.setPomAgtObj(pomAgtObj);
					pomAgtObj.AGTLogon(agentExtension, agentPassword, true, "en-US", "IST", "Default");

					boolean releaseLineCalled = false;
					boolean wrapUpCalled = false;

					while (exit == false) {

						if (worker.getCallStartTime() != null && releaseLineCalled == false) {
							if ((Calendar.getInstance().getTimeInMillis()
									- worker.getCallStartTime().getTime()) > callTime) {
								TestSDK_WBuilder.textArea_logs.append("\nRelease call.");
								pomAgtObj.AGTReleaseLine(worker.getSessionID());
								releaseLineCalled = true;
							}
						}

						if (worker.getWrapUpTime() != null && wrapUpCalled == false) {
							if ((Calendar.getInstance().getTimeInMillis()
									- worker.getWrapUpTime().getTime()) > wrapTime) {
								TestSDK_WBuilder.textArea_logs.append("\nWrap up the call. ");
								pomAgtObj.AGTWrapupContact(worker.getPOMCompletionCode(), worker.getSessionID());
								wrapUpCalled = true;
							}
						}

						Thread.sleep(1000);
					}

					/* Remove Agent from Library after AGTLogoff is proce4ssed. */
					POMAgentFactory.removePOMAgent(agentId);

					/* Cleanup SDK Library */
					POMAgentFactory.deinit();

				} else {
					TestSDK_WBuilder.textArea_logs.append("\nPOMAgentFactory initialization failed.");
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

			TestSDK_WBuilder.textArea_logs.append("\nExit");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void exitThread() {
		exit = true;
	}

	private static SSLContext getSSLContext() {
		FileInputStream stream = null;
		try {
			
			TestSDK_WBuilder.textArea_logs.append("\nReceived cert File path: " + trustStore);
			char[] passphrase = password.toCharArray();
			KeyStore ks = null;
			SecureRandom secureRandom = null;
			if (fipsMode == 1) {
				TestSDK_WBuilder.textArea_logs.append("\nArguments provided in FIPS mode: ");
				secureRandom = SecureRandom.getInstance("DEFAULT", "BCFIPS");
				ks = KeyStore.getInstance("BCFKS", "BCFIPS");
			} else {
				TestSDK_WBuilder.textArea_logs.append("\nRunning in normal mode: ");
				ks = KeyStore.getInstance("JKS");
				// secureRandom = SecureRandom.getInstance("DEFAULT");
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
		} catch (Exception e) {
			TestSDK_WBuilder.textArea_logs.append("\nException in getSSLContext.");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
