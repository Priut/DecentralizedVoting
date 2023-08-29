package com.licenta.VotingDapp;

import com.licenta.VotingDapp.contracts.Voting;
import com.licenta.VotingDapp.contracts.VotingToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class VotingDappConfiguration {

    @Value("${ethereum.contract.address}")
    private String contractAddress;
    @Value("${ethereum.credentials.privatekey}")
    private String privateKey;

    private static final String VOTING_TOKEN_CONTRACT_ADDRESS = "0xDa0347bB19c03881F3ee124e0aD79475c704EcbF";


    @Bean
    public Web3j web3j() {
        String ganacheUrl = "http://localhost:7545";
        return Web3j.build(new HttpService(ganacheUrl));
    }
    @Bean
    public Credentials credentials() {
        return Credentials.create(privateKey);
    }
    @Bean
    public Voting voting(Web3j web3j, Credentials credentials) {
        return Voting.load(contractAddress, web3j, credentials, new CustomGasProvider());
    }
    @Bean
    public VotingToken votingToken(Web3j web3j, Credentials credentials) {
        return VotingToken.load(VOTING_TOKEN_CONTRACT_ADDRESS, web3j, credentials, new CustomGasProvider());
    }

}