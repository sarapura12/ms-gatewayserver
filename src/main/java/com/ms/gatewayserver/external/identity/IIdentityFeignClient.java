package com.ms.gatewayserver.external.identity;

import com.ms.gatewayserver.external.identity.models.TokenRequest;
import com.ms.gatewayserver.external.identity.models.VerificationResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ms-identity")
public interface IIdentityFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "/api/auth/validate", consumes = "application/json")
    Void validateToken(@RequestBody TokenRequest request);
}
