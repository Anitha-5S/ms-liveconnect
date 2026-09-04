package com.c2.lc.lib.bo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchBO extends PageBO {

    @SerializedName("c_search_term")
    @Size(min = 3, max = 30, message = "'Search Term' has to be length {min} to {max} characters!")
    private String searchTerm;

    public SearchBO(String searchString, int page, int limit) {
        this.searchTerm = searchString;
        this.setPage(page);
        this.setLimit(limit);
    }

    @Size(min = 3, message = "Minimum of {min} characters required!")
    @SerializedName("c_in_search_term")
    private String inSearchTerm;

    @SerializedName("c_sort")
    private String sort;

}
