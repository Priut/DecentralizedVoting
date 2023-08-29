import http from 'k6/http';
import { check, group, sleep } from 'k6';
function getRandomOption() {
    return Math.random() < 0.5 ? 0 : 1;
}
export let options = {
    vus: 1,
    iterations:300,
    duration:'1h'
};
const BASE_URL = 'http://localhost:6001/api/v1';
const pollId = 4;
let privateKey;
export default function () {
    // Create a new account
    group('Create new account', () => {
        const createAccountResponse = http.post(`${BASE_URL}/account`);

        check(createAccountResponse, {
            'Create account: status is 200': (r) => r.status === 200,
        });

        const accountData = JSON.parse(createAccountResponse.body);
        privateKey = accountData.privateKey;
        sleep(0.5);
    });
    // Vote on the poll
    group('Vote', () => {
        const voteData = {
            userPrivateKey: privateKey,
            optionId: getRandomOption(),
        };
        const voteResponse = http.put(`${BASE_URL}/poll/${pollId}/vote`, JSON.stringify(voteData), {
            headers: { 'Content-Type': 'application/json' },
        });

        check(voteResponse, {
            'Vote: status is 204': (r) => r.status === 204,
        });
    sleep(0.5);
    });
}
