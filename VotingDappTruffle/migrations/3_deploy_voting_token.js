const VotingToken = artifacts.require("VotingToken");

module.exports = function(deployer) {
  deployer.deploy(VotingToken, "1000000000000000000000000");  // Alocă 1 milion de tokenuri la desfășurare
};

