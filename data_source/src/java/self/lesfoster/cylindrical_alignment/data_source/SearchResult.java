/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

