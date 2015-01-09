/**
 * 
 */
package ibt.ortc.plugins.IbtRealtimeSJ;

import ibt.ortc.extensibility.OrtcClient;
import ibt.ortc.extensibility.OrtcFactory;

public class IbtRealtimeSJFactory implements OrtcFactory {

	/* (non-Javadoc)
	 * @see ibt.ortc.android.extensibility.OrtcFactory#createClient()
	 */
	@Override
	public OrtcClient createClient() {
		// TODO Auto-generated method stub
		return new IbtRealtimeSJClient();
	}

}
