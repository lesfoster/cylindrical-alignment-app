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


package self.lesfoster.cylindrical_alignment.data_source;

import java.io.Serializable;

/**
 * Bean for passing search results around.
 * Created by Leslie L Foster on 11/28/2015, 05/28/2016.
 */
public class SearchResult implements Serializable {
    private String fetchId;
    private String description;

    public String getFetchId() {
        return fetchId;
    }

    public void setFetchId(String fetchId) {
        this.fetchId = fetchId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	@Override
    public String toString() {
        return getDescription();
    }
	
	@Override
	public boolean equals(Object o) {
		boolean rtnVal = false;
		if (fetchId != null  &&  description != null  &&  o instanceof SearchResult) {
			SearchResult other = (SearchResult)o;
			rtnVal = fetchId.equals(other.getFetchId())  &&  description.equals(other.getDescription());
		}
		return rtnVal;
	}
	
	@Override
	public int hashCode() {
		if (fetchId == null) {
			return Integer.MAX_VALUE;
		}
		else {
			return fetchId.hashCode();
		}
	}
}

