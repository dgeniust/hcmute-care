package vn.edu.hcmute.utecare.util;

public interface AppConst {
    String SEARCH_SPEC_OPERATOR = "(\\w+?)([<:>~!])(.*)(\\p{Punct}?)(\\p{Punct}?)";
    String SORT_BY = "(\\w+?)(:)(.*)";
}
