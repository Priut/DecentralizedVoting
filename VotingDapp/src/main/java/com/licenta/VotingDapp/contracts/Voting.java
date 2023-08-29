package com.licenta.VotingDapp.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class Voting extends Contract {
    public static final String BINARY = "\"0x608060405234801561001057600080fd5b5061135f806100206000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80631a8cbcaa146100675780637e3d71c31461009a5780639207891d146100b6578063ac2f0074146100d4578063b384abef14610106578063f43b877814610122575b600080fd5b610081600480360381019061007c9190610bb9565b61013e565b6040516100919493929190610f22565b60405180910390f35b6100b460048036038101906100af9190610b4d565b610331565b005b6100be6104ba565b6040516100cb919061101c565b60405180910390f35b6100ee60048036038101906100e99190610bb9565b6104c0565b6040516100fd93929190611037565b60405180910390f35b610120600480360381019061011b9190610be2565b61057f565b005b61013c60048036038101906101379190610bb9565b610800565b005b606080606060008060016000878152602001908152602001600020905080600101805461016a906111f9565b80601f0160208091040260200160405190810160405280929190818152602001828054610196906111f9565b80156101e35780601f106101b8576101008083540402835291602001916101e3565b820191906000526020600020905b8154815290600101906020018083116101c657829003601f168201915b5050505050945080600201805480602002602001604051908101604052809291908181526020016000905b828210156102ba57838290600052602060002001805461022d906111f9565b80601f0160208091040260200160405190810160405280929190818152602001828054610259906111f9565b80156102a65780601f1061027b576101008083540402835291602001916102a6565b820191906000526020600020905b81548152906001019060200180831161028957829003601f168201915b50505050508152602001906001019061020e565b5050505093508060030180548060200260200160405190810160405280929190818152602001828054801561030e57602002820191906000526020600020905b8154815260200190600101908083116102fa575b505050505092508060040160009054906101000a900460ff169150509193509193565b6001815111610375576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161036c90610f9c565b60405180910390fd5b6000808154809291906103879061122b565b91905055506000600160008054815260200190815260200160002090506000548160000181905550828160010190805190602001906103c7929190610895565b50818160020190805190602001906103e092919061091b565b50815167ffffffffffffffff811115610422577f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b6040519080825280602002602001820160405280156104505781602001602082028036833780820191505090505b5081600301908051906020019061046892919061097b565b5060018160040160006101000a81548160ff0219169083151502179055506000547fab0309b6731a34e8750174d3c2c9591a34a2bf0c1425a30b34122be08d05073960405160405180910390a2505050565b60005481565b60016020528060005260406000206000915090508060000154908060010180546104e9906111f9565b80601f0160208091040260200160405190810160405280929190818152602001828054610515906111f9565b80156105625780601f1061053757610100808354040283529160200191610562565b820191906000526020600020905b81548152906001019060200180831161054557829003601f168201915b5050505050908060040160009054906101000a900460ff16905083565b6001600083815260200190815260200160002060040160009054906101000a900460ff166105e2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016105d990610fdc565b60405180910390fd5b6001600083815260200190815260200160002060050160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff1615610683576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161067a90610ffc565b60405180910390fd5b600160008381526020019081526020016000206002018054905081106106de576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106d590610f7c565b60405180910390fd5b60016000838152602001908152602001600020600301818154811061072c577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b9060005260206000200160008154809291906107479061122b565b9190505550600180600084815260200190815260200160002060050160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055503373ffffffffffffffffffffffffffffffffffffffff1681837f7fe1d4e6b34e228b5dc059fcdc037c71b216fb2417f47c171e505144a5e4f5fc60405160405180910390a45050565b6001600082815260200190815260200160002060040160009054906101000a900460ff16610863576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161085a90610fbc565b60405180910390fd5b60006001600083815260200190815260200160002060040160006101000a81548160ff02191690831515021790555050565b8280546108a1906111f9565b90600052602060002090601f0160209004810192826108c3576000855561090a565b82601f106108dc57805160ff191683800117855561090a565b8280016001018555821561090a579182015b828111156109095782518255916020019190600101906108ee565b5b50905061091791906109c8565b5090565b82805482825590600052602060002090810192821561096a579160200282015b82811115610969578251829080519060200190610959929190610895565b509160200191906001019061093b565b5b50905061097791906109e5565b5090565b8280548282559060005260206000209081019282156109b7579160200282015b828111156109b657825182559160200191906001019061099b565b5b5090506109c491906109c8565b5090565b5b808211156109e15760008160009055506001016109c9565b5090565b5b80821115610a0557600081816109fc9190610a09565b506001016109e6565b5090565b508054610a15906111f9565b6000825580601f10610a275750610a46565b601f016020900490600052602060002090810190610a4591906109c8565b5b50565b6000610a5c610a57846110a6565b611075565b9050808382526020820190508260005b85811015610a9c5781358501610a828882610b0e565b845260208401935060208301925050600181019050610a6c565b5050509392505050565b6000610ab9610ab4846110d2565b611075565b905082815260208101848484011115610ad157600080fd5b610adc8482856111b7565b509392505050565b600082601f830112610af557600080fd5b8135610b05848260208601610a49565b91505092915050565b600082601f830112610b1f57600080fd5b8135610b2f848260208601610aa6565b91505092915050565b600081359050610b4781611312565b92915050565b60008060408385031215610b6057600080fd5b600083013567ffffffffffffffff811115610b7a57600080fd5b610b8685828601610b0e565b925050602083013567ffffffffffffffff811115610ba357600080fd5b610baf85828601610ae4565b9150509250929050565b600060208284031215610bcb57600080fd5b6000610bd984828501610b38565b91505092915050565b60008060408385031215610bf557600080fd5b6000610c0385828601610b38565b9250506020610c1485828601610b38565b9150509250929050565b6000610c2a8383610d2c565b905092915050565b6000610c3e8383610f04565b60208301905092915050565b6000610c5582611122565b610c5f818561115d565b935083602082028501610c7185611102565b8060005b85811015610cad5784840389528151610c8e8582610c1e565b9450610c9983611143565b925060208a01995050600181019050610c75565b50829750879550505050505092915050565b6000610cca8261112d565b610cd4818561116e565b9350610cdf83611112565b8060005b83811015610d10578151610cf78882610c32565b9750610d0283611150565b925050600181019050610ce3565b5085935050505092915050565b610d26816111a1565b82525050565b6000610d3782611138565b610d41818561117f565b9350610d518185602086016111c6565b610d5a81611301565b840191505092915050565b6000610d7082611138565b610d7a8185611190565b9350610d8a8185602086016111c6565b610d9381611301565b840191505092915050565b6000610dab600e83611190565b91507f496e76616c6964206f7074696f6e0000000000000000000000000000000000006000830152602082019050919050565b6000610deb602183611190565b91507f4174206c656173742074776f206f7074696f6e7320617265207265717569726560008301527f64000000000000000000000000000000000000000000000000000000000000006020830152604082019050919050565b6000610e51601583611190565b91507f506f6c6c20697320616c726561647920656e64656400000000000000000000006000830152602082019050919050565b6000610e91601283611190565b91507f506f6c6c206973206e6f742061637469766500000000000000000000000000006000830152602082019050919050565b6000610ed1601683611190565b91507f596f75206861766520616c726561647920766f746564000000000000000000006000830152602082019050919050565b610f0d816111ad565b82525050565b610f1c816111ad565b82525050565b60006080820190508181036000830152610f3c8187610d65565b90508181036020830152610f508186610c4a565b90508181036040830152610f648185610cbf565b9050610f736060830184610d1d565b95945050505050565b60006020820190508181036000830152610f9581610d9e565b9050919050565b60006020820190508181036000830152610fb581610dde565b9050919050565b60006020820190508181036000830152610fd581610e44565b9050919050565b60006020820190508181036000830152610ff581610e84565b9050919050565b6000602082019050818103600083015261101581610ec4565b9050919050565b60006020820190506110316000830184610f13565b92915050565b600060608201905061104c6000830186610f13565b818103602083015261105e8185610d65565b905061106d6040830184610d1d565b949350505050565b6000604051905081810181811067ffffffffffffffff8211171561109c5761109b6112d2565b5b8060405250919050565b600067ffffffffffffffff8211156110c1576110c06112d2565b5b602082029050602081019050919050565b600067ffffffffffffffff8211156110ed576110ec6112d2565b5b601f19601f8301169050602081019050919050565b6000819050602082019050919050565b6000819050602082019050919050565b600081519050919050565b600081519050919050565b600081519050919050565b6000602082019050919050565b6000602082019050919050565b600082825260208201905092915050565b600082825260208201905092915050565b600082825260208201905092915050565b600082825260208201905092915050565b60008115159050919050565b6000819050919050565b82818337600083830152505050565b60005b838110156111e45780820151818401526020810190506111c9565b838111156111f3576000848401525b50505050565b6000600282049050600182168061121157607f821691505b60208210811415611225576112246112a3565b5b50919050565b6000611236826111ad565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82141561126957611268611274565b5b600182019050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b6000601f19601f8301169050919050565b61131b816111ad565b811461132657600080fd5b5056fea264697066735822122028ab9175af29786da47dfa18a46d3cdef02db62a5e11de5d39909ee4765fa27764736f6c63430008000033\"\n";

    public static final String FUNC_POLLCOUNT = "pollCount";

    public static final String FUNC_POLLS = "polls";

    public static final String FUNC_CREATEPOLL = "createPoll";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_ENDPOLL = "endPoll";

    public static final String FUNC_GETPOLL = "getPoll";

    public static final Event POLLCREATED_EVENT = new Event("PollCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}));
    ;

    public static final Event VOTECAST_EVENT = new Event("VoteCast", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }



    public static List<PollCreatedEventResponse> getPollCreatedEvents(TransactionReceipt transactionReceipt) {
        final Event event = POLLCREATED_EVENT;
        List<Log> logs = transactionReceipt.getLogs();
        List<PollCreatedEventResponse> responses = new ArrayList<>();

        for (Log log : logs) {
            if (log.getTopics().size() > 0 && log.getTopics().get(0).equals(EventEncoder.encode(event))) {
                EventValues eventValues = staticExtractEventParameters(event, log);
                PollCreatedEventResponse response = new PollCreatedEventResponse();

                response.log = log;
                response.pollId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                responses.add(response);
            }
        }

        return responses;
    }




    public Flowable<PollCreatedEventResponse> pollCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, PollCreatedEventResponse>() {
            @Override
            public PollCreatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(POLLCREATED_EVENT, log);
                PollCreatedEventResponse typedResponse = new PollCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.pollId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PollCreatedEventResponse> pollCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(POLLCREATED_EVENT));
        return pollCreatedEventFlowable(filter);
    }



    public static List<VoteCastEventResponse> getVoteCastEvents(TransactionReceipt transactionReceipt) {
        final Event event = VOTECAST_EVENT;
        List<Log> logs = transactionReceipt.getLogs();
        List<VoteCastEventResponse> responses = new ArrayList<>();

        for (Log log : logs) {
            if (log.getTopics().size() > 0 && log.getTopics().get(0).equals(EventEncoder.encode(event))) {
                EventValues eventValues = staticExtractEventParameters(event, log);
                VoteCastEventResponse response = new VoteCastEventResponse();

                response.log = log;
                response.pollId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                response.optionId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                response.voter = (String) eventValues.getIndexedValues().get(2).getValue();
                responses.add(response);
            }
        }

        return responses;
    }


    public Flowable<VoteCastEventResponse> voteCastEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, VoteCastEventResponse>() {
            @Override
            public VoteCastEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTECAST_EVENT, log);
                VoteCastEventResponse typedResponse = new VoteCastEventResponse();
                typedResponse.log = log;
                typedResponse.pollId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.optionId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.voter = (String) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VoteCastEventResponse> voteCastEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTECAST_EVENT));
        return voteCastEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> pollCount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_POLLCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, String, Boolean>> polls(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_POLLS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, String, Boolean>>(function,
                new Callable<Tuple3<BigInteger, String, Boolean>>() {
                    @Override
                    public Tuple3<BigInteger, String, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, String, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> createPoll(String _question, List<String> _options) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEPOLL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_question), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(_options, org.web3j.abi.datatypes.Utf8String.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger _pollId, BigInteger _optionId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_pollId), 
                new org.web3j.abi.datatypes.generated.Uint256(_optionId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> endPoll(BigInteger _pollId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ENDPOLL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_pollId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple4<String, List<String>, List<BigInteger>, Boolean>> getPoll(BigInteger _pollId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETPOLL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_pollId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple4<String, List<String>, List<BigInteger>, Boolean>>(function,
                new Callable<Tuple4<String, List<String>, List<BigInteger>, Boolean>>() {
                    @Override
                    public Tuple4<String, List<String>, List<BigInteger>, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, List<String>, List<BigInteger>, Boolean>(
                                (String) results.get(0).getValue(), 
                                convertToNative((List<Utf8String>) results.get(1).getValue()), 
                                convertToNative((List<Uint256>) results.get(2).getValue()), 
                                (Boolean) results.get(3).getValue());
                    }
                });
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Voting(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Voting(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Voting.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Voting> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Voting.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Voting.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Voting> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Voting.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class PollCreatedEventResponse extends BaseEventResponse {
        public BigInteger pollId;
    }

    public static class VoteCastEventResponse extends BaseEventResponse {
        public BigInteger pollId;

        public BigInteger optionId;

        public String voter;
    }
}
