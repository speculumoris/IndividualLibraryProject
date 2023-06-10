package com.lib.dto.response;

public class ImageSavedResponse extends LibResponse{

    private String imageId;

    public ImageSavedResponse(String message, boolean success, String imageId) {
        super(message, success);
        this.imageId = imageId;
    }

}
