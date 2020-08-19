package com.eki.aws.serverless.domain.user;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.eki.aws.serverless.ApiGatewayResponse;

public class GetAllUserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	private static final Logger LOG = LogManager.getLogger(GetAllUserHandler.class);

	@Override

	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("GetAllUserHandler::handleRequest");

		try {

			// get all users

			List<UserEntity> userEntitys = new UserEntity().list();

			LOG.info("result list all users" + userEntitys.size());
			// send the response back

			return ApiGatewayResponse.builder()

					.setStatusCode(200)

					.setObjectBody(userEntitys)

					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))

					.build();

		} catch (Exception ex) {

			return ApiGatewayResponse.builder()

					.setStatusCode(500)

					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))

					.build();

		}

	}

}