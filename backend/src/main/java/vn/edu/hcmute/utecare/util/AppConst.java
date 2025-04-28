package vn.edu.hcmute.utecare.util;

public interface AppConst {
    String SEARCH_SPEC_OPERATOR = "(\\w+?)([:<>!])(.*)";
    String SORT_BY = "(\\w+?)(:)(.*)";
    static final String AUTHORIZATION = "Authorization";
    static final String AT_BLACKLIST_PREFIX = "bl_at:";
}
