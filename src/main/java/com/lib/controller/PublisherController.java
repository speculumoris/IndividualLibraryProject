package com.lib.controller;

import com.lib.domain.Publisher;
import com.lib.dto.PublisherDTO;
import com.lib.dto.request.PublisherRequest;
import com.lib.dto.request.UpdatePublisherRequest;
import com.lib.dto.response.LibResponse;
import com.lib.dto.response.ResponseMessage;
import com.lib.service.PublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping("/visitors")
    public ResponseEntity<Page<PublisherDTO>> getAllPublisher(@RequestParam("page") int page,
                                                        @RequestParam("size") int size,
                                                        @RequestParam("sort") String prop,
                                                        @RequestParam(value = "direction",
                                                                required=false,
                                                                defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable=PageRequest.of(page,size,Sort.by(direction,prop));
        Page<PublisherDTO> publisherDTOPage=publisherService.getAllPublisherByPage(pageable);

        return ResponseEntity.ok(publisherDTOPage);
    }
    @GetMapping("/visitors/{id}")
    public ResponseEntity<PublisherDTO> publisherById(@PathVariable Long id){
       PublisherDTO publisherDTO= publisherService.findPublisherById(id);

       return ResponseEntity.ok(publisherDTO);
    }
    @GetMapping
    public ResponseEntity<List<PublisherDTO>> getAllpublisher(){
        List<PublisherDTO> publisher=publisherService.getAllPublisher();
        return ResponseEntity.ok(publisher);
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> savePublisher(@Valid @RequestBody PublisherRequest publisherRequest){
        publisherService.savePublisher(publisherRequest);

        LibResponse libResponse=new LibResponse(ResponseMessage.PUBLISHER_CREATED_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(libResponse,HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> updatePublisher(@PathVariable Long id,
                                                       @Valid @RequestBody UpdatePublisherRequest updatePublisherRequest){
        publisherService.updatePublisher(id,updatePublisherRequest);
        LibResponse libResponse=new LibResponse(ResponseMessage.PUBLISHER_UPDATED_RESPONSE_MESSAGE,true);
        return  ResponseEntity.ok(libResponse);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> removePublisher(@PathVariable Long id){
        publisherService.removePublisher(id);
        LibResponse libResponse=new LibResponse(ResponseMessage.PUBLISHER_DELETED_RESPONSE_MESSAGE,true);
        return  ResponseEntity.ok(libResponse);


    }




}
