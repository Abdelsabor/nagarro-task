package com.nagarro.task;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.nagarro.task.controller.dto.AuthenticationDTO;
import com.nagarro.task.controller.response.AuthenticationResponse;
import com.nagarro.task.controller.response.StatementResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthorizationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	private HttpEntity<AuthenticationDTO> request;

	private ResponseEntity<AuthenticationResponse> response;

	private final String AUTH_URL = "/api/authenticate";

	private final String SEARCH_URL = "/api/statement/account/3/search";
	
	private final String StatEMENT_URL = "/api/statement";

	private ResponseEntity<List<StatementResponse>> statementResponse;

	@Before
	public void init() {

		AuthenticationDTO authenticationDTO = new AuthenticationDTO();
		authenticationDTO.setUsername("user");
		authenticationDTO.setPassword("user");

		request = new HttpEntity<>(authenticationDTO, getTrueHeaders());

		response = testRestTemplate.postForEntity(AUTH_URL, request, AuthenticationResponse.class);

	}

	@Test
	public void testSearchByUserRole() {

		statementResponse = testRestTemplate.exchange(SEARCH_URL, HttpMethod.GET,
				new HttpEntity<>(getTrueHeadersWithToken(response.getBody().getToken())),
				new ParameterizedTypeReference<List<StatementResponse>>() {
				});

		Assert.assertEquals(HttpStatus.UNAUTHORIZED, statementResponse.getStatusCode());

	}
	
	@Test
	public void testSearchByUserRoleWithInvalidToken() {

		statementResponse = testRestTemplate.exchange(StatEMENT_URL, HttpMethod.GET,
				new HttpEntity<>(getTrueHeadersWithToken("")),
				new ParameterizedTypeReference<List<StatementResponse>>() {
				});

		Assert.assertEquals(HttpStatus.UNAUTHORIZED, statementResponse.getStatusCode());

	}

	HttpHeaders getTrueHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	HttpHeaders getTrueHeadersWithToken(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + token);

		return headers;
	}

}
