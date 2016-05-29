/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.data_source;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Leslie L Foster on 11/28/2015.
 */
public class ResultsParser {
    /**
     * Parses the hit results from some query.  Packs the stuff into a collection of name/value-pair maps.
     *
     * @param all string with all hits from query.
     * @return the divided collection.
     */
    public List<SearchResult> parseHits(String all) {
        List<SearchResult> searchResults = new ArrayList<SearchResult>();
        try {
            JSONObject jsonContainer = new JSONObject(all);
            JSONArray jsonArray = jsonContainer.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                // This object has the following breakdown.
                //  {"content-type":"text/plain","id":7,"created-by":"lesfoster","date":1443844800000,"species":"Sus scrofa"}
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // For now: not interpreting the content type.
                String idStr = jsonObject.getString("id");
                String creator = jsonObject.getString("created-by");
                String dateTimeStr = jsonObject.getString("date");
                String speciesName = jsonObject.getString("species");

                SearchResult searchResult = new SearchResult();
                searchResult.setDescription(String.format("%s [%s], %s", speciesName, formatDate(dateTimeStr), creator ));
                searchResult.setFetchId(idStr);

                searchResults.add( searchResult );
            }
        } catch (JSONException jsone) {
            throw new RuntimeException(jsone);
        }
        return searchResults;

    }

    private String formatDate(String dateTimeStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date(Long.parseLong(dateTimeStr)));
    }

}

