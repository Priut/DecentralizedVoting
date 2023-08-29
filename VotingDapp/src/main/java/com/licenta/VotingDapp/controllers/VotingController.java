package com.licenta.VotingDapp.controllers;


import com.licenta.VotingDapp.contracts.VotingToken;
import com.licenta.VotingDapp.dtos.AccountDTO;
import com.licenta.VotingDapp.dtos.PollDTO;
import com.licenta.VotingDapp.contracts.Voting;
import com.licenta.VotingDapp.dtos.VoteRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
public class VotingController {
    private final Web3j web3j;
    private final Credentials credentials;
    private final Voting voting;
    private final VotingToken votingToken;
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(6721975);


    @Autowired
    public VotingController(Web3j web3j, Credentials credentials, Voting voting, VotingToken votingToken) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.voting = voting;
        this.votingToken = votingToken;
    }
    public Credentials createNewAccount() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, InterruptedException, ExecutionException {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        return Credentials.create(ecKeyPair);
    }

    public void transferEther(Credentials fromCredentials, String toAddress, BigDecimal etherAmount) throws InterruptedException, ExecutionException, IOException {
        BigInteger nonce = getNonce(fromCredentials.getAddress());
        BigInteger gasPrice = getGasPrice();

        // Convertim Ether in Wei
        BigInteger value = Convert.toWei(etherAmount, Convert.Unit.ETHER).toBigInteger();

        // crearea tranzactiei
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, gasPrice, GAS_LIMIT, toAddress, value);

        // Semnarea tranzactiei
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, fromCredentials);
        String signedTransaction = Numeric.toHexString(signedMessage);

        // Trimitem tranzactia
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedTransaction).sendAsync().get();

        // Asteptam ca aceasta sa fie minata
        String transactionHash = ethSendTransaction.getTransactionHash();
        EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();
        while (transactionReceipt.getTransactionReceipt().isEmpty()) {
            Thread.sleep(1);
            transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();
        }
    }
    public void transferVotingToken(Credentials fromCredentials, String toAddress, BigInteger tokenAmount) throws Exception {
        // Incarcam contractul
        VotingToken userVotingToken = VotingToken.load(
                votingToken.getContractAddress(), web3j, fromCredentials, getGasPrice(), GAS_LIMIT);

        // executam functia de transfer din contract
        TransactionReceipt transactionReceipt = userVotingToken.transfer(toAddress, tokenAmount).send();

        // asteptam ca tranzactia sa fie minata
        EthGetTransactionReceipt ethGetTransactionReceipt =
                web3j.ethGetTransactionReceipt(transactionReceipt.getTransactionHash()).sendAsync().get();

        while (ethGetTransactionReceipt.getTransactionReceipt().isEmpty()) {
            Thread.sleep(1000);
            ethGetTransactionReceipt =
                    web3j.ethGetTransactionReceipt(transactionReceipt.getTransactionHash()).sendAsync().get();
        }
    }

    private BigInteger getNonce(String address) throws InterruptedException, ExecutionException {
        return web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
    }

    private BigInteger getGasPrice() throws InterruptedException, ExecutionException {
        return web3j.ethGasPrice().sendAsync().get().getGasPrice();
    }

    public BigDecimal calculateExactEther(BigInteger gasPrice) {

        BigInteger weiRequired = GAS_LIMIT.multiply(gasPrice);
        return Convert.fromWei(new BigDecimal(weiRequired), Convert.Unit.ETHER);  // Convertim Wei to Ether.
    }
    public BigInteger getVotingTokenBalance(Credentials credentials, String accountAddress) throws Exception {
        // incarcam contractul
        VotingToken userVotingToken = VotingToken.load(
                votingToken.getContractAddress(), web3j, credentials, getGasPrice(), GAS_LIMIT);

        // verificam balanta
        return userVotingToken.balanceOf(accountAddress).send();
    }

    @PostMapping("/account")
    public ResponseEntity<?> createAccount() {
        try {
            Credentials newAccountCredentials = createNewAccount();
            BigDecimal exactEther = calculateExactEther(getGasPrice());
            transferEther(credentials, newAccountCredentials.getAddress(), exactEther.multiply(BigDecimal.valueOf(2)));
            transferVotingToken(credentials, newAccountCredentials.getAddress(), BigInteger.ONE);

            Map<String, String> response = new HashMap<>();
            response.put("address", newAccountCredentials.getAddress());
            response.put("privateKey", newAccountCredentials.getEcKeyPair().getPrivateKey().toString(16));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/account/transferFunds")
    public ResponseEntity<?> getFundsForVote(@RequestBody AccountDTO account){
       try {
           BigDecimal exactEther = calculateExactEther(getGasPrice());
           transferEther(credentials, account.getAddress(), exactEther.multiply(BigDecimal.valueOf(2)));
           transferVotingToken(credentials, account.getAddress(), BigInteger.ONE);


           return new ResponseEntity<>("Account provided with funds!", HttpStatus.OK);
       }
       catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }

    @PostMapping("/poll")
    public ResponseEntity<String> createPoll(@RequestBody PollDTO pollDto)  {
        String question = pollDto.getQuestion();
        List<String> options = pollDto.getOptions();

        // Convertim la tipul specific de date
        List<org.web3j.abi.datatypes.Utf8String> contractOptions = options.stream()
                .map(org.web3j.abi.datatypes.Utf8String::new)
                .toList();

        // Apelam functia din contract pentru creare poll
        TransactionReceipt createPollTransactionReceipt = null;
        try {
            createPollTransactionReceipt = voting.createPoll(question, options).send();
        } catch (Exception e) {
            if(e.getMessage().equals("Error processing transaction request: VM Exception while processing transaction: revert At least two options are required"))
             return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("At least two options are required!");
        }

        // Gasim id-ul
        List<Voting.PollCreatedEventResponse> pollCreatedEvents = voting.getPollCreatedEvents(createPollTransactionReceipt);
        if (pollCreatedEvents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create poll.");
        }
        BigInteger pollId = pollCreatedEvents.get(0).pollId;

        return new ResponseEntity<>("Poll created successfully with ID: " + pollId.toString(),HttpStatus.CREATED);

    }

    @PutMapping("/poll/{pollId}/vote")
    public ResponseEntity<String> vote(@PathVariable("pollId") BigInteger pollId, @RequestBody VoteRequestDTO voteRequestDTO){
        try {
            Credentials userCredentials = Credentials.create(voteRequestDTO.getUserPrivateKey());
            BigInteger balance = getVotingTokenBalance(userCredentials, userCredentials.getAddress());
            if (balance.compareTo(BigInteger.ONE) < 0) {
                return new ResponseEntity<>("Insufficient voting tokens! Please make a request for your account to be refilled!", HttpStatus.FORBIDDEN);
            }

            Voting userVoting = voting.load(voting.getContractAddress(), web3j, userCredentials, getGasPrice(), GAS_LIMIT);
            TransactionReceipt transactionReceipt = userVoting.vote(pollId, voteRequestDTO.getOptionId()).send();

            transferVotingToken(userCredentials, credentials.getAddress(), BigInteger.ONE);




            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            if(Objects.equals(e.getMessage(), "Error processing transaction request: VM Exception while processing transaction: revert You have already voted"))
                return new ResponseEntity<>("You voted already in this poll!",HttpStatus.CONFLICT);
            if(e.getMessage().equals("Error processing transaction request: insufficient funds for gas * price + value")||e.getMessage().equals("Error processing transaction request: VM Exception while processing transaction: revert ERC20: transfer amount exceeds balance"))
                return new ResponseEntity<>("You don't have enough funds! Please make a request for your account to be refilled!",HttpStatus.FORBIDDEN);
            if(e.getMessage().equals("Error processing transaction request: VM Exception while processing transaction: revert Poll is not active"))
                return new ResponseEntity<>("Poll is closed!",HttpStatus.CONFLICT);
            if(e.getMessage().equals("Error processing transaction request: VM Exception while processing transaction: revert Invalid option"))
                return new ResponseEntity<>("Invalid option!",HttpStatus.UNPROCESSABLE_ENTITY);
            else return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/poll/{pollId}/endPoll")
    public ResponseEntity<String> endPoll(@PathVariable("pollId") BigInteger pollId) {
        try {
            TransactionReceipt createPollTransactionReceipt = voting.endPoll(pollId).send();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            if(e.getMessage().equals("Error processing transaction request: VM Exception while processing transaction: revert Poll is already ended"))
                return new ResponseEntity<>("Poll was ended already!", HttpStatus.CONFLICT);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/poll/{pollId}")
    public ResponseEntity<?> getPollResults(@PathVariable("pollId") BigInteger pollId) {
        try {
            Tuple4<String, List<String>, List<BigInteger>, Boolean> pollData = voting.getPoll(pollId).send();
            String title = pollData.component1();
            List<String> candidates = pollData.component2();
            List<BigInteger> pollResults = pollData.component3();
            Boolean pollEnded = pollData.component4();

            if(candidates.isEmpty() || pollResults.isEmpty())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);


            Map<String, BigInteger> candidateVotes = new LinkedHashMap<>();
            for(int i = 0; i < candidates.size(); i++){
                candidateVotes.put(candidates.get(i), pollResults.get(i));
            }

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("title", title);
            response.put("votes", candidateVotes);
            response.put("active", pollEnded);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}