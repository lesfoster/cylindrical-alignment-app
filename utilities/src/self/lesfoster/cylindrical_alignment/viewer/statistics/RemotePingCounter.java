/*
 CDDL HEADER START

 The contents of this file are subject to the terms of the
 Common Development and Distribution License (the "License").
 You may not use this file except in compliance with the License.

 You can obtain a copy of the license at
   https://opensource.org/licenses/CDDL-1.0.
 See the License for the specific language governing permissions
 and limitations under the License.

 When distributing Covered Code, include this CDDL HEADER in each
 file and include the License file at
    https://opensource.org/licenses/CDDL-1.0.
 If applicable, add the following below this CDDL HEADER, with the
 fields enclosed by brackets "[]" replaced with your own identifying
 information: Portions Copyright [yyyy] [name of copyright owner]

 CDDL HEADER END
*/


package self.lesfoster.cylindrical_alignment.viewer.statistics;

import com.boxysystems.jgoogleanalytics.FocusPoint;
import com.boxysystems.jgoogleanalytics.JGoogleAnalyticsTracker;
import self.lesfoster.cylindrical_alignment.constants.Constants;

/**
 * Created by IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 5/26/12
 * Time: 8:34 PM
 *
 * Registers a usage event against a remote HTML server, so that useful information
 * may be gathered regarding the user base.
 */
public class RemotePingCounter {
    /** This starts a thread that sends a single HTTP request. */
    public void registerUsage( String eventName ) {

        // Rules:
        // - background.
        // - does not prevent system exit if still running.
        // - minimal chatter on failure.
        try {
            // No-Op for demo
            //if ( 0 == 0 )
            //    return;
            // Register usage so usership can be counted.  Give the user an option out.
            String remotePingProp = System.getProperty( "stat" );
            if ( remotePingProp == null  ||  remotePingProp.equals( "1" ) ) {
                //Google analytics tracking code for Library Finder
                JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(
                        "CylindricalBlastViewer", Constants.CODE_VERSION, "UA-32145721-1"
                );
                FocusPoint focusPoint = new FocusPoint( eventName );
                tracker.trackAsynchronously( focusPoint );
            }
        } catch ( Exception ex ) {
            System.out.println("Ping thread failed: " + ex.getMessage() );
        }
    }

}

