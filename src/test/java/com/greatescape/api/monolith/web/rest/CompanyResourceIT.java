package com.greatescape.api.monolith.web.rest;

import com.greatescape.api.monolith.ApiMonolithApp;
import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.repository.CompanyRepository;
import com.greatescape.api.monolith.service.CompanyQueryService;
import com.greatescape.api.monolith.service.CompanyService;
import com.greatescape.api.monolith.service.dto.CompanyDTO;
import com.greatescape.api.monolith.service.mapper.CompanyMapper;
import java.util.List;
import javax.persistence.EntityManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@SpringBootTest(classes = ApiMonolithApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CompanyResourceIT {

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LEGAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAXPAYER_NUMBER = "68";
    private static final String UPDATED_TAXPAYER_NUMBER = "9";

    private static final Integer DEFAULT_DISCOUNT_IN_PERCENTS = 0;
    private static final Integer UPDATED_DISCOUNT_IN_PERCENTS = 1;
    private static final Integer SMALLER_DISCOUNT_IN_PERCENTS = 0 - 1;

    private static final Integer DEFAULT_COMMISSION_IN_PERCENTS = 0;
    private static final Integer UPDATED_COMMISSION_IN_PERCENTS = 1;
    private static final Integer SMALLER_COMMISSION_IN_PERCENTS = 0 - 1;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyQueryService companyQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyMockMvc;

    private Company company;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .slug(DEFAULT_SLUG)
            .title(DEFAULT_TITLE)
            .legalName(DEFAULT_LEGAL_NAME)
            .taxpayerNumber(DEFAULT_TAXPAYER_NUMBER)
            .discountInPercents(DEFAULT_DISCOUNT_IN_PERCENTS)
            .commissionInPercents(DEFAULT_COMMISSION_IN_PERCENTS);
        return company;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity(EntityManager em) {
        Company company = new Company()
            .slug(UPDATED_SLUG)
            .title(UPDATED_TITLE)
            .legalName(UPDATED_LEGAL_NAME)
            .taxpayerNumber(UPDATED_TAXPAYER_NUMBER)
            .discountInPercents(UPDATED_DISCOUNT_IN_PERCENTS)
            .commissionInPercents(UPDATED_COMMISSION_IN_PERCENTS);
        return company;
    }

    @BeforeEach
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();
        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testCompany.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCompany.getLegalName()).isEqualTo(DEFAULT_LEGAL_NAME);
        assertThat(testCompany.getTaxpayerNumber()).isEqualTo(DEFAULT_TAXPAYER_NUMBER);
        assertThat(testCompany.getDiscountInPercents()).isEqualTo(DEFAULT_DISCOUNT_IN_PERCENTS);
        assertThat(testCompany.getCommissionInPercents()).isEqualTo(DEFAULT_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void createCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company with an existing ID
        company.setId(1L);
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setSlug(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);


        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setTitle(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);


        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLegalNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setLegalName(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);


        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].legalName").value(hasItem(DEFAULT_LEGAL_NAME)))
            .andExpect(jsonPath("$.[*].taxpayerNumber").value(hasItem(DEFAULT_TAXPAYER_NUMBER)))
            .andExpect(jsonPath("$.[*].discountInPercents").value(hasItem(DEFAULT_DISCOUNT_IN_PERCENTS)))
            .andExpect(jsonPath("$.[*].commissionInPercents").value(hasItem(DEFAULT_COMMISSION_IN_PERCENTS)));
    }

    @Test
    @Transactional
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.legalName").value(DEFAULT_LEGAL_NAME))
            .andExpect(jsonPath("$.taxpayerNumber").value(DEFAULT_TAXPAYER_NUMBER))
            .andExpect(jsonPath("$.discountInPercents").value(DEFAULT_DISCOUNT_IN_PERCENTS))
            .andExpect(jsonPath("$.commissionInPercents").value(DEFAULT_COMMISSION_IN_PERCENTS));
    }


    @Test
    @Transactional
    public void getCompaniesByIdFiltering() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        Long id = company.getId();

        defaultCompanyShouldBeFound("id.equals=" + id);
        defaultCompanyShouldNotBeFound("id.notEquals=" + id);

        defaultCompanyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.greaterThan=" + id);

        defaultCompanyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCompaniesBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where slug equals to DEFAULT_SLUG
        defaultCompanyShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the companyList where slug equals to UPDATED_SLUG
        defaultCompanyShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllCompaniesBySlugIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where slug not equals to DEFAULT_SLUG
        defaultCompanyShouldNotBeFound("slug.notEquals=" + DEFAULT_SLUG);

        // Get all the companyList where slug not equals to UPDATED_SLUG
        defaultCompanyShouldBeFound("slug.notEquals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllCompaniesBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultCompanyShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the companyList where slug equals to UPDATED_SLUG
        defaultCompanyShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllCompaniesBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where slug is not null
        defaultCompanyShouldBeFound("slug.specified=true");

        // Get all the companyList where slug is null
        defaultCompanyShouldNotBeFound("slug.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesBySlugContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where slug contains DEFAULT_SLUG
        defaultCompanyShouldBeFound("slug.contains=" + DEFAULT_SLUG);

        // Get all the companyList where slug contains UPDATED_SLUG
        defaultCompanyShouldNotBeFound("slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllCompaniesBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where slug does not contain DEFAULT_SLUG
        defaultCompanyShouldNotBeFound("slug.doesNotContain=" + DEFAULT_SLUG);

        // Get all the companyList where slug does not contain UPDATED_SLUG
        defaultCompanyShouldBeFound("slug.doesNotContain=" + UPDATED_SLUG);
    }


    @Test
    @Transactional
    public void getAllCompaniesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where title equals to DEFAULT_TITLE
        defaultCompanyShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the companyList where title equals to UPDATED_TITLE
        defaultCompanyShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where title not equals to DEFAULT_TITLE
        defaultCompanyShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the companyList where title not equals to UPDATED_TITLE
        defaultCompanyShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCompanyShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the companyList where title equals to UPDATED_TITLE
        defaultCompanyShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where title is not null
        defaultCompanyShouldBeFound("title.specified=true");

        // Get all the companyList where title is null
        defaultCompanyShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByTitleContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where title contains DEFAULT_TITLE
        defaultCompanyShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the companyList where title contains UPDATED_TITLE
        defaultCompanyShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where title does not contain DEFAULT_TITLE
        defaultCompanyShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the companyList where title does not contain UPDATED_TITLE
        defaultCompanyShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllCompaniesByLegalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where legalName equals to DEFAULT_LEGAL_NAME
        defaultCompanyShouldBeFound("legalName.equals=" + DEFAULT_LEGAL_NAME);

        // Get all the companyList where legalName equals to UPDATED_LEGAL_NAME
        defaultCompanyShouldNotBeFound("legalName.equals=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByLegalNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where legalName not equals to DEFAULT_LEGAL_NAME
        defaultCompanyShouldNotBeFound("legalName.notEquals=" + DEFAULT_LEGAL_NAME);

        // Get all the companyList where legalName not equals to UPDATED_LEGAL_NAME
        defaultCompanyShouldBeFound("legalName.notEquals=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByLegalNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where legalName in DEFAULT_LEGAL_NAME or UPDATED_LEGAL_NAME
        defaultCompanyShouldBeFound("legalName.in=" + DEFAULT_LEGAL_NAME + "," + UPDATED_LEGAL_NAME);

        // Get all the companyList where legalName equals to UPDATED_LEGAL_NAME
        defaultCompanyShouldNotBeFound("legalName.in=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByLegalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where legalName is not null
        defaultCompanyShouldBeFound("legalName.specified=true");

        // Get all the companyList where legalName is null
        defaultCompanyShouldNotBeFound("legalName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByLegalNameContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where legalName contains DEFAULT_LEGAL_NAME
        defaultCompanyShouldBeFound("legalName.contains=" + DEFAULT_LEGAL_NAME);

        // Get all the companyList where legalName contains UPDATED_LEGAL_NAME
        defaultCompanyShouldNotBeFound("legalName.contains=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    public void getAllCompaniesByLegalNameNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where legalName does not contain DEFAULT_LEGAL_NAME
        defaultCompanyShouldNotBeFound("legalName.doesNotContain=" + DEFAULT_LEGAL_NAME);

        // Get all the companyList where legalName does not contain UPDATED_LEGAL_NAME
        defaultCompanyShouldBeFound("legalName.doesNotContain=" + UPDATED_LEGAL_NAME);
    }


    @Test
    @Transactional
    public void getAllCompaniesByTaxpayerNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxpayerNumber equals to DEFAULT_TAXPAYER_NUMBER
        defaultCompanyShouldBeFound("taxpayerNumber.equals=" + DEFAULT_TAXPAYER_NUMBER);

        // Get all the companyList where taxpayerNumber equals to UPDATED_TAXPAYER_NUMBER
        defaultCompanyShouldNotBeFound("taxpayerNumber.equals=" + UPDATED_TAXPAYER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTaxpayerNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxpayerNumber not equals to DEFAULT_TAXPAYER_NUMBER
        defaultCompanyShouldNotBeFound("taxpayerNumber.notEquals=" + DEFAULT_TAXPAYER_NUMBER);

        // Get all the companyList where taxpayerNumber not equals to UPDATED_TAXPAYER_NUMBER
        defaultCompanyShouldBeFound("taxpayerNumber.notEquals=" + UPDATED_TAXPAYER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTaxpayerNumberIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxpayerNumber in DEFAULT_TAXPAYER_NUMBER or UPDATED_TAXPAYER_NUMBER
        defaultCompanyShouldBeFound("taxpayerNumber.in=" + DEFAULT_TAXPAYER_NUMBER + "," + UPDATED_TAXPAYER_NUMBER);

        // Get all the companyList where taxpayerNumber equals to UPDATED_TAXPAYER_NUMBER
        defaultCompanyShouldNotBeFound("taxpayerNumber.in=" + UPDATED_TAXPAYER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTaxpayerNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxpayerNumber is not null
        defaultCompanyShouldBeFound("taxpayerNumber.specified=true");

        // Get all the companyList where taxpayerNumber is null
        defaultCompanyShouldNotBeFound("taxpayerNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompaniesByTaxpayerNumberContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxpayerNumber contains DEFAULT_TAXPAYER_NUMBER
        defaultCompanyShouldBeFound("taxpayerNumber.contains=" + DEFAULT_TAXPAYER_NUMBER);

        // Get all the companyList where taxpayerNumber contains UPDATED_TAXPAYER_NUMBER
        defaultCompanyShouldNotBeFound("taxpayerNumber.contains=" + UPDATED_TAXPAYER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCompaniesByTaxpayerNumberNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where taxpayerNumber does not contain DEFAULT_TAXPAYER_NUMBER
        defaultCompanyShouldNotBeFound("taxpayerNumber.doesNotContain=" + DEFAULT_TAXPAYER_NUMBER);

        // Get all the companyList where taxpayerNumber does not contain UPDATED_TAXPAYER_NUMBER
        defaultCompanyShouldBeFound("taxpayerNumber.doesNotContain=" + UPDATED_TAXPAYER_NUMBER);
    }


    @Test
    @Transactional
    public void getAllCompaniesByDiscountInPercentsIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where discountInPercents equals to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldBeFound("discountInPercents.equals=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the companyList where discountInPercents equals to UPDATED_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldNotBeFound("discountInPercents.equals=" + UPDATED_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllCompaniesByDiscountInPercentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where discountInPercents not equals to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldNotBeFound("discountInPercents.notEquals=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the companyList where discountInPercents not equals to UPDATED_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldBeFound("discountInPercents.notEquals=" + UPDATED_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllCompaniesByDiscountInPercentsIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where discountInPercents in DEFAULT_DISCOUNT_IN_PERCENTS or UPDATED_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldBeFound("discountInPercents.in=" + DEFAULT_DISCOUNT_IN_PERCENTS + "," + UPDATED_DISCOUNT_IN_PERCENTS);

        // Get all the companyList where discountInPercents equals to UPDATED_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldNotBeFound("discountInPercents.in=" + UPDATED_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllCompaniesByDiscountInPercentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where discountInPercents is not null
        defaultCompanyShouldBeFound("discountInPercents.specified=true");

        // Get all the companyList where discountInPercents is null
        defaultCompanyShouldNotBeFound("discountInPercents.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByDiscountInPercentsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where discountInPercents is greater than or equal to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldBeFound("discountInPercents.greaterThanOrEqual=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the companyList where discountInPercents is greater than or equal to (DEFAULT_DISCOUNT_IN_PERCENTS + 1)
        defaultCompanyShouldNotBeFound("discountInPercents.greaterThanOrEqual=" + (DEFAULT_DISCOUNT_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllCompaniesByDiscountInPercentsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where discountInPercents is less than or equal to DEFAULT_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldBeFound("discountInPercents.lessThanOrEqual=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the companyList where discountInPercents is less than or equal to SMALLER_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldNotBeFound("discountInPercents.lessThanOrEqual=" + SMALLER_DISCOUNT_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllCompaniesByDiscountInPercentsIsLessThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where discountInPercents is less than DEFAULT_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldNotBeFound("discountInPercents.lessThan=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the companyList where discountInPercents is less than (DEFAULT_DISCOUNT_IN_PERCENTS + 1)
        defaultCompanyShouldBeFound("discountInPercents.lessThan=" + (DEFAULT_DISCOUNT_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllCompaniesByDiscountInPercentsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where discountInPercents is greater than DEFAULT_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldNotBeFound("discountInPercents.greaterThan=" + DEFAULT_DISCOUNT_IN_PERCENTS);

        // Get all the companyList where discountInPercents is greater than SMALLER_DISCOUNT_IN_PERCENTS
        defaultCompanyShouldBeFound("discountInPercents.greaterThan=" + SMALLER_DISCOUNT_IN_PERCENTS);
    }


    @Test
    @Transactional
    public void getAllCompaniesByCommissionInPercentsIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where commissionInPercents equals to DEFAULT_COMMISSION_IN_PERCENTS
        defaultCompanyShouldBeFound("commissionInPercents.equals=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the companyList where commissionInPercents equals to UPDATED_COMMISSION_IN_PERCENTS
        defaultCompanyShouldNotBeFound("commissionInPercents.equals=" + UPDATED_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCommissionInPercentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where commissionInPercents not equals to DEFAULT_COMMISSION_IN_PERCENTS
        defaultCompanyShouldNotBeFound("commissionInPercents.notEquals=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the companyList where commissionInPercents not equals to UPDATED_COMMISSION_IN_PERCENTS
        defaultCompanyShouldBeFound("commissionInPercents.notEquals=" + UPDATED_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCommissionInPercentsIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where commissionInPercents in DEFAULT_COMMISSION_IN_PERCENTS or UPDATED_COMMISSION_IN_PERCENTS
        defaultCompanyShouldBeFound("commissionInPercents.in=" + DEFAULT_COMMISSION_IN_PERCENTS + "," + UPDATED_COMMISSION_IN_PERCENTS);

        // Get all the companyList where commissionInPercents equals to UPDATED_COMMISSION_IN_PERCENTS
        defaultCompanyShouldNotBeFound("commissionInPercents.in=" + UPDATED_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCommissionInPercentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where commissionInPercents is not null
        defaultCompanyShouldBeFound("commissionInPercents.specified=true");

        // Get all the companyList where commissionInPercents is null
        defaultCompanyShouldNotBeFound("commissionInPercents.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompaniesByCommissionInPercentsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where commissionInPercents is greater than or equal to DEFAULT_COMMISSION_IN_PERCENTS
        defaultCompanyShouldBeFound("commissionInPercents.greaterThanOrEqual=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the companyList where commissionInPercents is greater than or equal to (DEFAULT_COMMISSION_IN_PERCENTS + 1)
        defaultCompanyShouldNotBeFound("commissionInPercents.greaterThanOrEqual=" + (DEFAULT_COMMISSION_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllCompaniesByCommissionInPercentsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where commissionInPercents is less than or equal to DEFAULT_COMMISSION_IN_PERCENTS
        defaultCompanyShouldBeFound("commissionInPercents.lessThanOrEqual=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the companyList where commissionInPercents is less than or equal to SMALLER_COMMISSION_IN_PERCENTS
        defaultCompanyShouldNotBeFound("commissionInPercents.lessThanOrEqual=" + SMALLER_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void getAllCompaniesByCommissionInPercentsIsLessThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where commissionInPercents is less than DEFAULT_COMMISSION_IN_PERCENTS
        defaultCompanyShouldNotBeFound("commissionInPercents.lessThan=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the companyList where commissionInPercents is less than (DEFAULT_COMMISSION_IN_PERCENTS + 1)
        defaultCompanyShouldBeFound("commissionInPercents.lessThan=" + (DEFAULT_COMMISSION_IN_PERCENTS + 1));
    }

    @Test
    @Transactional
    public void getAllCompaniesByCommissionInPercentsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where commissionInPercents is greater than DEFAULT_COMMISSION_IN_PERCENTS
        defaultCompanyShouldNotBeFound("commissionInPercents.greaterThan=" + DEFAULT_COMMISSION_IN_PERCENTS);

        // Get all the companyList where commissionInPercents is greater than SMALLER_COMMISSION_IN_PERCENTS
        defaultCompanyShouldBeFound("commissionInPercents.greaterThan=" + SMALLER_COMMISSION_IN_PERCENTS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompanyShouldBeFound(String filter) throws Exception {
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].legalName").value(hasItem(DEFAULT_LEGAL_NAME)))
            .andExpect(jsonPath("$.[*].taxpayerNumber").value(hasItem(DEFAULT_TAXPAYER_NUMBER)))
            .andExpect(jsonPath("$.[*].discountInPercents").value(hasItem(DEFAULT_DISCOUNT_IN_PERCENTS)))
            .andExpect(jsonPath("$.[*].commissionInPercents").value(hasItem(DEFAULT_COMMISSION_IN_PERCENTS)));

        // Check, that the count call also returns 1
        restCompanyMockMvc.perform(get("/api/companies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompanyShouldNotBeFound(String filter) throws Exception {
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyMockMvc.perform(get("/api/companies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .slug(UPDATED_SLUG)
            .title(UPDATED_TITLE)
            .legalName(UPDATED_LEGAL_NAME)
            .taxpayerNumber(UPDATED_TAXPAYER_NUMBER)
            .discountInPercents(UPDATED_DISCOUNT_IN_PERCENTS)
            .commissionInPercents(UPDATED_COMMISSION_IN_PERCENTS);
        CompanyDTO companyDTO = companyMapper.toDto(updatedCompany);

        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testCompany.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCompany.getLegalName()).isEqualTo(UPDATED_LEGAL_NAME);
        assertThat(testCompany.getTaxpayerNumber()).isEqualTo(UPDATED_TAXPAYER_NUMBER);
        assertThat(testCompany.getDiscountInPercents()).isEqualTo(UPDATED_DISCOUNT_IN_PERCENTS);
        assertThat(testCompany.getCommissionInPercents()).isEqualTo(UPDATED_COMMISSION_IN_PERCENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Delete the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
