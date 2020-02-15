package com.assertsolutions.dto;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement
@JsonAutoDetect
@JsonSerialize
@ApiModel(description = "Request DTO Object")
public class Request {
    
    @JsonProperty
    @ApiModelProperty(dataType = "String")
    @Size(min = 10, max = 45)
    public  String cc;
    

    @JsonProperty
    @ApiModelProperty(dataType = "String")
    @Size(min = 10, max = 45)
    public  String name;
    
    

}
