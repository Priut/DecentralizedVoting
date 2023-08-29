package com.licenta.VotingDapp.dtos;

import lombok.Data;

import java.math.BigInteger;
@Data
public class VoteRequestDTO {
    private BigInteger optionId;
    private String userPrivateKey;

}
