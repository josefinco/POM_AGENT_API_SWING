package sampleclient;

import java.util.Date;
import java.util.HashMap;

import com.avaya.sdk.PAMSocketInfo;
import com.avaya.sdk.Agent.POMAgent;
import com.avaya.sdk.Agent.POMAgentHandlerInterface;
import com.avaya.sdk.Data.POMAgentState;
import com.avaya.sdk.Data.POMAgentStatus;
import com.avaya.sdk.Data.POMAttribute;
import com.avaya.sdk.Data.POMCallState;
import com.avaya.sdk.Data.POMCallbackDest;
import com.avaya.sdk.Data.POMCallbackType;
import com.avaya.sdk.Data.POMCapabilities;
import com.avaya.sdk.Data.POMCompletionCode;
import com.avaya.sdk.Data.POMContact;
import com.avaya.sdk.Data.POMContactNumber;
import com.avaya.sdk.Data.POMContextStoreData;
import com.avaya.sdk.Data.POMCustomerDetails;
import com.avaya.sdk.Data.POMDestination;
import com.avaya.sdk.Data.POMDestinationType;
import com.avaya.sdk.Data.POMDialFailReason;
import com.avaya.sdk.Data.POMErrorCode;
import com.avaya.sdk.Data.POMErrorInfo;
import com.avaya.sdk.Data.POMKeyValuePair;
import com.avaya.sdk.Data.POMNailupStatus;
import com.avaya.sdk.Data.POMWrapupDetails;



public class SDKWorker implements POMAgentHandlerInterface 

{
	
	
	private String sessionID ;
	
	private  POMCompletionCode POMCompletionCode; 
	
	private  POMContactNumber POMContactNumber;
	
	private  boolean isPreview= false;
	
	private boolean isManual = false;

	private  POMAgent pomAgtObj;	
	
	private  POMAgentStatus agtStatus;
	
	private  String agentId;
	
	private  Date callStartTime = null;
	
	private Date wrapUpTime = null;
	
	public SDKWorker(String agentId)
	{
		this.agentId = agentId;
	}
	
	public void setPomAgtObj(POMAgent pomAgtObj) {
		this.pomAgtObj = pomAgtObj;
	}



	public Date getCallStartTime() {
		return callStartTime;
	}

		

	public Date getWrapUpTime() {
		return wrapUpTime;
	}

	
	
	public POMCompletionCode getPOMCompletionCode() {
		return POMCompletionCode;
	}

	
	
	@Override
	public void AGTSetCustomerDetailRESP(String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTBlendToInboundRESP(int result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTBlendToOutboundRESP(int result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTNailupAgentRESP(int result) throws Exception
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTReadyForNailupRESP(int result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTLostNailingRESP(POMWrapupDetails wrapupDetails,
			String sessionID, int result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTPendingLogoutRESP(int result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GetAgentStatusResponseRESP(int result) throws Exception {
		TestSDK_WBuilder.textArea_logs.append("\nGetAgentStatusResponseRESP received  result  " + result);
		
	}

	@Override
	public void AGTAddAgentNoteRESP(String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTRefreshAgentNotesRESP(String[] agentNotes, String sessionID,
			int result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTGetTimezonesRESP(POMKeyValuePair[] timezones, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTAvailableForNailupRESP(int result) throws Exception 
	{
		
		checkError(result);
	}

	@Override
	public void AGTAgentDisconnectedRESP(int result) throws Exception {
		// TODO Auto-generated method stub
			
	}

	@Override
	public void AGTAddToDNCRESP(String sessionID, int result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AgentSDKConnectedRESP(HashMap<String, PAMSocketInfo> zoneIPMap)
			throws Exception {
	
		
	}

	@Override
	public void GetPAMForZoneRESP(HashMap<String, PAMSocketInfo> zoneIPMap)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GetAgentStatus(String agentID) throws Exception
	{
		
		TestSDK_WBuilder.textArea_logs.append("\nGetAgentStatus received.");
		pomAgtObj.GetAgentStatusResponse(agtStatus);
		
	}

	@Override
	public void AGTGetZoneListRESP(String[] zoneList, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTIsInDNCRESP(boolean present, int result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTSaveAgentForHARESP(int result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTSkillsChangedRESP(int result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTGetContactAttributesRESP(POMAttribute[] pomContactAttributes, String sessionID, int result)
			throws Exception {
			
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTStateChangedNotify(POMAgentState agentState)
			throws Exception 
	{
		TestSDK_WBuilder.textArea_logs.append("\nAgent state changed to " + agentState.name());
		agtStatus.setAgentState(agentState) ;
		printAgentState();
		
		pomAgtObj.AGTAvailableForNailup();
				
		
	}
	@Override
	public void AGTCallNotify(POMContact contact, String sessionID)
			throws Exception {
		// TODO Auto-generated method stub
//		TestSDK_WBuilder.textArea_logs.append("\n Agent received contact : sessionID = " +sessionID + " getContactType = " + contact.getContactType().name() + " contact list name = " + contact.getContactListName() + " contact list id = " + contact.getContactListId());
		this.sessionID = sessionID;
		
		if( contact.getContactType().name().equals("Preview"))
		{
			isPreview= true;
			this.POMContactNumber = contact.getContactNumbers()[0];
		} else if (contact.getContactType().name().equals("Manual"))
		{
			isPreview = true;
			isManual = true;
			this.POMContactNumber = contact.getContactNumbers()[0];
		}
		
		pomAgtObj.AGTGetCustomerDetails(sessionID);
		
		callStartTime = new Date();
				
		
		
	}

	@Override
	public void AGTAutoReleaseLine(POMWrapupDetails wrapupDetails,
			String sessionID) throws Exception {
		
		TestSDK_WBuilder.textArea_logs.append("\nSending commnad to change agent state to Pending Not Ready.");
		// Making Agent Pending not  ready so that agent can not take next call
		pomAgtObj.AGTStateChange(POMAgentState.NotReady, "1", "Not Available", false);
				
		wrapUpTime = new Date();
				
		pomAgtObj.AGTGetCompCodes(sessionID);
		
	}

	@Override
	public void AGTConsultNotify(POMContact contact, String requestingAgentId,
			String sessionID) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTConsultCancelled(String requestingAgentID, String sessionID)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTTransferNotify(POMContact contact, String sessionID)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTConferenceNotify(POMContact pomContact, String sessionID)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTConferenceEnded(String sessionID) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTConferenceOwnershipChanged(POMContact pomContact,
			String sessionID) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTCapabilitiesChanged(POMCapabilities capabilities)
			throws Exception {
		// TODO Auto-generated method stub
		TestSDK_WBuilder.textArea_logs.append("\nNEW CAPABILITY - " + capabilities.toString());
	}

	@Override
	public void AGTNailupChange(POMNailupStatus nailupStatus) throws Exception
	{
		// TODO Auto-generated method stub
		agtStatus.setNailupStatus(nailupStatus);
		printAgentState();
		if( nailupStatus == POMNailupStatus.PendingNailUp )
		{
			TestSDK_WBuilder.textArea_logs.append("\nSending Ready for Nailup.");
			pomAgtObj.AGTReadyForNailup();
		}
	}

	@Override
	public void AGTCallStateChangedNotify(POMCallState callState)
			throws Exception {
		agtStatus.setCallState(callState);
		printAgentState();
		
	}

	@Override
	public void AGTDialFailed(POMWrapupDetails wrapupDetails,
			POMDialFailReason dialFailReason, String sessionID)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTConsultDialFailed(POMDialFailReason dialFailReason,
			String sessionID) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTConsultPending(String requestingAgent,
			String requestingAgentID, String requestingCampaign,
			String sessionID) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTPendingConsultComplete(String sessionID) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void POMAvailable() throws Exception {
		
		TestSDK_WBuilder.textArea_logs.append("\n Received POMAvailable " );
		
	}

	@Override
	public void POMNotAvailable() throws Exception {
		
		TestSDK_WBuilder.textArea_logs.append("\n Received POMNotAvailable " );
		
	}

	@Override
	public void AGTPreviewCallbackPending(String dueTime, String sessionID)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTPreviewCallbackCancelled(String sessionID) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTEnableCancelConsult(String sessionID) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTInvalidCommandName(String commandName) throws Exception {
		// TODO Auto-generated method stub				
		
	}

	@Override
	public void AGTCustomerDetailsChanged(POMAttribute detail, String sessionID)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTBlendedToInbound() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTBlendedToOutbound() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTZoneDown(String zoneName, int gracePeriodInMins)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTJobAttached(String campaignName) throws Exception {
		// TODO Auto-generated method stub
		
		TestSDK_WBuilder.textArea_logs.append("\nAgent attached to job " + campaignName );
		
	}

	@Override
	public void AGTGetErrorInfoRESP(POMErrorInfo errorInfo, int result)
			throws Exception {
		TestSDK_WBuilder.textArea_logs.append("\nError info " + errorInfo.geterrorString() );
		
	}

	@Override
	public void AGTDialFailedErrorInfo(POMWrapupDetails wrapupDetails,
			POMErrorInfo failErrorInfo, String sessionID) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTConsultDialFailedErrorInfo(POMErrorInfo failErrorInfo,
			String sessionID) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AGTSessionIDChange(String oldSessionID, String newSessionID)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTLogonRESP(int result) throws Exception {
		
		if( result != 0 )
		{
			TestSDK_WBuilder.textArea_logs.append("\nAgent Login failed." + result);
		}
		agtStatus = new POMAgentStatus();
		agtStatus.setAgentState(POMAgentState.NotReady);
		agtStatus.setCallState(POMCallState.NoState);
		agtStatus.setNailupStatus(POMNailupStatus.NotNailedUp);
				
		
		printAgentState();
		
		TestSDK_WBuilder.textArea_logs.append("\nAgent login successful. Sending agent state change : Ready.");
		
		pomAgtObj.AGTStateChange(POMAgentState.Ready, "1", "Available", false);
	}


	@Override
	public void AGTLogoffRESP(int result) throws Exception {
		
		TestSDK_WBuilder.textArea_logs.append("\nAGTLogoffRESP done.");
		
	}


	@Override
	public void AGTStateChangeRESP(POMAgentState agentState, int result)
			throws Exception {
		
		checkError(result);
	}


	@Override
	public void AGTHoldCallRESP(String sessionID, int result) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTUnholdCallRESP(String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTReleaseLineRESP(POMWrapupDetails wrapupDetails,
			String sessionID, int result) throws Exception {
		
		TestSDK_WBuilder.textArea_logs.append("\nSending commnad to change agent state to Pending Not Ready.");
		// Making Agent Pending not  ready so that agent can not take next call
		pomAgtObj.AGTStateChange(POMAgentState.NotReady, "1", "Not Available", false);
				
		wrapUpTime = new Date();
				
		pomAgtObj.AGTGetCompCodes(sessionID);
				
		
	}


	@Override
	public void AGTGetCompCodesRESP(POMCompletionCode[] completionCodesList,
			String sessionID, int result) throws Exception 
	{
		// TODO Auto-generated method stub
		TestSDK_WBuilder.textArea_logs.append("\nAGTGetCompCodesRESP received.");
		for( int i=0; i< completionCodesList.length; i++)
		{
			TestSDK_WBuilder.textArea_logs.append("\nCompention code Id = " + completionCodesList[i].getcodeID() + " Code = " + completionCodesList[i].getcodeValue());
		}
		POMCompletionCode = completionCodesList[0];
			
		
	}


	@Override
	public void AGTWrapupContactRESP(String sessionID, int result)
			throws Exception {
	
		pomAgtObj.AGTLogoff();
	}


	@Override
	public void AGTConsultCallRESP(String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTCancelConsultRESP(String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTGetConsultTypesRESP(POMDestinationType[] destinationTypes,
			boolean allowFreeForm, String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTGetConsultDestsForTypeRESP(POMDestinationType type,
			POMDestination[] destinations, String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTCompleteTransferRESP(boolean canDispose,
			POMWrapupDetails wrapupDetails, String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTStartConfRESP(String sessionID, int result) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTEndConfRESP(String sessionID, int result) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTConfChangeOwnershipRESP(String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTExtendWrapupRESP(POMWrapupDetails wrapupDetails,
			String sessionID, int result) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTRedialRESP(String sessionID, int result) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTSendDTMFRESP(String sessionID, int result) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTGetCallbackTypesRESP(POMCallbackType[] callbackTypes,
			String defaultExpiryTime, String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTGetCallbackDestsForTypeRESP(POMCallbackType type,
			POMCallbackDest[] callbackDests, String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTCreateCallbackRESP(String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTGetErrorStringRESP(String errorMsg,
			String localizedErrorMsg, int result) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTPreviewDialRESP(String sessionID, int result)
			throws Exception {
		// TODO Auto-generated method stub
		if (isManual)
			pomAgtObj.AGTManualCallConnect(sessionID);
	}


	@Override
	public void AGTPreviewCancelRESP(POMWrapupDetails wrapupDetails,
			String sessionID, int result) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void AGTGetCustomerDetailsRESP(POMCustomerDetails customerDetails,
			String sessionID, int result) throws Exception {
		checkError(result);
		TestSDK_WBuilder.textArea_logs.append("\n***AGTGetCustomerDetailsRESP - " + isPreview + "  " + isManual);
		if(isPreview)
		{
			pomAgtObj.AGTPreviewDial(POMContactNumber, sessionID);
		}
				
	}


	@Override
	public void AGTAgentLoggedOut() throws Exception {		
		
	}
	
	
	private  String padLeft(String s, int n)
	{
		return String.format("%1$" + n + "s", s);
	}
	
	 public String getSessionID() {
		return sessionID;
	}

	private  void printAgentState()
	{
		 StringBuffer quotaTable = new StringBuffer("\n------------------------------------------------------------------------------------");
		 quotaTable.append("\n" + padLeft("AgentId", 15) + padLeft("Agent State", 15) + padLeft("Call State", 15)
                + padLeft("Nail State", 15) );
		
		quotaTable.append("\n" + padLeft(agentId, 15)
	             + padLeft(agtStatus.getAgentState().name()  , 15)
	             + padLeft(agtStatus.getCallState().name(), 15) + padLeft(agtStatus.getNailupStatus().name(), 15));
		
		quotaTable.append("\n------------------------------------------------------------------------------------");
		TestSDK_WBuilder.textArea_logs.append("\n" + quotaTable.toString());
	}
	 
	 private void checkError( int error)
	 {
		 
		 if( error > 0 )
			{
				TestSDK_WBuilder.textArea_logs.append("\n Agent state change failed. ");
				POMErrorCode err = new POMErrorCode();
				
				try 
				{
					err.seterrorCode(String.valueOf(error));
					pomAgtObj.AGTGetErrorInfo(err);
					
				} catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		 
	 }


	@Override
	public void AGTGetWorkRequestIdRESP(POMContextStoreData pomContextStoreData,
	                                    String sessionID,
	                                    int result) throws Exception
	{
		// TODO Auto-generated method stub
	}


	@Override
	public void AGTManualCallConnectRESP(String arg0, int arg1) throws Exception
	{
		// TODO Auto-generated method stub
		TestSDK_WBuilder.textArea_logs.append("\n***** AGTManualCallConnectRESP received.******");
		printAgentState();
	}
		
	
}
