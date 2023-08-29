pragma solidity ^0.8.0;

contract Voting {
    struct Poll {
        uint256 id;
        string question;
        string[] options;
        uint256[] voteCounts;
        bool active;
        mapping(address => bool) voters;
    }

    uint256 public pollCount;
    mapping(uint256 => Poll) public polls;

    event PollCreated(uint256 indexed pollId);
    event VoteCast(uint256 indexed pollId, uint256 indexed optionId, address indexed voter);

    function createPoll(string memory _question, string[] memory _options) public {
        require(_options.length > 1, "At least two options are required");

        pollCount++;
        Poll storage newPoll = polls[pollCount];
        newPoll.id = pollCount;
        newPoll.question = _question;
        newPoll.options = _options;
        newPoll.voteCounts = new uint256[](_options.length);
        newPoll.active = true;

        emit PollCreated(pollCount);
    }

    function vote(uint256 _pollId, uint256 _optionId) public {
        require(polls[_pollId].active, "Poll is not active");
        require(!polls[_pollId].voters[msg.sender], "You have already voted");
        require(_optionId < polls[_pollId].options.length, "Invalid option");

        polls[_pollId].voteCounts[_optionId]++;
        polls[_pollId].voters[msg.sender] = true;

        emit VoteCast(_pollId, _optionId, msg.sender);
    }

    function endPoll(uint256 _pollId) public {
        require(polls[_pollId].active, "Poll is already ended");
        polls[_pollId].active = false;
    }

    function getPoll(uint256 _pollId) public view returns (string memory question, string[] memory options, uint256[] memory voteCounts, bool active) {
        Poll storage poll = polls[_pollId];
        question = poll.question;
        options = poll.options;
        voteCounts = poll.voteCounts;
        active = poll.active;
    }
}
