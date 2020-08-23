# aws-java-membership-api ( NOT YET DEPLOYED to AWS !!!)
A  Proof of Concept for Java 8 & AWS Serverless for REST:
A simple Backend app for CRUD (UserEntity) based on AWS API Gateway, AWS Lambda and DynamodB;
Infrastructure-as-a-Code is provided by 'The Serverless Framework';

Observations:  
1.) Java Lambda Functions for REST are rather slow regarding execution time (and compared to Node.js-based Lambda Functions);
2.) Java && Serverless and Java && AWS provide quite limited documentations, code examples in comparison to Node.js.
3.) The Serverless Framework  provides limited support for offline/local Testing of REST/Lambda: The serverless-offline plugin
does not yet support the call of the REST resources via the URL (The Serverless Framework Team is working on that issue),
thus all tests have to be executes via the Serverless CLI console ( sls invoke -f <functions_name>)

Conclusion: This Proof of Concept suggests that as a programming language, Java is not well suited for Cloud serverless REST APIs
when compared with Node.js. Maybe the use of long running AWS EC2 instances should be preferred for Java REST-based Apps.
