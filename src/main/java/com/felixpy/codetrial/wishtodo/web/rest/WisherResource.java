package com.felixpy.codetrial.wishtodo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.felixpy.codetrial.wishtodo.domain.Wisher;
import com.felixpy.codetrial.wishtodo.repository.WisherRepository;
import com.felixpy.codetrial.wishtodo.web.rest.errors.BadRequestAlertException;
import com.felixpy.codetrial.wishtodo.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Wisher.
 */
@RestController
@RequestMapping("/api")
public class WisherResource {

    private final Logger log = LoggerFactory.getLogger(WisherResource.class);

    private static final String ENTITY_NAME = "wishtodoWisher";

    private final WisherRepository wisherRepository;

    public WisherResource(WisherRepository wisherRepository) {
        this.wisherRepository = wisherRepository;
    }

    /**
     * POST  /wishers : Create a new wisher.
     *
     * @param wisher the wisher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wisher, or with status 400 (Bad Request) if the wisher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wishers")
    @Timed
    public ResponseEntity<Wisher> createWisher(@Valid @RequestBody Wisher wisher) throws URISyntaxException {
        log.debug("REST request to save Wisher : {}", wisher);
        if (wisher.getId() != null) {
            throw new BadRequestAlertException("A new wisher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Wisher result = wisherRepository.save(wisher);
        return ResponseEntity.created(new URI("/api/wishers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wishers : Updates an existing wisher.
     *
     * @param wisher the wisher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wisher,
     * or with status 400 (Bad Request) if the wisher is not valid,
     * or with status 500 (Internal Server Error) if the wisher couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wishers")
    @Timed
    public ResponseEntity<Wisher> updateWisher(@Valid @RequestBody Wisher wisher) throws URISyntaxException {
        log.debug("REST request to update Wisher : {}", wisher);
        if (wisher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Wisher result = wisherRepository.save(wisher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wisher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wishers : get all the wishers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wishers in body
     */
    @GetMapping("/wishers")
    @Timed
    public List<Wisher> getAllWishers() {
        log.debug("REST request to get all Wishers");
        return wisherRepository.findAll();
    }

    /**
     * GET  /wishers/:id : get the "id" wisher.
     *
     * @param id the id of the wisher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wisher, or with status 404 (Not Found)
     */
    @GetMapping("/wishers/{id}")
    @Timed
    public ResponseEntity<Wisher> getWisher(@PathVariable Long id) {
        log.debug("REST request to get Wisher : {}", id);
        Optional<Wisher> wisher = wisherRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wisher);
    }

    /**
     * DELETE  /wishers/:id : delete the "id" wisher.
     *
     * @param id the id of the wisher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wishers/{id}")
    @Timed
    public ResponseEntity<Void> deleteWisher(@PathVariable Long id) {
        log.debug("REST request to delete Wisher : {}", id);

        wisherRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
