
package reoger.hut.paperplaner.customtabs;

import android.support.customtabs.CustomTabsClient;

/**
 * Created by Lizhaotailang on 2016/9/4.
 * Callback for events when connecting and disconnecting from Custom Tabs Service.
 */

public interface ServiceConnectionCallback {

    /**
     * Called when the service is connected.
     * @param client a CustomTabsClient
     */
    void onServiceConnected(CustomTabsClient client);

    /**
     * Called when the service is disconnected.
     */
    void onServiceDisconnected();

}
