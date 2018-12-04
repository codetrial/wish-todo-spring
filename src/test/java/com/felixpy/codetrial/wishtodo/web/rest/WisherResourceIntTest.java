package com.felixpy.codetrial.wishtodo.web.rest;

import com.felixpy.codetrial.wishtodo.WishtodoApp;

import com.felixpy.codetrial.wishtodo.domain.Wisher;
import com.felixpy.codetrial.wishtodo.repository.WisherRepository;
import com.felixpy.codetrial.wishtodo.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.felixpy.codetrial.wishtodo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WisherResource REST controller.
 *
 * @see WisherResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WishtodoApp.class)
public class WisherResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private WisherRepository wisherRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWisherMockMvc;

    private Wisher wisher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WisherResource wisherResource = new WisherResource(wisherRepository);
        this.restWisherMockMvc = MockMvcBuilders.standaloneSetup(wisherResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wisher createEntity(EntityManager em) {
        Wisher wisher = new Wisher()
            .name(DEFAULT_NAME);
        return wisher;
    }

    @Before
    public void initTest() {
        wisher = createEntity(em);
    }

    @Test
    @Transactional
    public void createWisher() throws Exception {
        int databaseSizeBeforeCreate = wisherRepository.findAll().size();

        // Create the Wisher
        restWisherMockMvc.perform(post("/api/wishers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wisher)))
            .andExpect(status().isCreated());

        // Validate the Wisher in the database
        List<Wisher> wisherList = wisherRepository.findAll();
        assertThat(wisherList).hasSize(databaseSizeBeforeCreate + 1);
        Wisher testWisher = wisherList.get(wisherList.size() - 1);
        assertThat(testWisher.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createWisherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wisherRepository.findAll().size();

        // Create the Wisher with an existing ID
        wisher.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWisherMockMvc.perform(post("/api/wishers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wisher)))
            .andExpect(status().isBadRequest());

        // Validate the Wisher in the database
        List<Wisher> wisherList = wisherRepository.findAll();
        assertThat(wisherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wisherRepository.findAll().size();
        // set the field null
        wisher.setName(null);

        // Create the Wisher, which fails.

        restWisherMockMvc.perform(post("/api/wishers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wisher)))
            .andExpect(status().isBadRequest());

        List<Wisher> wisherList = wisherRepository.findAll();
        assertThat(wisherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWishers() throws Exception {
        // Initialize the database
        wisherRepository.saveAndFlush(wisher);

        // Get all the wisherList
        restWisherMockMvc.perform(get("/api/wishers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wisher.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getWisher() throws Exception {
        // Initialize the database
        wisherRepository.saveAndFlush(wisher);

        // Get the wisher
        restWisherMockMvc.perform(get("/api/wishers/{id}", wisher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wisher.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWisher() throws Exception {
        // Get the wisher
        restWisherMockMvc.perform(get("/api/wishers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWisher() throws Exception {
        // Initialize the database
        wisherRepository.saveAndFlush(wisher);

        int databaseSizeBeforeUpdate = wisherRepository.findAll().size();

        // Update the wisher
        Wisher updatedWisher = wisherRepository.findById(wisher.getId()).get();
        // Disconnect from session so that the updates on updatedWisher are not directly saved in db
        em.detach(updatedWisher);
        updatedWisher
            .name(UPDATED_NAME);

        restWisherMockMvc.perform(put("/api/wishers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWisher)))
            .andExpect(status().isOk());

        // Validate the Wisher in the database
        List<Wisher> wisherList = wisherRepository.findAll();
        assertThat(wisherList).hasSize(databaseSizeBeforeUpdate);
        Wisher testWisher = wisherList.get(wisherList.size() - 1);
        assertThat(testWisher.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingWisher() throws Exception {
        int databaseSizeBeforeUpdate = wisherRepository.findAll().size();

        // Create the Wisher

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWisherMockMvc.perform(put("/api/wishers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wisher)))
            .andExpect(status().isBadRequest());

        // Validate the Wisher in the database
        List<Wisher> wisherList = wisherRepository.findAll();
        assertThat(wisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWisher() throws Exception {
        // Initialize the database
        wisherRepository.saveAndFlush(wisher);

        int databaseSizeBeforeDelete = wisherRepository.findAll().size();

        // Get the wisher
        restWisherMockMvc.perform(delete("/api/wishers/{id}", wisher.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Wisher> wisherList = wisherRepository.findAll();
        assertThat(wisherList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wisher.class);
        Wisher wisher1 = new Wisher();
        wisher1.setId(1L);
        Wisher wisher2 = new Wisher();
        wisher2.setId(wisher1.getId());
        assertThat(wisher1).isEqualTo(wisher2);
        wisher2.setId(2L);
        assertThat(wisher1).isNotEqualTo(wisher2);
        wisher1.setId(null);
        assertThat(wisher1).isNotEqualTo(wisher2);
    }
}
