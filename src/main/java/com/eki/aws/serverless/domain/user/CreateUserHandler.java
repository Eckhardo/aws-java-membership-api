package com.eki.aws.serverless.domain.user;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.eki.aws.serverless.ApiGatewayResponse;
import com.eki.aws.serverless.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CreateUserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(CreateUserHandler.class);
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Override

	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		LOG.info("CreateUserHandler:: handleRequest");
		LOG.info("CONTEXT: " + gson.toJson(context));
		LOG.info("BODY: " + gson.toJson(input.get("body")));
		try {

			// get the 'body' from input
			Map<String, String> body = (Map<String, String>) input.get("body");
			LOG.info("body:" + body);

			UserEntity user = new UserEntity();
			user.setUsername(body.get("user_name"));
			user.setFirstname(body.get("first_name"));
			user.setLastname(body.get("last_name"));
			user.setEmail(body.get("email"));
			user.setPhone(body.get("phone"));
			user.setMobile(body.get("mobile"));
			user.setAdmissionDate(Integer.parseInt(body.get("admission_date")));
			user.setAddress(body.get("address"));
			user.setCity(body.get("city"));
			user.setZip(Integer.parseInt(body.get("zip")));
			user.setAdmin(false);
			user.setActive(true);
			LOG.info("user:" + user.toString());
			user.save(user);

			// send the response back

			return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(user)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless")).build();

		} catch (Exception ex) {

			LOG.error("Error in saving user: " + ex);
			// send the error response back
			Response responseBody = new Response("Error in saving user: ", input);

			return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless")).build();
		}
	}
}