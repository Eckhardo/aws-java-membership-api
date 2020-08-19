package com.eki.aws.serverless.domain.user;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.eki.aws.serverless.ApiGatewayResponse;
import com.eki.aws.serverless.Response;

/**
 * 
 * @author eckha
 *
 */
public class DeleteUserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	private static final Logger LOG = LogManager.getLogger(DeleteUserHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
		try {

			String userName = pathParameters.get("userName");
			// get the Product by id
			Boolean success = new UserEntity().delete(userName);
			// send the response back
			if (success) {
				return ApiGatewayResponse.builder()

						.setStatusCode(204)
						.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless")).build();

			} else {

				return ApiGatewayResponse.builder().setStatusCode(404)
						.setObjectBody("User with id: '" + userName + "' not found.")
						.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless")).build();
			}

		} catch (Exception ex) {
			Response responseBody = new Response("Error in deleting user: ", input);
			LOG.error("Error in deleting product: " + ex);
			return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody).build();
		}
	}

}
