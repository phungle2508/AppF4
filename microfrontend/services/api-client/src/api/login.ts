import axios from 'axios';

const KEYCLOAK_URL = 'https://keycloak.appf4.io.vn/realms/jhipster/protocol/openid-connect';

// Login by username/password - returns tokens
export async function login(username: string, password: string) {
    const params = new URLSearchParams();
    params.append('grant_type', 'password');
    params.append('client_id', 'web_app');
    params.append('username', username);
    params.append('password', password);

    const response = await axios.post(`${KEYCLOAK_URL}/token`, params.toString(), {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });
    return response.data;
}

// Get user info using access token
export async function getUserInfo(accessToken: string) {
    const response = await axios.get('https://keycloak.appf4.io.vn/realms/jhipster/account', {
        headers: {
            Authorization: `Bearer ${accessToken}`,
        },
    });
    return response.data;
}
