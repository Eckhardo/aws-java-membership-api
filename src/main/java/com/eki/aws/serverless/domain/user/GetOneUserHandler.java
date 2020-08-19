package com.eki.aws.serverless.domain.user;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.eki.aws.serverless.ApiGatewayResponse;

public class GetOneUserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	private static final Logger LOG = LogManager.getLogger(GetOneUserHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("GetOneUserHandler::handleRequest");
		try {

			// get the 'pathParameters' from input
			Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
			LOG.info("pathParams:" + pathParameters);
			String userName = pathParameters.get("userName");

			// get the User by pk
			UserEntity user = new UserEntity().get(userName);
			// send the response back
			if (user != null) {
				return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(user)
						.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless")).build();

			} else {

				return ApiGatewayResponse.builder()

						.setStatusCode(404).setObjectBody("User with id: '" + userName + "' not found.")
						.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless")).build();
			}

		} catch (Exception ex) {
			LOG.error("error in retrieving user :", ex);
			return ApiGatewayResponse.builder().setStatusCode(500)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless")).build();
		}
	}
}
