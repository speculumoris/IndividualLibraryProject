package com.lib.service;

import com.lib.domain.Author;
import com.lib.domain.Publisher;
import com.lib.dto.PublisherDTO;
import com.lib.dto.request.PublisherRequest;
import com.lib.dto.request.UpdatePublisherRequest;
import com.lib.exception.BadRequestException;
import com.lib.exception.ResourceNotFoundException;
import com.lib.exception.message.ErrorMessage;
import com.lib.mapper.PublisherMapper;
import com.lib.repository.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    private final PublisherMapper publisherMapper;

    public PublisherService(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }


    public Page<PublisherDTO> getAllPublisherByPage(Pageable pageable) {
        Page<Publisher> publishers = publisherRepository.findAll(pageable);

        return publishers.map(publisher -> publisherMapper.publisherToDTO(publisher));
    }

    public List<PublisherDTO> getAllPublisher() {
        List<Publisher> publishers = publisherRepository.findAll();
        return publisherMapper.publisherMap(publishers);
    }

    public PublisherDTO getPublishersById(Long id) {
        Publisher publisher = publisherRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.PUBLISHER_NOT_FOUND_EXCEPTION, id)));
        return publisherMapper.publisherToDTO(publisher);
    }



    public void updatePublisher(Long id, UpdatePublisherRequest updatePublisherRequest) {
        Publisher publisher = getPublisherById(id);

        if (publisher.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
        publisher.setName(updatePublisherRequest.getName());
        publisher.setBuiltIn(updatePublisherRequest.isBuiltIn());
        publisherRepository.save(publisher);
    }

    private Publisher getPublisherById(Long id) {
        return publisherRepository.findPublisherById(id).orElseThrow
                (() -> new ResourceNotFoundException(String.format(ErrorMessage.PUBLISHER_NOT_FOUND_EXCEPTION, id)));
    }

    public void deletePublisherById(Long id) {
        Publisher publisher = getPublisherById(id);

        if (publisher.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
        publisherRepository.delete(publisher);
    }

    public void savePublisher(PublisherRequest publisherRequest) {
        Publisher publisher=new Publisher();

        publisher.setName(publisherRequest.getName());

        publisherRepository.save(publisher);

    }
}
