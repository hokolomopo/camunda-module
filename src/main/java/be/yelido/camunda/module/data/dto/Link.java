package be.yelido.camunda.module.data.dto;

import lombok.Data;

@Data
public class Link {
    public String method;
    public String href;
    public String rel;
}
