package com.nagarro.task;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.nagarro.task.controller.dto.AuthenticationDTO;
import com.nagarro.task.controller.response.AuthenticationResponse;
import com.nagarro.task.model.AuthenticationRepresentation;
import com.nagarro.task.service.AuthenticationRepresentationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

	private final String AUTH_URL = "/api/authenticate";

	@Autowired
	private TestRestTemplate testRestTemplate;

	private HttpEntity<AuthenticationDTO> request;

	@MockBean
	private AuthenticationRepresentationService authenticationRepresentationService;

	@Test
	public void testBadCredentials() {

		AuthenticationDTO authenticationDTO = new AuthenticationDTO();
		authenticationDTO.setUsername("admin");
		authenticationDTO.setPassword("password");

		request = new HttpEntity<>(authenticationDTO, getTrueHeaders());

		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(AUTH_URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

	}

	@Test
	public void testUserNotFound() {

		AuthenticationDTO authenticationDTO = new AuthenticationDTO();
		authenticationDTO.setUsername("adminn");
		authenticationDTO.setPassword("password");

		request = new HttpEntity<>(authenticationDTO, getTrueHeaders());

		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(AUTH_URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

	}

	@Test
	public void testAuthintecationRedisError() {

		Mockito.when(authenticationRepresentationService.findById(ArgumentMatchers.anyString()))
				.thenThrow(RuntimeException.class);

		AuthenticationDTO authenticationDTO = new AuthenticationDTO();
		authenticationDTO.setUsername("user");
		authenticationDTO.setPassword("user");

		request = new HttpEntity<>(authenticationDTO, getTrueHeaders());

		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(AUTH_URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		Assert.assertNotNull(response.getBody());

	}

	@Test
	public void testAuthintecationWithAlreadyLoginUser() {

		AuthenticationRepresentation representation = new AuthenticationRepresentation("admin", "iugiug", 5L);

		Optional<AuthenticationRepresentation> optional = Optional.of(representation);

		Mockito.when(authenticationRepresentationService.findById(ArgumentMatchers.anyString())).thenReturn(optional);

		AuthenticationDTO authenticationDTO = new AuthenticationDTO();
		authenticationDTO.setUsername("user");
		authenticationDTO.setPassword("user");

		request = new HttpEntity<>(authenticationDTO, getTrueHeaders());

		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(AUTH_URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		Assert.assertNotNull(response.getBody());

	}

	@Test
	public void testAuthintecationSucessLogin() {

		AuthenticationDTO authenticationDTO = new AuthenticationDTO();
		authenticationDTO.setUsername("admin");
		authenticationDTO.setPassword("admin");

		request = new HttpEntity<>(authenticationDTO, getTrueHeaders());

		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(AUTH_URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());

	}

	HttpHeaders getTrueHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

}
