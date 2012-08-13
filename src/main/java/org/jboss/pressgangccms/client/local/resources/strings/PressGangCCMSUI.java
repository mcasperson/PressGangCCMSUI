package org.jboss.pressgangccms.client.local.resources.strings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

public interface PressGangCCMSUI extends Constants
{
	public static final PressGangCCMSUI INSTANCE = GWT.create(PressGangCCMSUI.class); 
	
	String PressGangCCMS();

	String Welcome();

	String Search();

	String Common();

	String Troubleshooting();

	String PleaseWait();
	
	String Categories();
	
	String Included();
	
	String Excluded();
	
	String Tags();
}
