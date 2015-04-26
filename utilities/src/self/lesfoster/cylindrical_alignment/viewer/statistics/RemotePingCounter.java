/*
  Cylindrical Blast Viewer: allows viewing of Biomolecular
  alignment data, in 3D, by forming the alignments into a
  cylinder, such that the alignments are arranged like the staves
  of a barrel. The cylinder can spin, and if it spins, it can
  do so at two different speeds.  When stopped, properties can
  be seen for the aligned values.
  Copyright (C) 2005 Leslie L. Foster

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
            if ( 0 == 0 )
                return;
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

