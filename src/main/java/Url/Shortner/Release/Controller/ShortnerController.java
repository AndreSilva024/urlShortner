package Url.Shortner.Release.Controller;

import java.nio.charset.StandardCharsets;

import Url.Shortner.Release.Model.ShortnerModel;
import Url.Shortner.Release.Repository.ShortnerRepository;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.common.hash.Hashing;
@RestController
@RequestMapping("/url")
public class ShortnerController {

    Logger logger = LoggerFactory.getLogger(ShortnerController.class);

    @Autowired
    private ShortnerRepository shortnerRepository;

    @PostMapping
    public ShortnerModel create(@RequestBody String url) {

        UrlValidator urlValidator = new UrlValidator(
                new String[] {"http","https"}
        );

        if (urlValidator.isValid(url)) {

            if (shortnerRepository.existsByUrl(url) == false){
                String  hash =  Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
                ShortnerModel urlShort = shortnerRepository.save(new ShortnerModel(url, hash));
                logger.info("created register for " + urlShort.getUrl() +  " hash " + urlShort.getHash() );
                return urlShort;
            }else{
                ShortnerModel urlShort = shortnerRepository.findByUrl(url);
                return urlShort;

            }
        }

        throw new RuntimeException("URL invalid: " + url);

    }

    @GetMapping("/{hash}")
    public ShortnerModel geturl(@PathVariable String hash) {
        ShortnerModel url = shortnerRepository.findByHash(hash);
        try{
            logger.info("retrived data " + url.getUrl());
        }catch (NullPointerException exception){
            logger.info("Data no exist");
        }
        return url;
    }

    @DeleteMapping
    public void deleteAll(){

        shortnerRepository.deleteAll();
        logger.warn("All data record deleted");

    }

}
