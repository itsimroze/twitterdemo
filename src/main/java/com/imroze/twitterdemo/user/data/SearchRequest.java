package com.imroze.twitterdemo.user.data;

import lombok.Data;

@Data
public class SearchRequest {
  private String keyword;
  private SearchType searchType;
}
