# iam-oidc-client

IAM OIDC Client is a lightweight, web-based OpenID Connect client designed to test, debug, and explore OAuth 2.0 and OpenID Connect integrations with INDIGO IAM and other compatible identity providers.

## Key features

- Dynamic OpenID Connect provider discovery.
- Authorization Code Flow with PKCE.
- Support for both public and confidential clients.
- Customizable issuer and requested scopes through the web interface.
- Visualization of access, ID, and refresh tokens, including decoded JWT contents.
- UserInfo endpoint integration.
- Token introspection support.
- Configurable client settings through environment variables.
- Optional hiding of raw token values.

## Technology stack

- Java 21
- Spring Boot 4.2
- Spring Security
- Thymeleaf

## Client registration

Before running the application, register an OAuth 2.0 / OpenID Connect client with INDIGO IAM or the identity provider identified by the configured issuer.

The client registration should include the following settings:

| Setting | Value |
|---|---|
| Grant type | `authorization_code` |
| Response type | `code` |
| Redirect URI | `http://localhost:9090/iam-test-client/login/oauth2/code/dynamic` |
| Token endpoint authentication method | `client_secret_basic` or `none` (for public clients) |
| PKCE | Enabled (`S256`) |
| Suggested scopes | `openid profile email offline_access` |

Enable the `refresh_token` grant type if refresh tokens are required.

Use the registered client ID and client secret to configure the application through the `IAM_CLIENT_ID` and `IAM_CLIENT_SECRET` environment variables.

For public clients, set `IAM_CLIENT_SECRET` to an empty string.

### Redirect URI

The default redirect URI follows this pattern:

```
{baseUrl}/login/oauth2/code/dynamic
```

If the application is deployed on a different host, port, context path, or behind a reverse proxy, register the corresponding externally accessible redirect URI with the identity provider.

For example:

```
https://oidc-client.example.org/iam-test-client/login/oauth2/code/dynamic
```

The registered redirect URI must exactly match the one used by the application during the authorization request.

When deploying behind a trusted reverse proxy, configure `IAM_CLIENT_FORWARD_HEADERS_STRATEGY=framework` if forwarded headers are needed to resolve the external application URL correctly.

## Docker distribution

Multi-architecture Docker images are available on [Docker Hub](https://hub.docker.com/r/indigoiam/iam-oidc-client), supporting both `linux/amd64` and `linux/arm64`.

```bash
docker pull indigoiam/iam-oidc-client:v0.1.0
```

To start the application:

```bash
docker run --rm -p 9090:9090 \
  -e IAM_CLIENT_ID=your-client-id \
  -e IAM_CLIENT_SECRET=your-client-secret \
  -e IAM_CLIENT_ISSUER=https://your-iam-instance \
  indigoiam/iam-oidc-client:v0.1.0
```

For public clients, set `IAM_CLIENT_SECRET` to an empty string.

The application is available at http://localhost:9090/iam-test-client/.

The container runs as a non-root user.

> **Note:** This application is intended for testing and debugging purposes. It should not be considered a production-ready relying party or exposed to untrusted users without appropriate security controls.

Feedback, bug reports, and contributions are welcome!
