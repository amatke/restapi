package com.matovic.restapi.controllers;

import com.matovic.restapi.models.PageRepository;
import com.matovic.restapi.models.entities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/admin/pages", produces = "application/json")
@CrossOrigin(origins = "*")        // bilo koji domen moze da pristupi
public class AdminPageRestController {

    @Autowired
    private PageRepository pageRepository;

    @GetMapping
    public List<Page> getAllPages(){

        return pageRepository.findAllByOrderByTitleAsc();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Page> getPageBySlug(@PathVariable(value = "slug") String slug){
        Optional<Page> page = Optional.ofNullable(pageRepository.findBySlug(slug));
        if(page.isPresent()){
            return new ResponseEntity<>(page.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Page addPage(@RequestBody Page page){
        pageRepository.save(page);
        return page;
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<Page> edit(@PathVariable Long id){
        Optional<Page> page = Optional.of(pageRepository.getOne(id));
        return new ResponseEntity<> (page.get(), HttpStatus.OK);
    }


    @PutMapping(path = "/edit", consumes = "application/json")
    public Page put(@RequestBody Page page){
        return pageRepository.save(page);
    }

    @GetMapping(path = "/edit/{slug}/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Page findBySlugAndIdNot(@PathVariable Long id, @PathVariable String slug){
        return pageRepository.findBySlugAndIdNot(slug, id);
    }


    @DeleteMapping("/delete/{id}")
    public String deletePage(@PathVariable Long id){
        try {
            pageRepository.deleteById(id);
            return "Page deleted.";
        } catch (Exception e){
            return ("Page with id: " + id + " not exist");
        }
    }

}
