package com.lib.dto.response;

public class ImageSavedResponse extends LibResponse{

    private String imageId;

    public ImageSavedResponse(String imageId,String message,boolean success){
        super(message,success);
        this.imageId=imageId;
    }
}
