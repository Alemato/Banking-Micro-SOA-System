package it.univaq.sose.bancomatservice.webservice;

import jakarta.jws.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@WebService(serviceName = "BankAccountService", portName = "BankAccountPort",
        targetNamespace = "",
        endpointInterface = "it.univaq.sose.bancomatservice.webservice.BancomatService")
public class BancomatServiceImpl implements BancomatService {
}
