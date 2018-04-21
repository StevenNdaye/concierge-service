package com.concierge.controller;

import com.concierge.entity.Customer;
import com.concierge.exception.CustomerNotFoundException;
import com.concierge.repository.CustomerRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.util.concurrent.Callable;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private File root;
    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    public CustomerController(CustomerRepository customerRepository, @Value("${upload.dir:${user.home}/images}") String uploadDir) {
        this.customerRepository = customerRepository;
        this.root = new File(uploadDir);

        Assert.isTrue(this.root.exists() || this.root.mkdirs(), String.format("The path '%s' must exist.", this.root.getAbsolutePath()));
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    ResponseEntity<?> options() {
        //@formatter:off
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.HEAD, HttpMethod.OPTIONS, HttpMethod.PUT, HttpMethod.DELETE)
                .build();
        //@formatter:on
    }

    @GetMapping
    ResponseEntity<Iterable<Customer>> getCollection() {
        return ResponseEntity.ok(this.customerRepository.findAll());
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Customer> get(@PathVariable Long id) {
        return this.customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @PostMapping
    ResponseEntity<Customer> post(@RequestBody Customer c) {

        Customer customer = this.customerRepository.save(new Customer(c.getFirstName(), c.getLastName()));

        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}")
                .buildAndExpand(customer.getId()).toUri();

        return ResponseEntity.created(uri).body(customer);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {

        return this.customerRepository.findById(id)
                .map(c -> {
                    customerRepository.delete(c);
                    return ResponseEntity.noContent().build();
                }).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    ResponseEntity<?> head(@PathVariable Long id) {
        return this.customerRepository.findById(id)
                .map(exists -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<Customer> put(@PathVariable Long id, @RequestBody Customer c) {

        return this.customerRepository
                .findById(id)
                .map(existing -> {
                    Customer customer = this.customerRepository.save(new Customer(existing.getId(), c.getFirstName(), c.getLastName()));
                    URI selfLink = URI.create(fromCurrentRequest().toUriString());
                    return ResponseEntity.created(selfLink).body(customer);
                }).orElseThrow(() -> new CustomerNotFoundException(id));

    }

    @GetMapping(value = "/{id}/photo")
    ResponseEntity<Resource> read(@PathVariable Long id) {
        return this.customerRepository
                .findById(id)
                .map(customer -> {
                    File file = fileFor(customer);
                    Assert.isTrue(file.exists(), String.format("file-not-found %s", file.getAbsolutePath()));
                    Resource fileSystemResource = new FileSystemResource(file);
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                            .body(fileSystemResource);
                }).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT}, value = "/{id}/photo")
    Callable<ResponseEntity<?>> write(@PathVariable Long id, @RequestParam MultipartFile file) {

        log.info(String.format("upload-start/customers/%s/photo (%s bytes)", id, file.getSize()));

        return () -> this.customerRepository
                .findById(id)
                .map(customer -> {
                    File fileForCustomer = fileFor(customer);

                    try (
                            InputStream in = file.getInputStream();
                            OutputStream out = new FileOutputStream(fileForCustomer)) {

                        FileCopyUtils.copy(in, out);

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    URI location = fromCurrentRequest().buildAndExpand(id).toUri();

                    log.info(String.format("upload-finish/customers/%s/photo (%s)", id, location));

                    return ResponseEntity.created(location)
                            .build();
                }).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    private File fileFor(Customer customer) {
        return new File(this.root, Long.toString(customer.getId()));
    }
}
