package com.licenta.VotingDapp.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PollDTO {

    private String question;

    private List<String> options;

}
