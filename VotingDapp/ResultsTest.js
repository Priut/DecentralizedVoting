import http from 'k6/http';
import { check, group, sleep } from 'k6';
export let options={
    stages:[
        {duration:'5m', target:100},
        {duration:'10m', target:100},
        {duration:'5m', target:0},
    ]
}

const BASE_URL = 'http://localhost:6001/api/v1';
const pollId = 4;
let privateKey;
export default function () {
    var url = `${BASE_URL}/poll/${pollId}`;


    var params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.get(url, params);
    check(res, {
        'Poll retrieved with success!': (r) => r.status === 200,
    });
    if (res.status !== 200) {
        console.log(`Request failed. Status: ${res.status}, Body: ${res.body}`);
    }

    sleep(0.5);
}
